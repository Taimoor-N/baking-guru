package com.example.android.bakingguru.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface IngredientDao {

    @Query("SELECT * FROM Ingredient ORDER BY name")
    List<Ingredient> loadAllIngredients();

    @Query("SELECT * FROM Ingredient WHERE recipeId = :recipeId")
    List<Ingredient> loadIngredientsByRecipeId(int recipeId);

    @Query("SELECT * FROM Ingredient WHERE id = :id")
    Ingredient loadIngredientById(int id);

    @Insert
    void insertIngredient(Ingredient ingredient);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllIngredients(List<Ingredient> ingredients);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIngredient(Ingredient ingredient);

    @Delete
    void deleteIngredient(Ingredient ingredient);

}
