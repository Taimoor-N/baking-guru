package com.example.android.bakingguru.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.android.bakingguru.MainActivity;
import com.example.android.bakingguru.R;
import com.example.android.bakingguru.database.Ingredient;
import com.example.android.bakingguru.model.BakingRecipesPojo;

import java.util.ArrayList;

public class AppUtil {

    public static boolean isTabletView(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static boolean isLandscapeView() {
        return Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isTabletOrLandscapeView(Context context) {
        return isTabletView(context) || isLandscapeView();
    }

    public static String getRecipeIngredients(Context context, BakingRecipesPojo bakingRecipesPojo, int recipeId) {
        ArrayList<Ingredient> ingredients = bakingRecipesPojo.getIngredients();
        ArrayList<Ingredient> matchingIngredients = new ArrayList<>();
        String recipeIngredients = "";

        // Isolate ingredients that match with the Recipe ID
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRecipeId() == recipeId) {
                matchingIngredients.add(ingredient);
            }
        }

        int numOfIngredients = matchingIngredients.size();
        for (int i=0; i<numOfIngredients; i++) {
            recipeIngredients += context.getResources().getString(R.string.recipe_detail_ingredients, matchingIngredients.get(i).getQuantity(), matchingIngredients.get(i).getMeasure(), matchingIngredients.get(i).getName());
            if (numOfIngredients > 1 && i < (numOfIngredients - 1)){
                recipeIngredients += ", ";
            }
            if (i == (numOfIngredients - 1)) {
                recipeIngredients += ".";
            }
        }

        return recipeIngredients;
    }
}
