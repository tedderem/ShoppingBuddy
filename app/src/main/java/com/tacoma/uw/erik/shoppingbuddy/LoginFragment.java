package com.tacoma.uw.erik.shoppingbuddy;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Fragment for the login view of the Shopping Buddy.
 */
public class LoginFragment extends Fragment {

    /** The URL needed for logging in a shopper. */
    public static final String LOGIN_URL = "http://cssgate.insttech.washington.edu/~tedderem/validateShopper.php?username=";

    /** The tag for debugging purposes. */
    private static final String TAG = "LOGIN";

    /**
     * Default non-arg constructor.
     */
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * {@inheritDoc}
     *
     * Sets up the listeners for each view item.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            //create the login button on click listener
            final EditText user = (EditText) getView().findViewById(R.id.loginText);
            Button login = (Button) getView().findViewById(R.id.login_button);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String executeURL = LOGIN_URL + user.getText().toString();
                    new LoginShopperWebTask().execute(executeURL);
                }
            });

            //create guest button onclick listener
            Button guest = (Button) getView().findViewById(R.id.guest_button);
            guest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MealFragment fragment = new MealFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

            //set the register text view's onclick listener
            TextView register = (TextView) getView().findViewById(R.id.textView);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RegisterFragment fragment = new RegisterFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    /**
     * AsyncTask used for logging in a shopper.
     */
    private class LoginShopperWebTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
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

        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) throws IOException {
            Reader reader;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Parse JSON
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("password");
                if (status.equalsIgnoreCase("error")) {
                    Toast.makeText(getActivity(), "Invalid username",
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    //while view isnt null
                    if (getView() != null) {
                        //retrieve input password and database password
                        final EditText passwordField = (EditText) getView().findViewById(R.id.passwordText);
                        final String password = String.valueOf(passwordField.getText().toString().hashCode());

                        //if equal, enter into menu fragment
                        if (status.equals(password)) {
                            //update the preferences file to denote being logged in
                            SharedPreferences prefs = getActivity().getSharedPreferences(
                                    getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor =  prefs.edit();
                            editor.putBoolean(getString(R.string.LOGGEDIN), true);
                            editor.apply();

                            MealFragment fragment = new MealFragment();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, fragment)
                                    .commit();
                        } else { //display error to user
                            Toast.makeText(getActivity(), "Invalid Username or Password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            } catch(Exception e) {
                Log.d(TAG, "Parsing JSON Exception " +
                        e.getMessage());
            }
        }
    }
}
