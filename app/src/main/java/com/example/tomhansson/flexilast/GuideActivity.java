package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GuideActivity extends AppCompatActivity {


    /* When the back button is pressed the application goes to GuideActivity. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }

    /* Handles the button. */

    public void buttonHandler(View view) {

        if (view.getId() == R.id.avfall_button) {
            Intent i = new Intent(GuideActivity.this, AvfallActivity.class);
            startActivity(i);
            this.finish();
        }

        if (view.getId() == R.id.platt_button) {
            Intent i = new Intent(GuideActivity.this, PlattActivity.class);
            startActivity(i);
            this.finish();
        }

        if (view.getId() == R.id.vaeg_button) {
            Intent i = new Intent(GuideActivity.this, VagActivity.class);
            startActivity(i);
            this.finish();
        }

        if (view.getId() == R.id.falltrad_button) {
            Intent i = new Intent(GuideActivity.this, FallTradActivity.class);
            startActivity(i);
            this.finish();
        }

    }

}
