package com.example.yugenshtil.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.Account.Registration;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        final ImageView bLogin = (ImageView) findViewById(R.id.mainMenuLoginButton);
        final ImageView bRegistration = (ImageView) findViewById(R.id.mainMenuRegistrationButton);
        final ImageView bAboutApp = (ImageView) findViewById(R.id.mainMenuLoginAboutAppButton);
        final ImageView bContactUs = (ImageView) findViewById(R.id.mainMenuContactUsButton);

        // Listener of the MainMenu - Login Button
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Login clicked");
                Intent loginIntent = new Intent(MainMenu.this,Login.class);
                startActivity(loginIntent);

            }
        });

        // Listener of the MainMenu - Registration Button
        bRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Registration clicked");
                Intent registerIntent = new Intent(MainMenu.this,Registration.class);
                MainMenu.this.startActivity(registerIntent);

            }
        });

        // Listener of the MainMenu - About App Button
        bAboutApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","About app clicked");
                   Intent aboutAppIntent = new Intent(MainMenu.this,AboutApp.class);
                   startActivity(aboutAppIntent);

            }
        });

        // Listener of the MainMenu - Contact Us Button
        bContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Contact Us");
                Intent contactUsIntent = new Intent(MainMenu.this,Test.class);
                startActivity(contactUsIntent);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
