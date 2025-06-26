package com.example.listadecompras.models;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {
    @SerializedName("name")
    public String name;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("version")
    public String version;

    public RegisterRequest(String name, String email, String password, String version) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.version = version;
    }
}
