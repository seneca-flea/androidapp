package com.example.yugenshtil.finalproject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.ItemBuy.Buy;
import com.example.yugenshtil.finalproject.model.MyMessagesListDisplayActivity;
import com.example.yugenshtil.finalproject.useCases.History;
import com.example.yugenshtil.finalproject.useCases.MyFavorites;
import com.example.yugenshtil.finalproject.useCases.MyMessages;
import com.example.yugenshtil.finalproject.useCases.Sell;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserMenu extends Activity {

    private String id = "";
    private String token = "";
    private String email = "";
    public ProgressDialog pd;
    private String GETUSERINFO = "http://senecafleamarket.azurewebsites.net/api/User/CurrentUser";
    private String DELETEUSERURL = "http://senecafleamarket.azurewebsites.net/api/User/";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("UserId", "");
        email = sharedpreferences.getString("Email", "");
        token = sharedpreferences.getString("token", "");

        final ImageView bSell = (ImageView) findViewById(R.id.userMenuSellButton);
        final ImageView bBuy = (ImageView) findViewById(R.id.userMenuBuyButton);
        final ImageView bHistory = (ImageView) findViewById(R.id.userMenuHistoryButton);
        final ImageView bFavorites = (ImageView) findViewById(R.id.userMenuMyFavoritesButton);
        final ImageView bMessage = (ImageView) findViewById(R.id.userMenuMessagesButton);
        final ImageView bContactUs = (ImageView) findViewById(R.id.userMenuContactUsButton);

        hasNotification();

        // Listener of the UserMenu - Sell Button
        bSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG : ", "Sell button clicked on userMenu");

                Intent sellIntent = new Intent(UserMenu.this, Sell.class);
                sellIntent.putExtra("id", id);
                //  sellIntent.putExtra("fullName", fullName);
                sellIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(sellIntent);

            }
        });

        // Listener of the UserMenu - Buy Button
        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Buy button clicked on userMenu");
                Intent buyIntent = new Intent(UserMenu.this, Buy.class);
                startActivity(buyIntent);
                finish();

            }
        });

        // Listener of the UserMenu - History Button
        bHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG : ", "History button clicked on userMenu");
                Intent historyIntent = new Intent(UserMenu.this, History.class);
                historyIntent.putExtra("id", id);
                //  historyIntent.putExtra("fullName", fullName);
                historyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(historyIntent);

            }
        });

        // Listener of the UserMenu - MyFavorites Button
        bFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Login clicked");
                Intent myFavoritesIntent = new Intent(UserMenu.this, MyFavorites.class);
                startActivity(myFavoritesIntent);

            }
        });

        // Listener of the UserMenu - Message Button
        bMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Message button clicked on userMenu");
                Intent messageIntent = new Intent(UserMenu.this, MyMessages.class);
                startActivity(messageIntent);

            }
        });

        //Ricardo: updated to correct typo (bMessage for bContactUs)
        // Listener of the UserMenu - ContactUs Button
        bContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg", "Login clicked");
                Intent contactUsIntent = new Intent(UserMenu.this, ContactUs.class);
                startActivity(contactUsIntent);
            }
        });
    }


    private void hasNotification() {

        Log.d("LOG : ", "checking notifications for user.");


        Log.d("LOG : ","checking notifications for user.");
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        JsonObjectRequest jsObjGetRequest = new JsonObjectRequest(Request.Method.GET, GETUSERINFO, null, new Response.Listener<JSONObject>() {
            String myMessagesList = "";

            @Override
            public void onResponse(JSONObject response) {

                if (response != null) {
                    JSONObject items = response;

                    if (items != null) {
                        Log.d("Log : ", "user info: " + items.toString());

                        try {
                            myMessagesList += items.getString("HasNewMessage");
                            Log.d("LOG : ", "has message is: " + myMessagesList);
                            if (myMessagesList.contains("false")) {
                                Log.d("LOG : ", "No new messages");
                            } else {
                                ImageView messages = (ImageView) findViewById(R.id.userMenuMessagesButton);
                                messages.setImageResource(R.drawable.bt4myfavorites);
                                Log.d("LOG : ", "Triggering notification");
                                Log.d("LOG : ", "has message");
                                NotificationCompat.Builder mBuilder =
                                        (NotificationCompat.Builder) new NotificationCompat.Builder(UserMenu.this)
                                                .setSmallIcon(R.drawable.notification_icon)
                                                .setContentTitle("New message")
                                                .setContentText("You have a new message!");

                                Intent resultIntent = new Intent(UserMenu.this, MyMessages.class);

                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(UserMenu.this);

                                stackBuilder.addParentStack(MyMessages.class);

                                stackBuilder.addNextIntent(resultIntent);
                                PendingIntent resultPendingIntent =
                                        stackBuilder.getPendingIntent(
                                                0,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );
                                mBuilder.setContentIntent(resultPendingIntent);
                                NotificationManager mNotificationManager =
                                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                                int mId = 1;
                                mNotificationManager.notify(mId, mBuilder.build());

                                Toast.makeText(getApplicationContext(), "You have a new message, check notifications", Toast.LENGTH_LONG).show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No messages", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "No messages", Toast.LENGTH_SHORT).show();
                }

                pd.cancel();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pd.cancel();
                Log.d("LOG :", "error : " + error);

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);

                Log.d("Token ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(UserMenu.this).addToRequestQueue(jsObjGetRequest);
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
        } else if (id == R.id.action_logout) {
            SharedPreferences preferences = getSharedPreferences("MyPrefs", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent mainMenuIntent = new Intent(UserMenu.this, MainMenu.class);
            startActivity(mainMenuIntent);
        } else if (id == R.id.action_deactivate) {

            deactivateAccount();

            Intent mainMenuIntent = new Intent(UserMenu.this, MainMenu.class);
            startActivity(mainMenuIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    public void deactivateAccount() {
        Log.d("Oleg", "Deleteing id " + id);
        pd = ProgressDialog.show(this, "", "Account is deactivating...Please wait...", true);
        StringRequest dr = new StringRequest(Request.Method.DELETE, DELETEUSERURL + id,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        Log.d("Oleg", "Response is " + response);
                        //    adapter.notifyDataSetChanged();
                        Intent aboutAppIntent = new Intent(UserMenu.this, MainMenu.class);
                        startActivity(aboutAppIntent);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("Oleg", "Response error is " + error);

                    }
                }
        );
        MySingleton.getInstance(UserMenu.this).addToRequestQueue(dr);


    }
}
