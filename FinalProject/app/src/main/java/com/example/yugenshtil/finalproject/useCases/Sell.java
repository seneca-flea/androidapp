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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.Item.AddItem;
import com.example.yugenshtil.finalproject.Item.EditItem;
import com.example.yugenshtil.finalproject.model.ItemDisplayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Sell extends Activity  implements DerpAdapter.ItemClickCallback{

    SharedPreferences sharedpreferences;
    private String GETITEMSURL="http://senecafleamarket.azurewebsites.net/api/Item/filter?userid=";
    private String DELETEITEMSURL="http://senecafleamarket.azurewebsites.net/api/Item/";



    private String ITEMHISTORYURL1="http://senecafleamarket.azurewebsites.net/api/User/";
    private String ITEMHISTORYURL2="/History";

    //for itemHistory
    private String ItemId="";
    private String SellerId="";
    TextView tvItemsList;
    String myItems = "";

    JSONArray jsonArray=null;
    private String id = "";
    private String token = "";

    // New
    private RecyclerView recView;
    private DerpAdapter adapter;
    public ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        id = sharedpreferences.getString("UserId", "");
        token = sharedpreferences.getString("token", "");
        Log.d("LOG : ","User ID is " + id);
      //  final TextView tvCongratulation = (TextView) findViewById(R.id.sellTVCongratulations);
      //  tvItemsList = (TextView) findViewById(R.id.sellTVitemsList);
        final Button btAddItem = (Button) findViewById(R.id.sellBTaddItem);
        
     //   tvCongratulation.setText(fullName + ", here is the list pf products you sell");
     //   tvItemsList.setText(myItems);
        getMyItems();


            Log.d("Oleg","oool");





        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("oleg", "Add new item");
                Intent addItemIntent = new Intent(Sell.this, AddItem.class);
                addItemIntent.putExtra("userId", id);


                startActivity(addItemIntent);

            }
        });


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sell, menu);
        Log.d("Oleg","onCreate hrer");
        return true;
    }

    public void getMyItems(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("LOG : ","getMyItems for Sell.java running");
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETITEMSURL+id, null, new Response.Listener<JSONArray>() {


            String myItemsList="";

            @Override
            public void onResponse(JSONArray response) {

                pd.cancel();

                if(response!=null){

                    JSONArray items = response;
                    jsonArray = response;
                    if(items!=null) {
                        Log.d("Oleg", "size " + items.length());
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                myItemsList+="Title: "+ item.getString("Title")+" Status:"+item.getString("Status")+" Price: " + item.getString("Price")+"\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                       // tvItemsList.setText(myItemsList);
                    }


                    recView = (RecyclerView)findViewById(R.id.rec_list);
                    //LayoutManager: GridLayoutManager or StaggeredGridLayoutManager

                    recView.setLayoutManager(new LinearLayoutManager(Sell.this));
                    //Send list here
                //WAS   adapter = new DerpAdapter(DerpData.getListData(),Sell.this,jsonArray);

                       adapter = new DerpAdapter(Sell.this,jsonArray);

                    Log.d("Oleg","Setting adapter");
                    recView.setAdapter(adapter);
                    adapter.setItemClickCallback(Sell.this);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
                    itemTouchHelper.attachToRecyclerView(recView);

                    Button addItem = (Button) findViewById(R.id.sellBTaddItem);
                    addItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Oleg","Add new clicked");
                            Intent loginIntent = new Intent(Sell.this,AddItem.class);
                            startActivity(loginIntent);
                        }
                    });



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
        }){

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

        MySingleton.getInstance(Sell.this).addToRequestQueue(jsObjRequest);

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
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    private void addItemToList() {
       // ListItem item = DerpData.getRandomListItem();
      //  listData.add(item);
        //adapter.notifyItemInserted(listData.indexOf(item));
    }

    private void moveItem(int oldPos, int newPos) {

      //  ListItem item = (ListItem) listData.get(oldPos);
      //  listData.remove(oldPos);
      //  listData.add(newPos, item);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem(final int position) {
      //  listData.remove(position);
      //  adapter.notifyItemRemoved(position);
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

            Intent mainMenuIntent = new Intent(Sell.this,MainMenu.class);
            startActivity(mainMenuIntent);



        }

        return super.onOptionsItemSelected(item);
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
    public void onUpdateIconClick(int p) {
        try {
            JSONObject item = (JSONObject)jsonArray.get(p);
           // String updateItemId = item.getString("ItemId");
            Log.d("Oleg","you would like to update id " + p);
            updateAnItem(p);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    //    adapter.setListData(listData);
        adapter.notifyDataSetChanged();
    }

    public void onHistoryIconClick(int p) {
        try {
            Log.d("LOG : ","onHistoryIconClick running ");
            JSONObject item = (JSONObject)jsonArray.get(p);
            String ItemId = item.getString("ItemId");
            Log.d("LOG : ","adding item : " + p + " to item history");
            AddItemHistory(ItemId,p);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //    adapter.setListData(listData);
        adapter.notifyDataSetChanged();
    }

    public void onDeleteIconClick(int p) {

        try {
            JSONObject item = (JSONObject)jsonArray.get(p);
            String deleteItemId = item.getString("ItemId");
            Log.d("Oleg","you would like to delete it id " + deleteItemId);
            deleteAnItem(deleteItemId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


      //  ListItem item = (ListItem) listData.get(p);
        //update our Data

    //    if(item.isFavourite()){
     //       item.setFavourite(false);

    //    }else
       //     item.setFavourite(true);

        Log.d("Oleg","onSecondaryIconClick");

        //    adapter.setListData(listData);
        adapter.notifyDataSetChanged();
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
                        Intent aboutAppIntent = new Intent(Sell.this,Sell.class);
                        startActivity(aboutAppIntent);


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
        MySingleton.getInstance(Sell.this).addToRequestQueue(dr);

    }

    //itemHistory
    public void AddItemHistory(String id,int p) {
        pd = ProgressDialog.show(this,"","Adding item to history. Please wait...", true);
        Log.d("LOG : ","AddItemHistory running on sell.java");

        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            Map<String, String> params = new HashMap();
            params.put("ItemId", id);
            params.put("SellerId", item.get("SellerId").toString());
            String userId = item.get("SellerId").toString();
            JSONObject parameters = new JSONObject(params);
            Log.d("LOG : ", "JSON for addItem is " + parameters);
            String URL = ITEMHISTORYURL1 + userId + ITEMHISTORYURL2;

            Log.d("LOG : ", "URL for request is  " + URL);

            JsonObjectRequest jsObjPostRequest = new JsonObjectRequest(Request.Method.POST, URL,parameters, new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            pd.cancel();
                            Log.d("Response", " " +response);
                            Intent historyIntent = new Intent(Sell.this,History.class);
                            startActivity(historyIntent);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            pd.cancel();

                            String errorText = error.toString();

                            if (errorText.contains("End of input at character 0 of")){
                                Toast toast = Toast.makeText(getApplicationContext(),"Item added to item history",Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else {
                                Log.d("LOG : ", "the following error occurred in addItemHistory for Sell.java : " + error);
                            }
                        }
                    }
            ){


                //token inserted here (when implemented)
            };

            MySingleton.getInstance(Sell.this).addToRequestQueue(jsObjPostRequest);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context,"Item added to history", duration);
            toast.show();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent historyIntent = new Intent(Sell.this,History.class);
            startActivity(historyIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateAnItem(int id) {
       // pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);

        try {
            JSONObject item = (JSONObject) jsonArray.get(id);
            Intent i = new Intent(Sell.this, EditItem.class);
            i.putExtra("ItemId",item.get("ItemId").toString());
            i.putExtra("Title",item.get("Title").toString());
            i.putExtra("SellerId",item.get("SellerId").toString());
            i.putExtra("Description",item.get("Description").toString());
            i.putExtra("Price",item.get("Price").toString());
            startActivity(i);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Sell.this, UserMenu.class));
        finish();

    }

}