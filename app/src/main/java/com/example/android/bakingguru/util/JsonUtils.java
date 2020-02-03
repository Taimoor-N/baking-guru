package com.example.android.bakingguru.util;

import com.example.android.bakingguru.database.Ingredient;
import com.example.android.bakingguru.database.Recipe;
import com.example.android.bakingguru.database.Step;
import com.example.android.bakingguru.model.BakingRecipesPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    public static BakingRecipesPojo parseBakingRecipesJson(String json) throws JSONException {

        final String JSON_RECIPE_ID = "id";
        final String JSON_RECIPE_NAME = "name";
        final String JSON_RECIPE_SERVINGS = "servings";
        final String JSON_RECIPE_IMAGE_URL = "image";

        final String JSON_INGREDIENTS_TITLE = "ingredients";
        final String JSON_INGREDIENT_NAME = "ingredient";
        final String JSON_INGREDIENT_QUANTITY = "quantity";
        final String JSON_INGREDIENT_MEASURE = "measure";

        final String JSON_STEPS_TITLE = "steps";
        final String JSON_STEP_ID = "id";
        final String JSON_STEP_SHORT_DESC = "shortDescription";
        final String JSON_STEP_DESC = "description";
        final String JSON_STEP_VIDEO_URL = "videoURL";
        final String JSON_STEP_THUMBNAIL_URL = "thumbnailURL";

        ArrayList<Recipe> recipes = new ArrayList<>();
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ArrayList<Step> steps = new ArrayList<>();

        Recipe currentRecipe;
        Ingredient currentIngredient;
        Step currentStep;

        JSONArray recipeArray = new JSONArray(json);
        for (int i = 0; i < recipeArray.length(); i++) {
            try {
                // Populate recipe details and add to recipe list (i.e. recipes)
                JSONObject recipeData = recipeArray.getJSONObject(i);
                currentRecipe = new Recipe();
                currentRecipe.setId(recipeData.getInt(JSON_RECIPE_ID));
                currentRecipe.setName(recipeData.getString(JSON_RECIPE_NAME));
                currentRecipe.setServings(recipeData.getInt(JSON_RECIPE_SERVINGS));
                currentRecipe.setImageUrl(recipeData.getString(JSON_RECIPE_IMAGE_URL));
                recipes.add(currentRecipe);

                JSONArray ingredientArray = recipeData.getJSONArray(JSON_INGREDIENTS_TITLE);
                for (int index = 0; index < ingredientArray.length(); index++) {
                    // Populate ingredient details and add to ingredient list (i.e. ingredients)
                    JSONObject ingredientData = ingredientArray.getJSONObject(index);
                    currentIngredient = new Ingredient();
                    currentIngredient.setRecipeId(currentRecipe.getId());
                    currentIngredient.setName(ingredientData.getString(JSON_INGREDIENT_NAME));
                    currentIngredient.setQuantity(ingredientData.getInt(JSON_INGREDIENT_QUANTITY));
                    currentIngredient.setMeasure(ingredientData.getString(JSON_INGREDIENT_MEASURE));
                    ingredients.add(currentIngredient);
                }

                JSONArray stepArray = recipeData.getJSONArray(JSON_STEPS_TITLE);
                for (int index = 0; index < stepArray.length(); index++) {
                    // Populate step details and add to step list (i.e. steps)
                    JSONObject stepData = stepArray.getJSONObject(index);
                    currentStep = new Step();
                    currentStep.setRecipeId(currentRecipe.getId());
                    currentStep.setId(stepData.getInt(JSON_STEP_ID));
                    currentStep.setShortDescription(stepData.getString(JSON_STEP_SHORT_DESC));
                    currentStep.setDescription(stepData.getString(JSON_STEP_DESC));
                    currentStep.setVideoUrl(stepData.getString(JSON_STEP_VIDEO_URL));
                    currentStep.setThumbnailUrl(stepData.getString(JSON_STEP_THUMBNAIL_URL));
                    steps.add(currentStep);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return new BakingRecipesPojo(recipes, ingredients, steps);
    }

}
