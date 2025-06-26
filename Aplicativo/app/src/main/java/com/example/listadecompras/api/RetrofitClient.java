package com.example.listadecompras.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getInstance(String token) {
        return new Retrofit.Builder()
                .baseUrl("https://192.168.0.122:5001/api/") // Sua URL local
                .addConverterFactory(GsonConverterFactory.create())
                .client(AuthInterceptor.getClient(token))
                .build();
    }
}
