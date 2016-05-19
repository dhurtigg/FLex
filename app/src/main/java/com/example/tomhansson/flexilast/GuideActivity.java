package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GuideActivity extends AppCompatActivity {

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }

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

        if (view.getId() == R.id.uppfart_button) {
            Intent i = new Intent(GuideActivity.this, UppfartActivity.class);
            startActivity(i);
            this.finish();
        }

        if (view.getId() == R.id.tradack_button) {
            Intent i = new Intent(GuideActivity.this, TradackActivity.class);
            startActivity(i);
            this.finish();
        }
    }

}
