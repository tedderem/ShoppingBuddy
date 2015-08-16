package com.tacoma.uw.erik.shoppingbuddy.model;

import java.io.Serializable;

/**
 * A class which represents an ingredient. An ingredient is made up of a name and an amount needed
 * of this ingredient.
 */
public class Ingredient implements Serializable {

    /** The name of this ingredient. */
    private String myName;

    /** The amount needed of this ingredient. */
    private String myAmount;

    /**
     * Constructor of a new Ingredient object based on a given name and amount.
     *
     * @param theName The name of the ingredient.
     * @param theAmount The amount needed of this ingredient.
     */
    public Ingredient(final String theName, final String theAmount) {
        myName = theName;
        myAmount = theAmount;
    }

    /**
     * Getter method which returns the name of this ingredient.
     *
     * @return A String representing the name of this ingredient.
     */
    public String getName() {
        return myName;
    }

    /**
     * Getter method for the amount needed of this ingredient for a recipe.
     *
     * @return A String representing the amount needed for this ingredient.
     */
    public String getAmount() {
        return myAmount;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return myAmount + " of " + myName;
    }
}
