package com.example.android.bakingguru.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
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
public class StepDetailFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener {

    private static final String TAG = StepDetailFragment.class.getSimpleName();

    @BindView(R.id.pv_step_detail_video) PlayerView mPlayerView;
    @BindView(R.id.tv_step_detail_description) TextView mStepDescription;
    @BindView(R.id.btn_step_detail_previous) Button mPreviousStepBtn;
    @BindView(R.id.btn_step_detail_next) Button mNextStepBtn;

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

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
        Context context = this.getContext();

        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            mBakingRecipesPojo = (BakingRecipesPojo) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_BAKING_RECIPE_POJO);
            mRecipeSteps = (ArrayList<Step>) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_RECIPE_STEPS);
            mCurrentStep = (Step) savedInstanceState.getSerializable(Constants.SAVE_INSTANCE_CURRENT_STEP);
        }

        mStepDescription.setText(mCurrentStep.getDescription());
        mPreviousStepBtn.setOnClickListener(this);
        mNextStepBtn.setOnClickListener(this);

        String videoUrl = mCurrentStep.getVideoUrl();
        if (videoUrl != null && !videoUrl.equals("")) {
            showPlayer();
            initializeMediaSession(context);
            initializePlayer(Uri.parse(mCurrentStep.getVideoUrl()), context);
        } else {
            hidePlayer();
        }

        return rootView;
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
        if (mExoPlayer != null) {
            releasePlayer();
        }
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
        unbinder.unbind();
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

    private void showPlayer() {
        mPlayerView.setVisibility(View.VISIBLE);
    }

    private void hidePlayer() {
        mPlayerView.setVisibility(View.GONE);
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession(Context context) {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(context, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri, Context context) {
        if (mExoPlayer == null) {
            mExoPlayer = new SimpleExoPlayer.Builder(context).build();
            mPlayerView.setPlayer(mExoPlayer);
            // Set the ExoPlayer.EventListener to this activity
            mExoPlayer.addListener(this);
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
     * Called when the value returned from either playWhenReady or playbackState changes.
     *
     * @param playWhenReady Whether playback will proceed when ready.
     * @param playbackState The new playback state.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    /**
     * Called when an error occurs. The playback state will transition to STATE_IDLE
     * immediately after this method is called. The player instance can still be used, and release
     * must still be called on the player should it no longer be required.
     *
     * @param error The error.
     */
    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // Implement error handling here
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {

        /**
         * Override to handle requests to begin playback.
         */
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        /**
         * Override to handle requests to pause playback.
         */
        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        /**
         * Override to handle requests to skip to the next media item.
         */
        @Override
        public void onSkipToNext() {
            mExoPlayer.seekTo(0);
        }

        /**
         * Override to handle requests to skip to the previous media item.
         */
        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
