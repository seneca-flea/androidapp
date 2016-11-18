package com.example.yugenshtil.finalproject.useCases;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.Test;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.BuyItemAdapter;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.item.AddItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.yugenshtil.finalproject.R.id.btDCN_Buy;
import static com.example.yugenshtil.finalproject.R.id.start;

public class Buy extends AppCompatActivity {


    private String GETALLITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter?status=Available";
    private String GETCOURSEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter?title=";
    private String GETRANGEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/price?min=100&max=400";
    private JSONArray jsonArray=null;
    public ProgressDialog pd;
    private RecyclerView recView;
    private BuyItemAdapter adapter;
    private double priceFilterLow = 0.0;
    private double priceFilterMax = 10000000;

    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private LinearLayout relativeLayout;

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

    public void getCourse(String encodedTitle){

        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETCOURSEITEMSURL+encodedTitle, null, new Response.Listener<JSONArray>() {

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
                Log.d("Oleg","Blin" );

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

        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_buy, menu);

        MenuItem searchItem = menu.findItem(R.id.mSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();


        MenuItem item = menu.findItem(R.id.mFilter);





   /*     MenuItem searchFilter = menu.findItem(R.id.mFilter);

        searchFilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("Oleg","Search button is clicked");
                startActivity(new Intent(Buy.this,Test.class));



                return false;
            }
        });*/


 //NEW
     /*   getMenuInflater().inflate(R.menu.menu_buy, menu);
        MenuItem searchItem = menu.findItem(R.id.mSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        MenuItem searchFilter = menu.findItem(R.id.mFilter);

        searchFilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("Oleg","Search button is clicked");
                startActivity(new Intent(Buy.this,Test.class));



                return false;
            }
        });
*/



/*
                relativeLayout = (LinearLayout) findViewById(R.id.buyItemMenu);

                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.rangepopup,null);
    popupWindow = new PopupWindow(container, ViewGroup.LayoutParams.MATCH_PARENT,300, true);
                popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY,0,0);

                final TextView popUp = (TextView) findViewById(R.id.tvRange_Buy);
                popUp.setText("Oleg");
                final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
           //     popUp.setText("Progress " + seekBar.getProgress() + " Max " + seekBar.getMax());
               seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        popUp.setText("Progress " + progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                container.setOnTouchListener(new View.OnTouchListener(){


                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });*/



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

              //  String rawString = query;
                Log.d("Oleg","Received query " + query);


                String encodedUrl="";
                try {
                    encodedUrl = URLEncoder.encode(query, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d("Oleg","Encoded " + encodedUrl);




                 getCourse(encodedUrl);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Log.d("Oleg","Received string " );
                return false;
            }
        });









        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        Log.d("Oleg","Clicked here");
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
        else if(id==R.id.mFilter){
            Log.d("Oleg","Filter Yeaah :)");
            Intent mainMenuIntent = new Intent(Buy.this,RangeChoose.class);
            startActivityForResult(mainMenuIntent,100);
        }
        else if(id==R.id.mProgramFilter){
            Log.d("Oleg","Program Filter Yeaah :)");
            Intent programFilterIntent = new Intent(Buy.this,ProgramFilter.class);
            startActivity(programFilterIntent);





        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        // Get the Camera instance as the activity achieves full user focus
        Log.d("Oleg","On resume");
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("Oleg","onActivityResult ");
        if(resultCode == 100){
                getRange();
            // Storing result in a variable called myvar
            // get("website") 'website' is the key value result data
            Log.d("Oleg","returned 100 ");
        }

    }

}
