package com.example.android.bakingguru.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingguru.R;
import com.example.android.bakingguru.StepDetailActivity;
import com.example.android.bakingguru.adapters.StepAdapter;
import com.example.android.bakingguru.database.Ingredient;
import com.example.android.bakingguru.database.Step;
import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    @BindView(R.id.rv_steps) RecyclerView mRecyclerView;
    @BindView(R.id.tv_recipe_detail_ingredients_list) TextView mRecipeIngredients;

    private int mRecipeId;
    private BakingRecipesPojo mBakingRecipesPojo;

    private Unbinder unbinder;
    private StepAdapter mStepAdapter;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mBakingRecipesPojo = (BakingRecipesPojo) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO);
            mRecipeId = savedInstanceState.getInt(Constants.SAVE_INSTANCE_RECIPE_ID);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mStepAdapter = new StepAdapter(this);
        mRecyclerView.setAdapter(mStepAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        mStepAdapter.setSteps(getRecipeSteps(mRecipeId));

        mRecipeIngredients.setText(getRecipeIngredients(mRecipeId));

        return rootView;
    }

    private String getRecipeIngredients(int recipeId) {
        ArrayList<Ingredient> ingredients = mBakingRecipesPojo.getIngredients();
        ArrayList<Ingredient> matchingIngredients = new ArrayList<>();
        String recipeIngredients = "";

        // Isolate ingredients that match with the Recipe ID
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getRecipeId() == recipeId) {
                matchingIngredients.add(ingredient);
            }
        }

        int numOfIngredients = matchingIngredients.size();
        for (int i=0; i<numOfIngredients; i++) {
            recipeIngredients += getString(R.string.recipe_detail_ingredients, matchingIngredients.get(i).getQuantity(), matchingIngredients.get(i).getMeasure(), matchingIngredients.get(i).getName());
            if (numOfIngredients > 1 && i < (numOfIngredients - 1)){
                recipeIngredients += ", ";
            }
        }

        return recipeIngredients;
    }

    private ArrayList<Step> getRecipeSteps(int recipeId) {
        ArrayList<Step> steps = mBakingRecipesPojo.getSteps();
        ArrayList<Step> matchingSteps = new ArrayList<>();

        // Isolate steps that match with the Recipe ID
        for (Step step : steps) {
            if (step.getRecipeId() == recipeId) {
                matchingSteps.add(step);
            }
        }

        return matchingSteps;
    }

    /**
     * This function sets the Recipe ID when this fragment is created.
     * @param recipeId The ID of the Recipe for which the details should be displayed.
     */
    public void setRecipeId(int recipeId) {
        mRecipeId = recipeId;
    }

    public void setBakingRecipesPojo(BakingRecipesPojo bakingRecipesPojo) {
        mBakingRecipesPojo = bakingRecipesPojo;
    }

    /**
     * This method handles RecyclerView item clicks.
     * @param step Step data.
     */
    @Override
    public void onClick(Step step) {
        final Intent intent = new Intent(this.getContext(), StepDetailActivity.class);
        intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, mBakingRecipesPojo);
        intent.putExtra(Constants.INTENT_RECIPE_STEPS, getRecipeSteps(mRecipeId).toArray());
        intent.putExtra(Constants.INTENT_CURRENT_STEP, step);
        startActivity(intent);
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO, mBakingRecipesPojo);
        currentState.putInt(Constants.SAVE_INSTANCE_RECIPE_ID, mRecipeId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
