package com.example.android.bakingguru.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingguru.R;
import com.example.android.bakingguru.StepDetailActivity;
import com.example.android.bakingguru.database.Step;
import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.Constants;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.pv_step_detail_video) PlayerView mPlayerView;
    @BindView(R.id.tv_step_detail_description) TextView mStepDescription;
    @BindView(R.id.btn_step_detail_previous) Button mPreviousStepBtn;
    @BindView(R.id.btn_step_detail_next) Button mNextStepBtn;

    private SimpleExoPlayer mExoPlayer;
    private BakingRecipesPojo mBakingRecipesPojo;
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
            mBakingRecipesPojo = (BakingRecipesPojo) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO);
            mRecipeSteps = (ArrayList<Step>) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_RECIPE_STEPS);
            mCurrentStep = (Step) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_CURRENT_STEP);
        }

        mStepDescription.setText(mCurrentStep.getDescription());
        mPreviousStepBtn.setOnClickListener(this);
        mNextStepBtn.setOnClickListener(this);

        initializePlayer(Uri.parse(mCurrentStep.getVideoUrl()), this.getContext());

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

    public void setBakingRecipesPojo(BakingRecipesPojo bakingRecipesPojo) {
        mBakingRecipesPojo = bakingRecipesPojo;
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
            intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, mBakingRecipesPojo);
            intent.putExtra(Constants.INTENT_RECIPE_STEPS, mRecipeSteps.toArray());
            intent.putExtra(Constants.INTENT_CURRENT_STEP, mRecipeSteps.get(currentStepPosition - 1));
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
            intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, mBakingRecipesPojo);
            intent.putExtra(Constants.INTENT_RECIPE_STEPS, mRecipeSteps.toArray());
            intent.putExtra(Constants.INTENT_CURRENT_STEP, mRecipeSteps.get(currentStepPosition + 1));
            startActivity(intent);
        }
    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri, Context context) {
        if (mExoPlayer == null) {
            mExoPlayer = new SimpleExoPlayer.Builder(context).build();
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(context, "Baking Guru");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO, mBakingRecipesPojo);
        currentState.putSerializable(Constants.SAVE_INSTANCE_RECIPE_STEPS, mRecipeSteps);
        currentState.putSerializable(Constants.SAVE_INSTANCE_CURRENT_STEP, mCurrentStep);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        unbinder.unbind();
    }

}