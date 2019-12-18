package com.example.theangkringan.services;

import android.content.Context;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheAngkringanServices {

    private static Retrofit retrofit;
    public static final String BASE_URL = "https://theangkringan.harmonydshine.web.id/api/v1/";
    private static AppPreferences userPreference;

    public static Retrofit getRetrofit(final Context mContext) {
        userPreference = new AppPreferences(mContext);
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", userPreference.getUserToken(mContext))
                        .build();
                return chain.proceed(request);
            }
        }).build();

        OkHttpClient.Builder okhttp = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp.addInterceptor(logging);

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okhttp.build())
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
