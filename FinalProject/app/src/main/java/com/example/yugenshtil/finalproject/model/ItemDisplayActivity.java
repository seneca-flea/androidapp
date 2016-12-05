package com.example.yugenshtil.finalproject.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.Item.AddItem;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.Item.EditItem;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.useCases.Sell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemDisplayActivity extends Activity {

    private static String ItemId = "";
    private static String Title = "";
    private static String SellerId = "";
    private static String Description = "";
    private static String Price = "";
    private ImageView image=null;
    private String GETITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/user/";
    private String DELETEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/";
    public ProgressDialog pd;
    SharedPreferences sharedpreferences;
    String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);

        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");

        final TextView etTitle = (TextView) findViewById(R.id.tvItemBuy_Title);
        final TextView etDescription = (TextView) findViewById(R.id.tvItemBuy_Description);
        final TextView etPrice = (TextView) findViewById(R.id.tvItemBuy_Price);
        final ImageView imDelete = (ImageView) findViewById(R.id.imDelete_itemDisplay);
        final ImageView imUpdate = (ImageView) findViewById(R.id.ivItemBuy_Favorite);
        image = (ImageView) findViewById(R.id.ivImage_ItemDisplay);




        Intent i = getIntent();
        Bundle extras = i.getExtras();


        ItemId = extras.getString("ItemId");
        SellerId = extras.getString("SellerId");
        Title = extras.getString("Title");
        Description = extras.getString("Description");
        Price = extras.getString("Price");

        setItem(ItemId);

        etTitle.setText(Title);
        etDescription.setText(Description);
        etPrice.setText(Price);

        imDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("LOG : ","Delete clicked");
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
        Log.d("LOG :","Gonna delete");
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

    void setItem(String id){

        JsonArrayRequest sr = new JsonArrayRequest(Request.Method.GET,"http://senecafleamarket.azurewebsites.net/api/Item/" +id, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    if(response!=null){
                        JSONObject s = response.getJSONObject(0);
                        Log.d("Oleg","s is " + s.toString());
                        byte[] decodedString = Base64.decode(s.getString("Photo"), Base64.DEFAULT);
                        Log.d("Oleg","Decoded BYTES " + decodedString );

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image.setImageBitmap(decodedByte);
                    }



                 //   Log.d("Oleg","Got JSON " + response.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                headers.put("Accept","image/*");
                return headers;
            }
        };

        MySingleton.getInstance(ItemDisplayActivity.this).addToRequestQueue(sr);




    }



}
