package com.tacoma.uw.erik.shoppingbuddy.control;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tacoma.uw.erik.shoppingbuddy.R;
import com.tacoma.uw.erik.shoppingbuddy.model.Meal;

import java.util.List;

/**
 * ArrayAdapter class for showing the list of meals the user can select from.
 */
public class MealAdapter extends ArrayAdapter<Meal> {

    /** The list of meals to be displayed. */
    private List<Meal> mealList;
    /** The context of the listview. */
    private Context context;

    /**
     * Constructor of a new meal adapter.
     * @param mealList The list being shown.
     * @param ctx The context.
     */
    public MealAdapter(List<Meal> mealList, Context ctx) {
        super(ctx, R.layout.meal_item, mealList);
        this.mealList = mealList;
        context = ctx;
    }

    /**
     * Method for returning the view of the selected position as well as setting up said view.
     *
     * @param position The position of the view in the listview.
     * @param convertView The view being assigned.
     * @param parent The parent.
     * @return The view of this selected item.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.meal_item, parent, false);
        }
        // Now we can fill the layout with the right values
        TextView name = (TextView) convertView.findViewById(R.id.mealName);
        TextView difficulty = (TextView) convertView.findViewById(R.id.mealDifficulty);
        TextView serves = (TextView) convertView.findViewById(R.id.mealServes);
        TextView createdBy = (TextView) convertView.findViewById(R.id.mealCreatedBy);
        TextView cuisine = (TextView) convertView.findViewById(R.id.mealCuisine);

        final Meal m = mealList.get(position);

        //Set the checkbox view and its on check listener
        final CheckBox check = (CheckBox) convertView.findViewById(R.id.checkBox);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set the meal to be selected based on the checkbox value
                m.setSelected(isChecked);
                Log.d("Meal Adapter", String.valueOf(m.getSelected()));
            }
        });
        //double check the checkboxes state to avoid unseen checkboxes taking on this state
        check.setChecked(m.getSelected());

        //set the values of the meal
        name.setText(m.getName());
        difficulty.setText("Difficulty: " + String.valueOf(m.getDifficulty()));
        serves.setText("Serves: " + m.getServes());
        createdBy.setText("Created By: " + m.getCreateBy());
        cuisine.setText("Cuisine Type: " + m.getCuisineType());

        return convertView;
    }
}