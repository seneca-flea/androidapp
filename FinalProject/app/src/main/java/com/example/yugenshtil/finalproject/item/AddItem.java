package com.example.yugenshtil.finalproject.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.yugenshtil.finalproject.LoginPage;
import com.example.yugenshtil.finalproject.MainMenu;
import com.example.yugenshtil.finalproject.MySingleton;
import com.example.yugenshtil.finalproject.R;
//import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.UserMenu;
import com.example.yugenshtil.finalproject.adapter.DerpAdapter;
import com.example.yugenshtil.finalproject.model.DerpData;
import com.example.yugenshtil.finalproject.useCases.Sell;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import layout.AddBook;
import layout.AddMaterial;
import layout.NoItemAdded;


public class AddItem extends Activity {

    private String id = "";
    public String type = "Book";
    private String title = "";
    private String description = "";
    private double price = 0.0;
    private String errors = "";
    private String ADDITEMURL="http://senecaflea.azurewebsites.net/api/Item";
    private static final String PROTOCOL_CHARSET = "utf-8";
    private String imageCode ="";
    Fragment fragment;
    ArrayList<String> difficultyLevelOptionsList = new ArrayList<String>();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.d("Oleg", "Preferences " + sharedpreferences);
        if (sharedpreferences.contains("id")) {
            id = sharedpreferences.getString("id", "");
        }

        final Button btBook = (Button) findViewById(R.id.btAddItem_Book);
        final Button btMaterial = (Button) findViewById(R.id.btAddItem_Material);
        final Button btSave = (Button) findViewById(R.id.btAddItem_Save);
        final ImageButton ibImage = (ImageButton) findViewById(R.id.ibAddItem_Image);


        //   final Button btAddImage = (Button) findViewById(R.id.addImageBTaddItem);
        //   final EditText etTitle = (EditText) findViewById(R.id.addItemETtitle);
        //   final EditText etDescription = (EditText) findViewById(R.id.addItemETDescription);
        //   final EditText etPrice = (EditText) findViewById(R.id.addItemETprice);


        // Spinner. Does not work
        Spinner spinner = (Spinner) findViewById(R.id.sp_itemtype);


        difficultyLevelOptionsList.add("Please select type");
        difficultyLevelOptionsList.add("Book");
        difficultyLevelOptionsList.add("Item");
        //difficultyLevelOptionsList.add("Too Hard");


        // Create the ArrayAdapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddItem.this,android.R.layout.simple_spinner_item,difficultyLevelOptionsList);

        // Set the Adapter
        spinner.setAdapter(arrayAdapter);


        // Set the ClickListener for Spinner
        spinner.setOnItemSelectedListener(new  AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView,
                                       View view, int i, long l) {
                // TODO Auto-generated method stub
                Toast.makeText(AddItem.this,"You Selected : "

                        + difficultyLevelOptionsList.get(i)+" Level ",Toast.LENGTH_SHORT).show();

                Fragment fragment;


                if (i==0) {
                    Log.d("Oleg", "No choosen");
                    fragment = new NoItemAdded();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();
                }

                if (i==1) {
                    Log.d("Oleg", "Book");
                    fragment = new AddBook();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();
                }

                if (i==2) {
                    Log.d("Oleg", "Meterial");
                    fragment = new AddMaterial();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();
                }



            }
            // If no option selected
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });






        /*

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.itemtype_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
       // spinner.setOnItemSelectedListener(new SpinnerActivity());
*/



        // Waiting for Add Image Image Button to be clicked
        ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addImage = new Intent(AddItem.this, AddImage.class);
                startActivityForResult(addImage, 1990);
            }
        });


        // Waiting for Save Button to be clicked
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equals("Book")) {
                    EditText bookTitle = (EditText) findViewById(R.id.addBookETtitle);
                    Log.d("Oleg", "" + bookTitle.getText());

                }


                /*
                title = etTitle.getText().toString();
                description = etDescription.getText().toString();
                price = Double.parseDouble(etPrice.getText().toString());

                if (validateInput()) {
                    Map<String, String> params = new HashMap();
                    params.put("Title", imageCode);
                    params.put("Status", "Available");
                    params.put("Description", description);
                    params.put("Price", String.valueOf(price));
                    params.put("SellerId", String.valueOf(id));

                    JSONObject parameters = new JSONObject(params);
                    Log.d("Oleg ", "JSON is " + parameters);
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, ADDITEMURL, parameters, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {



                            Log.d("Oleg", "Response is " + response);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.d("Oleg", "Error is " + error);
                            // TODO Auto-generated method stub

                        }



                    });

                    MySingleton.getInstance(AddItem.this).addToRequestQueue(jsObjRequest);

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, "Item was added. Redirection to Items page", duration);
                    toast.show();

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent sellIntent = new Intent(AddItem.this,Sell.class);
                    // RegistrationPage.this.startActivity(loginIntent);
                    startActivity(sellIntent);

                } else {

                    Context context = getApplicationContext();
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, errors, duration);
                    toast.show();

                }*/
            }
        });

    }


    /*
    public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {

            Log.d("Oleg", "Selcted spinner " + pos);

            if(pos ==1) {
                fragment = new AddBook();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.itemfrag,fragment);
                ft.commitAllowingStateLoss();
            }

            if(pos ==0) {
                fragment = new NoItemAdded();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.itemfrag,fragment);
                ft.commit();
            }

            else if(pos==2){
                fragment = new AddMaterial();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.itemfrag,fragment);
                ft.commit();
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
            Log.d("Oleg","Nothing selected");
        }
    }

*/
            // Waiting to be clicked on of the options: Item/Material
            public void changeFragment(View v) {
                Log.d("Oleg", "Here");
                Fragment fragment;

                if (v == findViewById(R.id.btAddItem_Material)) {
                    Log.d("Oleg", "Meterial");
                    fragment = new AddMaterial();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();
                    //   Log.d("Oleg","Book");
                    type = "Material";
                } else if (v == findViewById(R.id.btAddItem_Book)) {
                    fragment = new AddBook();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.itemfrag, fragment);
                    ft.commit();
                    Log.d("Oleg", "Book");
                    type = "Book";
                }


            }

    public boolean validateInput(){
        boolean inputIsValid = true;
        // Log.d("Oleg","firstNAme va" +firstName);
        errors="";
        if(title.equals("")){
            //  Log.d("Oleg","firstNAme" +firstName);
            errors+="Please, do not leave title blank\n";
            inputIsValid = false;
        }
        if(description.equals("")){
            //  Log.d("Oleg","lastName" +lastName);
            errors+="Please, do not leave description blank\n";
            inputIsValid = false;
        }
        if(price==0 || price==0.0){
            //  Log.d("Oleg","email" +email);
            errors+="Please, provide correct price for the item\n";
            inputIsValid = false;
        }

        return inputIsValid;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_item, menu);
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
        else if(id==R.id.action_logout){
            SharedPreferences preferences = getSharedPreferences("MyPrefs", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();

            Intent mainMenuIntent = new Intent(AddItem.this,MainMenu.class);
            startActivity(mainMenuIntent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("oleg","we got");
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1990) : {
                if (resultCode == Activity.RESULT_OK) {
                    String newText = data.getStringExtra("Base64");
                    imageCode = newText;
                    Log.d("Oleg","Base64 is " + newText);
                }
                break;
            }
        }
    }


}
