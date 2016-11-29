package com.example.yugenshtil.finalproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.ItemBuy.Buy;
import com.example.yugenshtil.finalproject.useCases.History;
import com.example.yugenshtil.finalproject.useCases.MyFavorites;
import com.example.yugenshtil.finalproject.useCases.MyMessages;
import com.example.yugenshtil.finalproject.useCases.Sell;

public class UserMenu extends Activity {

    private String id = "";
    private String fullName="";
    public ProgressDialog pd;
    private String DELETEUSERURL="http://senecaflea.azurewebsites.net/api/User/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        Log.d("Oleg","UserMenu");
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
                Log.d("LOG : ", "Sell button clicked on userMenu");

                Intent sellIntent = new Intent(UserMenu.this,Sell.class);
                sellIntent.putExtra("id", id);
                sellIntent.putExtra("fullName", fullName);
                sellIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                finish();

            }
        });

        // Listener of the UserMenu - History Button
        bHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG : ", "History button clicked on userMenu");
                Intent historyIntent = new Intent(UserMenu.this,History.class);
                historyIntent.putExtra("id", id);
                historyIntent.putExtra("fullName", fullName);
                historyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

        //Ricardo: updated to correct typo (bMessage for bContactUs)
        // Listener of the UserMenu - ContactUs Button
        bContactUs.setOnClickListener(new View.OnClickListener() {
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
        else if(id==R.id.action_logout){
            SharedPreferences preferences = getSharedPreferences("MyPrefs", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent mainMenuIntent = new Intent(UserMenu.this,MainMenu.class);
            startActivity(mainMenuIntent);
        }
        else if(id==R.id.action_deactivate){

            deactivateAccount();

            Intent mainMenuIntent = new Intent(UserMenu.this,MainMenu.class);
            startActivity(mainMenuIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    public void deactivateAccount(){
        Log.d("Oleg","Deleteing id " + id);
        pd = ProgressDialog.show(this, "", "Account is deactivating...Please wait...", true);
        StringRequest dr = new StringRequest(Request.Method.DELETE, DELETEUSERURL+id,
                new Response.Listener<String>()
                {


                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        Log.d("Oleg","Response is "+response);
                        //    adapter.notifyDataSetChanged();
                        Intent aboutAppIntent = new Intent(UserMenu.this,MainMenu.class);
                        startActivity(aboutAppIntent);


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("Oleg","Response error is "+error);

                    }
                }
        );
        MySingleton.getInstance(UserMenu.this).addToRequestQueue(dr);


    }
}
