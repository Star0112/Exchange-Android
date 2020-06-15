# Swipe Wallet Android Application

This is a Swipe Wallet Android App README to guide how to generate signed apk.

## Installation
Clone this repository and import into **Android Studio**
```bash
git clone https://github.com/SwipeWallet/swipe-wallet-android
```

## Prerequisites
### IDE:
Android Studio 3.4.2 or newer

### SDK:
- `compileSdkVersion: 28`
- `buildToolsVersion: 28.0.3`

## Build variants
Use the Android Studio *Build Variants* button to choose between **development** (sandbox) and **production** flavors combined with debug and release build types


## Generating signed APK
From Android Studio:
1. ***Build*** menu
2. ***Generate Signed APK...***
3. Fill in the keystore information *(you only need to do this once manually and then let Android Studio remember it)*
