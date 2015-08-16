package com.tacoma.uw.erik.shoppingbuddy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a Meal including the various descriptors and the ingredients necessary
 * to create this meal.
 */
public class Meal implements Serializable {

    /**
     * The name of this meal.
     */
    private String myName;

    /**
     * The ID for this meal that is stored in the database.
     */
    private int myID;

    /**
     * The directions for cooking this meal.
     */
    private String myDirections;

    /**
     * The difficulty of this meal.
     */
    private int myDifficulty;

    /**
     * The number of people this meal can serve.
     */
    private String myServes;

    /**
     * The individual, website, or organization that created this recipe.
     */
    private String myCreateBy;

    /**
     * The cuisine type of this recipe.
     */
    private String myCuisineType;

    /**
     * Boolean to depict if this meal was selected by the user.
     */
    private Boolean mySelected;

    /**
     * A list of ingredients that this meal requires.
     */
    private List<Ingredient> myIngredients;
    private int difficulty;

    /**
     * Constructor of a new meal.
     *
     * @param name The name of the meal.
     * @param ID The ID of the meal.
     * @param directions The directions for creating the meal.
     * @param serves The number of people the meal can serve.
     * @param createdBy Who created this meal.
     * @param cuisineType The cuisine type of this meal.
     */
    public Meal(final String name, final int ID, final String directions, final int difficulty,
                final String serves, final String createdBy, final String cuisineType) {
        myName = name;
        myID = ID;
        myDirections = directions;
        myServes = serves;
        myCreateBy = createdBy;
        myCuisineType = cuisineType;
        myDifficulty = difficulty;
        mySelected = false;

        myIngredients = new ArrayList<>();
    }

    /**
     * Setter method which adds a new ingredient to this meal.
     *
     * @param theIng The ingredient to be adding to this meal's recipe.
     */
    public void addIngredient(final Ingredient theIng) {
        myIngredients.add(theIng);
    }

    /**
     * Method which sets that this meal was selected (or unselected) to be included in shopping list.
     *
     * @param selectedState The state of the meal selection.
     */
    public void setSelected(final Boolean selectedState) {
        mySelected = selectedState;
    }

    public boolean getSelected() {
        return mySelected;
    }

    /**
     * Getter method for this meal's name.
     *
     * @return The string representation of this meal's name.
     */
    public String getName() {
        return myName;
    }

    /**
     * Getter method for the database ID of this meal.
     * @return An int representing the ID of this meal.
     */
    public int getID() {
        return myID;
    }

    /**
     * Getter method for the meal's cooking directions.
     * @return A string representing the meal's cooking directions.
     */
    public String getDirections() {
        return myDirections;
    }

    /**
     * Getter method for the number of people served by this meal.
     * @return A string representing the number of people served.
     */
    public String getServes() {
        return myServes;
    }

    /**
     * Getter method for the person(s) who created this meal.
     * @return A string representing the creater of the meal.
     */
    public String getCreateBy() {
        return myCreateBy;
    }

    /**
     * Getter method for the cuisine type of this meal.
     *
     * @return A string of the cuisine type.
     */
    public String getCuisineType() {
        return myCuisineType;
    }

    /**
     * Getter method for this meal's difficulty.
     *
     * @return an int representing the meals difficulty.
     */
    public int getDifficulty() {
        return myDifficulty;
    }

    /**
     * Getter method for the list of ingredients of a meal.
     *
     * @return A list of ingredients.
     */
    public List<Ingredient> getIngredients() {
        return myIngredients;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Meal{" +
                "Name='" + myName + '\'' +
                ", ID=" + myID +
                ", Directions='" + myDirections + '\'' +
                ", yServes='" + myServes + '\'' +
                ", CreateBy='" + myCreateBy + '\'' +
                ", CuisineType='" + myCuisineType + '\'' +
                ", Ingredients=" + myIngredients +
                '}';
    }
}
