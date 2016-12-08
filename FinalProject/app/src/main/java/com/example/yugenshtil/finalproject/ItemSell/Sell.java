package com.example.yugenshtil.finalproject.ItemSell;


/*
Class was created and designed by Oleg Mytryniuk
 */
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.Account.Login;
import com.example.yugenshtil.finalproject.ItemEdit.EditBook;
import com.example.yugenshtil.finalproject.ItemEdit.EditMaterial;
import com.example.yugenshtil.finalproject.OtherUseCases.MainMenu;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.OtherUseCases.UserMenu;
import com.example.yugenshtil.finalproject.Adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.useCases.History;

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
        final Button btAddItem = (Button) findViewById(R.id.sellBTaddItem);

        getMyItems();

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
                    jsonArray = new JSONArray();
                    if(items!=null) {
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                if(item.getString("Status").equals("Available")){
                                    jsonArray.put(item);

                                }
                                myItemsList+="Title: "+ item.getString("Title")+" Status:"+item.getString("Status")+" Price: " + item.getString("Price")+"\n";
                                Log.d("JSON",""+item.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    recView = (RecyclerView)findViewById(R.id.rec_list);
                    recView.setLayoutManager(new LinearLayoutManager(Sell.this));
                    adapter = new DerpAdapter(Sell.this,jsonArray);
                    recView.setAdapter(adapter);
                    adapter.setItemClickCallback(Sell.this);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
                    itemTouchHelper.attachToRecyclerView(recView);

                    Button addItem = (Button) findViewById(R.id.sellBTaddItem);
                    addItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
            }
        }){

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

           @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);
                headers.put("Accept","image/*");
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
            displayItem(item.getString("ItemId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdateIconClick(int p) {
        try {
            JSONObject item = (JSONObject)jsonArray.get(p);
            updateAnItem(p);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    //    adapter.setListData(listData);
        adapter.notifyDataSetChanged();
    }

    public void onHistoryIconClick(int p) {
        try {
            JSONObject item = (JSONObject)jsonArray.get(p);
            String ItemId = item.getString("ItemId");
            AddItemHistory(ItemId,p);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    public void onDeleteIconClick(int p) {

        try {
            JSONObject item = (JSONObject)jsonArray.get(p);
            String deleteItemId = item.getString("ItemId");
            deleteAnItem(deleteItemId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("LOG : ","onSecondaryIconClick");
        adapter.notifyDataSetChanged();
    }

    public void deleteAnItem(String id){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        StringRequest dr = new StringRequest(Request.Method.DELETE, DELETEITEMSURL+id,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                       pd.cancel();
                        Intent aboutAppIntent = new Intent(Sell.this,Sell.class);
                        startActivity(aboutAppIntent);
                        Toast toast = Toast.makeText(getApplicationContext(),"Item was successfully deleted",Toast.LENGTH_SHORT);
                        toast.show();

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast toast = Toast.makeText(getApplicationContext(),"Item was not deleted",Toast.LENGTH_SHORT);
                        toast.show();

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
                headers.put("Authorization","Bearer "+token);
                return headers;
            }
        };
        MySingleton.getInstance(Sell.this).addToRequestQueue(dr);

    }

    //itemHistory
    public void AddItemHistory(String id,int p) {
        pd = ProgressDialog.show(this,"","Adding item to history. Please wait...", true);
        try {
            JSONObject item = (JSONObject) jsonArray.get(p);
            Map<String, String> params = new HashMap();
            params.put("ItemId", id);
            params.put("SellerId", item.get("SellerId").toString());
            String userId = item.get("SellerId").toString();
            JSONObject parameters = new JSONObject(params);
            Log.d("LOG : ", "JSON for addItem is " + parameters);
            String URL = ITEMHISTORYURL1 + userId + ITEMHISTORYURL2;

            JsonObjectRequest jsObjPostRequest = new JsonObjectRequest(Request.Method.POST, URL,parameters, new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response) {
                            // response
                            pd.cancel();
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

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=UTF-8";
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }

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
            String itemType =  item.get("Type").toString();

            Intent i;

            if(itemType.equals("Book")){
                i = new Intent(Sell.this, EditBook.class);
                i.putExtra("BookTitle",item.get("BookTitle").toString());
                i.putExtra("BookYear",item.get("BookYear").toString());
                i.putExtra("BookPublisher",item.get("BookPublisher").toString());
                i.putExtra("BookAuthor",item.get("BookAuthor").toString());
            }
            else{
                i = new Intent(Sell.this, EditMaterial.class);
            }

            i.putExtra("ItemId",item.get("ItemId").toString());
            i.putExtra("Title",item.get("Title").toString());
            i.putExtra("SellerId",item.get("SellerId").toString());
            i.putExtra("Description",item.get("Description").toString());
            i.putExtra("Price",item.get("Price").toString());
            i.putExtra("Type",item.get("Type").toString());
            i.putExtra("CourseProgram",item.get("CourseProgram").toString());


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

    void displayItem(String id){

        JsonRequest sr = new JsonObjectRequest(Request.Method.GET,"http://senecafleamarket.azurewebsites.net/api/Item/" +id, null,  new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response!=null){

                        JSONObject item = response;
                        Intent i  = new Intent(Sell.this, ItemDisplayActivity.class);
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
                        extras.putString("Type",item.get("Type").toString());
                        i.putExtras(extras);
                        startActivity(i);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+token);

                return headers;
            }
        };

        MySingleton.getInstance(Sell.this).addToRequestQueue(sr);
    }

}