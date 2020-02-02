package com.example.android.bakingguru;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingguru.adapters.StepAdapter;
import com.example.android.bakingguru.database.Ingredient;
import com.example.android.bakingguru.database.Step;
import com.example.android.bakingguru.util.MockDataGenerator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment implements StepAdapter.StepAdapterOnClickHandler {

    public static final String INTENT_RECIPE_STEPS = "INTENT_RECIPE_STEPS";
    public static final String INTENT_CURRENT_STEP = "INTENT_CURRENT_STEP";

    @BindView(R.id.rv_steps) RecyclerView mRecyclerView;
    @BindView(R.id.tv_recipe_detail_ingredients_list) TextView mRecipeIngredients;

    private int mRecipeId;

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
        ArrayList<Ingredient> ingredients = MockDataGenerator.createMockIngredients();
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
        ArrayList<Step> steps = MockDataGenerator.createMockSteps();
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
    void setRecipeId(int recipeId) {
        mRecipeId = recipeId;
    }

    /**
     * This method handles RecyclerView item clicks.
     * @param step Step data.
     */
    @Override
    public void onClick(Step step) {
        final Intent intent = new Intent(this.getContext(), StepDetailActivity.class);
        intent.putExtra(INTENT_RECIPE_STEPS, getRecipeSteps(mRecipeId).toArray());
        intent.putExtra(INTENT_CURRENT_STEP, step);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
