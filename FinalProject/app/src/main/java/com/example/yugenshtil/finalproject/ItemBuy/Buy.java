package com.example.yugenshtil.finalproject.ItemBuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.BuyItemAdapter;
import com.example.yugenshtil.finalproject.model.ItemDisplayActivity;
import com.example.yugenshtil.finalproject.useCases.ProgramFilter;
import com.example.yugenshtil.finalproject.useCases.RangeChoose;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Buy extends AppCompatActivity  implements BuyItemAdapter.ItemClickCallback {


    private String GETALLITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter?status=Available";
    private String GETCOURSEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter?title=";
    private String GETRANGEITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/price?min=";
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


        final Button btDisplayNew = (Button) findViewById(R.id.btDisplayAll_Buy);
    //    final Button btRange = (Button) findViewById(R.id.btRange_Buy);


        getItems();

        btDisplayNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Clicked to DCN");
                getItems();

            }
        });

      /*  btRange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Clicked to Range");
                getRange();
            }
        });*/

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

                    Log.d("Oleg","Setting adapter1");
                    recView.setAdapter(adapter);
                    adapter.setItemClickCallback(Buy.this);

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
                getItems();
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

                    Log.d("Oleg","Setting adapter2");
                    recView.setAdapter(adapter);
                    adapter.setItemClickCallback(Buy.this);

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

    public void getRange(String mn, String mx){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETRANGEITEMSURL+mn+"&max="+mx, null, new Response.Listener<JSONArray>() {

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
                       adapter.setItemClickCallback(Buy.this);

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

            if (getCallingActivity() == null) {
               Log.d("Oleg","No activity");
            } else {
                Log.d("Oleg","Yes activity");
                //This Activity was called by startActivityForResult
            }
            String from =  mainMenuIntent.getStringExtra("from");
            String to =  mainMenuIntent.getStringExtra("to");
            Log.d("Oleg","From " + from + " To:" + to);



        }
        else if(id==R.id.mProgramFilter){
            Log.d("Oleg","Program Filter Yeaah :)");
            Intent programFilterIntent = new Intent(Buy.this,ProgramFilter.class);
            startActivityForResult(programFilterIntent,110);





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

            String from =  data.getStringExtra("from");
            String to =  data.getStringExtra("to");
            Log.d("Oleg",to);
            Log.d("Oleg","From " + from + " To:" + to);

                getRange(from, to);
            // Storing result in a variable called myvar
            // get("website") 'website' is the key value result data
            Log.d("Oleg","returned 100 ");
        }
        if(resultCode == 110){


            ArrayList<String> res = data.getStringArrayListExtra("stock_list");
            Log.d("Oleg","returned 110 " + res.size());
            for(String s : res){
                Log.d("Oleg",s);

            }


        }

    }

    @Override
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

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        Intent back = new Intent(this, UserMenu.class);
        startActivity(back);
    }
}
