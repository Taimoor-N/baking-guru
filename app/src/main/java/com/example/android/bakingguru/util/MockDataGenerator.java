package com.example.android.bakingguru.util;

import com.example.android.bakingguru.database.Ingredient;
import com.example.android.bakingguru.database.Recipe;
import com.example.android.bakingguru.database.Step;

import java.util.ArrayList;

public class MockDataGenerator {

    public static ArrayList<Recipe> createMockRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(0, "Cake", 8, ""));
        recipes.add(new Recipe(1, "Ice Cream", 12, ""));
        recipes.add(new Recipe(2, "Apple Pie", 14, ""));
        recipes.add(new Recipe(3, "Donuts", 2, ""));
        recipes.add(new Recipe(4, "Tango", 4, ""));
        recipes.add(new Recipe(5, "Salsa", 4, ""));
        return recipes;
    }

    public static ArrayList<Ingredient> createMockIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(0, 0, "Sugar", 5, "TBLSP"));
        ingredients.add(new Ingredient(1, 0, "salt", 3, "TBLSP"));
        ingredients.add(new Ingredient(2, 0, "water", 1, "CUP"));
        ingredients.add(new Ingredient(3, 0, "vanilla", 5, "TBLSP"));

        ingredients.add(new Ingredient(4, 1, "Graham Cracker crumbs", 2, "CUP"));
        ingredients.add(new Ingredient(5, 1, "unsalted butter, melted", 3, "TBLSP"));
        ingredients.add(new Ingredient(6, 1, "water", 1, "CUP"));
        ingredients.add(new Ingredient(7, 1, "vanilla", 5, "TBLSP"));
        ingredients.add(new Ingredient(8, 1, "cream cheese(softened)", 4, "OZ"));
        ingredients.add(new Ingredient(9, 1, "Mascapone Cheese(room temperature)", 500, "G"));

        ingredients.add(new Ingredient(10, 2, "Sugar", 5, "TBLSP"));
        ingredients.add(new Ingredient(11, 2, "salt", 3, "TBLSP"));
        ingredients.add(new Ingredient(12, 2, "cream cheese(softened)", 4, "OZ"));

        ingredients.add(new Ingredient(13, 3, "Sugar", 5, "TBLSP"));
        ingredients.add(new Ingredient(14, 3, "salt", 3, "TBLSP"));
        ingredients.add(new Ingredient(15, 3, "water", 1, "CUP"));
        ingredients.add(new Ingredient(16, 3, "vanilla", 5, "TBLSP"));
        ingredients.add(new Ingredient(17, 3, "Mascapone Cheese(room temperature)", 500, "G"));
        ingredients.add(new Ingredient(18, 3, "cream cheese(softened)", 4, "OZ"));
        ingredients.add(new Ingredient(19, 3, "heavy cream(cold)", 1, "CUP"));

        ingredients.add(new Ingredient(20, 4, "Sugar", 5, "TBLSP"));
        ingredients.add(new Ingredient(21, 4, "salt", 3, "TBLSP"));
        ingredients.add(new Ingredient(22, 4, "water", 1, "CUP"));
        ingredients.add(new Ingredient(23, 4, "vanilla", 5, "TBLSP"));
        ingredients.add(new Ingredient(24, 4, "heavy cream(cold)", 1, "CUP"));

        ingredients.add(new Ingredient(25, 5, "Sugar", 5, "TBLSP"));
        ingredients.add(new Ingredient(26, 5, "salt", 3, "TBLSP"));
        ingredients.add(new Ingredient(27, 5, "water", 1, "CUP"));
        ingredients.add(new Ingredient(28, 5, "vanilla", 5, "TBLSP"));
        ingredients.add(new Ingredient(29, 5, "cream cheese(softened)", 4, "OZ"));

        return ingredients;
    }

    public static ArrayList<Step> createMockSteps() {
        ArrayList<Step> steps = new ArrayList<>();
        steps.add(new Step(0, 0, "Introduction", "Recipe Introduction", "", ""));
        steps.add(new Step(1, 0, "First Step", "1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.", "", ""));
        steps.add(new Step(2, 0, "Second Step", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.", "", ""));

        steps.add(new Step(3, 1, "Introduction", "Recipe Introduction", "", ""));
        steps.add(new Step(4, 1, "First Step", "1. Preheat the oven to 350\\u00b0F. Butter the bottoms and sides of two 9\\\" round pans with 2\\\"-high sides. Cover the bottoms of the pans with rounds of parchment paper, and butter the paper as well.", "", ""));
        steps.add(new Step(5, 1, "Second Step", "2. Combine the cake flour, 400 grams (2 cups) of sugar, baking powder, and 1 teaspoon of salt in the bowl of a stand mixer. Using the paddle attachment, beat at low speed until the dry ingredients are mixed together, about one minute.", "", ""));
        steps.add(new Step(6, 1, "Third Step", "3. Mix both sugars into the melted chocolate in a large mixing bowl until mixture is smooth and uniform.", "", ""));

        steps.add(new Step(7, 2, "Introduction", "Recipe Introduction", "", ""));
        steps.add(new Step(8, 2, "First Step", "1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.", "", ""));
        steps.add(new Step(9, 2, "Second Step", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.", "", ""));
        steps.add(new Step(10, 2, "Third Step", "3. Mix both sugars into the melted chocolate in a large mixing bowl until mixture is smooth and uniform.", "", ""));
        steps.add(new Step(11, 2, "Fourth Step", "4. Sift together the flour, cocoa, and salt in a small bowl and whisk until mixture is uniform and no clumps remain.", "", ""));

        steps.add(new Step(12, 3, "Introduction", "Recipe Introduction", "", ""));
        steps.add(new Step(13, 3, "First Step", "1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.", "", ""));
        steps.add(new Step(14, 3, "Second Step", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.", "", ""));

        steps.add(new Step(15, 4, "Introduction", "Recipe Introduction", "", ""));
        steps.add(new Step(16, 4, "First Step", "1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.", "", ""));
        steps.add(new Step(17, 4, "Second Step", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.", "", ""));
        steps.add(new Step(18, 4, "Third Step", "3. Lightly beat together the egg yolks, 1 tablespoon of vanilla, and 80 grams (1/3 cup) of the milk in a small bowl.", "", ""));

        steps.add(new Step(19, 5, "Introduction", "Recipe Introduction", "", ""));
        steps.add(new Step(20, 5, "First Step", "1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.", "", ""));
        steps.add(new Step(21, 5, "Second Step", "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.", "", ""));

        return steps;
    }

}
