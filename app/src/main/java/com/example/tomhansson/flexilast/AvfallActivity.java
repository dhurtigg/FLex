package com.example.tomhansson.flexilast;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

/**
 * Created by Daniel on 2016-05-17.
 */
public class AvfallActivity extends AppCompatActivity {
    private int mPriceWaste, mTempPrice, mPriceTransport;
    private String mServiceType, mPriceString, mAmount , mShippingChoice;

    private Spinner mWasteSpinner;
    private Spinner mShippingSpinner;
    private VideoView mVideoView;
    private DatabaseHandler mDatabaseHandler;
    private Button mCalculateButton;

    /* Sets up the DatabaseHandler, a videoView, spinners, button. When button is clicked
     * the selected options in the spinner is put into variables and the price is calculated.
     * A dialog window pops with the price and a conformation button. When conformation button
     * is clicked mServiceType, mAmount, mPriceString, mShippingChoice is sent to a new
     * BestallActivity.
     * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avfall);


        mDatabaseHandler = new DatabaseHandler(this);
        mDatabaseHandler.getPrice();


        initiateVideoView();
        initiateSpinners();

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
        mShippingChoice = mShippingSpinner.getSelectedItem().toString();
        mPriceTransport = Integer.parseInt(mDatabaseHandler.getPriceString(mShippingChoice));
        String wasteChoice = mWasteSpinner.getSelectedItem().toString();
        mPriceWaste = Integer.parseInt(mDatabaseHandler.getPriceString(wasteChoice));

        double berTransport = mPriceTransport;
        double berWaste = mPriceWaste;

        mTempPrice = (int) (berWaste + berTransport);

        mServiceType = wasteChoice;
        mAmount = "0";
        mPriceString = Integer.toString(mTempPrice);

    }

    /* Initiates a videoView. */

    private void initiateVideoView()
    {
        mVideoView = (VideoView) findViewById(R.id.videoAvfall);

        mVideoView.setVideoPath(
                "android.resource://" + getPackageName() + "/" + R.raw.inst);

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    return false;
                } else {
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
                        MediaController mediaController = new MediaController(AvfallActivity.this);
                        mVideoView.setMediaController(mediaController);
                        mediaController.setAnchorView(mVideoView);
                    }
                });
            }
        });
    }

    /* Initiates the two spinners. */

    private void initiateSpinners()
    {
        mShippingSpinner = (Spinner)findViewById(R.id.fraktM);
        String[] kommun = new String[]{"Stor säck Eslöv", "Stor säck Höör", "Stor säck Lund", "Stor säck Hässleholm",
                "Container Eslöv", "Container Höör", "Container Lund", "Container Hässleholm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kommun);
        mShippingSpinner.setAdapter(adapter);

        mWasteSpinner = (Spinner)findViewById(R.id.avfallM);
        String[] avf = new String[]{"Trädgårdsavfall", "Ris", "Tryckt virke", "Virke", "Blandat"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, avf);
        mWasteSpinner.setAdapter(adapter1);
    }

    /* Initiates a alert dialog. */

    private void initiateAlertDialog()
    {

        AlertDialog alertDialog = new AlertDialog.Builder(AvfallActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Uppskattat pris:");
        alertDialog.setMessage(mTempPrice+" SEK");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Beställ", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(AvfallActivity.this, BestallActivity.class);
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

    /* When the back button is pressed the application goes to GuideActivity. */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }
}
