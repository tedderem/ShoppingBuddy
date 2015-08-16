package com.tacoma.uw.erik.shoppingbuddy;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.tacoma.uw.erik.shoppingbuddy.control.MealAdapter;
import com.tacoma.uw.erik.shoppingbuddy.model.Ingredient;
import com.tacoma.uw.erik.shoppingbuddy.model.Meal;
import com.tacoma.uw.erik.shoppingbuddy.model.ShoppingList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * ListFragment class that shows the meals to allow users to create a shopping list.
 *
 * @author Erik Tedder
 */
public class MealFragment extends ListFragment {

    /** Tag for debugging purposes. */
    private static final String TAG = "MealList";
    /** The url to get meals. */
    private static final String MEAL_URL = "http://cssgate.insttech.washington.edu/~tedderem/getMeals.php";
    /** The url to get ingredients for a meal. */
    private static final String ING_URL = "http://cssgate.insttech.washington.edu/~tedderem/getIngredients.php?id=";
    /** The reference to the list view. */
    private ListView myListView;
    /** The list containing all the meals. */
    private ArrayList<Meal> myMealList;

    /**
     * Default, no-argument constructor for the fragment.
     */
    public MealFragment() {
        // Required empty public constructor
    }

    /**
     * {@inheritDoc}
     *
     * Method also sets the meal list and list view.
     */
    @Override
    public void onStart() {
        super.onStart();

        //Set up list view and meal list and button
        if (getView() != null) {
            myListView = (ListView) getView().findViewById(android.R.id.list);
            myMealList = new ArrayList<>();

            Button button = (Button) getView().findViewById(R.id.create_list_button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prepIngredients();
                }
            });
        }
        //retrieve all the meals from the database
        new GetMealsWebTask().execute(MEAL_URL);
    }

    /**
     * Method that iterates through the meal list to find out which meals had been selected.
     */
    private void prepIngredients() {
        //iterate through and find selected meals
        List<Meal> selectedMeals = new ArrayList<>();
        for(Meal m : myMealList) {
            if (m.getSelected()) {
                selectedMeals.add(m);
            }
        }

        //create shopping list and pass to bundle
        ShoppingList shoppingList = new ShoppingList(selectedMeals);
        Bundle b = new Bundle();
        b.putSerializable(getString(R.string.shopping_list_bundle), shoppingList);

        ListFragment fragment = new ShoppingListFragment();
        fragment.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }


    /**
     * Method for updating all the meals taken from the database with their appropriate ingredients.
     */
    private void updateMealIngredients() {
        for (int i = 0; i < myMealList.size(); i++) {
            String url = ING_URL + String.valueOf(myMealList.get(i).getID());
            new GetIngredientsWebTask().prep(url, i);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal, container, false);
    }

    /**
     * A custom AsyncTask class that is responsible for getting all the meals within the off-site
     * database.
     */
    private class GetMealsWebTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. The url may be invalid.";
            }
        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();

                //utilize a buffered reader to retrieve web service output
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(is));

                //concatenate string based on buffered reader's lines
                String contentAsString = "";
                String s;
                while ((s = buffer.readLine()) != null) {
                    contentAsString += s;
                }

                Log.d(TAG, "The string is: " + contentAsString);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (Exception e) {
                Log.d(TAG, "Something happened" + e.getMessage());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Parse JSON
            try {
                JSONArray array = new JSONArray(s);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    //retrieve necessary information
                    int id = obj.getInt("mealID");
                    String name = obj.getString("name");
                    int difficulty = obj.getInt("difficulty");
                    String serves = obj.getString("serves");
                    String directions = obj.getString("directions");
                    String createdBy = obj.getString("createdBy");
                    String cuisine = obj.getString("cuisine type");

                    Meal m = new Meal(name, id, directions, difficulty, serves, createdBy, cuisine);
                    myMealList.add(m);
                }
                MealAdapter adapter = new MealAdapter(myMealList, getActivity());
                myListView.setAdapter(adapter);
                updateMealIngredients();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A custom AsyncTask class that is responsible for getting all the meals within the off-site
     * database.
     */
    private class GetIngredientsWebTask extends AsyncTask<String, Void, String> {

        /**
         * The index of the meal getting ingredients for.
         */
        private int mealIndex;

        /**
         * Method which will take an index as a parameter and execute a given url.
         * @param theUrl the url to execute.
         * @param theIndex The index of the meal.
         */
        public void prep(final String theUrl, final int theIndex) {
            execute(theUrl);
            mealIndex = theIndex;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. The url may be invalid.";
            }
        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();

                //utilize a buffered reader to retrieve web service output
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(is));

                //concatenate string based on buffered reader's lines
                String contentAsString = "";
                String s;
                while ((s = buffer.readLine()) != null) {
                    contentAsString += s;
                }

                Log.d(TAG, "The string is: " + contentAsString);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch (Exception e) {
                Log.d(TAG, "Something happened" + e.getMessage());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Parse JSON
            try {
                JSONArray array = new JSONArray(s);

                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    //retrieve necessary information
                    String amount = obj.getString("amount");
                    String name = obj.getString("name");

                    Ingredient ing = new Ingredient(name, amount);

                    myMealList.get(mealIndex).addIngredient(ing);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
