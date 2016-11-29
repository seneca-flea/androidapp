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
import com.example.yugenshtil.finalproject.item.EditItem;
import com.example.yugenshtil.finalproject.useCases.Sell;

public class ItemDisplayActivity extends Activity {

    private static String ItemId = "";
    private static String Title = "";
    private static String SellerId = "";
    private static String Description = "";
    private static String Price = "";
    private String GETITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/user/";
    private String DELETEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/";
    public ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);

        final TextView etTitle = (TextView) findViewById(R.id.tvTitle_itemDisplay);
        final TextView etDescription = (TextView) findViewById(R.id.tvDescription_itemDisplay);
        final TextView etPrice = (TextView) findViewById(R.id.tvPrice_itemDisplay);
        final ImageView imDelete = (ImageView) findViewById(R.id.imDelete_itemDisplay);
        final ImageView imUpdate = (ImageView) findViewById(R.id.imUpdate_itemDisplay);




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
                Log.d("Oleg","Delete clicked");
                deleteAnItem(ItemId);

            }
        });

        imUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAnItem(ItemId);

            }
        });

    }

    public void deleteAnItem(String id){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("Oleg","Gonna delete");
        StringRequest dr = new StringRequest(Request.Method.DELETE, DELETEITEMSURL+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        Log.d("Oleg","Response is "+response);
                        //    adapter.notifyDataSetChanged();
                        Intent sellIntent = new Intent(ItemDisplayActivity.this,Sell.class);
                        startActivity(sellIntent);

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
        MySingleton.getInstance(ItemDisplayActivity.this).addToRequestQueue(dr);

    }


    public void updateAnItem(String id) {
        // pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        try {
            Log.d("Oleg","Update");
            Intent i = new Intent(ItemDisplayActivity.this, EditItem.class);
            i.putExtra("ItemId",ItemId);
            i.putExtra("Title",Title);
            i.putExtra("SellerId",SellerId);
            i.putExtra("Description",Description);
            i.putExtra("Price",Price);
            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
        }


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

            Intent mainMenuIntent = new Intent(ItemDisplayActivity.this,MainMenu.class);
            startActivity(mainMenuIntent);



        }

        return super.onOptionsItemSelected(item);
    }



}
