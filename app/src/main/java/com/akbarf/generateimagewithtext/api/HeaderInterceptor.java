package com.akbarf.generateimagewithtext.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private final String mApiKey;

    public HeaderInterceptor(@Nullable String apiKey) {
        this.mApiKey = apiKey;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder requestBuilder = originalRequest.newBuilder()
                .header("Authorization", mApiKey);

        Request modifiedRequest = requestBuilder.build();
        return chain.proceed(modifiedRequest);
    }
}
