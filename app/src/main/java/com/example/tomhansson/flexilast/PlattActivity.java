package com.example.tomhansson.flexilast;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.*;
import android.view.*;
import android.widget.*;

/**
 * Created by Daniel on 2016-05-17.
 */
public class PlattActivity extends AppCompatActivity  {
    private int mPriceService, mPriceTransport, mTempPrice;
    private String mServiceType, mPriceString, mAmount, mShippingChoice;
    private DatabaseHandler mDatabaseHandler;

    private Button mCalculateButton;
    private EditText mLengthTxt,mWidthTxt;
    private Spinner mShippingSpinner;
    private VideoView mVideoView;

    /* Sets up the DatabaseHandler, a videoView, spinner, button. When button is clicked
     * the selected options in the spinner is put into variables and the price is calculated.
     * A dialog window pops with the price and a conformation button. When conformation button
     * is clicked mServiceType, mAmount, mPriceString, mShippingChoice is sent to a new
     * BestallActivity.
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platt);

        mDatabaseHandler = new DatabaseHandler(this);
        mDatabaseHandler.getPrice();

        mServiceType = "Stenmjöl 0-8";         // TODO: Implement getServiceType in DatabaseHandler.

        mLengthTxt = (EditText) findViewById(R.id.lengthT);
        mWidthTxt = (EditText) findViewById(R.id.widthT);

        initiateVideoView();
        initiateSpinner();

        mCalculateButton = (Button) findViewById(R.id.berB);
        mCalculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                calculate();
                initiateAlertDialog();

            }

        });
    }

    /* Calculates the price */

    private void calculate()
    {
        int length = 0;
        int width = 0;

        if (mLengthTxt.getText().length() > 0) {
            length = Integer.parseInt(mLengthTxt.getText().toString());
        }

        if(mWidthTxt.getText().length() > 0) {
            width = Integer.parseInt(mWidthTxt.getText().toString());
        }
        mShippingChoice = mShippingSpinner.getSelectedItem().toString();

        mPriceService = Integer.parseInt(mDatabaseHandler.getPriceString("Stenmjöl 0-8"));
        mPriceTransport = Integer.parseInt(mDatabaseHandler.getPriceString(mShippingChoice));


        double calculateTransport = mPriceTransport;
        double calculateService = mPriceService;
        int tempAmount = (int) (length*width*0.05*1.4);
        mTempPrice = (int) ((tempAmount*calculateService) + calculateTransport);

        mPriceString = Integer.toString(mTempPrice);
        mAmount = Integer.toString(tempAmount)+ " ton";
    }


    /* Initiates the spinner. */

    private void initiateSpinner()
    {
        mShippingSpinner = (Spinner)findViewById(R.id.fraktM);
        String[] kommun = new String[]{"Eslöv", "Höör", "Lund", "Hässleholm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kommun);
        mShippingSpinner.setAdapter(adapter);
    }


    /* Initiates a alert dialog. */

    private void initiateAlertDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(PlattActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Uppskattat pris:");
        alertDialog.setMessage(mTempPrice+" SEK");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Beställ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(PlattActivity.this, BestallActivity.class);
                i.putExtra("ORDER_SERVICE_TYPE", mServiceType);
                i.putExtra("ORDER_AMOUNT", mAmount);
                i.putExtra("ORDER_PRICE", mPriceString);
                i.putExtra("ORDER_DESTINATION", mShippingChoice);
                startActivity(i);
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Avbryt", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.show();
    }

    /* Initiates a videoView. */

    private void initiateVideoView(){
        mVideoView =
                (VideoView) findViewById(R.id.videoPlatt);

        mVideoView.setVideoPath(
                "android.resource://" + getPackageName() + "/" + R.raw.inst);

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(mVideoView.isPlaying())
                {
                    mVideoView.pause();
                    return false;
                }
                else
                {
                    mVideoView.start();
                    return false;
                }

            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController mediaController = new MediaController(PlattActivity.this);
                        mVideoView.setMediaController(mediaController);
                        mediaController.setAnchorView(mVideoView);
                    }
                });
            }
        });
    }

    /* When the back button is pressed the application goes to GuideActivity. */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }

}