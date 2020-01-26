package com.example.android.bakingguru.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ingredient")
public class Ingredient implements Serializable {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private int recipeId;
    private String name;
    private int quantity;
    private String measure;

    @Ignore
    public Ingredient(int recipeId, String name, int quantity, String measure) {
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
    }

    public Ingredient(int id, int recipeId, String name, int quantity, String measure) {
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

}
