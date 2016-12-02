package com.example.yugenshtil.finalproject.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.useCases.History;

public class ItemHistoryDisplayActivity extends Activity {

    private static String ItemId = "";
    private static String Title = "";
    private static String SellerId = "";
    private static String Description = "";
    private static String Price = "";
    private String GETITEMSURL="http://senecafleamarket.azurewebsites.net/api/User/{id}/History";
    private String DELETEITEMSURL1="http://senecafleamarket.azurewebsites.net/api/User/"; //add userId
    private String DELETEITEMSURL2="/History"; //add historyId
    public ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history_display);

        final TextView etTitle = (TextView) findViewById(R.id.tvItemBuy_Title);
        final TextView etDescription = (TextView) findViewById(R.id.tvItemBuy_Description);
        final TextView etPrice = (TextView) findViewById(R.id.tvItemBuy_Price);
        final ImageView imDelete = (ImageView) findViewById(R.id.imDelete_itemDisplay);


        Intent i = getIntent();
        Bundle extras = i.getExtras();


        ItemId = extras.getString("ItemId");
        SellerId = extras.getString("SellerId");
        Title = extras.getString("Title");
        Description = extras.getString("Description");
        Price = extras.getString("Price");

        etTitle.setText(Title);
        etDescription.setText(Description);
        etPrice.setText(Price);

        imDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("LOG:","Delete on item history clicked");
                deleteAnItem(ItemId);

            }
        });

    }

    public void deleteAnItem(String id){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("LOG :","deleteAnItem on item hisory is running.");

        String URL = DELETEITEMSURL1 + SellerId + DELETEITEMSURL2 + ItemId;

        StringRequest dr = new StringRequest(Request.Method.DELETE, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        Log.d("LOG :","Response is "+response);
                        //    adapter.notifyDataSetChanged();
                        Intent deleteIntent = new Intent(ItemHistoryDisplayActivity.this,History.class);
                        startActivity(deleteIntent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("LOG :","Response error is "+error);
                    }
                }
        );
        MySingleton.getInstance(ItemHistoryDisplayActivity.this).addToRequestQueue(dr);

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

            Intent mainMenuIntent = new Intent(ItemHistoryDisplayActivity.this,MainMenu.class);
            startActivity(mainMenuIntent);
        }

        return super.onOptionsItemSelected(item);
    }



}
