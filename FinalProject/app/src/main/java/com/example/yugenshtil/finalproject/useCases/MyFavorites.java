package com.example.yugenshtil.finalproject.useCases;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.MyFavoritesAdapter;
import com.example.yugenshtil.finalproject.model.ItemDisplayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFavorites extends Activity implements MyFavoritesAdapter.ItemClickCallback{

    private String GETALLMYFAVORITES="http://senecaflea.azurewebsites.net/api/User/";
    private String REMOVEMYFAVORITES="http://senecaflea.azurewebsites.net/api/User/";


    private JSONArray jsonArray=null;
    public ProgressDialog pd;
    private RecyclerView recView;
    private MyFavoritesAdapter adapter;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    private String id="";


    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private LinearLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorites);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("id", "");


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
                        Log.d("Oleg", "size " + items.length());
                        Log.d("Oleg", "JSON " + items.toString());
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                //  myItemsList+="Title: "+ item.getString("Title")+" Status:"+item.getString("Status")+" Price: " + item.getString("Price")+"\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // tvItemsList.setText(myItemsList);
                    }

                    recView = (RecyclerView)findViewById(R.id.recfavorites_list);
                    //LayoutManager: GridLayoutManager or StaggeredGridLayoutManager

                    recView.setLayoutManager(new LinearLayoutManager(MyFavorites.this));
                    //Send list here
                    //WAS   adapter = new DerpAdapter(DerpData.getListData(),Sell.this,jsonArray);

                    adapter = new MyFavoritesAdapter(MyFavorites.this,jsonArray);

                    Log.d("Oleg","Setting adapter");
                    recView.setAdapter(adapter);
                    adapter.setItemClickCallback(MyFavorites.this);

                    //    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
                    //    itemTouchHelper.attachToRecyclerView(recView);


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
                Log.d("Oleg","error" + error);

            }
        });

        MySingleton.getInstance(MyFavorites.this).addToRequestQueue(jsObjRequest);

    }

    public void deleteMyFaforite(String itemId){
        Log.d("Oleg","Item id is " + id);
        JSONObject jsonObject = new JSONObject();
      //  JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, REMOVEMYFAVORITES+id+"/RemoveFavorite/"+itemId,jsonObject,
        StringRequest jsObjPutRequest = new StringRequest(Request.Method.PUT, REMOVEMYFAVORITES+id+"/RemoveFavorite/"+itemId,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", " " +response);
                        Log.d("Oleg","MyFavorite was deleted");
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
        );

        MySingleton.getInstance(MyFavorites.this).addToRequestQueue(jsObjPutRequest);
    }

    @Override
    public void onMyFavoriteDeleteClick(int p) {
        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            Log.d("Oleg","Wanna delete an item with Id " + item.get("ItemId"));
            String itemId = item.get("ItemId").toString();
            deleteMyFaforite(itemId);
          /*  Intent i  = new Intent(this, ItemDisplayActivity.class);
            Bundle extras = new Bundle();
            extras.putString("ItemId",item.get("ItemId").toString());
            extras.putString("Title",item.get("Title").toString());
            extras.putString("SellerId",item.get("SellerId").toString());
            extras.putString("Description",item.get("Description").toString());
            extras.putString("Price",item.get("Price").toString());
            i.putExtras(extras);
            startActivity(i);*/

        } catch (JSONException e) {
            Log.d("Oleg","Error " + e.getMessage());
        }

    }

    @Override
    public void onMyFavoriteAddClick(int p) {
        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            Log.d("Oleg","Wanna add item with Id " + item.get("ItemId"));
            String itemId = item.get("ItemId").toString();
            addMyFaforite(itemId);
          /*  Intent i  = new Intent(this, ItemDisplayActivity.class);
            Bundle extras = new Bundle();
            extras.putString("ItemId",item.get("ItemId").toString());
            extras.putString("Title",item.get("Title").toString());
            extras.putString("SellerId",item.get("SellerId").toString());
            extras.putString("Description",item.get("Description").toString());
            extras.putString("Price",item.get("Price").toString());
            i.putExtras(extras);
            startActivity(i);*/

        } catch (JSONException e) {
            Log.d("Oleg","Error " + e.getMessage());
        }

    }

    @Override
    public void onItemClick(int p) {
        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            Log.d("Oleg","Wanna add item with Id " + item.get("ItemId"));
           Intent i  = new Intent(this, ItemDisplayActivity.class);
            Bundle extras = new Bundle();
            extras.putString("ItemId",item.get("ItemId").toString());
            extras.putString("Title",item.get("Title").toString());
            extras.putString("SellerId",item.get("SellerId").toString());
            extras.putString("Description",item.get("Description").toString());
            extras.putString("Price",item.get("Price").toString());
            i.putExtras(extras);
            startActivity(i);

        } catch (JSONException e) {
            Log.d("Oleg","Error " + e.getMessage());
        }

    }

    public void addMyFaforite(String itemId){
        Log.d("Oleg","Item id is " + id);
        JSONObject jsonObject = new JSONObject();
        //  JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, REMOVEMYFAVORITES+id+"/RemoveFavorite/"+itemId,jsonObject,
        StringRequest jsObjPutRequest = new StringRequest(Request.Method.PUT, REMOVEMYFAVORITES+id+"/RemoveFavorite/"+itemId,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", " " +response);
                        Log.d("Oleg","MyFavorite was deleted");
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
        );

        MySingleton.getInstance(MyFavorites.this).addToRequestQueue(jsObjPutRequest);
    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent back = new Intent(this, UserMenu.class);
        startActivity(back);
    }
}
