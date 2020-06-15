MarqetaKit Android SDK
==============

The MarqetaKit Android SDK makes it easy to add In-App Payment Card Provisioning to Android Pay within your applications.
>### Note
>
>The MarqetaKit library is built using API 26, with a minimum SDK version of 21.

## Contents

- [Get the Code](#get-the-code)
- [Add the SDK to Your Project](#add-the-sdk-directly-to-your-project)
- [Use Cases](#use-cases)
- [Requirements](#requirements)
- [Testing](#testing)

## Get the Code

### Add the SDK directly to Your Project
1. Download the `https://github.com/marqeta/MarqetaKit-android/blob/master/marqetakit/marqetaKit-{version}.aar` file to your root project folder.
2. The {version} number should match the major version of the Google Play services library dependencies of your main project. You can find this in your project's `build.gradle` file. For example, if your app uses version 10.+ of the the `com.google.android.gms:play-services-maps` library, you should use the `marqetaKit-10-2-0.aar`. If your project does not use any Google Play Services libraries, it is recommended you use the latest version of the MarqetaKit SDK available.
3. In Android Studio, select `File` -> `New Project`.
4. Select `Import .JAR/.AAR Package` and click Next.
5. For File Name, select the `marqetaKit-{version}.aar` file in your projects root directory and click Finish.
6. In your main projects `build.gradle` file, add the following dependency: `compile project(":marqetaKit")`.
7. Sync project.


### Add private Google Play Services libraries

You will need to obtain the Google Pay specific versions of all Google Play Services and Firebase libraries you currently utilize in your app from your Google representative. 

#### Local Maven Setup

If you haven't set up your local maven repo, you can do so by following the `Install Maven` and `Configure Maven` steps on this page:
http://blog.mattcarroll.name/2015/05/android-library-project-android-studio-and-local-maven-repository/

Extract the Google Pay libraries into the location where you set up your local maven repo. Add `mavenLocal()` to the repositories section of your project's root build.gradle file:

```
buildscript {
    repositories {
        mavenLocal()
        jcenter()
        google()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.0'
        classpath 'com.google.gms:google-services:3.1.0'
    }
...
```

Finally, add the tap and pay library dependency to your project's `build.gradle`:  
`implementation 'com.google.android.gms:play-services-tapandpay:11.8.0'`


#### Private Repo Setup

If you have multiple developers working on the codebase, you may want to consider adding the Google Pay libraries to a private repo. [Artifactory](https://jfrog.com/artifactory/) and [Nexus](https://blog.sonatype.com/) are a popular options for internal library management. If you want a cheaper option, there are alternatives like [My Maven Repo](https://mymavenrepo.com/) or [Using an Amazon S3 bucket](http://www.yegor256.com/2015/09/07/maven-repository-amazon-s3.html).

Deploy the Google Play libraries to your repo and add the repo URL to your project root's build.gradle:

```
buildscript {
    repositories {
        maven {
            url : "https://myrepositoryurl.com/repo"
        }
        jcenter()
        google()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.0'
        classpath 'com.google.gms:google-services:3.1.0'
    }
...
```

Add the tap and pay library dependency to your project's `build.gradle`:  
`implementation 'com.google.android.gms:play-services-tapandpay:11.8.0'`



## Use Cases


### Device Eligibility

Please reference the latest Android Pay website to determine device eligibility: https://www.android.com/pay/. In order to be eligible for push provisioning, the test device will need to have an active SIM card.



### SDK creation and initialization

In `OnCreate()` of your card list activity, create an instance of the `MarqetaKit` class:

```
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // create SDK
    marqetaKit = new MarqetaKit(this);
}
```

Next, initialize the SDK. This method is best run in the `onResume()` method of the activity to ensure the Android Pay wallet references are always up to date.

```
// initialize wallet
marqetaKit.initializeWallet(new MarqetaKit.WalletInitializationCallback() {
    @Override
    public void onWalletInitialized() {
        // fetch card data from API service
    }

    @Override
    public void onWalletSetupFailed() {
       // failure path
    }
});
```

### SDK Memory Management

The MarqetaKit SDK uses its own instance of the `GooglePlayAPI` client to perform provisioning requests. All initialization and termination of the client is wrapped and handled by the MarqetaKit API. To ensure proper shutdown of the client, call the `destroy()` method on the `MarqetaKit` class instance in the `onDestroy()` lifecycle method of your activity:

```
@Override
protected void onDestroy() {
    super.onDestroy();

    // clean up references
    marqetaKit.destroy();
}
```

If your application requires use of the `GooglePlayAPI` client for other features, it is recommended you create a seperate instance of this class in your activity. This will allow for independent management of the client without any concerns about  conflicting with the MarqetaKit SDK. Multiple instances of the `GooglePlayAPI` client can run within the same Activity without issue.


### Wallet Creation

If the user has not set up Android Pay previously, the MarqetaKit APIs will automatically create it for them. When this occurs, a callback to the Activity's `onActivityResult(int requestCode, int resultCode, Intent data)` method with a `requestCode` value of **REQUEST_CODE_CREATE_WALLET** will occur. After receiving this callback, you should call the `marqetaKit.initializeWallet` method again:

```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
        switch (requestCode) {
        case REQUEST_CODE_CREATE_WALLET:
            // re-initialize wallet parameters after new wallet was created
            marqetaKit.initializeWallet(new MarqetaKit.WalletInitializationCallback() {
                @Override
                public void onWalletInitialized() {
                    // fetch card data from API service
                }

                @Override
                public void onWalletSetupFailed() {
                    // failure path
                }
            });
            break;
        ...
        }
   }
```


## Card Status

### Card Data

Implement the following protocols in your Card data object to make them compatible with MarqetaKit.

`ICard`
`IDigitalWalletTokens`

After the card data has been returned from the API service, sync the token status with the tokens that are present in the Android Pay wallet:

```
marqetaKit.syncTokens(responseCards.getCards(), new MarqetaKit.TokenSyncCallback() {
    @Override
    public void onSyncComplete() {
        // display card list with Android Pay status
    }
});
```

## Provision Card

### Tokenize Data

Implement the following protocols in your Card Tokenization API request and response data objects:

`IProvisionResponse`
`ITokenizeData`
`IUserAddress`
`IProvisionRequest`

### Call Provisioning Service

Invoke the push provision method by passing the card, device type, empty `ProvisionRequest` object (which will be populated by the MarqetaKit pushProvision method) and a handler at that will execute your company's Tokenize Data API service:

```
marqetaKit.pushProvision(card, "MOBILE_PHONE", new ProvisionRequest(), new MarqetaKit.PushProvisionRequestor() {

    @Override
    public void callProvisionApi(IProvisionRequest provisionRequest, final MarqetaKit.PushProvisionApiResponseDelegate delegate) {

      // Make service request to API endpoint with provisionRequest data.
      // API endpoint should return ProvisionResponse object:

      Api.request(provisionRequest, Callback<ProvisionResponse> response {
          // send response to MarqetaKit.PushProvisionApiResponseDelegate to handle provisioning call to Android Pay APIs
          delegate.onResponse(response)
      });

   }
});
```

This will launch the Android Pay app with the appropriate data required to complete provisioning. Once complete, the user will be returned back to your app along with the result of the provisioning request.


### Provisioning Result

The Android Pay APIs will return the result of the provision to your Activity through the `onActivityResult(int requestCode, int resultCode, Intent data)` method. You should then refresh the data from your API service and resync the tokens to reflect the new token that was added.

```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  super.onActivityResult(requestCode, resultCode, data);

  if (resultCode == RESULT_OK) {
      switch (requestCode) {
          case REQUEST_CODE_TOKENIZE:
          case REQUEST_CODE_PUSH_TOKENIZE:
              // Successfully added to Android Pay
              // Fetch fresh card data from API service
              break;
      }

```

## Delete Token

A previously provisioned token can be deleted by invoking the `deleteToken` method on the marqetaKit API object. This method requires an `ICard` object that with valid `tokenReferenceId` and `tokenServiceProvider` values. Since the value is dependent on how your service returns the card's network provider, the `tokenServiceProvider` value must be set manually in your ICard implementation object:

```
@Override
public int getTokenServiceProvider() {
    /*
    These values may vary based on what your service returns as the network names
     */
    if (network.equalsIgnoreCase("mastercard")) {
        return TapAndPay.TOKEN_PROVIDER_MASTERCARD;
    } else if (network.equalsIgnoreCase("visa")) {
        return TapAndPay.TOKEN_PROVIDER_VISA;
    } else if (network.equalsIgnoreCase("amex")) {
        return TapAndPay.TOKEN_PROVIDER_AMEX;
    }
    ...

    return -1;
}

```

The `deleteToken` method throws a `MarqetaKitException` if the required values in the `ICard` object are not set. Use a try/catch block to verify the request was processed correctly:

```
   public void deleteCard(ICard card) {
        try {
            marqetaKit.deleteToken(card);
        } catch (MarqetaKitException e) {
            Toast.makeText(this, getString(R.string.failed_to_remove), Toast.LENGTH_LONG).show();
        }
    }
```

The result of the delete will be sent to the `onActivityResult()` method of the Activity through the  `REQUEST_CODE_DELETE_TOKEN` requestCode:

```
 @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

     if (resultCode == RESULT_OK) {
            switch (requestCode) {
                ...
                case REQUEST_CODE_DELETE_TOKEN:
                    //Token has been deleted
                    Toast.makeText(this, getString(R.string.removed_from_android_pay), Toast.LENGTH_LONG).show();
                    break;
```


## Requirements

* Android Studio
* Android devices that support varies screen sizes and resolutions. Testing on devices with other configured payments providers (such as Samsung Pay) is also recommended.
* A provisioning API on your back-end that passes through request and response data to Marqeta's digital wallet provision requests API https://www.marqeta.com/api/docs/WEB4zykAAKUpdPTF/digital-wallets-management#create_digital_wallet_token_provision_request_for_android_pay


## Testing

* Testing In-App Provisioning in production must be done via an app (package name) that has been whitelisted by Google. Please contact your Marqeta customer success representative about whitelisting your App Package Name for testing.
* Testing can be performed in both the sandbox environment and production environment. Please consult [Google's Issuer Help Center](https://support.google.com/androidpay/issuers/answer/7339245?hl=en&ref_topic=6376361) documentation for instructions on how to set up the sandbox environment.
* All testing must be performed on a physical device with an active SIM card.


## License

Access, use, disclosure, distribution or re-distribution of this Digital Wallet SDK is governed by the DIGITAL WALLET SDK LICENSE AGREEMENT. Please contact your Marqeta Customer Success representative to be granted a license.

"Digital Wallet SDK" shall mean the Digital Wallet Software Development Kit, in source code and/or executable code format, together with command files, Documentation, and any derivative works, enhancements, improvements, additions, maintenance modifications, updates, upgrades, or other versions of the foregoing which Marqeta may in its sole discretion elect to provide to You from time to time.
