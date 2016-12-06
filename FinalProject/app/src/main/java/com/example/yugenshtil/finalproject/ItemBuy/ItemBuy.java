package com.example.yugenshtil.finalproject.ItemBuy;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.Item.EditItem;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.model.ItemDisplayActivity;
import com.example.yugenshtil.finalproject.model.MyMessagesListDisplayActivity;
import com.example.yugenshtil.finalproject.useCases.Sell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yugenshtil on 29/11/16.
 */

public class ItemBuy extends Activity {

    private static String ItemId = "";
    private static String Title = "";
    private static String SellerId = "";
    private static String Description = "";
    private static String Price = "";
    private static String Course = "";
    private static String Program = "";
    private static String Year = "";
    private static String Publisher = "";
    private static String Author = "";

    private ImageView image=null;

    private String GETITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/user/";
    private String DELETEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/";
    public ProgressDialog pd;
    SharedPreferences sharedpreferences;
    String token="";
    private JSONArray jsonArray=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_sell_display);
        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        token = sharedpreferences.getString("token", "");

        final TextView etTitle = (TextView) findViewById(R.id.tvItem_Title);
        final TextView etDescription = (TextView) findViewById(R.id.tv_sell_Desc);
        final TextView etPrice = (TextView) findViewById(R.id.tvItem_Price);
        final TextView etProgram = (TextView) findViewById(R.id.tv_itemSell_Program);
        final TextView etYear = (TextView) findViewById(R.id.tv_itemSell_Year);
        final TextView etCourse = (TextView) findViewById(R.id.tv_iteSell_Course);
        final TextView etPublisher = (TextView) findViewById(R.id.tv_itemSell_Publisher);
        final TextView etAuthor = (TextView) findViewById(R.id.tv_itemSell_Author);

     //   final ImageView ivFavorite = (ImageView) findViewById(R.id.ivItemBuy_Favorite);
        final ImageView ivMessage = (ImageView) findViewById(R.id.ivItemBuy_Message);


        image = (ImageView) findViewById(R.id.ivItemBuy_Image);




        Intent i = getIntent();
        Bundle extras = i.getExtras();


        ItemId = extras.getString("ItemId");
        SellerId = extras.getString("SellerId");
        Title = extras.getString("Title");
        Description = extras.getString("Description");
        Price = extras.getString("Price");
        Course = extras.getString("Course");
        Program = extras.getString("Program");
        Year = extras.getString("Year");
        Publisher = extras.getString("Publisher");
        Author = extras.getString("Author");


        setItem(ItemId);

        etTitle.setText(Title);
        etDescription.setText(Description);
        etPrice.setText("$ " + Price);
        if (Course.contains("null") || Course.contains("NA")) {
            etCourse.setText("n/a");
        }
        else {
            etCourse.setText(Course);
        }
        if (Program.contains("null") ) {
            etProgram.setText("n/a");
        }
        else {
            etProgram.setText(Program);
        }
        if (Year.contains("null") || Year.contains("0")) {
            etYear.setText("n/a");
        }
        else {
            etYear.setText(Year);
        }
        if (Publisher.contains("null")) {
            etPublisher.setText("n/a");
        }
        else {
            etPublisher.setText(Publisher);
        }
        if (Author.contains("null")) {
            etAuthor.setText("n/a");
        }
        else {
            etAuthor.setText(Author);
        }

      /*  ivFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.d("Oleg","Myfav clicked");
               // deleteAnItem(ItemId);

            }
        });*/

        ivMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("LOG : ","Message clicked on ItemBuy.java");
                //TODO: implement method to start messageactivity
               // updateAnItem(ItemId);

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
                        Intent sellIntent = new Intent(ItemBuy.this,Sell.class);
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
        MySingleton.getInstance(ItemBuy.this).addToRequestQueue(dr);

    }


    public void updateAnItem(String id) {
        // pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        try {
            Log.d("Oleg","Update");
            Intent i = new Intent(ItemBuy.this, EditItem.class);
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

            Intent mainMenuIntent = new Intent(ItemBuy.this,MainMenu.class);
            startActivity(mainMenuIntent);



        }

        return super.onOptionsItemSelected(item);
    }

    void setItem(String id){
        Log.d("ID","id is " + id);
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest sr = new JsonArrayRequest(Request.Method.GET,"http://senecafleamarket.azurewebsites.net/api/Item/" +id, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    if(response!=null){
                        JSONObject s = response.getJSONObject(0);
                        Log.d("LOG : ","s is " + s.toString());
                        Log.d("oleg"," Bytes got"+s.getString("Photo"));
                        byte[] decodedString = Base64.decode(s.getString("Photo"), Base64.DEFAULT);
                        Log.d("LOG : ","Decoded BYTES " + decodedString );

                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image.setImageBitmap(decodedByte);
                        pd.cancel();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
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

        MySingleton.getInstance(ItemBuy.this).addToRequestQueue(sr);
    }

}
