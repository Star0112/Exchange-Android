package com.urgentrn.urncexchange.ui.fragments.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.api.ApiCallback;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.api.AppCallback;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.Banner;
import com.urgentrn.urncexchange.models.NetworkStatus;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.models.request.UpdateUserRequest;
import com.urgentrn.urncexchange.models.response.ActivateResponse;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.GetAccountsResponse;
import com.urgentrn.urncexchange.models.response.GetBannersResponse;
//import com.urgentrn.urncexchange.ui.DocsActivity_;
//import com.urgentrn.urncexchange.ui.account.AddAccountActivity_;
//import com.urgentrn.urncexchange.ui.account.ManageAccountActivity_;
import com.urgentrn.urncexchange.ui.adapter.BannerPagerAdapter;
import com.urgentrn.urncexchange.ui.adapter.LinkedAccountAdapter;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;
import com.urgentrn.urncexchange.ui.contacts.ManageContactsActivity_;
import com.urgentrn.urncexchange.ui.kyc.DocUploadActivity_;
import com.urgentrn.urncexchange.ui.transactions.NetworkActivity_;
import com.urgentrn.urncexchange.ui.view.CircleIndicator;
import com.urgentrn.urncexchange.utils.Constants;
import com.urgentrn.urncexchange.utils.Utils;
import com.zopim.android.sdk.api.ZopimChat;
import com.zopim.android.sdk.model.VisitorInfo;
import com.zopim.android.sdk.prechat.EmailTranscript;
import com.zopim.android.sdk.prechat.PreChatForm;
import com.zopim.android.sdk.prechat.ZopimChatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import me.aflak.libraries.dialog.FingerprintDialog;

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends BaseFragment implements ApiCallback {

    @ViewById
    TextView txtUserName, txtTierLevel, txtVerifyTitle, txtVerifyDescription, txtLevel;

    @ViewById
    View llVerify, llBanner, llNetworkPending;

    @ViewById
    ViewPager viewPager;

    @ViewById
    CircleIndicator indicator;

    @ViewById
    RecyclerView recyclerView;

    @ViewById
    Switch switchDiscounts, switchPasscode, switchBiometrics, switch2fa;

    private BannerPagerAdapter bannerAdapter;
    private LinkedAccountAdapter adapter;
    private static List<Banner> banners;

    @AfterViews
    protected void init() {
        setBackgroundColor(getResources().getColor(R.color.colorBackground));
        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        // Linked Accounts View
//        adapter = new LinkedAccountAdapter(position -> startActivity(new Intent(getContext(), ManageAccountActivity_.class)));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        final DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
//        recyclerView.addItemDecoration(decoration);
//        recyclerView.setAdapter(adapter);
//
//        switchPasscode.setChecked(ExchangeApplication.getApp().getPreferences().isPasscodeEnabled());
//        switchBiometrics.setChecked(ExchangeApplication.getApp().getPreferences().isFingerprintEnabled());
    }

    @Override
    public void onResume() {
        super.onResume();

        updateView(getUser()); // calling this function here because of new verification flow
        updateAccountView(null); // calling this function here because of remove event
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(User user) {
        if (user == null) return;

//        final int tierLevel = user.getTierLevel();

        final String username = user.getUsername();
        if (username.isEmpty()) {
            txtUserName.setVisibility(View.GONE);
        } else {
            txtUserName.setVisibility(View.VISIBLE);
            txtUserName.setText(username);
        }
//        txtTierLevel.setText(getString(R.string.tier, tierLevel));
//
//        if (tierLevel < 2) {
//            llVerify.setVisibility(View.VISIBLE);
//            llBanner.setVisibility(View.GONE);
//            if (tierLevel == 0) {
//                txtVerifyTitle.setText(R.string.access_wallet);
//                txtVerifyDescription.setText(R.string.complete_profile);
//            } else if (tierLevel == 1) {
//                txtVerifyTitle.setText(R.string.access_order);
//                txtVerifyDescription.setText(R.string.complete_tier2);
//            }
//        } else {
//            llVerify.setVisibility(View.GONE);
//            llBanner.setVisibility(View.VISIBLE);
//            if (banners == null) {
//                ApiClient.getInterface().getBanners().enqueue(new AppCallback<>(this));
//            } else {
//                showBanner();
//            }
//        }
//
//        txtLevel.setText(String.format(Locale.US, "%s %d", getString(R.string.level), tierLevel));

        updateNetworkView();

//        switch2fa.setChecked(user.isMfa());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAccountView(GetAccountsResponse data) {
        adapter.setData(AppData.getInstance().getAccounts());
    }

    private void updateNetworkView() {
//        if (getUser() == null) return;
//        if (getUser().getSXPStatus() == 1 && getUser().getActivateSXP() == 0) {
//            llNetworkPending.setVisibility(View.VISIBLE);
//            switchDiscounts.setVisibility(View.GONE);
//        } else {
//            llNetworkPending.setVisibility(View.GONE);
//            switchDiscounts.setChecked(getUser().getActivateSXP() == 1);
//            switchDiscounts.setVisibility(View.VISIBLE);
//        }
    }

    private void showBanner() {
        if (banners.size() > 0) {
            if (bannerAdapter == null) {
                bannerAdapter = new BannerPagerAdapter(banners);
                viewPager.setAdapter(bannerAdapter);
                indicator.setViewPager(viewPager);
            }
        } else {
            llBanner.setVisibility(View.GONE);
        }
    }

    @Click(R.id.btnVerify)
    void onVerifyAccountClicked() {
        if (getUser() == null) return;
        startActivity(new Intent(getContext(), DocUploadActivity_.class));
    }

    @Click(R.id.llAddPayment)
    void onAddPaymentClicked() {
//        if (AppData.getInstance().getFlowData() == null) return;
//        final Intent intent = new Intent(getContext(), AddAccountActivity_.class);
//        startActivity(intent);
    }

    @Click(R.id.llTierLevel)
    void onTierLevelClicked() {
        ((BaseFragment)getParentFragment()).replaceFragment(new AccountLevelFragment_(), false);
    }

    @Click(R.id.llRewards)
    void onRewardsClicked() {
        ((BaseFragment)getParentFragment()).replaceFragment(new RewardsFragment_(), false);
    }

    @CheckedChange(R.id.switchDiscounts)
    void onDiscountsChecked(boolean isChecked) { // SXP Network
//        if (getUser() == null || isChecked == (getUser().getActivateSXP() == 1)) return;
//        final HashMap<String, Integer> request = new HashMap<>();
//        request.put("activation", 1 - getUser().getActivateSXP());
//        ApiClient.getInterface().activateSXP(request).enqueue(new AppCallback<>(getContext(), new ApiCallback() {
//            @Override
//            public void onResponse(BaseResponse response) {
//                if (response instanceof ActivateResponse) {
//                    final NetworkStatus data = ((ActivateResponse)response).getData();
//                    getUser().setSXPStatus(data.getSXPStatus());
//                    getUser().setActivateSXP(data.getActivateSXP());
//                    updateNetworkView();
//
//                    final Intent intent = new Intent(getContext(), NetworkActivity_.class);
//                    intent.putExtra("depositAddress", data.getDepositAddress());
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onFailure(String message) {
//                switchDiscounts.setChecked(!isChecked);
//            }
//        }));
    }

    @Click(R.id.llNetwork)
    void onNetworkClicked() {
        if (switchDiscounts.getVisibility() == View.VISIBLE) return;
        final Intent intent = new Intent(getContext(), NetworkActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.llAddress)
    void onAddressClicked() {
//        if (getUser() == null) return;
//        if (getUser().getTierLevel() == 0) {
//            onVerifyAccountClicked();
//        } else {
//            ((BaseFragment)getParentFragment()).replaceFragment(new AddressFragment_(), false);
//        }
    }

    @Click(R.id.llPhone)
    void onPhoneClicked() {
        if (getUser() == null) return;
        ((BaseFragment)getParentFragment()).replaceFragment(new PhoneFragment_(), false);
    }

    @Click(R.id.llContacts)
    void onContactsClicked() {
        final Intent intent = new Intent(getContext(), ManageContactsActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.llDocs)
    void onDocsClicked() {
//        if (ExchangeApplication.getApp().getConfig() == null) return;
//        final Intent intent = new Intent(getContext(), DocsActivity_.class);
//        startActivity(intent);
    }

    @Click(R.id.llCurrency)
    void onCurrencyClicked() {
        final List<String> options = new ArrayList<>();
        options.add(getString(R.string.set_default_currency));
        options.add(getString(R.string.add_currencies));

        final String[] items = new String[options.size()];
        options.toArray(items);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.multi_currency)
                .setItems(items, (dialog, which) -> {
                    final CurrencyFragment fragment = new CurrencyFragment_();
                    final Bundle args = new Bundle();
                    args.putBoolean("is_default", which == 0);
                    fragment.setArguments(args);
                    ((BaseFragment)getParentFragment()).replaceFragment(fragment, false);
                })
                .show();
    }

    @CheckedChange(R.id.switchPasscode)
    void onPasscodeChecked(boolean isChecked) {
        if (getActivity() == null) return;
        if (isChecked == ExchangeApplication.getApp().getPreferences().isPasscodeEnabled()) return;
        if (isChecked) {
            ExchangeApplication.getApp().getPreferences().setPasscodeEnabled(true);
        } else {
            ((BaseActivity)getActivity()).showPasscodeDialog(Constants.SecurityType.SETTING, null, isSuccess -> {
                if (isSuccess) {
                    ExchangeApplication.getApp().getPreferences().setPasscodeEnabled(false);
                } else {
                    switchPasscode.setChecked(true);
                }
            });
        }
    }

    @CheckedChange(R.id.switchBiometrics)
    void onBiometricsChecked(boolean isChecked) {
        if (getActivity() == null) return;
        if (isChecked == ExchangeApplication.getApp().getPreferences().isFingerprintEnabled()) return;
        if (!FingerprintDialog.isAvailable(getContext())) {
            showAlert("Fingerprint is not available");
            ExchangeApplication.getApp().getPreferences().setFingerprintEnabled(false);
            switchBiometrics.setChecked(false);
            return;
        }
        if (isChecked) {
            ExchangeApplication.getApp().getPreferences().setFingerprintEnabled(isChecked);
        } else if (!isChecked) {
            ((BaseActivity)getActivity()).showFingerprintDialog(Constants.SecurityType.SETTING, isSuccess -> {
                if (isSuccess) {
                    ExchangeApplication.getApp().getPreferences().setFingerprintEnabled(isChecked);
                } else {
                    switchBiometrics.setChecked(!isChecked);
                }
            });
        }
    }

    @CheckedChange(R.id.switch2fa)
    void on2faChecked(boolean isChecked) {
//        if (getUser() == null || isChecked == getUser().isMfa()) return;
//        final UpdateUserRequest request = new UpdateUserRequest();
//        request.setMfa(isChecked);
//        ApiClient.getInterface().updateUser(request).enqueue(new AppCallback<>(getContext(), new ApiCallback() {
//            @Override
//            public void onResponse(BaseResponse response) {
//                getUser().setMfa(isChecked);
//            }
//
//            @Override
//            public void onFailure(String message) {
//                switch2fa.setChecked(!isChecked);
//            }
//        }));
    }

    @Click(R.id.llHelp)
    void onHelpClicked() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://help.exchange.io")));
    }

    @Click(R.id.llSupport)
    void onSupportClicked() {
        Utils.sendMail(getActivity());
    }

    @Click(R.id.llChat)
    void onChatClicked() {
        initializeChatSdk();

        final PreChatForm build = new PreChatForm.Builder()
                .name(PreChatForm.Field.REQUIRED)
                .email(PreChatForm.Field.REQUIRED)
                .phoneNumber(PreChatForm.Field.REQUIRED)
                .build();

        final ZopimChat.SessionConfig department = new ZopimChat.SessionConfig()
                .preChatForm(build)
                .department("support")
                .emailTranscript(EmailTranscript.DISABLED)
                .tags("subscription", "android");

        ZopimChatActivity.startActivity(getContext(), department);
    }

    /**
     * Init Zopim Visitor info
     */
    private void initializeChatSdk() {
        if (getUser() == null) return;
//        final VisitorInfo.Builder builder = new VisitorInfo.Builder()
//                .name(getUser().getUsername())
//                .email(getUser().getEmail())
//                .note(Utils.getDeviceInfo())
//                .phoneNumber(getUser().getPhone());
                
//        ZopimChat.setVisitorInfo(builder.build());
    }

    @Click(R.id.llSignOut)
    void onSignOut() {
        ApiClient.getInterface().logout().enqueue(new AppCallback<>(getContext(), new ApiCallback() {
            @Override
            public void onResponse(BaseResponse response) {
                ExchangeApplication.getApp().logout(getActivity());
            }

            @Override
            public void onFailure(String message) {
                ExchangeApplication.getApp().logout(getActivity());
            }
        }));
    }

    @Override
    public void onResponse(BaseResponse response) {
        if (response instanceof GetBannersResponse) {
            banners = ((GetBannersResponse)response).getData();
            if (banners == null) banners = new ArrayList<>();
            showBanner();
        }
    }

    @Override
    public void onFailure(String message) {

    }
}
