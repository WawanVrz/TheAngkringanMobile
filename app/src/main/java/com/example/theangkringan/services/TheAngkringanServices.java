package com.example.theangkringan.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TheAngkringanServices {

    private static Retrofit retrofit;
    public static final String BASE_URL = "https://theangkringan.harmonydshine.web.id/api/v1/";

    public static Retrofit getRetrofit() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
