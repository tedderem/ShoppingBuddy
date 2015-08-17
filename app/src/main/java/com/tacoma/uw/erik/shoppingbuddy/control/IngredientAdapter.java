package com.tacoma.uw.erik.shoppingbuddy.control;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tacoma.uw.erik.shoppingbuddy.R;
import com.tacoma.uw.erik.shoppingbuddy.model.IngredientList;

import java.util.List;

/**
 * ArrayAdapter class for IngredientList items to help in displaying them to the user for shopping.
 */
public class IngredientAdapter extends ArrayAdapter<IngredientList> {

    /** The list of ingredients to be shown. */
    private List<IngredientList> myList;
    /** The context of this adapter. */
    private Context context;

    /**
     * Constructor of this adapter.
     * @param myList The list that is being shown.
     * @param ctx The context of the list view.
     */
    public IngredientAdapter(List<IngredientList> myList, Context ctx) {
        super(ctx, R.layout.ingredient_item, myList);

        this.myList = myList;

        context = ctx;
    }

    /**
     * Gets the view for the given position clicked in the list view.
     *
     * @param position The position of the item.
     * @param convertView The view of the item.
     * @param parent The parent of the item.
     * @return Returns the view of this selected item.
     */
    public View getView(final int position, View convertView, ViewGroup parent) {

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ingredient_item, parent, false);
        }
        // Now we can fill the layout with the right values
        final TextView name = (TextView) convertView.findViewById(R.id.ingredient_name_view);
        final TextView amount = (TextView) convertView.findViewById(R.id.amount_text_view);

        //set on click listener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myList.get(position).setSelected(!myList.get(position).getSelected());
                if (myList.get(position).getSelected()) {
                    name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    amount.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }

            }
        });
        //double check to ensure view is selected properly
        setViewSelected(convertView, position);

        name.setText(myList.get(position).getName());
        amount.setText(myList.get(position).getAmounts());

        return convertView;
    }

    /**
     * Necessary method to stop views from being selected when the listviews are reloaded.
     * @param v The view being checked.
     * @param position The position in the list.
     */
    private void setViewSelected(View v, int position) {
        final TextView name = (TextView) v.findViewById(R.id.ingredient_name_view);
        final TextView amount = (TextView) v.findViewById(R.id.amount_text_view);

        //if the item is selected, scratch it out.. otherwise unstrike it
        if (myList.get(position).getSelected()) {
            name.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            amount.setPaintFlags(name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            name.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            amount.setPaintFlags(name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}