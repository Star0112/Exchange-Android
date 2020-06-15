package com.urgentrn.urncexchange.api;

import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.ExchangeApplication;
import com.urgentrn.urncexchange.utils.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiInterface mApiInterface = null;

    public static ApiInterface getInterface() {
        if (mApiInterface == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            if (ExchangeApplication.getApp().getToken() != null) {
                httpClient.addInterceptor(new HeaderInterceptor());
            }

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(loggingInterceptor);
            }

            String apiUrl = Constants.API_URL;
            int timeout = Constants.CONNECT_TIMEOUT;
            if (false && ExchangeApplication.getApp().getConfig() != null) {
                apiUrl = Constants.getApiUrl(ExchangeApplication.getApp().getConfig().getDomain());
                timeout = ExchangeApplication.getApp().getConfig().getTimeout();
            }

            httpClient.addInterceptor(new ConnectivityInterceptor(ExchangeApplication.getApp().getApplicationContext()))
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .connectTimeout(timeout, TimeUnit.SECONDS);

            httpClient.certificatePinner(new CertificatePinner.Builder()
                    .add(apiUrl.replace("https://", "").replace("/api/", ""),
                            "sha256/7MekSxGGmfbaVwRu7fjtBkWMgaTp9mvvn79TDkFrCBQ=",
                            "sha256/EMQHKJCimj0DX6wDD06hilqoSU30bitTgiiKTeW/Dm8=",
                            "sha256/3kcNJzkUJ1RqMXJzFX4Zxux5WfETK+uL6Viq9lJNn4o=",
                            "sha256/Y9mvm0exBk1JoQ57f9Vm28jKo5lFm/woKcVxrYxu80o="
                    )
                    .build());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mApiInterface = retrofit.create(ApiInterface.class);
        }
        return mApiInterface;
    }

    public static void setInterface(ApiInterface apiInterface) {
        mApiInterface = apiInterface;
    }
}
