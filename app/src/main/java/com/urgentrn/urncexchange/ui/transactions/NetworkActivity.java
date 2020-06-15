package com.urgentrn.urncexchange.ui.transactions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetUserResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.WalletUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_network)
public class NetworkActivity extends BaseActivity implements ApiCallback {

    @ViewById
    View llBackground, cardView, indicator;

    @ViewById
    TextView txtTitle, txtAddress;

    private String depositAddress;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getUser();
            handler.postDelayed(this, Constants.API_REQUEST_INTERVAL_SHORT);
        }
    };

    @AfterViews
    protected void init() {
        overridePendingTransition(R.anim.slide_in_up, R.anim.no_animation);

        final int backgroundColor = getResources().getColor(R.color.colorYellow);
        setStatusBarColor(backgroundColor);
        llBackground.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {backgroundColor, Color.WHITE}));

        depositAddress = getIntent().getStringExtra("depositAddress");

        updateView();

        final RotateAnimation rotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(1500);
        rotate.setRepeatCount(Animation.INFINITE);
        indicator.startAnimation(rotate);
    }

    @Override
    protected void onStart() {
        super.onStart();

        handler.postDelayed(runnable, 3);
    }

    @Override
    protected void onStop() {
        super.onStop();

        handler.removeCallbacks(runnable);
    }

    private void getUser() {
        ApiClient.getInterface().getUser().enqueue(new AppCallback<>(this));
    }

    private void updateView() {
        final boolean isPending = ExchangeApplication.getApp().getUser().getSXPStatus() == 1;

        txtTitle.setText(Html.fromHtml(getString(isPending ? R.string.network_pending : R.string.network_activate_hint).replace("1 SXP", "<b>1 SXP</b>")));
        cardView.setVisibility(isPending ? View.INVISIBLE : View.VISIBLE);
        txtAddress.setText(depositAddress);
    }

    public void onCopy(View v) {
        final ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(getString(R.string.referral_code), depositAddress);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, R.string.address_copied, Toast.LENGTH_SHORT).show();
    }

    public void onShare(View v) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, depositAddress);
        startActivity(Intent.createChooser(intent, "Share Deposit Address"));
    }

    public void onClose(View v) {
        onBackPressed();
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetUserResponse) {
            final User user = ((GetUserResponse)response).getData();
            if (user.getActivateSXP() == 1 && ExchangeApplication.getApp().getUser().getActivateSXP() == 0 || user.getSXPStatus() == 0 && ExchangeApplication.getApp().getUser().getSXPStatus() == 1) {
                final Intent intent = new Intent(this, BuySellSuccessActivity_.class);
                intent.putExtra("type", WalletUtils.TransactionType.NETWORK);
                intent.putExtra("name", user.getActivateSXP() == 1 ? "activated" : "deactivated");
                startActivity(intent);
                finish();
            } else {
                updateView();
            }
            ExchangeApplication.getApp().setUser(user, true);
        }
    }

    @Override
    public void onFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.no_animation, R.anim.slide_out_bottom);
    }
}
