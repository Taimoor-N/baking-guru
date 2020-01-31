package com.example.android.bakingguru;


import android.content.Intent;
import android.content.res.Configuration;
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

    public static final String INTENT_RECIPE_ID = "INTENT_RECIPE_ID";

    @BindView(R.id.rv_recipes) RecyclerView mRecyclerView;
    private Unbinder unbinder;

    private RecipeAdapter mRecipeAdapter;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), getRequiredGridCols());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        mRecipeAdapter.setRecipes(MockDataGenerator.createMockRecipes());
        mRecipeAdapter.setSteps(MockDataGenerator.createMockSteps());

        return rootView;
    }

    /**
     * This function returns the number of columns for grid layout
     * - 2 columns for portrait and 3 columns for landscape layout
     * @return number of columns for grid layout
     */
    private int getRequiredGridCols() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
