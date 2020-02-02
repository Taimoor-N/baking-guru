package com.example.android.bakingguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakingguru.fragments.RecipeDetailFragment;
import com.example.android.bakingguru.fragments.RecipeListFragment;
import com.example.android.bakingguru.util.AppUtil;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String SAVE_INSTANCE_RECIPE_LIST_FRAGMENT_CREATED = "save_instance_recipe_list_fragment_created";

    private int mRecipeId;
    private boolean mRecipeListFragmentCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            populateIntentExtras(intent);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (AppUtil.isTabletOrLandscapeView(this)) {
                RecipeListFragment recipeListFragment = createRecipeListFragment();
                RecipeDetailFragment recipeDetailFragment = createRecipeDetailFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_container, recipeDetailFragment)
                        .add(R.id.recipe_list_container, recipeListFragment)
                        .commit();
                mRecipeListFragmentCreated = true;
            } else {
                RecipeDetailFragment recipeDetailFragment = createRecipeDetailFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_container, recipeDetailFragment)
                        .commit();
                mRecipeListFragmentCreated = false;
            }
        }

        else if (!savedInstanceState.getBoolean(SAVE_INSTANCE_RECIPE_LIST_FRAGMENT_CREATED)) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeListFragment recipeListFragment = createRecipeListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_list_container, recipeListFragment)
                    .commit();
            mRecipeListFragmentCreated = true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        populateIntentExtras(intent);

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeDetailFragment recipeDetailFragment = createRecipeDetailFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();
    }

    private RecipeListFragment createRecipeListFragment() {
        RecipeListFragment recipeListFragment = new RecipeListFragment();
        recipeListFragment.setGridCols(1);
        return recipeListFragment;
    }

    private RecipeDetailFragment createRecipeDetailFragment() {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setRecipeId(mRecipeId);
        return recipeDetailFragment;
    }

    private void populateIntentExtras(Intent intent) {
        mRecipeId = intent.getIntExtra(RecipeListFragment.INTENT_RECIPE_ID, -1);
    }

    @Override
    protected void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putBoolean(SAVE_INSTANCE_RECIPE_LIST_FRAGMENT_CREATED, mRecipeListFragmentCreated);
    }

}
