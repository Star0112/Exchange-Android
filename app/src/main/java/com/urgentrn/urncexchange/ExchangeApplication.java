package com.urgentrn.urncexchange;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.urgentrn.urncexchange.api.ApiClient;
import com.urgentrn.urncexchange.fcm.ExchangeMessagingService;
import com.urgentrn.urncexchange.models.AppConfig;
import com.urgentrn.urncexchange.models.AppData;
import com.urgentrn.urncexchange.models.User;
import com.urgentrn.urncexchange.ui.HomeActivity;
import com.urgentrn.urncexchange.ui.HomeActivity_;
import com.urgentrn.urncexchange.ui.SplashActivity;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.utils.AppPreferences;
import com.urgentrn.urncexchange.utils.Constants;
import com.zopim.android.sdk.api.ZopimChat;

import org.greenrobot.eventbus.EventBus;

public class ExchangeApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static ExchangeApplication mApp;

    private Activity currentActivity;
    public int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    private AppPreferences mPreferences;
    private AppConfig mConfig;
    private String mToken;
    private User mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);

        mApp = this;
        mPreferences = new AppPreferences(this, AppPreferences.PREFERENCE_NAME);
        mToken = mPreferences.getToken();

        ZopimChat.init(getString(R.string.zopim_account_id));
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        currentActivity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
        activityReferences = activityReferences + 1;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activityReferences == 1 && !isActivityChangingConfigurations) {
            if (BuildConfig.DEBUG) Log.d(getClass().getSimpleName(), "App enters foreground!!!");
            if (getUser() != null && mPreferences.getPasscode() != null && (mPreferences.termsAccepted()) &&
                    activity instanceof BaseActivity && !(activity instanceof SplashActivity)) {
                ((BaseActivity)activity).showPassDialog(Constants.SecurityType.DEFAULT, isSuccess -> {
                    if (!isSuccess) {
                        logout(activity);
                    }
                });
            }
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        isActivityChangingConfigurations = activity.isChangingConfigurations();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            if (BuildConfig.DEBUG) Log.d(getClass().getSimpleName(), "App enters background!!!");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        mApp = null;
    }

    public static ExchangeApplication getApp() {
        return mApp;
    }

    public AppPreferences getPreferences() {
        return mPreferences;
    }

    public AppConfig getConfig() {
        return mConfig;
    }

    public void setConfig(AppConfig config) {
        this.mConfig = config;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token, boolean saveToPreference) {
        mToken = token;
        mPreferences.setToken(saveToPreference ? token : null);
        ApiClient.setInterface(null);
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        setUser(user, false);
    }

    public void setUser(User user, boolean shouldBroadcast) {
        this.mUser = user;
        if (shouldBroadcast) {
            EventBus.getDefault().post(user);
        }
    }

    public void logout(Activity activity) {
        logout(activity, false);
    }

    public void logout(Activity activity, boolean saveUser) {
        final Intent intent = new Intent(this, HomeActivity_.class);
        if (activity != null) {
            if (activity instanceof HomeActivity) return;
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(intent);
            activity.finish();
        } else if (currentActivity != null) {
            if (currentActivity instanceof HomeActivity) return;
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            currentActivity.startActivity(intent);
            currentActivity.finish();
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        setToken(null, false);
        if (!saveUser) setUser(null);
        mPreferences.clear();
        AppData.getInstance().clearData();
        FirebaseMessaging.getInstance().unsubscribeFromTopic(ExchangeMessagingService.DEFAULT_TOPIC);
    }
}
