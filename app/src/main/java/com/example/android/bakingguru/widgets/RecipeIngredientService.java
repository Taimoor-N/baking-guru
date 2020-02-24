package com.example.android.bakingguru.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.Constants;

public class RecipeIngredientService extends IntentService {

    public RecipeIngredientService() {
        super("RecipeIngredientService");
    }

    public static void startActionUpdateRecipeIngredientWidget(Context context, BakingRecipesPojo bakingRecipesPojo, int recipeId) {
        Intent intent = new Intent(context, RecipeIngredientService.class);
        intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, bakingRecipesPojo);
        intent.putExtra(Constants.INTENT_RECIPE_ID, recipeId);
        intent.setAction(Constants.ACTION_UPDATE_INGREDIENT_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.ACTION_UPDATE_INGREDIENT_WIDGET.equals(action)) {
                BakingRecipesPojo bakingRecipesPojo = (BakingRecipesPojo) intent.getSerializableExtra(Constants.INTENT_BAKING_RECIPES_POJO);
                int recipeId = intent.getIntExtra(Constants.INTENT_RECIPE_ID, -1);
                handleActionUpdateIngredientWidget(bakingRecipesPojo, recipeId);
            }
        }
    }

    private void handleActionUpdateIngredientWidget(BakingRecipesPojo bakingRecipesPojo, int recipeId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidgetProvider.class));
        // Update all widgets
        IngredientWidgetProvider.updateIngredientWidgets(this, appWidgetManager, appWidgetIds, bakingRecipesPojo, recipeId);
    }

}
