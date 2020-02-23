package com.example.android.bakingguru.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingguru.R;
import com.example.android.bakingguru.StepDetailActivity;
import com.example.android.bakingguru.database.Step;
import com.example.android.bakingguru.model.BakingRecipesPojo;
import com.example.android.bakingguru.util.AppUtil;
import com.example.android.bakingguru.util.Constants;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerControlView;
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
    private Dialog mPlayerFullScreenDialog;
    private ImageView mPlayerFullScreenIcon;
    private FrameLayout mPlayerFullScreenButton;

    private Boolean mPlayerFullScreenInd = false;
    private int mPlayerResumeWindow;
    private long mPlayerResumePosition;

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
            mPlayerResumeWindow = savedInstanceState.getInt(Constants.SAVE_INSTANCE_PLAYER_RESUME_WINDOW);
            mPlayerResumePosition = savedInstanceState.getLong(Constants.SAVE_INSTANCE_PLAYER_RESUME_POSITION);
            mPlayerFullScreenInd = savedInstanceState.getBoolean(Constants.SAVE_INSTANCE_PLAYER_FULLSCREEN_IND);
        }

        mStepDescription.setText(mCurrentStep.getDescription());
        initializePrevStepBtn();
        initializeNextStepBtn();

        String videoUrl = mCurrentStep.getVideoUrl();
        if (videoUrl != null && !videoUrl.equals("")) {
            showPlayer();
            initializeMediaSession(context);
            initializePlayer(Uri.parse(mCurrentStep.getVideoUrl()), context);
        } else {
            hidePlayer();
        }

        if (AppUtil.isLandscapeView() && mExoPlayer != null && mPlayerView.getPlayer() != null) {
            mPlayerFullScreenInd = true;
            openFullscreenDialog();
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
        currentState.putInt(Constants.SAVE_INSTANCE_PLAYER_RESUME_WINDOW, mPlayerResumeWindow);
        currentState.putLong(Constants.SAVE_INSTANCE_PLAYER_RESUME_POSITION, mPlayerResumePosition);
        currentState.putBoolean(Constants.SAVE_INSTANCE_PLAYER_FULLSCREEN_IND, mPlayerFullScreenInd);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null && mPlayerView.getPlayer() != null) {
            mPlayerResumeWindow = mPlayerView.getPlayer().getCurrentWindowIndex();
            mPlayerResumePosition = mPlayerView.getPlayer().getContentPosition();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPlayerFullScreenDialog != null) {
            closeFullscreenDialog();
        }
        releasePlayer();
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
        int currentStepPosition = getCurrentRecipeStepPosition();
        if (currentStepPosition > 0) {
            final Intent intent = new Intent(this.getContext(), StepDetailActivity.class);
            intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, mBakingRecipesPojo);
            intent.putExtra(Constants.INTENT_RECIPE_STEPS, mRecipeSteps.toArray());
            intent.putExtra(Constants.INTENT_CURRENT_STEP, mRecipeSteps.get(currentStepPosition - 1));
            startActivity(intent);
        }
    }

    private void nextButtonClick() {
        int currentStepPosition = getCurrentRecipeStepPosition();
        if ((currentStepPosition != -1) & (currentStepPosition < (mRecipeSteps.size() - 1))) {
            final Intent intent = new Intent(this.getContext(), StepDetailActivity.class);
            intent.putExtra(Constants.INTENT_BAKING_RECIPES_POJO, mBakingRecipesPojo);
            intent.putExtra(Constants.INTENT_RECIPE_STEPS, mRecipeSteps.toArray());
            intent.putExtra(Constants.INTENT_CURRENT_STEP, mRecipeSteps.get(currentStepPosition + 1));
            startActivity(intent);
        }
    }

    private int getCurrentRecipeStepPosition() {
        for (int i=0; i<mRecipeSteps.size(); i++) {
            if (mRecipeSteps.get(i).getId() == mCurrentStep.getId()) {
                return i;
            }
        }
        return -1;
    }

    private void initializePrevStepBtn() {
        if (getCurrentRecipeStepPosition() <= 0) {
            mPreviousStepBtn.setVisibility(View.INVISIBLE);
        } else {
            mPreviousStepBtn.setVisibility(View.VISIBLE);
            mPreviousStepBtn.setOnClickListener(this);
        }
    }

    private void initializeNextStepBtn() {
        if (getCurrentRecipeStepPosition() >= (mRecipeSteps.size() - 1)) {
            mNextStepBtn.setVisibility(View.INVISIBLE);
        } else {
            mNextStepBtn.setVisibility(View.VISIBLE);
            mNextStepBtn.setOnClickListener(this);
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
            // Initialize full screen dialog and button for ExoPlayer
            initFullscreenDialog();
            initFullscreenButton();
            // Set the ExoPlayer.EventListener to this activity
            mExoPlayer.addListener(this);
            // Prepare the MediaSource
            String userAgent = Util.getUserAgent(context, "Baking Guru");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, userAgent);
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
            // Resume video at T - 100ms if it was playing and current position was > 100ms.
            // 100ms (0.1sec) is used to overcome a bug in ExoPlayer where video stops responding
            // if resumed at the end of video duration.
            if (mPlayerResumeWindow != C.INDEX_UNSET && mPlayerResumePosition > 100) {
                mPlayerView.getPlayer().seekTo(mPlayerResumeWindow, mPlayerResumePosition - 100);
            }
        }
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null && mPlayerView.getPlayer() != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
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

    /**
     * This function allows the user to exit player's fullscreen mode by either pressing the shrink
     * button in the lower right of their screen, or by using their device’s back button.
     */
    private void initFullscreenDialog() {
        mPlayerFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mPlayerFullScreenInd)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    /**
     * This method removes the PlayerView from the activity, and adds a new instance of
     * the view to the fullscreen dialog.
     */
    private void openFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mPlayerFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPlayerFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
        mPlayerFullScreenInd = true;
        mPlayerFullScreenDialog.show();
    }

    /**
     * This method adds a new PlayerView to the activity, removes the view from the fullscreen
     * dialog, and dismisses the dialog.
     */
    private void closeFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        ((AspectRatioFrameLayout) getActivity().findViewById(R.id.arfl_step_detail_video_container)).addView(mPlayerView);
        mPlayerFullScreenInd = false;
        mPlayerFullScreenDialog.dismiss();
        mPlayerFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
    }

    /**
     * This method initializes the full screen button.
     */
    private void initFullscreenButton() {
        PlayerControlView controlView = mPlayerView.findViewById(R.id.exo_controller);
        mPlayerFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mPlayerFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mPlayerFullScreenButton.setOnClickListener((View v) -> {
            if (!mPlayerFullScreenInd)
                openFullscreenDialog();
            else
                closeFullscreenDialog();
        });
    }

}
