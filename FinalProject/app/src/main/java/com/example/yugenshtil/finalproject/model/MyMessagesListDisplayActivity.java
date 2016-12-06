package com.example.yugenshtil.finalproject.model;

/**
 * Displays chat screen, messages are sent from here!
 */
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.adapter.MyMessagesListAdapter;
import com.example.yugenshtil.finalproject.useCases.MyMessages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyMessagesListDisplayActivity extends AppCompatActivity implements MyMessagesListAdapter.ItemClickCallback {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    //TODO:update URLS to retrieve conversation by one user.
    //add id to get conversation
    //TODO: GETUSERCONVERSATIONURL will send two ids
    private String GETUSERCONVERSATIONURL="http://senecafleamarket.azurewebsites.net/api/Conversation/filter/Receiver/";
    private String GETUSERCONVERSATIONURLTWO="/withMessages";
    //private String DELETEUSERCONVERSATIONURL="http://senecaflea.azurewebsites.net/api/Conversation/";
    private String POSTMESSAGE ="http://senecafleamarket.azurewebsites.net/api/Message";

    JSONArray jsonArray=null;
    private String id = "";
    private String fullName="";
    private String item_Id = "";
    private String seller_Id ="";
    private String msg_Content="";
    private Date currDate = null;
    private String jsonDate = "";
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private String errors = "";
    private String token="";

    private RecyclerView recView;

    public ProgressDialog pd;
    private  MyMessagesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LOG : ","onCreate for MyMessagesListDisplayActivity.java running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymessageslistdisplay);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("UserId", "");
        fullName = sharedpreferences.getString("fullName", "");
        token = sharedpreferences.getString("token", "");


        Intent intent = getIntent();
        item_Id = intent.getStringExtra("itemIdMessageInt");
        seller_Id = intent.getStringExtra("sellerIdMessageInt");
        Log.d("LOG: this here: ", "" +seller_Id);
        currDate = new Date();
        jsonDate = dateFormat.format(currDate);
        //Testing Log.d("LOG : ","itemId: " + item_Id +" sellerId: " + seller_Id + " on date: " +jsonDate);

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
        Log.d("LOG : ", "sendMessage running on myMessagesListDisplayActivity.java");

        TextView messageText = (TextView) findViewById(R.id.tv_messageContent);

        msg_Content = messageText.getText().toString();

        if (validateInput()){
            pd = ProgressDialog.show(this,"","Sending message, please wait..",true);

            Map<String, String> params = new HashMap();
            params.put("Text", msg_Content);
            params.put("SenderId", id);
            params.put("ReceiverId", seller_Id);
            params.put("Time", jsonDate);
            params.put("ItemId",item_Id);

            JSONObject parameters = new JSONObject(params);
            Log.d("LOG : ", "JSON is " + parameters);

            JsonObjectRequest jsObjPostRequest = new JsonObjectRequest(Request.Method.POST, POSTMESSAGE, parameters, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    pd.cancel();
                    //Testing: Log.d("LOG :", "Response is " + response);
                    //Testing: Log.d("LOG :", "The message was sent :) ");

                    Intent msgIntent = new Intent(MyMessagesListDisplayActivity.this, MyMessagesListDisplayActivity.class);
                    msgIntent.putExtra("itemIdMessageInt",item_Id);
                    msgIntent.putExtra("sellerIdMessageInt",seller_Id);
                    startActivity(msgIntent);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.cancel();
                    Log.d("LOG :", "Error is " + error);
                }
            }){

                /*@Override
                public String getBodyContentType() {
                    return "application/json; charset=UTF-8";
                }*/

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    Log.d("LOG : ", "I will add token " + token);
                    headers.put("Authorization", "Bearer " + token);

                    // params.put("username",email);
                    //params.put("password", password);

                    Log.d("Token ", headers.toString());
                    return headers;
                }

            };

            MySingleton.getInstance(MyMessagesListDisplayActivity.this).addToRequestQueue(jsObjPostRequest);
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent displayIntent = new Intent(MyMessagesListDisplayActivity.this, MyMessagesListDisplayActivity.class);
            // RegistrationPage.this.startActivity(loginIntent);
            displayIntent.putExtra("itemIdMessageInt",item_Id);
            displayIntent.putExtra("sellerIdMessageInt",seller_Id);

            startActivity(displayIntent);

        } else {
            //message content was bad.

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, errors, duration);
            toast.show();

        }

    }

    public boolean validateInput(){
        boolean inputIsValid = true;
        errors="";
        if(msg_Content.equals("") || msg_Content.trim().length() <= 0){
            errors+="Please dont leave blank spaces in message";
            inputIsValid = false;
        }

        return  inputIsValid;
    }

    public void getMessages(){
        pd = ProgressDialog.show(this, "", "Loading messages. Please wait...", true);
        Log.d("LOG : ", "getMessages for MyMessagesListDisplayActivity.java is running");

        Map<String,String> params = new HashMap();
        //id is current users id, get request returns all messages where current user(id) is equal to sender or receiver.
        if (id == seller_Id){
            seller_Id = "1";
        }
        Log.d("LOG: this", "" +seller_Id);
        Log.d("LOG: this", "" + id);

        params.put("UserId",id);
        params.put("SenderId",seller_Id);
        JSONObject parameters = new JSONObject(params);

        Log.d("LOG : ","JSON is " + parameters);
        String URL = GETUSERCONVERSATIONURL + seller_Id + GETUSERCONVERSATIONURLTWO;
        Log.d("LOG : ","URL is " + URL);


        JsonObjectRequest jsObjGetRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            String myMessagesList="";


            @Override
            public void onResponse(JSONObject response) {

                pd.cancel();

                if(response!=null){
                    JSONObject items = response;

                    try {
                        jsonArray = items.getJSONArray("Messages");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(jsonArray!=null) {
                        Log.d("Log : ", "number of messages in this conversation are: " + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) jsonArray.get(i);
                                //TODO: RICO: update to accept the fields of the incoming messages(what each individual message will return)
                                //TODO: RICO: change Sender to appropriate name from response. then update in Adapter where **here**
                                myMessagesList += "Content: " + item.getString("Text") + " Sender:" + item.getString("SenderId") + "\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // tvItemsList.setText(myItemsList);


                        recView = (RecyclerView) findViewById(R.id.recViewMessageList);

                        recView.setLayoutManager(new LinearLayoutManager(MyMessagesListDisplayActivity.this));

                        adapter = new MyMessagesListAdapter(MyMessagesListDisplayActivity.this, jsonArray);

                        Log.d("LOG : ", "Setting adapter for MyMessagesListDisplay.java");
                        recView.setAdapter(adapter);
                        adapter.setItemClickCallback(MyMessagesListDisplayActivity.this);

                        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
                        itemTouchHelper.attachToRecyclerView(recView);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"No messages",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"No messages",Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                pd.cancel();
                Log.d("LOG :","error : " + error);

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

                Log.d("Token ", headers.toString());
                return headers;
            }
        }

                ;
        MySingleton.getInstance(MyMessagesListDisplayActivity.this).addToRequestQueue(jsObjGetRequest);
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

        //TODO: update to do nothing, items can't be clicked **connected to onClick in MyMessagesListAdapter.java
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
