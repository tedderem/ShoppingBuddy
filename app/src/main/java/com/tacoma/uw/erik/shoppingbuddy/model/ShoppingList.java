package com.tacoma.uw.erik.shoppingbuddy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which represents a shopping list.
 *
 * @author Erik Tedder
 */
public class ShoppingList implements Serializable {

    /** Random(ish) version ID for serializing. */
    private final static long serialVersionUID = 12385123091310924l;

    /** A mapping of ingredients to their amounts. */
    private List<IngredientList> myIngredients;

    /** A list of meals. */
    private List<Meal> myMeals;

    /**
     * Constructor of a new ShoppingList based on a given list of meals.
     *
     * @param theMeals The meals of this shopping list.
     */
    public ShoppingList(List<Meal> theMeals) {
        myMeals = theMeals;
        myIngredients = new ArrayList<>();
        HashMap<String, String> temp = new HashMap<>();

        //go through each meal and add its ingredients to the list.
        for (Meal m : myMeals) {
            for (Ingredient i : m.getIngredients()) {
                if (temp.containsKey(i.getName())) {
                    temp.put(i.getName(), temp.get(i.getName()) + "\n" + i.getAmount());
                } else {
                    temp.put(i.getName(), i.getAmount());
                }
            }
        }

        for (String s : temp.keySet()) {
            IngredientList l = new IngredientList(s, temp.get(s));
            myIngredients.add(l);
        }
    }

    /**
     * Method which returns the meals of this shopping list.
     *
     * @return A list of meals.
     */
    public List<Meal> getMeals() {
        return myMeals;
    }

    public List<IngredientList> getIngredients() {
        return myIngredients;
    }
}
