package com.example.listadecompras.api;
import com.example.listadecompras.models.*;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;

public interface ApiService {
    @POST("auth/register")
    Call<ApiResponse> register(@Body RegisterRequest request);

    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest request);

    @POST("auth/refresh")
    Call<AuthResponse> refresh(@Body RefreshRequest request);

    @GET("lists/user/{email}")
    Call<ListResponse> getUserLists(@Path("email") String email);

    @PUT("lists/update/{id}")
    Call<ApiResponse> updateList(@Path("id") int listId, @Body ListModel listModel);

    @DELETE("lists/delete/{id}")
    Call<ApiResponse> deleteList(@Path("id") int id);

}
