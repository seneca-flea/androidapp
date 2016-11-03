package com.example.yugenshtil.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yugenshtil.finalproject.useCases.Buy;
import com.example.yugenshtil.finalproject.useCases.History;
import com.example.yugenshtil.finalproject.useCases.MyFavorites;
import com.example.yugenshtil.finalproject.useCases.MyMessages;
import com.example.yugenshtil.finalproject.useCases.Sell;

public class UserMenu extends Activity {

    private String id = "";
    private String fullName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            fullName = extras.getString("fullName");

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, fullName+", welcome back! You id is " + id, duration);
            toast.show();



            //The key argument here must match that used in the other activity
        }


        final ImageView bSell = (ImageView) findViewById(R.id.userMenuSellButton);
        final ImageView bBuy = (ImageView) findViewById(R.id.userMenuBuyButton);
        final ImageView bHistory = (ImageView) findViewById(R.id.userMenuHistoryButton);
        final ImageView bFavorites = (ImageView) findViewById(R.id.userMenuMyFavoritesButton);
        final ImageView bMessage = (ImageView) findViewById(R.id.userMenuMessagesButton);
        final ImageView bContactUs = (ImageView) findViewById(R.id.userMenuContactUsButton);

        // Listener of the UserMenu - Sell Button
        bSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Login clicked");

                Intent sellIntent = new Intent(UserMenu.this,Sell.class);
                sellIntent.putExtra("id", id);
                sellIntent.putExtra("fullName", fullName);
                startActivity(sellIntent);

            }
        });

        // Listener of the UserMenu - Buy Button
        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Login clicked");
                Intent buyIntent = new Intent(UserMenu.this,Buy.class);
                startActivity(buyIntent);

            }
        });

        // Listener of the UserMenu - History Button
        bHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Login clicked");
                Intent historyIntent = new Intent(UserMenu.this,History.class);
                startActivity(historyIntent);

            }
        });

        // Listener of the UserMenu - MyFavorites Button
        bFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Login clicked");
                Intent myFavoritesIntent = new Intent(UserMenu.this,MyFavorites.class);
                startActivity(myFavoritesIntent);

            }
        });

        // Listener of the UserMenu - Message Button
        bMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Login clicked");
                Intent messageIntent = new Intent(UserMenu.this,MyMessages.class);
                startActivity(messageIntent);

            }
        });

        // Listener of the UserMenu - ContactUs Button
        bMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Login clicked");
                Intent contactUsIntent = new Intent(UserMenu.this,ContactUs.class);
                startActivity(contactUsIntent);

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_menu, menu);
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
