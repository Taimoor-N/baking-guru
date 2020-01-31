package com.example.android.bakingguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

public class RecipeDetailActivity extends AppCompatActivity {

    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        mRecipeId = intent.getIntExtra(RecipeListFragment.INTENT_RECIPE_ID, -1);

        // Create a new RecipeListFragment
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setRecipeId(mRecipeId);

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();
    }

}
