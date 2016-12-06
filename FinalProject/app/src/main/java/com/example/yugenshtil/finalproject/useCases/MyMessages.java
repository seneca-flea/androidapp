

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.yugenshtil.finalproject.ServerConnection.MySingleton;
import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.UserMenu;

import com.example.yugenshtil.finalproject.adapter.MyMessagesAdapter;
import com.example.yugenshtil.finalproject.model.ItemDisplayActivity;
import com.example.yugenshtil.finalproject.model.MyMessagesListDisplayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyMessages extends Activity implements MyMessagesAdapter.ItemClickCallback {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";


    private String GETCONVERSATIONURL = "http://senecafleamarket.azurewebsites.net/api/Conversation";
    private String DELETECONVERSATIONURL = "http://senecaflea.azurewebsites.net/api/Conversation/?receiverId=";//missing ID


    //For testing: private String token = "rvgZI8JrpdCFy4JvMxLj6WlyvwxSiL8JTVmlafEuhiZpDcMn4E8xvRrYGrTrUnE_bGG2rKfMfDllUF0O6pQYPV7-C9JJQ7j8OCmOQhvmvYdXYJpYZjwsLoynoRtpdwPBOT_-lAyPrl8twOjfNaFCTXGsQ17ci5byDrIJclHsFSP7bhpkJ3dwTnAJvplRIHN2k0yYi9x4H1BEIC0qBaHZ5Omh1tlTIFzr3Zigkbfo014T9fy_jpvEoyNI3vES_w2jWrW8282DASK6JAFPAwUJr_-G1_mZrSLKrLFblQPFKbo-HLhLZTAQSq6zY14J7LJoBTyWu-nM6sEAeiCNYc5tZrP2gYKjLz-H119j_Uuw8xOOLKyKyNOZlGNtBqumc2weLF-ESDBvYCdFNGQKizCLz4Nwvp2CldBIzTZj9bw-lopSxZXPhO4TsKYko9xcZYauIe5PBOeoxqxzbxkXOnKQyZiwisPYec33opPW8bG-Aem_pDkuuhjJHGEv4Kdipxm3V1BnEquJnWPs2qTAc2dpgXpB6JHcD7DU84pPvstRAM4hsS7wccvbBZ5jf1zuLU-xbzDFwA";
    JSONArray jsonArray = null;
    private String id = "";
    private String fullName = "";
    private String token = "";

    private RecyclerView recView;

    public ProgressDialog pd;
    private MyMessagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_messages);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        id = sharedpreferences.getString("UserId", "");
        fullName = sharedpreferences.getString("fullName", "");
        token = sharedpreferences.getString("token", "");
        Log.d("LOG : ", "onCreate for my messages running");

        getMyConversations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_messages, menu);
        return true;
    }

    public void getMyConversations() {
        pd = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        Log.d("LOG : ", "getMyConversations in MyMessages.java is running");
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET, GETCONVERSATIONURL, null, new Response.Listener<JSONArray>() {
            String myMessagesList = "";

            @Override
            public void onResponse(JSONArray response) {

                pd.cancel();

                if (response != null) {
                    JSONArray items = response;


                    jsonArray = response;
                    if (items != null) {
                        Log.d("Log : ", "number of conversations is: " + items.length());
                        for (int i = 0; i < items.length(); i++) {
                            try {
                                JSONObject item = (JSONObject) items.get(i);
                                String title = item.getString("UserFirstName");
                                JSONObject recentmsg = item.getJSONObject("recetMessage");
                                String desc = recentmsg.getString("Text");
                                //TODO: Desc should be changed to field prevviewing conversation.
                                myMessagesList += "Title: " + title + " Des:" + desc + "\n";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // tvItemsList.setText(myItemsList);
                    }


                    recView = (RecyclerView) findViewById(R.id.recViewMessage);

                    recView.setLayoutManager(new LinearLayoutManager(MyMessages.this));

                    adapter = new MyMessagesAdapter(MyMessages.this, jsonArray);

                    Log.d("LOG : ", "Setting adapter for MyMessages.java");
                    recView.setAdapter(adapter);

                    adapter.setItemClickCallback(MyMessages.this);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
                    itemTouchHelper.attachToRecyclerView(recView);

                } else {
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
                Log.d("LOG :", "error : " + error);

            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

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
        MySingleton.getInstance(MyMessages.this).addToRequestQueue(jsObjRequest);
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

        Log.d("LOG : ", "onItemClick for myMessages.java");

        try {
            //TODO: develop MessageDissplayActivity to display individual messages

            JSONObject item = (JSONObject) jsonArray.get(p);
            Log.d("LOG : ", "item is: " + item);

            Intent i = new Intent(this, MyMessagesListDisplayActivity.class);

            String user1 = item.get("User1").toString();
            String user2 = item.get("User2").toString();
            if (user1 == id){
                i.putExtra("sellerIdMessageInt", user2);
            }
            else {
                i.putExtra("sellerIdMessageInt", user1);
            }

            i.putExtra("ConvId", item.get("ConversationId").toString());


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
                        deleteAnItem(viewHolder.getAdapterPosition());
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyMessages.this, UserMenu.class));
        Log.d("LOG : ", "Back button pressed on MyMessages.java");
        finish();

    }

    public void onDeleteIconClick(int p) {

        try {
            Log.d("LOG : ", "Delete icon clicked on MyMessages.java");
            JSONObject item = (JSONObject) jsonArray.get(p);
            String deleteConversationId = item.getString("User2");
            int deleteId = Integer.parseInt(deleteConversationId);
            Log.d("LOG : ", "deleting conversation, with receiverId: " + deleteConversationId);
            deleteAnItem(deleteId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteAnItem(int id) {
        pd = ProgressDialog.show(this, "", "Deleting. Please wait...", true);
        Log.d("LOG :", "deleteAnItem running on MyMessages.java");
        String URL = DELETECONVERSATIONURL + id;
        StringRequest dr = new StringRequest(Request.Method.DELETE, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //stop progress dialog
                        pd.cancel();
                        Log.d("LOG : ", "Response is " + response);

                        Intent conversationIntent = new Intent(MyMessages.this, MyMessages.class);

                        startActivity(conversationIntent);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("LOG : ", "Response error is " + error);

                    }
                }) {
/*            @Override
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
        MySingleton.getInstance(MyMessages.this).addToRequestQueue(dr);
    }
}
