package com.example.listadecompras.models;

public class RegisterRequest {
    public String name;
    public String email;
    public String password;
    public String version;

    public RegisterRequest(String name, String email, String password, String version) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.version = version;
    }
}
