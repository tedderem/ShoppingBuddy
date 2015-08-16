package com.tacoma.uw.erik.shoppingbuddy.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by BigErik on 8/15/2015.
 */
public class IngredientList implements Serializable{

    private String name;
    private String amounts;

    public IngredientList(String name, String amounts) {
        this.name = name;
        this.amounts = amounts;
    }

    public String getName() {
        return name;
    }

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
