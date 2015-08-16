package com.tacoma.uw.erik.shoppingbuddy;


import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tacoma.uw.erik.shoppingbuddy.control.IngredientAdapter;
import com.tacoma.uw.erik.shoppingbuddy.model.Ingredient;
import com.tacoma.uw.erik.shoppingbuddy.model.Meal;
import com.tacoma.uw.erik.shoppingbuddy.model.ShoppingList;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Fragment which will display all the ingredients a user will need.
 *
 * @author Erik Tedder
 */
public class ShoppingListFragment extends ListFragment {

    /**
     * The shopping list of this fragment.
     */
    private ShoppingList myShoppingList;

    /**
     * Default constructor for this fragment.
     */
    public ShoppingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            Button shareButton = (Button) getView().findViewById(R.id.directions_button);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveDirections();
                }
            });

            myShoppingList = (ShoppingList) getArguments().getSerializable(getString(R.string.shopping_list_bundle));

            ListView lv = (ListView) getView().findViewById(android.R.id.list);

            IngredientAdapter adapter = new IngredientAdapter(myShoppingList.getIngredients(), getActivity());
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        TextView name = (TextView) v.findViewById(R.id.ingredient_name_view);
        TextView amount = (TextView) v.findViewById(R.id.amount_text_view);

        if (name.getPaintFlags() == (name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG)) {
            name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            amount.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        } else {
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            amount.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    /**
     * Method for setting up the intent to save the directions.
     */
    private void saveDirections() {
        ArrayList<Meal> meals = (ArrayList<Meal>) myShoppingList.getMeals();

        //build the directions list
        StringBuilder sb = new StringBuilder();
        for (Meal m : meals) {
            sb.append(m.getName());
            sb.append(": ");
            sb.append("\n");
            sb.append(m.getDirections());
            sb.append("\n\nRequired Ingredients:\n");
            for (Ingredient i : m.getIngredients()) {
                sb.append(i.toString());
                sb.append("\n");
            }
            sb.append("\n");
        }

        //create the intent and start.
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Meal Recipe Directions");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());

        startActivity(Intent.createChooser(intent, "Send Via"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }


}
