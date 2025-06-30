package com.example.listadecompras.models;

import java.io.Serializable;

public class ItemModel implements Serializable {
    public int id;
    public int listId;
    public String name;
    public int quantity;
    public double value;
}
