package com.example.listadecompras.api;
import com.example.listadecompras.models.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/register")
    Call<ApiResponse> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/refresh")
    Call<AuthResponse> refresh(@Body RefreshRequest request);

    @GET("lists/user/{email}")
    Call<ListResponse> getUserLists(@Path("email") String email);


}
