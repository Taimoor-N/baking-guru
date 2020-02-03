package com.example.android.bakingguru.model;

import com.example.android.bakingguru.database.Ingredient;
import com.example.android.bakingguru.database.Recipe;
import com.example.android.bakingguru.database.Step;

import java.io.Serializable;
import java.util.ArrayList;

public class BakingRecipesPojo implements Serializable {

    private ArrayList<Recipe> recipes;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;

    public BakingRecipesPojo(ArrayList<Recipe> recipes, ArrayList<Ingredient> ingredients, ArrayList<Step> steps) {
        this.recipes = recipes;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

}
