package com.example.listadecompras.models;

public class AuthRequest {
    public String email;
    public String password;
    public String version;

    public AuthRequest(String email, String password, String version) {
        this.email = email;
        this.password = password;
        this.version = version;
    }
}

