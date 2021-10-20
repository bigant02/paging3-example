package com.photostream.paging3.API;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Ikhiloya Imokhai on 2019-10-19.
 * <p>
 * Retrofit Interceptor to intercept and decrypt response from the server
 */
public class DecryptionInterceptor implements Interceptor {
    private static final String TAG = DecryptionInterceptor.class.getSimpleName();

    //injects the type of decryption to be used
    public DecryptionInterceptor() {
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Log.d(TAG, "===============DECRYPTING RESPONSE===============");

        return chain.proceed(chain.request());
    }
}