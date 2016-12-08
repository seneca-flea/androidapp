package com.example.yugenshtil.finalproject.OtherUseCases;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yugenshtil.finalproject.R;

public class ContactUs extends Activity {

     String name ="";
     String email="";
     String content ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        final Button btSend = (Button) findViewById(R.id.bt_send_btn);

        EditText cNAme = (EditText) findViewById(R.id.et_contact_name);
        EditText cEmail = (EditText) findViewById(R.id.et_contact_email);
        EditText cContent = (EditText) findViewById(R.id.et_contact_content);

        name = cNAme.getText().toString();
        email = cEmail.getText().toString();
        content = cContent.getText().toString();


        btSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("LOG : ","send button send on contactUs.java");
                sendMessage();
            }
        });
    }

    public void sendMessage() {
        //Testing: Log.d("LOG : ","send button started sendMessage on contactUs.java");

        //Testing: Log.d("LOG : ", content);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.putExtra(Intent.EXTRA_EMAIL,"rbmezza@gmail.com");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent,getResources().getText(R.string.send_to)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_us, menu);
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
