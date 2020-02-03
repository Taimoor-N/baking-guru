package com.example.android.bakingguru.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingguru.R;
import com.example.android.bakingguru.RecipeDetailActivity;
import com.example.android.bakingguru.adapters.RecipeAdapter;
import com.example.android.bakingguru.database.AppDatabase;
import com.example.android.bakingguru.database.Recipe;
import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeListFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private AppDatabase mDb;
    private RecipeAdapter mRecipeAdapter;
    private BakingRecipesPojo mBakingRecipesPojo;

    private int mGridCols;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mDb = AppDatabase.getInstance(getContext());
        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mBakingRecipesPojo = (BakingRecipesPojo) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO);
            mGridCols = savedInstanceState.getInt(Constants.SAVE_INSTANCE_GRID_COLS);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), mGridCols);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        mRecipeAdapter.setRecipes(mBakingRecipesPojo.getRecipes());
        mRecipeAdapter.setSteps(mBakingRecipesPojo.getSteps());

        return rootView;
    }

    /**
     * This function sets the number of columns for grid layout
     * @param gridCols Number of columns for the grid layout
     */
    public void setGridCols(int gridCols) {
        mGridCols = gridCols;
    }

    public void setBakingRecipesPojo(BakingRecipesPojo bakingRecipesPojo) {
        mBakingRecipesPojo = bakingRecipesPojo;
    }

    /**
     * This method handles RecyclerView item clicks.
     * @param recipe Recipe data.
     */
    @Override
    public void onClick(Recipe recipe) {
        final Intent intent = new Intent(this.getContext(), RecipeDetailActivity.class);
        intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, mBakingRecipesPojo);
        intent.putExtra(Constants.INTENT_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO, mBakingRecipesPojo);
        currentState.putInt(Constants.SAVE_INSTANCE_GRID_COLS, mGridCols);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
