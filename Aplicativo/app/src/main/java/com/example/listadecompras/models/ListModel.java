package com.example.listadecompras.models;

import java.util.List;

public class ListModel {
    public int id;
    public String title;
    public String code;
    public String createdAt;
    public String expiresAt;
    public List<ItemModel> items;
}