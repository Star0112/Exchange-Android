package com.urgentrn.urncexchange.ui.account;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.PlaidAccount;
import com.urgentrn.urncexchange.models.PlaidApi;
import com.urgentrn.urncexchange.models.PlaidData;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.request.PlaidRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.ui.VerifySuccessActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_plaid)
public class PlaidActivity extends BaseActivity implements ApiCallback {

    @ViewById
    WebView webView;

    private PlaidApi plaidApi;

    @AfterViews
    protected void init() {
        plaidApi = (PlaidApi)getIntent().getSerializableExtra("data");

        if (plaidApi == null) { // TODO: when does this happen?
            onBackPressed();
            return;
        }

        showPlaidView();
    }

    private void showPlaidView() {
        // Initialize Link
        final HashMap<String, String> linkInitializeOptions = new HashMap<>();
        linkInitializeOptions.put("key", plaidApi.getKey());
        linkInitializeOptions.put("product", "auth");
        linkInitializeOptions.put("apiVersion", plaidApi.getApiVersion()); // set this to "v1" if using the legacy Plaid API
        linkInitializeOptions.put("version", plaidApi.getVersion());
        for (int i = 0; i < plaidApi.getCountryCodes().size(); i ++) {
            linkInitializeOptions.put("countryCodes" + "[" + i + "]", plaidApi.getCountryCodes().get(i));
        }
        linkInitializeOptions.put("env", plaidApi.getEnv());
        linkInitializeOptions.put("clientName", plaidApi.getClientName());
        linkInitializeOptions.put("selectAccount", "true");
        linkInitializeOptions.put("webhook", "http://requestb.in");
        linkInitializeOptions.put("baseUrl", "https://cdn.plaid.com/link/v2/stable/link.html");
        // If initializing Link in PATCH / update mode, also provide the public_token
        // linkInitializeOptions.put("token", "PUBLIC_TOKEN")

        // Generate the Link initialization URL based off of the configuration options.
        final Uri linkInitializationUrl = generateLinkInitializationUrl(linkInitializeOptions);

        // Modify Webview settings - all of these settings may not be applicable
        // or necessary for your integration.
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        WebView.setWebContentsDebuggingEnabled(true);

        // Initialize Link by loading the Link initialization URL in the WebView
        webView.loadUrl(linkInitializationUrl.toString());

        // Override the WebView's handler for redirects
        // Link communicates success and failure (analogous to the web's onSuccess and onExit
        // callbacks) via redirects.
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Parse the URL to determine if it's a special Plaid Link redirect or a request
                // for a standard URL (typically a forgotten password or account not setup link).
                // Handle Plaid Link redirects and open traditional pages directly in the  user's
                // preferred browser.
                Uri parsedUri = Uri.parse(url);
                if (parsedUri.getScheme() == null) return false;
                if (parsedUri.getScheme().equals("plaidlink")) {
                    String action = parsedUri.getHost();
                    HashMap<String, String> linkData = parseLinkUriData(parsedUri);

                    if (action == null) return false;
                    switch (action) {
                        case "connected":
                            // User successfully linked
                            addAccount(linkData);

                            // Reload Link in the WebView
                            // You will likely want to transition the view at this point.
                            webView.loadUrl(linkInitializationUrl.toString());
                            break;
                        case "exit":
                            onBackPressed();
                            break;
                        case "event":
                            // The event action is fired as the user moves through the Link flow
                            Log.d("Event name: ", linkData.get("event_name"));
                            break;
                        default:
                            Log.d("Link action detected: ", action);
                            break;
                    }
                    // Override URL loading
                    return true;
                } else if (parsedUri.getScheme().equals("https") || parsedUri.getScheme().equals("http")) {
                    // Open in browser - this is most typically for 'account locked' or
                    // 'forgotten password' redirects
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    // Override URL loading
                    return true;
                } else {
                    // Unknown case - do not override URL loading
                    return false;
                }
            }
        });
    }

    private void addAccount(HashMap<String, String> linkData) {
        final User user = ExchangeApplication.getApp().getUser();
        if (user == null) {
            onBackPressed();
            return;
        }

        final PlaidRequest request = new PlaidRequest();

        final PlaidData data = new PlaidData();
        data.setInstitution(linkData.get("institution_id"), linkData.get("institution_name"));
        data.setAccount(new PlaidAccount(
                linkData.get("account_id"),
                linkData.get("account_name"),
                linkData.get("account_type"),
                linkData.get("account_subtype"),
                linkData.get("account_mask")
        ));
        data.setAccountId(linkData.get("account_id"));
        data.setLinkSessionId(linkData.get("link_session_id"));
        data.setPublicToken(linkData.get("public_token"));

        try {
            final List<PlaidAccount> accounts = new ArrayList<>();
            final JSONArray jsonArray = new JSONArray(linkData.get("accounts"));
            for (int i = 0; i < jsonArray.length(); i ++) {
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                accounts.add(new PlaidAccount(
                        jsonObject.getString("_id"),
                        jsonObject.getJSONObject("meta").getString("name"),
                        jsonObject.getString("type"),
                        jsonObject.getString("subtype"),
                        jsonObject.getJSONObject("meta").getString("number")
                ));
            }
            data.setAccounts(accounts);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        request.setPublicToken(linkData.get("public_token"));
        request.setData(data);
        request.setUsername(user.getUsername());

        String callbackPath = plaidApi.getCallbackPath();
        if (!callbackPath.contains("http")) callbackPath = callbackPath.replace("api/", "");
        ApiClient.getInterface()
                .addPlaidAccount(callbackPath, request)
                .enqueue(new AppCallback<>(this, this));
    }

    // Generate a Link initialization URL based on a set of configuration options
    private Uri generateLinkInitializationUrl(HashMap<String,String>linkOptions) {
        Uri.Builder builder = Uri.parse(linkOptions.get("baseUrl"))
                .buildUpon()
                .appendQueryParameter("isWebview", "true")
                .appendQueryParameter("isMobile", "true");
        for (String key : linkOptions.keySet()) {
            if (!key.equals("baseUrl")) {
                builder.appendQueryParameter(key, linkOptions.get(key));
            }
        }
        return builder.build();
    }

    // Parse a Link redirect URL querystring into a HashMap for easy manipulation and access
    private HashMap<String, String> parseLinkUriData(Uri linkUri) {
        HashMap<String, String> linkData = new HashMap<>();
        for (String key : linkUri.getQueryParameterNames()) {
            linkData.put(key, linkUri.getQueryParameter(key));
        }
        return linkData;
    }

    @Override
    public void onResponse(BaseResponse response) {
        Intent intent = new Intent(this, VerifySuccessActivity_.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type", Constants.VerifyType.BANK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onFailure(String message) {

    }
}
