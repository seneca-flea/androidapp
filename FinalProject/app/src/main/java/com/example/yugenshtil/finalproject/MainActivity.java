package com.example.yugenshtil.finalproject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {


    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d("Oleg", "Preferences " + sharedpreferences);
        if (sharedpreferences.contains("id")) {


            Log.d("Oleg", "Preferences " + sharedpreferences);
            Log.d("Oleg", "Preferences " + sharedpreferences.getString("id", ""));
            Log.d("Oleg", "Preferences " + sharedpreferences.getString("fullName", ""));
            goToUserMenu();

            // et.setText(sharedpreferences.getString(PIN, ""));
        }
        else {
           // Intent intent2 = new Intent(getApplicationContext(),MainMenu.class);
            //startActivity(intent2);
           goToMainMenu();
        }
    }

    /** Called when the user clicks the Send button */
    public void goToMainMenu() {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Send button */
    public void goToUserMenu() {
        Intent intent = new Intent(this, UserMenu.class);
        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
