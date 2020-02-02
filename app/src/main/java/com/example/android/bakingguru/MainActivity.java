package com.example.android.bakingguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.android.bakingguru.fragments.RecipeListFragment;
import com.example.android.bakingguru.util.AppUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new RecipeListFragment
        RecipeListFragment recipeListFragment = new RecipeListFragment();

        // Set the number of grid columns for RecipeListFragment
        if (AppUtil.isLandscapeView()) {
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
