package com.android.priyanka.pushnotificationandroid;

import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static final String BASE_URL = "https:androidnotificationtutorial.firebaseapp.com/api/";
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        if(retrofit!=null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }



}
