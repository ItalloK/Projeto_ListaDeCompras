package com.example.listadecompras.api;

import com.example.listadecompras.models.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/register")
    Call<Void> register(@Body RegisterRequest request);

    @POST("auth/refresh")
    Call<AuthResponse> refresh(@Body RefreshRequest request);
}
