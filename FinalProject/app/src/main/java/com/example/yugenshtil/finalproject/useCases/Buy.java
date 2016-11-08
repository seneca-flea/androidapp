package com.example.yugenshtil.finalproject.useCases;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.BuyItemAdapter;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.item.AddItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.yugenshtil.finalproject.R.id.btDCN_Buy;

public class Buy extends Activity {


    private String GETALLITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/status/Available";
    private String GETCOURSEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/title/Data%20Communication";
    private String GETRANGEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/price?min=100&max=400";
    private JSONArray jsonArray=null;
    public ProgressDialog pd;
    private RecyclerView recView;
    private BuyItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);


        final Button btDCN = (Button) findViewById(R.id.btDCN_Buy);
        final Button btRange = (Button) findViewById(R.id.btRange_Buy);


        getItems();

        btDCN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Clicked to DCN");
                getCourse();

            }
        });

        btRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Clicked to Range");
                getRange();
            }
        });

    }

    public void getItems(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETALLITEMSURL, null, new Response.Listener<JSONArray>() {

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

                    recView = (RecyclerView)findViewById(R.id.recbuy_list);
                    //LayoutManager: GridLayoutManager or StaggeredGridLayoutManager

                    recView.setLayoutManager(new LinearLayoutManager(Buy.this));
                    //Send list here
                    //WAS   adapter = new DerpAdapter(DerpData.getListData(),Sell.this,jsonArray);

                    adapter = new BuyItemAdapter(Buy.this,jsonArray);

                    Log.d("Oleg","Setting adapter");
                    recView.setAdapter(adapter);
                //    adapter.setItemClickCallback(Buy.this);

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

        MySingleton.getInstance(Buy.this).addToRequestQueue(jsObjRequest);

    }

    public void getCourse(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETCOURSEITEMSURL, null, new Response.Listener<JSONArray>() {

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

                    recView = (RecyclerView)findViewById(R.id.recbuy_list);
                    //LayoutManager: GridLayoutManager or StaggeredGridLayoutManager

                    recView.setLayoutManager(new LinearLayoutManager(Buy.this));
                    //Send list here
                    //WAS   adapter = new DerpAdapter(DerpData.getListData(),Sell.this,jsonArray);

                    adapter = new BuyItemAdapter(Buy.this,jsonArray);

                    Log.d("Oleg","Setting adapter");
                    recView.setAdapter(adapter);
                    //    adapter.setItemClickCallback(Buy.this);

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

        MySingleton.getInstance(Buy.this).addToRequestQueue(jsObjRequest);

    }

    public void getRange(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETRANGEITEMSURL, null, new Response.Listener<JSONArray>() {

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

                    recView = (RecyclerView)findViewById(R.id.recbuy_list);
                    //LayoutManager: GridLayoutManager or StaggeredGridLayoutManager

                    recView.setLayoutManager(new LinearLayoutManager(Buy.this));
                    //Send list here
                    //WAS   adapter = new DerpAdapter(DerpData.getListData(),Sell.this,jsonArray);

                    adapter = new BuyItemAdapter(Buy.this,jsonArray);

                    Log.d("Oleg","Setting adapter");
                    recView.setAdapter(adapter);
                    //    adapter.setItemClickCallback(Buy.this);

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

        MySingleton.getInstance(Buy.this).addToRequestQueue(jsObjRequest);

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buy, menu);
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

            Intent mainMenuIntent = new Intent(Buy.this,MainMenu.class);
            startActivity(mainMenuIntent);



        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Buy.this, UserMenu.class));
        finish();

    }

}
