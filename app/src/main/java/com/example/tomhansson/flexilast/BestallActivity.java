package com.example.tomhansson.flexilast;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private String mEmail = "";
    private String mServiceType = "";
    private String mAmount = "";
    private String mPrice = "";
    private String mDestination = "";
    DatabaseHandler mDbHandler;

    private Button mOrderButton;
    private TextView mTextview;
    private EditText mEditTextView;

    /* Initiates TextView, Button and a DatabaseHandler. Adds a ClickListener to orderButton*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestall);

        Intent intent = getIntent();

        mServiceType = intent.getStringExtra("ORDER_SERVICE_TYPE");
        mAmount = intent.getStringExtra("ORDER_AMOUNT");
        mPrice = intent.getStringExtra("ORDER_PRICE");
        mDestination = intent.getStringExtra("ORDER_DESTINATION");

        mEditTextView = (EditText) findViewById(R.id.editText);

        mTextview = (TextView) findViewById(R.id.textview);
        mOrderButton = (Button) findViewById(R.id.bestallB);

        mDbHandler = new DatabaseHandler(this);


        mOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEditTextView.getText().toString();
                mDbHandler.insertIntoOrders(mEmail,mServiceType, mDestination ,mAmount,mPrice);
                initiateAlertDialog();

            }
        });
    }

     /* Initiates a alert dialog. */

    private void initiateAlertDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(BestallActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Tack för din beställning!");
        alertDialog.setMessage("Tryck på OK för att återvända till förstasidan");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(BestallActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        alertDialog.show();
    }

    /* When the back button is pressed the application goes to GuideActivity. */

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }

}
