package com.example.yugenshtil.finalproject.OtherUseCases;

/*
Class was designed by Oleg Mytryniuk
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.ItemBuy.ItemBuy;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.Adapter.MyFavoritesAdapter;
import com.example.yugenshtil.finalproject.ItemSell.ItemDisplayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyFavorites extends Activity implements MyFavoritesAdapter.ItemClickCallback{

    private String GETALLMYFAVORITES="http://senecafleamarket.azurewebsites.net/api/User/";
    private String MYFAVORITES="http://senecafleamarket.azurewebsites.net/api/User/";


    private JSONArray jsonArray=null;
    public ProgressDialog pd;
    private RecyclerView recView;
    private MyFavoritesAdapter adapter;
    SharedPreferences sharedpreferences;
    private String id="";
    private String token="";


    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private LinearLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("UserId", "");
        token = sharedpreferences.getString("token", "");

        getItems();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_favorites, menu);
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

            Intent mainMenuIntent = new Intent(MyFavorites.this,MainMenu.class);
            startActivity(mainMenuIntent);



        }

        return super.onOptionsItemSelected(item);
    }

    public void getItems(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETALLMYFAVORITES+id+"/Favorites", null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                pd.cancel();
                if(response!=null){
                    JSONArray items = response;
                    jsonArray = response;
                    if(items!=null) {
                       for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }

                    recView = (RecyclerView)findViewById(R.id.recfavorites_list);
                    recView.setLayoutManager(new LinearLayoutManager(MyFavorites.this));

                    adapter = new MyFavoritesAdapter(MyFavorites.this,jsonArray);
                    recView.setAdapter(adapter);
                    adapter.setItemClickCallback(MyFavorites.this);


                }else{
                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "JSON RETURNED NULL", duration);
                    toast.show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pd.cancel();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(MyFavorites.this).addToRequestQueue(jsObjRequest);

    }

    public void deleteMyFavorite(String itemId){
        JSONObject jsonObject = new JSONObject();
        StringRequest jsObjPutRequest = new StringRequest(Request.Method.DELETE, MYFAVORITES+id+"/Favorite/"+itemId,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", " " +response);

                    }


                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "Error is " + error);
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

        MySingleton.getInstance(MyFavorites.this).addToRequestQueue(jsObjPutRequest);
    }

    @Override
    public void onMyFavoriteDeleteClick(int p) {
        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            String itemId = item.get("ItemId").toString();
            deleteMyFavorite(itemId);
        } catch (JSONException e) {
            Log.d("TAG","Error " + e.getMessage());
        }

    }

    @Override
    public void onMyFavoriteAddClick(int p) {
        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            String itemId = item.get("ItemId").toString();
            addMyFavorite(itemId);

        } catch (JSONException e) {
            Log.d("TAG","Error " + e.getMessage());
        }

    }

    @Override
    public void onItemClick(int p) {
        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            Intent i  = new Intent(this, ItemBuy.class);
            Bundle extras = new Bundle();
            extras.putString("ItemId",item.get("ItemId").toString());
            extras.putString("Title",item.get("Title").toString());
            extras.putString("SellerId",item.get("SellerId").toString());
            extras.putString("Description",item.get("Description").toString());
            extras.putString("Price",item.get("Price").toString());
            extras.putString("Course",item.get("CourseName").toString());
            extras.putString("Program",item.get("CourseProgram").toString());
            extras.putString("Year",item.get("BookYear").toString());
            extras.putString("Publisher",item.get("BookPublisher").toString());
            extras.putString("Author",item.get("BookAuthor").toString());
            i.putExtras(extras);
            startActivity(i);

        } catch (JSONException e) {
            Log.d("TAG","Error " + e.getMessage());
        }

    }

    public void addMyFavorite(String itemId){
        JSONObject jsonObject = new JSONObject();
        //  JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, REMOVEMYFAVORITES+id+"/RemoveFavorite/"+itemId,jsonObject,
        StringRequest jsObjPutRequest = new StringRequest(Request.Method.PUT,MYFAVORITES+id+"/AddFavorite/"+itemId,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", " " +response);
                    }


                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "Error is " + error);
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

        MySingleton.getInstance(MyFavorites.this).addToRequestQueue(jsObjPutRequest);
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, UserMenu.class);
        startActivity(back);
    }
}
