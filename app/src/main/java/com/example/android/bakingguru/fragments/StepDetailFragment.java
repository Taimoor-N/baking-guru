package com.example.android.bakingguru.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingguru.R;
import com.example.android.bakingguru.StepDetailActivity;
import com.example.android.bakingguru.database.Step;
import com.example.android.bakingguru.fragments.RecipeDetailFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment implements View.OnClickListener {

    private static final String SAVE_INSTANCE_RECIPE_STEPS = "save_instance_recipe_steps";
    private static final String SAVE_INSTANCE_CURRENT_STEP = "save_instance_current_step";

    @BindView(R.id.iv_to_be_replaced_by_video) ImageView mStepImage;
    @BindView(R.id.tv_step_detail_description) TextView mStepDescription;
    @BindView(R.id.btn_step_detail_previous) Button mPreviousStepBtn;
    @BindView(R.id.btn_step_detail_next) Button mNextStepBtn;

    private ArrayList<Step> mRecipeSteps;
    private Step mCurrentStep;

    private Unbinder unbinder;


    public StepDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mRecipeSteps = (ArrayList<Step>) savedInstanceState.getSerializable(SAVE_INSTANCE_RECIPE_STEPS);
            mCurrentStep = (Step) savedInstanceState.getSerializable(SAVE_INSTANCE_CURRENT_STEP);
        }

        mStepImage.setImageResource(R.drawable.ic_videocam_placeholder_24dp);
        mStepDescription.setText(mCurrentStep.getDescription());
        mPreviousStepBtn.setOnClickListener(this);
        mNextStepBtn.setOnClickListener(this);

        return rootView;
    }

    /**
     * This function populates all the steps associated to the Recipe when this fragment is created.
     * @param recipeSteps Recipe Steps.
     */
    public void setRecipeSteps(ArrayList<Step> recipeSteps) {
        mRecipeSteps = recipeSteps;
    }

    /**
     * This function populates the currently selected step when this fragment is created.
     * @param currentStep Selected recipe step.
     */
    public void setCurrentStep(Step currentStep) {
        mCurrentStep = currentStep;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_step_detail_previous : previousButtonClick();
                break;
            case R.id.btn_step_detail_next : nextButtonClick();
                break;
        }
    }

    private void previousButtonClick() {
        int numSteps = mRecipeSteps.size();
        int currentStepPosition = -1;
        for (int i=0; i<numSteps; i++) {
            if (mRecipeSteps.get(i).getId() == mCurrentStep.getId()) {
                currentStepPosition = i;
                break;
            }
        }

        if (currentStepPosition > 0) {
            final Intent intent = new Intent(this.getContext(), StepDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.INTENT_RECIPE_STEPS, mRecipeSteps.toArray());
            intent.putExtra(RecipeDetailFragment.INTENT_CURRENT_STEP, mRecipeSteps.get(currentStepPosition - 1));
            startActivity(intent);
        }
    }

    private void nextButtonClick() {
        int numSteps = mRecipeSteps.size();
        int currentStepPosition = -1;
        for (int i=0; i<numSteps; i++) {
            if (mRecipeSteps.get(i).getId() == mCurrentStep.getId()) {
                currentStepPosition = i;
                break;
            }
        }

        if ((currentStepPosition != -1) & (currentStepPosition < (numSteps - 1))) {
            final Intent intent = new Intent(this.getContext(), StepDetailActivity.class);
            intent.putExtra(RecipeDetailFragment.INTENT_RECIPE_STEPS, mRecipeSteps.toArray());
            intent.putExtra(RecipeDetailFragment.INTENT_CURRENT_STEP, mRecipeSteps.get(currentStepPosition + 1));
            startActivity(intent);
        }
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putSerializable(SAVE_INSTANCE_RECIPE_STEPS, mRecipeSteps);
        currentState.putSerializable(SAVE_INSTANCE_CURRENT_STEP, mCurrentStep);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
