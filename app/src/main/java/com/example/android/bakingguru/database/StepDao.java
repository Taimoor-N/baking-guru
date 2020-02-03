package com.example.android.bakingguru.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StepDao {

    @Query("SELECT * FROM Step ORDER BY id")
    List<Step> loadAllSteps();

    @Query("SELECT * FROM Step WHERE recipeId = :recipeId")
    List<Step> loadStepsByRecipeId(int recipeId);

    @Query("SELECT * FROM Step WHERE id = :id")
    Step loadStepById(int id);

    @Insert
    void insertStep(Step step);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllSteps(List<Step> steps);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(Step step);

    @Delete
    void deleteStep(Step step);

}
