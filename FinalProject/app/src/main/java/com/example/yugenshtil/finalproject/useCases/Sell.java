package com.example.yugenshtil.finalproject.useCases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yugenshtil.finalproject.R;
import com.example.yugenshtil.finalproject.RegistrationPage;
import com.example.yugenshtil.finalproject.item.AddItem;

public class Sell extends Activity {

    private String id = "";
    private String fullName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            fullName = extras.getString("fullName");
           //The key argument here must match that used in the other activity
        }

        final TextView tvCongratulation = (TextView) findViewById(R.id.sellTVCongratulations);
        final TextView tvItemsList = (TextView) findViewById(R.id.sellTVitemsList);
        final Button btAddItem = (Button) findViewById(R.id.sellBTaddItem);

        tvCongratulation.setText(fullName+", here is the list pf products you sell");
        tvItemsList.setText(getMyItems());

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

    public String getMyItems(){
        String myItems = "";

        // TODO
        myItems="You have no items so far";

        return myItems;

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
