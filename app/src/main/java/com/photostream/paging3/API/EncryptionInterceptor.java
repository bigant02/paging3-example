package com.photostream.paging3.API;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ikhiloya Imokhai on 2019-10-19.
 * <p>
 * Retrofit Interceptor to intercept and encrypt response from the server
 */
public class EncryptionInterceptor implements Interceptor {
    private static final String TAG = EncryptionInterceptor.class.getSimpleName();

    //injects the type of encryption to be used
    public EncryptionInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Log.d(TAG, "===============ENCRYPTING REQUEST===============");
        return chain.proceed(request);
    }

}