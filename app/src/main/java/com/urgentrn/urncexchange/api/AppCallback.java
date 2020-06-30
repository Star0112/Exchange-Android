package com.urgentrn.urncexchange.api;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.urgentrn.urncexchange.R;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.models.request.LoginRequest;
import com.urgentrn.urncexchange.models.response.BaseResponse;
import com.urgentrn.urncexchange.models.response.LoginResponse;
import com.urgentrn.urncexchange.ui.base.BaseActivity;
import com.urgentrn.urncexchange.ui.base.BaseFragment;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppCallback<T> implements Callback<T> {

    private final ApiCallback mCallback;
    private Context mContext;
    private boolean showProgress;

    public AppCallback(ApiCallback callback) {
        this(null, false, callback);
    }

    public AppCallback(Context context, ApiCallback callback) {
        this(context, context != null, callback);
    }

    public AppCallback(Context context, boolean showProgress, ApiCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        this.showProgress = showProgress;
        if (callback instanceof BaseFragment) {
            ((BaseFragment)callback).setLoading(true);
            ((BaseFragment)mCallback).showProgressBar();
        }
        if (showProgress && mContext instanceof BaseActivity) {
            ((BaseActivity)mContext).showProgressBar();
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (mCallback instanceof BaseFragment) {
            ((BaseFragment)mCallback).setLoading(false);
            ((BaseFragment)mCallback).hideProgressBar();
        }
        if (showProgress && mContext instanceof BaseActivity) {
            ((BaseActivity)mContext).hideProgressBar();
        }
        String errorMessage = null;
        if (response.isSuccessful()) {
            BaseResponse baseResponse = (BaseResponse) response.body();
            if (baseResponse == null) {
                errorMessage = "Internal server error";
            } else if (baseResponse.getError() != null) {
                errorMessage = baseResponse.getErrorMessage();
            } else if (baseResponse.isSuccess().equals("ok")) {
                if (mCallback instanceof BaseFragment) {
                    if (!((BaseFragment) mCallback).isAdded()) return;
                } else if (mContext instanceof BaseActivity) {
                    if (((BaseActivity) mContext).isFinishing()) return;
                }
                mCallback.onResponse(baseResponse);
                return;
            } else {
                errorMessage = "Server error";
            }
        } else if (response.code() == 401) {
            if (ExchangeApplication.getApp().getToken() == null) return;
            final LoginRequest request = new LoginRequest(ExchangeApplication.getApp().getPreferences().getUsername());
            request.setRefreshToken(ExchangeApplication.getApp().getPreferences().getRefreshToken());
            ApiClient.getInterface()
                    .login(request)
                    .enqueue(new AppCallback<>(new ApiCallback() {
                        @Override
                        public void onResponse(BaseResponse loginResponse) {
                            final LoginResponse data = (LoginResponse)loginResponse;
                            ExchangeApplication.getApp().setToken(data.getAccessToken(), true);
                            ExchangeApplication.getApp().setUser(data.getUser());

                            mCallback.onResponse(loginResponse);
                        }

                        @Override
                        public void onFailure(String message) {
                            ExchangeApplication.getApp().logout(mContext instanceof Activity ? (Activity)mContext : null, true);
                            mCallback.onFailure(message);
                        }
                    }));
            ExchangeApplication.getApp().setToken(null, false);
            return;
        } else if (response.code() == 403 || response.code() == 522) {
            errorMessage = "Your connection was blocked due to security check. Please try again later.";
        } else if (response.code() == 404) {
            errorMessage = "This feature doesn't exist yet. Please try again later.";
        } else if (response.code() == 500) {
            errorMessage = response.message();
        } else {
            errorMessage = "unexpected error";
        }

        if (showProgress && mContext instanceof BaseActivity) {
            ((BaseActivity)mContext).showAlert(errorMessage);
        }
        mCallback.onFailure(errorMessage);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mCallback instanceof BaseFragment) {
            ((BaseFragment)mCallback).setLoading(false);
            ((BaseFragment)mCallback).hideProgressBar();
        }
        if (t instanceof UnknownHostException || t instanceof SocketTimeoutException) {
            if (mContext instanceof BaseActivity) {
                if (showProgress) {
                    ((BaseActivity)mContext).hideProgressBar();
                    ((BaseActivity)mContext).showAlert(mContext.getString(R.string.no_internet_connection));
                } else {
                    Toast.makeText(mContext, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
                    mCallback.onFailure(mContext.getString(R.string.no_internet_connection));
                }
            } else {
                mCallback.onFailure("There's no internet connection");
            }
            return;
        }
        if (mContext instanceof BaseActivity) {
            if (showProgress) {
                ((BaseActivity)mContext).hideProgressBar();
                ((BaseActivity)mContext).showAlert(t.getLocalizedMessage());
            } else if (t instanceof ConnectivityInterceptor.NoConnectivityException) {
                ((BaseActivity)mContext).showAlert(t.getLocalizedMessage(), (dialog, which) -> mCallback.onFailure(t.getLocalizedMessage()));
                return;
            }
        }
        mCallback.onFailure(t.getLocalizedMessage());
    }
}
