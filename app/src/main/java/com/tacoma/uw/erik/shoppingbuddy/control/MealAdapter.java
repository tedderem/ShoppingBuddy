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

public class MealAdapter extends ArrayAdapter<Meal> {

    private List<Meal> mealList;
    private Context context;

    public MealAdapter(List<Meal> mealList, Context ctx) {
        super(ctx, R.layout.meal_item, mealList);
        this.mealList = mealList;
        context = ctx;
    }

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

        CheckBox check = (CheckBox) convertView.findViewById(R.id.checkBox);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                m.setSelected(isChecked);
                Log.d("Meal Adapter", String.valueOf(m.getSelected()));
            }
        });

        name.setText(m.getName());
        difficulty.setText("Difficulty: " + String.valueOf(m.getDifficulty()));
        serves.setText("Serves: " + m.getServes());
        createdBy.setText("Created By: " + m.getCreateBy());
        cuisine.setText("Cuisine Type: " + m.getCuisineType());

        return convertView;
    }
}