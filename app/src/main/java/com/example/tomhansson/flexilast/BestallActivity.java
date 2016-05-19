package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Daniel on 2016-05-19.
 */
public class BestallActivity extends AppCompatActivity {

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, PlattActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestall);
    }
}
