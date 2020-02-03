package com.example.android.bakingguru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.bakingguru.database.Step;
import com.example.android.bakingguru.fragments.RecipeDetailFragment;
import com.example.android.bakingguru.fragments.StepDetailFragment;
import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.AppUtil;
import com.example.android.bakingguru.util.Constants;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {

    private BakingRecipesPojo mBakingRecipesPojo;
    private ArrayList<Step> mRecipeSteps;
    private Step mCurrentStep;
    private boolean mRecipeDetailFragmentCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState != null) {
            populateSavedInstanceStateData(savedInstanceState);
        }

        if (savedInstanceState == null) {
            // Populate mRecipeSteps and mCurrentStep from the extra data received via intent
            Intent intent = getIntent();
            populateIntentExtras(intent);

            if (AppUtil.isTabletOrLandscapeView(this)) {
                createAndAddStepDetailFragment();
                createAndAddRecipeDetailFragment();
            } else {
                createAndAddStepDetailFragment();
                mRecipeDetailFragmentCreated = false;
            }
        }
        else if (!mRecipeDetailFragmentCreated) {
            createAndAddRecipeDetailFragment();
            mRecipeDetailFragmentCreated = true;
        }
    }

    /**
     * Since launch mode of this activity is declared as "singleTop" in AndroidManifest, when the
     * activity is re-launched while at the top of the activity stack, instead of a new instance of
     * the activity being started, onNewIntent() will be called on the existing instance with the
     * Intent that was used to re-launch it.
     * @param intent The intent used to relaunch the activity
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        populateIntentExtras(intent);
        createAndReplaceStepDetailFragment();
    }

    private void populateIntentExtras(Intent intent) {
        mBakingRecipesPojo = (BakingRecipesPojo) intent.getSerializableExtra(Constants.INTENT_BAKING_RECIPES_POJO);
        Object recipeStepsListObj = intent.getSerializableExtra(Constants.INTENT_RECIPE_STEPS);
        Object currentStepObj = intent.getSerializableExtra(Constants.INTENT_CURRENT_STEP);
        if (recipeStepsListObj instanceof Object[]) {
            mRecipeSteps = new ArrayList<>();
            for (Object recipeObj : (Object[]) recipeStepsListObj) {
                mRecipeSteps.add((Step) recipeObj);
            }
        }
        mCurrentStep = (Step) currentStepObj;
    }

    private void createAndAddStepDetailFragment() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setBakingRecipesPojo(mBakingRecipesPojo);
        stepDetailFragment.setRecipeSteps(mRecipeSteps);
        stepDetailFragment.setCurrentStep(mCurrentStep);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    private void createAndReplaceStepDetailFragment() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setBakingRecipesPojo(mBakingRecipesPojo);
        stepDetailFragment.setRecipeSteps(mRecipeSteps);
        stepDetailFragment.setCurrentStep(mCurrentStep);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    private void createAndAddRecipeDetailFragment() {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setBakingRecipesPojo(mBakingRecipesPojo);
        recipeDetailFragment.setRecipeId(mCurrentStep.getRecipeId());
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_container, recipeDetailFragment)
                .commit();
        mRecipeDetailFragmentCreated = true;
    }

    private void populateSavedInstanceStateData(Bundle savedInstanceState) {
        mBakingRecipesPojo = (BakingRecipesPojo) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO);
        mRecipeDetailFragmentCreated = savedInstanceState.getBoolean(Constants.SAVE_INSTANCE_RECIPE_DETAIL_FRAGMENT_CREATED);
        mRecipeSteps = (ArrayList<Step>) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_RECIPE_STEPS);
        mCurrentStep = (Step) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_CURRENT_STEP);
    }

    @Override
    protected void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO, mBakingRecipesPojo);
        currentState.putBoolean(Constants.SAVE_INSTANCE_RECIPE_DETAIL_FRAGMENT_CREATED, mRecipeDetailFragmentCreated);
        currentState.putSerializable(Constants.SAVE_INSTANCE_RECIPE_STEPS, mRecipeSteps);
        currentState.putSerializable(Constants.SAVE_INSTANCE_CURRENT_STEP, mCurrentStep);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Check if Up button is pressed and pass the required intent (i.e. Recipe ID)
        if (item.getItemId() == android.R.id.home) {
            final Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, mBakingRecipesPojo);
            intent.putExtra(Constants.INTENT_RECIPE_ID, mCurrentStep.getRecipeId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
