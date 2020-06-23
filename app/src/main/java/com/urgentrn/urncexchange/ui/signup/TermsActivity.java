package com.urgentrn.urncexchange.ui.signup;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.request.UpdateUserRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.ui.MainActivity_;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_terms)
public class TermsActivity extends BaseActivity implements ApiCallback {

    @ViewById
    ImageView imgArrow1, imgArrow2;

    @ViewById
    WebView webView1, webView2;

    @ViewById
    TextView txtTerms;

    @ViewById
    CheckBox checkBox;

    @ViewById
    Button btnNext;

    @AfterViews
    protected void init() {
        setToolBar(true);

        onTerms(null);

        webView1.setWebViewClient(new WebViewClient());
        webView1.loadUrl("file:///android_asset/terms.html");
        webView2.setWebViewClient(new WebViewClient());
        webView2.loadUrl("file:///android_asset/privacy.html");

        final String termText = getString(R.string.accept_terms)
                .replace(getString(R.string.terms_of_service), "<a href=\"" + Utils.getTermLink("TERMS_OF_USE") + "\">" + getString(R.string.terms_conditions) + "</a>")
                .replace(getString(R.string.privacy_policy), "<a href=\"" + Utils.getTermLink("PRIVACY_POLICY") + "\">" + getString(R.string.privacy_policy) + "</a>");	        webView1.setWebViewClient(new WebViewClient());
        txtTerms.setText(Html.fromHtml(termText + "<br>"));
        txtTerms.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onTerms(View v) {
        imgArrow1.setImageResource(R.mipmap.ic_add);
        imgArrow1.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        imgArrow2.setImageResource(R.mipmap.ic_add);
        imgArrow2.setImageTintList(null);
        webView1.setVisibility(View.VISIBLE);
        webView2.setVisibility(View.GONE);
    }

    public void onPrivacy(View v) {
        imgArrow1.setImageResource(R.mipmap.ic_add);
        imgArrow1.setImageTintList(null);
        imgArrow2.setImageResource(R.mipmap.ic_add);
        imgArrow2.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        webView1.setVisibility(View.GONE);
        webView2.setVisibility(View.VISIBLE);
    }

    @CheckedChange(R.id.checkBox)
    void onCheckedChange(CompoundButton v, boolean isChecked) {
        btnNext.setEnabled(isChecked);
    }

    public void onNext(View v) {
        final UpdateUserRequest request = new UpdateUserRequest();
        request.acceptTerms(true);
        ApiClient.getInterface().updateUser(request).enqueue(new AppCallback<>(this, this));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetUserResponse) {
//            ExchangeApplication.getApp().getUser().acceptTerms(1);
            ExchangeApplication.getApp().getPreferences().acceptTerms(true);

            final Intent intent = new Intent(this, MainActivity_.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        if (Utils.isFromSplash(getIntent())) {
            Intent intent = new Intent(this, TouchIDActivity_.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        finish();
    }
}
