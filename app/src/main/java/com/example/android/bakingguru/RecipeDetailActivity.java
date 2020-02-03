package com.example.android.bakingguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakingguru.fragments.RecipeDetailFragment;
import com.example.android.bakingguru.fragments.RecipeListFragment;
import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.AppUtil;
import com.example.android.bakingguru.util.Constants;

public class RecipeDetailActivity extends AppCompatActivity {

    private BakingRecipesPojo mBakingRecipesPojo;

    private int mRecipeId;
    private boolean mRecipeListFragmentCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState != null) {
            mBakingRecipesPojo = (BakingRecipesPojo) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO);
            mRecipeListFragmentCreated = savedInstanceState.getBoolean(Constants.SAVE_INSTANCE_RECIPE_LIST_FRAGMENT_CREATED);
        }

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            populateIntentExtras(intent);

            if (AppUtil.isTabletOrLandscapeView(this)) {
                createAndAddRecipeListFragment();
                createAndAddRecipeDetailFragment();
            } else {
                createAndAddRecipeDetailFragment();
                mRecipeListFragmentCreated = false;
            }
        }
        else if (!mRecipeListFragmentCreated) {
            createAndAddRecipeListFragment();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        populateIntentExtras(intent);
        createAndReplaceRecipeDetailFragment();
    }

    private void createAndAddRecipeListFragment() {
        RecipeListFragment recipeListFragment = new RecipeListFragment();
        recipeListFragment.setBakingRecipesPojo(mBakingRecipesPojo);
        recipeListFragment.setGridCols(1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_list_container, recipeListFragment)
                .commit();
        mRecipeListFragmentCreated = true;
    }

    private void createAndAddRecipeDetailFragment() {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setBakingRecipesPojo(mBakingRecipesPojo);
        recipeDetailFragment.setRecipeId(mRecipeId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();
    }

    private void createAndReplaceRecipeDetailFragment() {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setBakingRecipesPojo(mBakingRecipesPojo);
        recipeDetailFragment.setRecipeId(mRecipeId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();
    }

    private void populateIntentExtras(Intent intent) {
        mBakingRecipesPojo = (BakingRecipesPojo) intent.getSerializableExtra(Constants.INTENT_BAKING_RECIPES_POJO);
        mRecipeId = intent.getIntExtra(Constants.INTENT_RECIPE_ID, -1);
    }

    @Override
    protected void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO, mBakingRecipesPojo);
        currentState.putBoolean(Constants.SAVE_INSTANCE_RECIPE_LIST_FRAGMENT_CREATED, mRecipeListFragmentCreated);
    }

}
