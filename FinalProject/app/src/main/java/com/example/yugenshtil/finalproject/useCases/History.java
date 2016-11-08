package com.example.yugenshtil.finalproject.useCases;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.model.ItemDisplayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class History extends Activity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //TODO: Must be changed to get items from history table
    private String GETITEMSURL="http://senecaflea.azurewebsites.net/api/";
//TODO: update for url for history
    private String DELETEITEMSURL="http://senecaflea.azurewebsites.net/api/";

    JSONArray jsonArray=null;
    private String id = "";
    private String fullName="";

    private RecyclerView recView;

    public ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("id", "");
        fullName = sharedpreferences.getString("fullName", "");
        Log.d("Ricardo","onCreate for history running");

        getHistoryItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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
    public void getHistoryItems(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETITEMSURL+id, null, new Response.Listener<JSONArray>() {
            String myItemsList="";

            @Override
            public void onResponse(JSONArray response) {

                pd.cancel();

                if(response!=null){
                    JSONArray items = response;

                    jsonArray = response;
                    if(items!=null) {
                        Log.d("Ricardo", "number of items on history is: " + items.length());
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


                    recView = (RecyclerView)findViewById(R.id.recViewHistory);

                    recView.setLayoutManager(new LinearLayoutManager(History.this));

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
                Log.d("Oleg","error" + error);

            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(History.this, UserMenu.class));
        Log.d("Ricardo","Back button pressed on history");
        finish();

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
        Log.d("Oleg","onSecondaryIconClick");


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
                        Intent aboutAppIntent = new Intent(History.this,Sell.class);
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
        );
        MySingleton.getInstance(History.this).addToRequestQueue(dr);

    }
}
