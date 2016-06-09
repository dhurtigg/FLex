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
    private String mServiceType, mPriceString, mAmount , mFraktVal;

    private Spinner wasteSpinner;
    private Spinner fraktSpinner;
    private VideoView videoView;
    private DatabaseHandler dbhandler;

    /* Sets up the DatabaseHandler, a videoView, spinners, button. When button is clicked
     * the selected options in the spinner is put into variables and the price is calculated.
     * A dialog window pops with the price and a conformation button. When conformation button
      * is clicked mServiceType, mAmount, mPriceString, mFraktVal is sent to a new BestallActivity.
      * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avfall);


        dbhandler = new DatabaseHandler(this);
        dbhandler.getPrice();


        initiateVideoView();
        initiateSpinners();

        Button berB = (Button) findViewById(R.id.berB);
        berB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                mFraktVal = fraktSpinner.getSelectedItem().toString();
                mPriceTransport = Integer.parseInt(dbhandler.getPriceString(mFraktVal));
                String wasteChoice = wasteSpinner.getSelectedItem().toString();
                mPriceWaste = Integer.parseInt(dbhandler.getPriceString(wasteChoice));

                double berTransport = mPriceTransport;
                double berWaste = mPriceWaste;

                mTempPrice = (int) (berWaste + berTransport);

                mServiceType = wasteChoice;
                mAmount = "0";
                mPriceString = Integer.toString(mTempPrice);

                initiateAlertDialog();
            }
        });
    }

    /* Initiates a videoView. */

    private void initiateVideoView()
    {
        videoView = (VideoView) findViewById(R.id.videoAvfall);

        videoView.setVideoPath(
                "android.resource://" + getPackageName() + "/" + R.raw.inst);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    return false;
                } else {
                    videoView.start();
                    return false;
                }
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        MediaController mediaController = new MediaController(AvfallActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                    }
                });
            }
        });
    }

    /* Initiates the two spinners. */

    private void initiateSpinners()
    {
        fraktSpinner = (Spinner)findViewById(R.id.fraktM);
        String[] kommun = new String[]{"Stor säck Eslöv", "Stor säck Höör", "Stor säck Lund", "Stor säck Hässleholm",
                "Container Eslöv", "Container Höör", "Container Lund", "Container Hässleholm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kommun);
        fraktSpinner.setAdapter(adapter);

        wasteSpinner = (Spinner)findViewById(R.id.avfallM);
        String[] avf = new String[]{"Trädgårdsavfall", "Ris", "Tryckt virke", "Virke", "Blandat"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, avf);
        wasteSpinner.setAdapter(adapter1);
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
                i.putExtra("ORDER_DESTINATION", mFraktVal);
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
