package com.example.yugenshtil.finalproject.model;

/**
 * Created by rbocanegramez on 11/25/2016.
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.adapter.MyMessagesListAdapter;
import com.example.yugenshtil.finalproject.useCases.MyMessages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyMessagesListDisplayActivity extends Activity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //TODO:update URLS to retrieve conversation by one user.
    //add id to get conversation
    private String GETUSERCONVERSATIONURL="http://senecaflea.azurewebsites.net/api/Conversation/";
    private String DELETEUSERCONVERSATIONURL="http://senecaflea.azurewebsites.net/api/Conversation/";
    private String POSTMESSAGE ="http://senecafleamarket.azurewebsites.net/api/Message";

    JSONArray jsonArray=null;
    private String id = "";
    private String fullName="";
    private String item_Id = "";
    private String seller_Id ="";
    private String msg_Content="";
    private TextView messageText;

    private RecyclerView recView;

    public ProgressDialog pd;
    private  MyMessagesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymessageslistdisplay);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("id", "");
        fullName = sharedpreferences.getString("fullName", "");
        Log.d("LOG : ","onCreate for MyMessagesListDisplayActivity.java running");

        Intent intent = getIntent();
        item_Id = intent.getStringExtra("ItemId");
        seller_Id = intent.getStringExtra("SellerId");

        Button sendButton = (Button) findViewById(R.id.bt_sendMessageButton);

        getMessages();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LOG : ","sen button clicked for myMessageListDisplayActivity.java");
                sendMessage();
            };

    });
    
    }

    private void sendMessage() {
        pd = ProgressDialog.show(this,"","Sending message, please wait..",true);
        Log.d("LOG : ", "sendMessage running on myMessagesListDisplayActivity.java");
    }

    public void getMessages(){
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("LOG : ", "getHistoryItems for History.java is running");
        String URL = GETUSERCONVERSATIONURL + id;
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            String myMessagesList="";

            @Override
            public void onResponse(JSONArray response) {

                pd.cancel();

                if(response!=null){
                    JSONArray items = response;

                    jsonArray = response;
                    if(items!=null) {
                        Log.d("Log : ", "number of messages in this conversation are: " + items.length());
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                //TODO: update to accept the fields of the incoming messages(what each individual message will return)
                                myMessagesList+="Title: "+ item.getString("Title")+" Status:"+item.getString("Status")+" Price: " + item.getString("Price")+"\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // tvItemsList.setText(myItemsList);
                    }


                    recView = (RecyclerView)findViewById(R.id.recViewMessageList);

                    recView.setLayoutManager(new LinearLayoutManager(MyMessagesListDisplayActivity.this));

                    adapter = new MyMessagesListAdapter(MyMessagesListDisplayActivity.this, jsonArray);

                    Log.d("LOG : ","Setting adapter for MyMessagesListDisplay.java");
                    recView.setAdapter(adapter);
                    //TODO: not working for some reason:
                    //adapter.setItemClickCallback(History.this);

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
        MySingleton.getInstance(MyMessagesListDisplayActivity.this).addToRequestQueue(jsObjRequest);
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

        //TODO: update to do nothing, items can't be clicked
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
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MyMessagesListDisplayActivity.this, MyMessages.class));
        Log.d("LOG : ","Back button pressed on MyMessagesListDisplay.java");
        finish();

    }
}
