package com.photostream.paging3.API;

import com.google.common.util.concurrent.ListenableFuture;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import com.photostream.paging3.Model.MovieResponse;

/*
    This class will provide us single instance of Retrofit
 */
public class APIClient {
    // Define APIInterface
    static APIInterface apiInterface;

    // Method to get APIInterface
    public static APIInterface getAPIInterface() {

        // Check for null
        if (apiInterface == null) {

            // Optional - Setup Http logging for debug purpose
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            // Create OkHttp Client
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            // Set logging
            client.addInterceptor(logging);
            client.addInterceptor(new EncryptionInterceptor());
            client.addInterceptor(new DecryptionInterceptor());
            // Add request interceptor to add API key as query string parameter to each request
            client.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        // Add API Key as query string parameter
                        .addQueryParameter("api_key", "18e67b8f8c2c58b337b67a8699039df1")
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
            // Create retrofit instance
            Retrofit retrofit = new Retrofit.Builder()
                    // set base url
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(GuavaCallAdapterFactory.create())
                    .client(client.build())
                    .build();

            // Init APIInterface
            apiInterface = retrofit.create(APIInterface.class);
        }
        return apiInterface;
    }

    /*
        Define API request calls in this interface
     */
    public interface APIInterface {
        // Define Get request with query string parameter as page number
        @GET("movie/popular")
        ListenableFuture<MovieResponse> getMoviesByPage(@Query("page") long page);
    }
}
