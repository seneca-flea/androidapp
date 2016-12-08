package com.example.yugenshtil.finalproject.ItemSell;

/*
The class was created by Oleg Mytryniuk
 */
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.ItemEdit.EditBook;
import com.example.yugenshtil.finalproject.ItemEdit.EditMaterial;
import com.example.yugenshtil.finalproject.OtherUseCases.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemDisplayActivity extends Activity {

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
    private static String Type = "";


    private ImageView image=null;
    private String GETITEMSURL="http://senecafleamarket.azurewebsites.net/api/Item/filter/user/";
    private String DELETEITEMSURL="http://senecafleamarket.azurewebsites.net/api/Item/";
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
        final TextView etProgram = (TextView) findViewById(R.id.tv_itemBuy_program);
        final TextView etYear = (TextView) findViewById(R.id.tv_itemBuy_year);
        final TextView etCourse = (TextView) findViewById(R.id.tv_itemBuy_Course);
        final TextView etPublisher = (TextView) findViewById(R.id.tv_itemBuy_Publisher);
        final TextView etAuthor = (TextView) findViewById(R.id.tv_itemBuy_Author);
        final ImageView imDelete = (ImageView) findViewById(R.id.imDelete_itemDisplay);
        final ImageView imUpdate = (ImageView) findViewById(R.id.ivItemBuy_Favorite);

        image = (ImageView) findViewById(R.id.ivImage_ItemDisplay);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        Type = extras.getString("Type");
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

        Log.d("Oleg","Type is " + Type);

        setItem(ItemId);

        etTitle.setText(Title);
        etDescription.setText(Description);
        etPrice.setText("$ " + Price);
        if (Course.contains("null")) {
            etCourse.setText("n/a");
        }
        else {
            etCourse.setText(Course);
        }
        if (Program.contains("null")) {
            etProgram.setText("n/a");
        }
        else {
            etProgram.setText(Program);
        }
        if (Type.equals("Material")) {
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
        StringRequest dr = new StringRequest(Request.Method.DELETE, DELETEITEMSURL+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        Log.d("LOG : ","Response is "+response);
                        Toast.makeText(getApplicationContext(), "Item was deleted", Toast.LENGTH_LONG).show();
                        Intent sellIntent = new Intent(ItemDisplayActivity.this,Sell.class);
                        startActivity(sellIntent);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(getApplicationContext(), "Item was not deleted", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                return headers;
            }
        };


        MySingleton.getInstance(ItemDisplayActivity.this).addToRequestQueue(dr);
    }


    public void updateAnItem(String id) {
        try {

            Intent i;

            if(Type.equals("Book")){
                i = new Intent(ItemDisplayActivity.this, EditBook.class);
                i.putExtra("BookTitle",Title);
                i.putExtra("BookYear",Year);
                i.putExtra("BookPublisher",Publisher);
                i.putExtra("BookAuthor",Author);
                startActivity(i);

            }else {
                i = new Intent(ItemDisplayActivity.this, EditMaterial.class);
            }
            i.putExtra("ItemId",ItemId);
            i.putExtra("Title",Title);
            i.putExtra("SellerId",SellerId);
            i.putExtra("Description",Description);
            i.putExtra("Price",Price);
            i.putExtra("CourseProgram",Program);
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
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest sr = new JsonArrayRequest(Request.Method.GET,"http://senecafleamarket.azurewebsites.net/api/Item/" +id, null,  new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    if(response!=null){
                        JSONObject s = response.getJSONObject(0);
                        byte[] decodedString = Base64.decode(s.getString("Photo"), Base64.DEFAULT);
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

        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        sr.setRetryPolicy(policy);
        MySingleton.getInstance(ItemDisplayActivity.this).addToRequestQueue(sr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_display_activity,menu);

        return true;
    }


}
