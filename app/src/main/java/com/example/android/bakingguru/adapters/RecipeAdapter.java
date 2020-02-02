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
import com.example.android.bakingguru.database.Recipe;
import com.example.android.bakingguru.database.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder>{

    private ArrayList<Recipe> mRecipes;
    private ArrayList<Step> mSteps;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    /**
     * Constructor for RecipeAdapter.
     * @param clickHandler The onClick handler for RecipeAdapter. This is called when an
     *                     item is clicked.
     */
    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    /**
     * Cache of the children views for a recipe list item.
     */
    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe_card_thumbnail) ImageView mRecipeThumbnail;
        @BindView(R.id.tv_recipe_card_title) TextView mRecipeTitle;
        @BindView(R.id.iv_recipe_card) ImageView mRecipeImage;
        @BindView(R.id.tv_recipe_card_steps_value) TextView mRecipeSteps;
        @BindView(R.id.tv_recipe_card_servings_value) TextView mRecipeServings;

        public RecipeAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipes.get(adapterPosition);
            mClickHandler.onClick(recipe);
        }

    }

    /**
     * This function is called when a new ViewHolder gets created.
     *
     * @param viewGroup The ViewGroup that contains each of the ViewHolders.
     * @param viewType Specifies the type of item in the RecyclerView.
     * @return A new RecipeAdapterViewHolder that hold the view of each recipe list item.
     */
    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new RecipeAdapterViewHolder(view);
    }

    /**
     * This function is called to display the recipe card at the specified position.
     * @param recipeAdapterViewHolder The ViewHolder that should be updated to represent the
     *                               contents of recipe list item at the given position in data set.
     * @param position The position of the recipe list item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder recipeAdapterViewHolder, int position) {
        recipeAdapterViewHolder.mRecipeThumbnail.setImageResource(R.drawable.ic_photo_placeholder_24dp);
        recipeAdapterViewHolder.mRecipeImage.setImageResource(R.drawable.ic_photo_placeholder_24dp);
        recipeAdapterViewHolder.mRecipeTitle.setText(mRecipes.get(position).getName());
        recipeAdapterViewHolder.mRecipeSteps.setText(String.valueOf(getNumOfStepsForRecipe(mRecipes.get(position).getId())));
        recipeAdapterViewHolder.mRecipeServings.setText(String.valueOf(mRecipes.get(position).getServings()));
    }

    /**
     * This function returns the total number of recipe cards to display.
     * @return The number of recipe list items available.
     */
    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }
        return mRecipes.size();
    }

    public int getNumOfStepsForRecipe(int recipeId) {
        if (mSteps == null) {
            return 0;
        }
        int matchingSteps = 0;
        for (Step step : mSteps) {
            if (step.getRecipeId() == recipeId) {
                matchingSteps++;
            }
        }
        return matchingSteps;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

}