package com.example.android.bakingguru;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingguru.adapters.RecipeAdapter;
import com.example.android.bakingguru.database.Recipe;
import com.example.android.bakingguru.util.MockDataGenerator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeListFragment extends Fragment implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String SAVE_INSTANCE_GRID_COLS = "save_instance_grid_cols";

    public static final String INTENT_RECIPE_ID = "INTENT_RECIPE_ID";

    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private RecipeAdapter mRecipeAdapter;

    private int mGridCols;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mGridCols = savedInstanceState.getInt(SAVE_INSTANCE_GRID_COLS);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), mGridCols);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        mRecipeAdapter.setRecipes(MockDataGenerator.createMockRecipes());
        mRecipeAdapter.setSteps(MockDataGenerator.createMockSteps());

        return rootView;
    }

    /**
     * This function sets the number of columns for grid layout
     * @param gridCols Number of columns for the grid layout
     */
    void setGridCols(int gridCols) {
        mGridCols = gridCols;
    }

    /**
     * This method handles RecyclerView item clicks.
     * @param recipe Recipe data.
     */
    @Override
    public void onClick(Recipe recipe) {
        final Intent intent = new Intent(this.getContext(), RecipeDetailActivity.class);
        intent.putExtra(INTENT_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(SAVE_INSTANCE_GRID_COLS, mGridCols);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
