package com.example.yugenshtil.finalproject.ItemBuy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.BuyItemAdapter;
import com.example.yugenshtil.finalproject.model.MyMessagesListDisplayActivity;
import com.example.yugenshtil.finalproject.useCases.ProgramFilter;
import com.example.yugenshtil.finalproject.useCases.RangeChoose;
import com.example.yugenshtil.finalproject.useCases.Sell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Buy extends AppCompatActivity  implements BuyItemAdapter.ItemClickCallback {


    private String GETALLITEMSURL="http://senecafleamarket.azurewebsites.net/api/Item/filter?status=Available";
    private String GETCOURSEITEMSURL="http://senecafleamarket.azurewebsites.net/api/Item/filter?title=";
    private String GETRANGEITEMSURL="http://senecafleamarket.azurewebsites.net/api/Item/filter/price?min=";
    private String GETMYFAVORITESURL="http://senecafleamarket.azurewebsites.net/api/User/";
    private String MYFAVORITES="http://senecafleamarket.azurewebsites.net/api/User/";
    SharedPreferences sharedpreferences;
    private String userId="";
    private String token="";
    private String programs="";
    private double priceMax=0.0;
    private double priceMin=400.0;


    private JSONArray jsonArray=null;
    public ProgressDialog pd;
    private RecyclerView recView;
    private BuyItemAdapter adapter;
    private double priceFilterLow = 0.0;
    private double priceFilterMax = 10000000;
    private List<String> favoriteIds;

    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private LinearLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        favoriteIds = new ArrayList<String>();

        //Get userId from SharedPreferences
        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("UserId", "");
        token = sharedpreferences.getString("token", "");
        priceMax = Double.parseDouble(sharedpreferences.getString("PriceMax",""));
        priceMin = Double.parseDouble(sharedpreferences.getString("PriceMin",""));
        programs = sharedpreferences.getString("Courses","");

        Log.d("Oleg","Max " + priceMax);
        Log.d("Oleg","Min " + priceMin);



        final Button btDisplayNew = (Button) findViewById(R.id.btDisplayAll_Buy);
    //    final Button btRange = (Button) findViewById(R.id.btRange_Buy);

        getFavorites();


        btDisplayNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Clicked to DCN");
                getItems();

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
                    jsonArray = new JSONArray();
                    if(items!=null) {
                        Log.d("Oleg", "JSON " + items.toString());
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                double price=0.0;
                                price = item.getDouble("Price");
                                if((price>=priceMin) && (price<=priceMax) && item.getString("Status").equals("Available")){
                                    jsonArray.put(item);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    recView = (RecyclerView)findViewById(R.id.recbuy_list);
                    recView.setLayoutManager(new LinearLayoutManager(Buy.this));
                    Log.d("Oleg","There are " + jsonArray.length() + " before I sent");
                    adapter = new BuyItemAdapter(Buy.this,jsonArray,favoriteIds);

                    recView.setAdapter(adapter);
                    adapter.setItemClickCallback(Buy.this);


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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(Buy.this).addToRequestQueue(jsObjRequest);

    }

    // Retrieve Myfavorites
    public void getFavorites(){

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETMYFAVORITESURL+userId+"/Favorites", null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                if(response!=null){
                    JSONArray items = response;
                    jsonArray = response;
                    if(items!=null) {
                        Log.d("Oleg", "size " + items.length());
                        Log.d("Oleg", "JSON " + items.toString());
                         for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                String itemId = item.getString("ItemId");
                                favoriteIds.add(itemId);
                              //  Log.d("Oleg","Id is  " + id);
                                //  myItemsList+="Title: "+ item.getString("Title")+" Status:"+item.getString("Status")+" Price: " + item.getString("Price")+"\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // tvItemsList.setText(myItemsList);
                    }



                }
                getItems();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                              Log.d("Oleg","error" + error);
                getItems();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(Buy.this).addToRequestQueue(jsObjRequest);

    }

    public void getCourse(String encodedTitle){
        //getFavorites();
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETCOURSEITEMSURL+encodedTitle, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                pd.cancel();
                if(response!=null){
                    JSONArray items = response;
                    jsonArray = new JSONArray();
                    if(items!=null) {

                        Log.d("Oleg", "JSON " + items.toString());
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                double price=0.0;
                                price = item.getDouble("Price");
                                if((price>=priceMin) && (price<=priceMax) && item.getString("Status").equals("Available")){
                                    jsonArray.put(item);

                                }

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

                    adapter = new BuyItemAdapter(Buy.this,jsonArray,favoriteIds);

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
        }




        ){
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



        };

        MySingleton.getInstance(Buy.this).addToRequestQueue(jsObjRequest);

    }

    public void getRange(String mn, String mx){
        priceMin = Double.parseDouble(mn);
        priceMax = Double.parseDouble(mx);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("PriceMin", mn);
        editor.putString("PriceMax", mx);
        editor.commit();

        getFavorites();

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
            Log.d("LOG : ", "it goest to buy.java");
            JSONObject item = (JSONObject) jsonArray.get(p);
            Intent i  = new Intent(this, ItemBuy.class);
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
    public void onMessageIconClick(int p){

        try {
            JSONObject item = (JSONObject) jsonArray.get(p);

            Log.d("LOG : ","onMessageIconClick for Buy.java on item :" + item.get("ItemId"));

            Intent msgIntent = new Intent(this, MyMessagesListDisplayActivity.class);

            String itemId = item.get("ItemId").toString();
            String sellerId = item.get("SellerId").toString();

            Log.d("LOG : ","itemId is " + itemId + "and seller id is " + sellerId);
            msgIntent.putExtra("itemIdMessageInt",itemId);
            msgIntent.putExtra("sellerIdMessageInt",sellerId);
            startActivity(msgIntent);
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

    @Override
    public void onMyFavoriteDeleteClick(int p) {
        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            Log.d("Oleg","Wanna delete an item with Id " + item.get("ItemId"));
            String itemId = item.get("ItemId").toString();
            deleteMyFavorite(itemId);
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
            addMyFavorite(itemId);
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

    public void addMyFavorite(String itemId){
        Log.d("Oleg","Item id is " + itemId);
        JSONObject jsonObject = new JSONObject();
        //  JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, REMOVEMYFAVORITES+id+"/RemoveFavorite/"+itemId,jsonObject,
        StringRequest jsObjPutRequest = new StringRequest(Request.Method.POST, MYFAVORITES+userId+"/Favorite/"+itemId,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", " " +response);
                        Log.d("Oleg","MyFavorite was added");
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

        MySingleton.getInstance(Buy.this).addToRequestQueue(jsObjPutRequest);
    }

    public void deleteMyFavorite(String itemId){
        Log.d("Oleg","Item id is " + itemId);
        JSONObject jsonObject = new JSONObject();
        //  JsonObjectRequest jsObjPutRequest = new JsonObjectRequest(Request.Method.PUT, REMOVEMYFAVORITES+id+"/RemoveFavorite/"+itemId,jsonObject,
        StringRequest jsObjPutRequest = new StringRequest(Request.Method.DELETE, MYFAVORITES+userId+"/Favorite/"+itemId,
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
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                return headers;
            }
        };

        MySingleton.getInstance(Buy.this).addToRequestQueue(jsObjPutRequest);
    }


}
