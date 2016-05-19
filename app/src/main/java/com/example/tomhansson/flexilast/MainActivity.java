package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void ButtonHandler(View view) {

        if (view.getId() == R.id.guide_button) {
            Intent i = new Intent(MainActivity.this, GuideActivity.class);
            startActivity(i);
            this.finish();
        }

        if (view.getId() == R.id.info_button) {
            Intent i = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(i);
            this.finish();
        }
    }


}
