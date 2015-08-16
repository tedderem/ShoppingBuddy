package com.tacoma.uw.erik.shoppingbuddy.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tacoma.uw.erik.shoppingbuddy.R;
import com.tacoma.uw.erik.shoppingbuddy.model.IngredientList;

import java.util.List;


public class IngredientAdapter extends ArrayAdapter<IngredientList> {

    private List<IngredientList> myList;
    private Context context;

    public IngredientAdapter(List<IngredientList> myList, Context ctx) {
        super(ctx, R.layout.ingredient_item, myList);

        this.myList = myList;

        context = ctx;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ingredient_item, parent, false);
        }
        // Now we can fill the layout with the right values
        TextView name = (TextView) convertView.findViewById(R.id.ingredient_name_view);
        TextView amount = (TextView) convertView.findViewById(R.id.amount_text_view);

        name.setText(myList.get(position).getName());
        amount.setText(myList.get(position).getAmounts());

        return convertView;
    }
}