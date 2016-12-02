package com.example.yugenshtil.finalproject.useCases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.HistoryAdapter;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.model.ItemDisplayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class History extends Activity implements  HistoryAdapter.ItemClickCallback {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String GETUSERHISTORYURL1="http://senecafleamarket.azurewebsites.net/api/User/";
    private String GETUSERHISTORYURL2="/History";
    private String DELETEUSERHISTORYURL1="http://senecafleamarket.azurewebsites.net/api/User/";
    private String DELETEUSERHISTORYURL2="/History/";

    JSONArray jsonArray=null;
    private String id = "";
    private String fullName="";
    private String token = "";
    private String historyItemId ="";

    private RecyclerView recView;

    public ProgressDialog pd;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);

<<<<<<< HEAD
        id = sharedpreferences.getString("UserId","");
        Log.d("LOG:", "id here is " + id );
=======

        id = sharedpreferences.getString("UserId", "");
>>>>>>> 6003de7412f76cece2fde3438152fce37f27299c
        fullName = sharedpreferences.getString("fullName", "");
        token = sharedpreferences.getString("token","");
        Log.d("LOG : ","onCreate for history running");

        getHistoryItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    public void getHistoryItems(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("LOG : ", "getHistoryItems for History.java is running");
        Log.d("LOG : ", "id is : " + id);
        String URL = GETUSERHISTORYURL1 + id + GETUSERHISTORYURL2;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            String myItemsList="";

            @Override
            public void onResponse(JSONArray response) {

                pd.cancel();

                if(response!=null){
                    JSONArray items = response;

                    jsonArray = response;
                    if(items!=null) {
                        Log.d("Log : ", "number of items on history is: " + items.length());
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                historyItemId = item.getString("Id");

                                JSONObject historyItem = item.getJSONObject("Item");
                                String itemId = historyItem.getString("ItemId");
                                String imageCount = historyItem.getString("ImagesCount");
                                String title = historyItem.getString("Title");
                                String status = historyItem.getString("Status");
                                String sellerId = historyItem.getString("SellerId");
                                String description = historyItem.getString("Description");
                                String price = historyItem.getString("Price");

                                JSONObject historySeller = item.getJSONObject("Seller");
                                String userId = historySeller.getString("UserId");
                                String email = historySeller.getString("Email");

                                String purchaseDate = item.getString("PurchaseDate");

                                myItemsList+="Id: "+ historyItemId+" Title:"+ title + "Desc: " + description + "Pice: " + price + "\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // tvItemsList.setText(myItemsList);
                    }


                    recView = (RecyclerView)findViewById(R.id.recViewHistory);

                    recView.setLayoutManager(new LinearLayoutManager(History.this));

                    adapter = new HistoryAdapter(History.this, jsonArray);

                    Log.d("LOG : ","Setting adapter for History.java");
                    recView.setAdapter(adapter);

                    adapter.setItemClickCallback(History.this);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
                    itemTouchHelper.attachToRecyclerView(recView);

                }
                else{
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
                Log.d("LOG :","error : " + error);

            }
        });
        MySingleton.getInstance(History.this).addToRequestQueue(jsObjRequest);
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

    public void onItemClick(int p) {

        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
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
            e.printStackTrace();
        }

    }



    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteAnItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }
    private void moveItem(int oldPos, int newPos) {

        //  ListItem item = (ListItem) listData.get(oldPos);
        //  listData.remove(oldPos);
        //  listData.add(newPos, item);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(History.this, UserMenu.class));
        Log.d("LOG : ","Back button pressed on history");
        finish();

    }


    public void onDeleteIconClick(int p) {

        try {
            Log.d("LOG : ","Delete icon clicked on itemHistory");
            JSONObject item = (JSONObject)jsonArray.get(p);

            String deleteItemId = item.getString("Id");

            int deleteItemIdInt = Integer.parseInt(deleteItemId);
            Log.d("LOG : ","deleting item form history, with id: " + deleteItemId);
            deleteAnItem(deleteItemIdInt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
    public void deleteAnItem(int id){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("LOG :","deleteAnItem running on itemHistory");

        String URL = DELETEUSERHISTORYURL1 + this.id + DELETEUSERHISTORYURL2 + id;
        StringRequest dr = new StringRequest(Request.Method.DELETE, URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //stop progress dialog
                        pd.cancel();
                        Log.d("LOG : ","Response is "+response);
                        //    adapter.notifyDataSetChanged();
                        Intent aboutAppIntent = new Intent(History.this,History.class);
                        startActivity(aboutAppIntent);


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("LOG : ","Response error is "+error);

                    }
                }
        ){
/*

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                Log.d("Oleg","I will add token " + token);
                headers.put("Authorization","Bearer "+token);
                // params.put("username",email);
                //params.put("password", password);

                Log.d("Token ", headers.toString());
                return headers;
            }

*/

        };
        MySingleton.getInstance(History.this).addToRequestQueue(dr);
    }
}
