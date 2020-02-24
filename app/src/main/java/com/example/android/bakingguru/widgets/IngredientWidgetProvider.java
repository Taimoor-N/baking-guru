package com.example.android.bakingguru.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.android.bakingguru.MainActivity;
import com.example.android.bakingguru.R;
import com.example.android.bakingguru.RecipeDetailActivity;
import com.example.android.bakingguru.database.Recipe;
import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.AppUtil;
import com.example.android.bakingguru.util.Constants;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientWidgetProvider extends AppWidgetProvider {

    private static BakingRecipesPojo mBakingRecipesPojo;
    private static int mRecipeId = -1;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, BakingRecipesPojo bakingRecipesPojo, int recipeId) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredient_widget);

        if (bakingRecipesPojo != null) {
            mBakingRecipesPojo = bakingRecipesPojo;
        }

        if (recipeId != -1) {
            mRecipeId = recipeId;
        } else if (mBakingRecipesPojo != null && mBakingRecipesPojo.getRecipes() != null) {
            mRecipeId = mBakingRecipesPojo.getRecipes().get(0).getId();
        }

        Recipe recipeToDisplay = null;
        if (mBakingRecipesPojo != null && mBakingRecipesPojo.getRecipes() != null) {
            for (Recipe recipe : mBakingRecipesPojo.getRecipes()) {
                if (recipe.getId() == mRecipeId) {
                    recipeToDisplay = recipe;
                    break;
                }
            }
        }

        // Populate views for the widget
        if (recipeToDisplay != null) {
            String recipeName = recipeToDisplay.getName();
            String recipeIngredients = AppUtil.getRecipeIngredients(context, mBakingRecipesPojo, mRecipeId);
            String recipeImageUrl = recipeToDisplay.getImageUrl();
            remoteViews.setTextViewText(R.id.tv_widget_recipe_name, recipeName);
            remoteViews.setTextViewText(R.id.tv_widget_recipe_ingredients, recipeIngredients);
            if (recipeImageUrl.equals("")) {
                remoteViews.setImageViewResource(R.id.iv_widget_recipe_thumbnail, R.drawable.ic_photo_placeholder_cake_white);
            } else {
                Picasso.with(context)
                        .load(recipeImageUrl)
                        .error(R.drawable.ic_photo_placeholder_cake_white)
                        .into(remoteViews, R.id.iv_widget_recipe_thumbnail, new int[]{appWidgetId});
            }
        } else {
            remoteViews.setTextViewText(R.id.tv_widget_recipe_name, String.valueOf(R.string.placeholder_recipe_card_name));
            remoteViews.setTextViewText(R.id.tv_widget_recipe_ingredients, String.valueOf(R.string.placeholder_recipe_detail_ingredients));
            remoteViews.setImageViewResource(R.id.iv_widget_recipe_thumbnail, R.drawable.ic_photo_placeholder_cake_white);
        }

        // Create Pending Intent to launch RecipeDetailActivity for the previously clicked recipe
        if (mBakingRecipesPojo != null && mRecipeId != -1) {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, mBakingRecipesPojo);
            intent.putExtra(Constants.INTENT_RECIPE_ID, mRecipeId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Do nothing (i.e. do not do automatic updates to the widget)
    }

    public static void updateIngredientWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, BakingRecipesPojo bakingRecipesPojo, int recipeId) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, bakingRecipesPojo, recipeId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

