package com.tacoma.uw.erik.shoppingbuddy.model;

import java.io.Serializable;

/**
 * Model class which represents the entire recipe list for selected meals.
 */
public class IngredientList implements Serializable{

    /** The name of the ingredient being needed. */
    private String name;
    /** A string of amounts the user needs to get for this ingredient. */
    private String amounts;
    /** A boolean to say whether or not this has been selected in the view. */
    private boolean selected;

    /**
     * Constructor of a new IngredientList item based on the name of the ingredient and the amounts
     * needed.
     * @param name The name of the ingredient.
     * @param amounts The amounts needed of the ingredient.
     */
    public IngredientList(String name, String amounts) {
        this.name = name;
        this.amounts = amounts;
        this.selected = false;
    }

    /**
     * Sets this ingredient list item as being selected.
     * @param theSelected The state in which this item should be set to.
     */
    public void setSelected(final boolean theSelected) {
        selected = theSelected;
    }

    /**
     * Getter method which returns the items selected state.
     * @return boolean showing whether this has been selected.
     */
    public boolean getSelected() {
        return selected;
    }

    /**
     * Getter method of this ingredient list's name.
     * @return String name of the object.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter method for this ingredient list's amounts.
     * @return String amounts of the ingredient.
     */
    public String getAmounts() {
        return amounts;
    }

    @Override
    public String toString() {
        return "IngredientList{" +
                "name='" + name + '\'' +
                ", amounts='" + amounts + '\'' +
                '}';
    }
}
