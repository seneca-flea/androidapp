package com.example.yugenshtil.finalproject.model;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yugenshtil.finalproject.R;

public class ItemDisplayActivity extends Activity {

    private static final String EXTRA_QUOTE = "EXTRA_QUOTE " ;
    private static final String EXTRA_ATTR = "EXTRA_ATTR" ;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_display);

        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
      ((TextView)findViewById(R.id.lbl_quote_text)).setText(extras.getString(EXTRA_QUOTE));
        ((TextView)findViewById(R.id.lbl_quote_attribution)).setText(extras.getString(EXTRA_ATTR));
    }
}
