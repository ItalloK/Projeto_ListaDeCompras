package com.example.listadecompras.models;

import com.google.gson.annotations.SerializedName;

public class AuthRequest {
    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("version")
    public String version;

    public AuthRequest(String email, String password, String version) {
        this.email = email;
        this.password = password;
        this.version = version;
    }
}