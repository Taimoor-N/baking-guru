package com.example.android.bakingguru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.bakingguru.R;
import com.example.android.bakingguru.database.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder>{

    private ArrayList<Step> mSteps;
    private final StepAdapterOnClickHandler mClickHandler;

    public interface StepAdapterOnClickHandler {
        void onClick(Step step);
    }

    /**
     * Constructor for StepAdapter.
     * @param clickHandler The onClick handler for StepAdapter. This is called when an
     *                     item is clicked.
     */
    public StepAdapter(StepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a step list item.
     */
    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_step_thumbnail) ImageView mStepThumbnail;
        @BindView(R.id.tv_step_short_description) TextView mStepShortDesc;

        public StepAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Step step = mSteps.get(adapterPosition);
            mClickHandler.onClick(step);
        }

    }

    /**
     * This function is called when a new ViewHolder gets created.
     *
     * @param viewGroup The ViewGroup that contains each of the ViewHolders.
     * @param viewType Specifies the type of item in the RecyclerView.
     * @return A new StepAdapterViewHolder that hold the view of each step list item.
     */
    @NonNull
    @Override
    public StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new StepAdapterViewHolder(view);
    }

    /**
     * This function is called to display the step list item at the specified position.
     * @param stepAdapterViewHolder The ViewHolder that should be updated to represent the
     *                               contents of step list item at the given position in data set.
     * @param position The position of the step list item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull StepAdapterViewHolder stepAdapterViewHolder, int position) {
        stepAdapterViewHolder.mStepShortDesc.setText(mSteps.get(position).getShortDescription());
        // Using Picasso to load Step Thumbnail
        Context context = stepAdapterViewHolder.mStepThumbnail.getContext();
        String imageUrl = mSteps.get(position).getThumbnailUrl();
        if (imageUrl.equals("")) {
            stepAdapterViewHolder.mStepThumbnail.setImageResource(R.drawable.ic_photo_placeholder_recipe_step);
        } else {
            Picasso.with(context)
                    .load(imageUrl)
                    .error(R.drawable.ic_photo_placeholder_recipe_step)
                    .into(stepAdapterViewHolder.mStepThumbnail);
        }
    }

    /**
     * This function returns the total number of step list items to display.
     * @return The number of step list items available.
     */
    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        }
        return mSteps.size();
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

}