package com.example.yugenshtil.finalproject.useCases;

import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yugenshtil.finalproject.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.RegistrationPage;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.item.AddItem;
import com.example.yugenshtil.finalproject.model.DerpData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sell extends Activity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String GETITEMSURL="http://senecaflea.azurewebsites.net/api/Item/filter/user/";
    TextView tvItemsList;
    String myItems = "";
    private String id = "";
    private String fullName="";

    // New
    private RecyclerView recView;
    private DerpAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        recView = (RecyclerView)findViewById(R.id.rec_list);
        //LayoutManager: GridLayoutManager or StaggeredGridLayoutManager

        recView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DerpAdapter(DerpData.getListData(),this);
        recView.setAdapter(adapter);



        Bundle extras = getIntent().getExtras();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d("Oleg", "Preferences " + sharedpreferences);
        id = sharedpreferences.getString("id", "");
        fullName = sharedpreferences.getString("fullName", "");





        final TextView tvCongratulation = (TextView) findViewById(R.id.sellTVCongratulations);
        tvItemsList = (TextView) findViewById(R.id.sellTVitemsList);
        final Button btAddItem = (Button) findViewById(R.id.sellBTaddItem);

        tvCongratulation.setText(fullName+", here is the list pf products you sell");
     //   tvItemsList.setText(myItems);
        getMyItems();


        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("oleg","Add new item");
                Intent addItemIntent = new Intent(Sell.this,AddItem.class);
                addItemIntent.putExtra("userId", id);



                startActivity(addItemIntent);

            }
        });


    }

    public void getMyItems(){

        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETITEMSURL+id, null, new Response.Listener<JSONArray>() {
            String myItemsList="";

            @Override
            public void onResponse(JSONArray response) {


                if(response!=null){

                    JSONArray items = response;
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

                        tvItemsList.setText(myItemsList);
                    }

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
                Log.d("Oleg","error" + error);

            }
        });

        MySingleton.getInstance(Sell.this).addToRequestQueue(jsObjRequest);











    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sell, menu);
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
}
