package com.example.android.bakingguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.bakingguru.database.Step;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {

    private ArrayList<Step> mRecipeSteps;
    private Step mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        // Populate mRecipeSteps and mCurrentStep from the extra data received via intent
        Intent intent = getIntent();
        populateIntentExtras(intent);

        StepDetailFragment stepDetailFragment = createStepDetailFragment();

        // Add the fragment to its container using a FragmentManager and a Transaction
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.step_detail_container, stepDetailFragment)
                .commit();
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
            mRecipeSteps = new ArrayList<Step>();
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

}
