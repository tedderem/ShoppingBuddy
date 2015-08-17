package com.tacoma.uw.erik.shoppingbuddy;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Fragment for registering a shopper.
 */
public class RegisterFragment extends Fragment {

    /** The URL needed to add a shopper to the system. */
    private static final String ADD_URL = "http://cssgate.insttech.washington.edu/~tedderem/addShopper.php?";
    /** Tag for debugging purposes. */
    private static final String TAG = "Registration";

    /**
     * Default, non-arg constructor.
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    /**
     * {@inheritDoc}
     * Sets register button.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getView() != null) {
            //set up the register button
            Button registerButton = (Button) getView().findViewById(R.id.create_button);

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkUserInputs();
                }
            });
        }
    }

    /**
     * Method which checks the user's input to ensure they are correct before attempting to
     * register the account.
     */
    private void checkUserInputs() {
        EditText username = (EditText) getView().findViewById(R.id.username_text);
        EditText passwordOne = (EditText) getView().findViewById(R.id.password_input);
        EditText passwordTwo = (EditText) getView().findViewById(R.id.password_reinput);
        EditText email = (EditText) getView().findViewById(R.id.email_input);

        //ensure all fields have values
        if(username.getText().toString().length() == 0
                || passwordOne.getText().toString().length() == 0
                || passwordTwo.getText().toString().length() == 0
                || email.getText().toString().length() == 0) {
            Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show();
        } else { //values ok
            if (passwordOne.getText().toString().equals(passwordTwo.getText().toString())) {
                //url to execute
                String executeURL = ADD_URL;
                executeURL += "username=" + username.getText().toString();
                //use the hash of the password (sub-par hashing function for now)
                executeURL += "&password="
                        + String.valueOf(passwordOne.getText().toString().hashCode());
                executeURL += "&email=" + email.getText().toString();

                new AddShopperWebTask().execute(executeURL);
            } else { //passwords do not match
                Toast.makeText(getActivity(), "Your passwords do not match",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Async Task for adding a shopper into the system.
     */
    private class AddShopperWebTask extends AsyncTask<String, Void, String> {

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
                String status = jsonObject.getString("result");
                //relay information back to user
                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getActivity(), "Account Successfully Created",
                            Toast.LENGTH_SHORT).show();

                    //create the correct fragment to display
                    LoginFragment fragment = new LoginFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                } else {
                    String reason = jsonObject.getString("error");
                    Toast.makeText(getActivity(), "Error: " + reason, Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e) {
                Log.d(TAG, "Parsing JSON Exception " +
                        e.getMessage());
            }
        }
    }
}
