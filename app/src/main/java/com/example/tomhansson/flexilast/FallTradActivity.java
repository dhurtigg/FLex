package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Daniel on 2016-05-26.
 */
public class FallTradActivity extends AppCompatActivity {


    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }
}
