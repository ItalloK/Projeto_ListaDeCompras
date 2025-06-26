package com.example.listadecompras.models;

import com.google.gson.annotations.SerializedName;

public class RefreshRequest {
    @SerializedName("refresh_token")
    public String refreshToken;

    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
