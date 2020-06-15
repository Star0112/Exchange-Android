package com.urgentrn.urncexchange.ui;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_term)
public class TermActivity extends BaseActivity {

    @ViewById
    TextView txtTitle;

    @ViewById
    WebView webView;

    private boolean loadingFinished = true;
    private boolean redirect = false;

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.no_animation);

        setToolBar(true);

        txtTitle.setText(getIntent().getStringExtra("title"));
        final String url = getIntent().getStringExtra("url");
        if (url != null) {
            showProgressBar();
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new TermViewClient());
            webView.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_right);
    }

    private class TermViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (!loadingFinished) {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadingFinished = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
            }

            if (loadingFinished && !redirect) {
                hideProgressBar();
            } else{
                redirect = false;
            }
        }
    }
}
