package com.example.tomhansson.flexilast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

/**
 * Created by Daniel on 2016-05-17.
 */
public class VagActivity extends AppCompatActivity {
    private DatabaseHandler mDatabaseHandler;
    private int mPriceTransport, mPriceService ,mTempPrice;
    private String mAmount, mPriceString, mServiceType, mDestination;

    private VideoView mVideoView;
    private Spinner mShippingSpinner, mServiceSpinner;
    private EditText mLengthTxt;

    /* Sets up the DatabaseHandler, a videoView, two spinners, button. When button is clicked
     * the selected options in the spinner is put into variables and the price is calculated.
     * A dialog window pops with the price and a conformation button. When conformation button
     * is clicked mServiceType, mAmount, mPriceString, mDestination is sent to a new
     * BestallActivity.
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vag);

        mDatabaseHandler = new DatabaseHandler(this);

        mDatabaseHandler.getPrice();

        mLengthTxt = (EditText) findViewById(R.id.lengthT);

        initiateVideoView();
        initiateSpinners();


        Button berB = (Button) findViewById(R.id.berB);
        berB.setOnClickListener(new View.OnClickListener() {
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

        if (mLengthTxt.getText().length() > 0) {
            length = Integer.parseInt(mLengthTxt.getText().toString());
        }

        String serviceChoic = mServiceSpinner.getSelectedItem().toString();
        String shippingChoice = mShippingSpinner.getSelectedItem().toString();

        mDestination = shippingChoice;
        mServiceType = serviceChoic;

        mPriceService = Integer.parseInt(mDatabaseHandler.getPriceString(serviceChoic));

        mPriceTransport = Integer.parseInt(mDatabaseHandler.getPriceString(shippingChoice));


        double calculateTransport = mPriceTransport;
        double calculateService = mPriceService;
        int tempAmount = length;
        mTempPrice = (int) ((length * calculateService) + calculateTransport);

        mPriceString = Integer.toString(mTempPrice);
        mAmount = Integer.toString(tempAmount)+ " km";
    }

    /* Initiates the two spinners. */

    private void initiateSpinners()
    {
        mShippingSpinner = (Spinner)findViewById(R.id.fraktM);
        String[] location = new String[]{"Eslöv", "Höör", "Lund", "Hässleholm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, location);
        mShippingSpinner.setAdapter(adapter);

        mServiceSpinner = (Spinner)findViewById(R.id.serviceM);
        String[] service = new String[]{"Hyvling", "Spridning", "Dammbindning"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, service);
        mServiceSpinner.setAdapter(adapter1);
    }

    /* Initiates a alert dialog. */

    private void initiateAlertDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(VagActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Uppskattat pris:");
        alertDialog.setMessage(mTempPrice+" SEK");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Beställ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(VagActivity.this, BestallActivity.class);
                i.putExtra("ORDER_SERVICE_TYPE", mServiceType);
                i.putExtra("ORDER_AMOUNT", mAmount);
                i.putExtra("ORDER_PRICE", mPriceString);
                i.putExtra("ORDER_DESTINATION", mDestination);
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

    private void initiateVideoView()
    {
        mVideoView = (VideoView) findViewById(R.id.videoVag);
        mVideoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.inst);
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
                        MediaController mediaController = new MediaController(VagActivity.this);
                        mVideoView.setMediaController(mediaController);
                        mediaController.setAnchorView(mVideoView);
                    }
                });
            }
        });
    }

    /* When the back button is pressed the application goes to GuideActivity. */

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }
}
