package com.urgentrn.urncexchange.api;

import com.urgentrn.urncexchange.ExchangeApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request original = chain.request();
        Request request;

        final String token = ExchangeApplication.getApp().getToken();
        if (token != null) {
            request = original.newBuilder()
                    .header("Authorization", token)
                    .method(original.method(), original.body())
                    .build();
        } else {
            request = original;
        }

        return chain.proceed(request);
    }
}
