package com.example.android.bakingguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;

import com.example.android.bakingguru.database.AppDatabase;
import com.example.android.bakingguru.fragments.RecipeListFragment;
import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.AppExecutors;
import com.example.android.bakingguru.util.AppUtil;
import com.example.android.bakingguru.util.JsonUtils;
import com.example.android.bakingguru.util.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getInstance(getApplicationContext());

        loadBakingRecipesFromServer();
    }

    /**
     * This method will get baking recipes in the background
     */
    private void loadBakingRecipesFromServer() {
        new FetchBakingRecipesTask().execute();
    }

    private class FetchBakingRecipesTask extends AsyncTask<Void, Void, BakingRecipesPojo> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BakingRecipesPojo doInBackground(Void... voids) {
            URL bakingRecipesUrl = NetworkUtils.getBakingRecipesURL();
            try {
                String jsonBakingRecipesResponse = NetworkUtils.getResponseFromHttpUrl(bakingRecipesUrl);
                return JsonUtils.parseBakingRecipesJson(jsonBakingRecipesResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(BakingRecipesPojo bakingRecipesPojo) {
            if (bakingRecipesPojo != null) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    mDb.recipeDao().insertAllRecipes(bakingRecipesPojo.getRecipes());
                    mDb.ingredientDao().insertAllIngredients(bakingRecipesPojo.getIngredients());
                    mDb.stepDao().insertAllSteps(bakingRecipesPojo.getSteps());
                    createAndAddRecipeListFragment(bakingRecipesPojo);
                });
            }
        }
    }

    private void createAndAddRecipeListFragment(BakingRecipesPojo bakingRecipesPojo) {
        RecipeListFragment recipeListFragment = new RecipeListFragment();
        recipeListFragment.setBakingRecipesPojo(bakingRecipesPojo);

        // Set the number of grid columns for RecipeListFragment
        if (AppUtil.isLandscapeView()) {
            recipeListFragment.setGridCols(3);
        } else if (AppUtil.isTabletView(this)) {
            recipeListFragment.setGridCols(2);
        } else {
            recipeListFragment.setGridCols(1);
        }

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_list_container, recipeListFragment)
                .commit();
    }

}
