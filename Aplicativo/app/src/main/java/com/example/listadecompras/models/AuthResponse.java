package com.example.listadecompras.models;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status")
    public boolean status;

    @SerializedName("message")
    public String message;

    @SerializedName("token")
    public String token;

    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("role")
    public String role;
}
