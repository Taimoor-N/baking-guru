package com.example.android.bakingguru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.bakingguru.database.Step;
import com.example.android.bakingguru.util.AppUtil;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {

    private static final String SAVE_INSTANCE_RECIPE_DETAIL_FRAGMENT_CREATED = "save_instance_recipe_detail_fragment_created";
    private static final String SAVE_INSTANCE_RECIPE_STEPS = "save_instance_recipe_steps";
    private static final String SAVE_INSTANCE_CURRENT_STEP = "save_instance_current_step";

    private ArrayList<Step> mRecipeSteps;
    private Step mCurrentStep;
    private boolean mRecipeDetailFragmentCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        if (savedInstanceState == null) {
            // Populate mRecipeSteps and mCurrentStep from the extra data received via intent
            Intent intent = getIntent();
            populateIntentExtras(intent);

            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (AppUtil.isTabletOrLandscapeView(this)) {
                RecipeDetailFragment recipeDetailFragment = createRecipeDetailFragment();
                StepDetailFragment stepDetailFragment = createStepDetailFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_container, recipeDetailFragment)
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
                mRecipeDetailFragmentCreated = true;
            } else {
                StepDetailFragment stepDetailFragment = createStepDetailFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepDetailFragment)
                        .commit();
                mRecipeDetailFragmentCreated = false;
            }
        }

        else if (!savedInstanceState.getBoolean(SAVE_INSTANCE_RECIPE_DETAIL_FRAGMENT_CREATED)) {
            populateSavedInstanceStateData(savedInstanceState);
            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeDetailFragment recipeDetailFragment = createRecipeDetailFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_container, recipeDetailFragment)
                    .commit();
            mRecipeDetailFragmentCreated = true;
        }

        else {
            populateSavedInstanceStateData(savedInstanceState);
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

        StepDetailFragment stepDetailFragment = createStepDetailFragment();

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    private void populateIntentExtras(Intent intent) {
        Object recipeStepsListObj = intent.getSerializableExtra(RecipeDetailFragment.INTENT_RECIPE_STEPS);
        Object currentStepObj = intent.getSerializableExtra(RecipeDetailFragment.INTENT_CURRENT_STEP);
        if (recipeStepsListObj instanceof Object[]) {
            mRecipeSteps = new ArrayList<>();
            for (Object recipeObj : (Object[]) recipeStepsListObj) {
                mRecipeSteps.add((Step) recipeObj);
            }
        }
        mCurrentStep = (Step) currentStepObj;
    }

    private StepDetailFragment createStepDetailFragment() {
        StepDetailFragment stepDetailFragment = new StepDetailFragment();
        stepDetailFragment.setRecipeSteps(mRecipeSteps);
        stepDetailFragment.setCurrentStep(mCurrentStep);
        return stepDetailFragment;
    }

    private RecipeDetailFragment createRecipeDetailFragment() {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setRecipeId(mCurrentStep.getRecipeId());
        return recipeDetailFragment;
    }

    private void populateSavedInstanceStateData(Bundle savedInstanceState) {
        mRecipeDetailFragmentCreated = savedInstanceState.getBoolean(SAVE_INSTANCE_RECIPE_DETAIL_FRAGMENT_CREATED);
        mRecipeSteps = (ArrayList<Step>) savedInstanceState.getSerializable(SAVE_INSTANCE_RECIPE_STEPS);
        mCurrentStep = (Step) savedInstanceState.getSerializable(SAVE_INSTANCE_CURRENT_STEP);
    }

    @Override
    protected void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putBoolean(SAVE_INSTANCE_RECIPE_DETAIL_FRAGMENT_CREATED, mRecipeDetailFragmentCreated);
        currentState.putSerializable(SAVE_INSTANCE_RECIPE_STEPS, mRecipeSteps);
        currentState.putSerializable(SAVE_INSTANCE_CURRENT_STEP, mCurrentStep);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Check if Up button is pressed and pass the required intent (i.e. Recipe ID)
        if (item.getItemId() == android.R.id.home) {
            final Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeListFragment.INTENT_RECIPE_ID, mCurrentStep.getRecipeId());
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
