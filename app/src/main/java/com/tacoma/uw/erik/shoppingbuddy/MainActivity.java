package com.tacoma.uw.erik.shoppingbuddy;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Main Activity which handles the fragments of the application for the Shopping Buddy app.
 */
public class MainActivity extends FragmentActivity {

    /**
     * {@inheritDoc}
     * Launches the log-in screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ensure that the view is not null
        if(findViewById(R.id.fragment_container) != null) {
            //check if app was already running to avoid odd behavior
            if (savedInstanceState != null) {
                return;
            }

            //check the logged in information
            SharedPreferences prefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
            boolean loggedIn = prefs.getBoolean(getString(R.string.LOGGEDIN), false);

            //decide on the logged in status and launch correct fragment
            if (loggedIn) {
                ListFragment fragment = new MealFragment();
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
            } else {
                Fragment fragment = new LoginFragment();
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * Sets the menu items.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.loggout_item) {
            SharedPreferences prefs = getSharedPreferences(getString(R.string.SHARED_PREFS),
                    Context.MODE_PRIVATE);
            if (prefs.getBoolean(getString(R.string.LOGGEDIN), false)) {
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putBoolean(getString(R.string.LOGGEDIN), false);
                prefEditor.commit();

                LoginFragment fragment = new LoginFragment();
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
