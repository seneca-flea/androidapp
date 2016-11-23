package com.example.yugenshtil.finalproject.useCases;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.yugenshtil.finalproject.R;

/**
 * Created by yugenshtil on 13/11/16.
 */
public class RangeChoose extends Activity {
    String min;
    String max="100";
    Button btApply;
    Button btReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rangepopup);

        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar5);

// get min and max text view
        final TextView tvMin = (TextView) findViewById(R.id.textView2);
        final TextView tvMax = (TextView) findViewById(R.id.textView3);
        btApply = (Button) findViewById(R.id.btApply);
        btReset = (Button) findViewById(R.id.btReset);
        DisplayMetrics dm =  new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.2));



// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                min = String.valueOf(minValue);
                max = String.valueOf(maxValue);
                Log.d("Oleg","Min is " + min + " and value " + String.valueOf(minValue));
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
            }
        });




        btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("Oleg","Clicked");
              Intent resultIntent = new Intent();
                Log.d("Oleg","Min " + min);
                Log.d("Oleg","Max " + max);
                resultIntent.putExtra("from", min);
                resultIntent.putExtra("to", max);

              //  resultIntent.putExtra("to", "I am getting");
                setResult(100, resultIntent);
                finish();


              //  Intent buyIntent = new Intent(RangeChoose.this,Buy.class);
                //startActivity(buyIntent);
              //  finish();
            }
        });


        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Oleg","Clicked");
                Intent resultIntent = new Intent();
                Log.d("Oleg","Min " + min);
                Log.d("Oleg","Max " + max);
                resultIntent.putExtra("from", "0");
                resultIntent.putExtra("to", "400");

                //  resultIntent.putExtra("to", "I am getting");
                setResult(100, resultIntent);
                finish();


                //  Intent buyIntent = new Intent(RangeChoose.this,Buy.class);
                //startActivity(buyIntent);
                //  finish();
            }
        });

    }


}
