package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Daniel on 2016-05-19.
 */
public class BestallActivity extends AppCompatActivity {

    private String email = "";
    private String serviceType = "";
    private String amount = "";
    private String price = "";
    private String destination = "";
    DatabaseHandler dbHandler;

    private Button orderButton;
    private TextView textview;
    private EditText editTextView;

    /* Initiates TextView, Button and a DatabaseHandle. Adds a ClickListener to orderButton*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestall);

        Intent intent = getIntent();

        serviceType = intent.getStringExtra("ORDER_SERVICE_TYPE");
        amount = intent.getStringExtra("ORDER_AMOUNT");
        price = intent.getStringExtra("ORDER_PRICE");
        destination = intent.getStringExtra("ORDER_DESTINATION");

        editTextView = (EditText) findViewById(R.id.editText);

        textview = (TextView) findViewById(R.id.textview);
        orderButton = (Button) findViewById(R.id.button1);

        dbHandler = new DatabaseHandler(this);



        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextView.getText().toString();
                dbHandler.insertIntoOrders(email,serviceType, destination ,amount,price);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }

}
