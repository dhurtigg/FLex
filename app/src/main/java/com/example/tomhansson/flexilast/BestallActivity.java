package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Daniel on 2016-05-19.
 */
public class BestallActivity extends AppCompatActivity {

    String email = "hejhej@hotmail.com";
    String gravelType = "Jord";
    String amount = "50";
    String price = "5000";

    DatabaseHandler dbHandler;

    private Button orderButton;
    private TextView textview;

    /* Initiates TextView, Button and a DatabaseHandle. Adds a ClickListener to orderButton*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestall);

        textview = (TextView) findViewById(R.id.textview);
        orderButton = (Button) findViewById(R.id.button1);

        dbHandler = new DatabaseHandler(this);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbHandler.insertIntoOrders(email,gravelType,amount,price);

            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, PlattActivity.class);
        startActivity(i);
        this.finish();
    }

}
