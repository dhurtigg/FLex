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
    private int priceService, priceTransport;
    private String serviceType;
    private String priceString;
    private String amount;
    private DatabaseHandler dbhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platt);

        dbhandler = new DatabaseHandler(this);

        dbhandler.getPrice();


        serviceType = "Stenmjöl 0-8";

        final VideoView videoView =
                (VideoView) findViewById(R.id.videoPlatt);

        videoView.setVideoPath(
                "android.resource://" + getPackageName() + "/" + R.raw.inst);

        videoView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(videoView.isPlaying())
                {
                    videoView.pause();
                    return false;
                }
                else
                {
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
                        MediaController mediaController = new MediaController(PlattActivity.this);
                        videoView.setMediaController(mediaController);
                        mediaController.setAnchorView(videoView);
                        //videoView.start();

                    }
                });
            }
        });



        final Spinner frakt = (Spinner)findViewById(R.id.fraktM);
        String[] kommun = new String[]{"Eslöv", "Höör", "Lund", "Hässleholm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kommun);
        frakt.setAdapter(adapter);



        final EditText lengthTxt = (EditText) findViewById(R.id.lengthT);
        final EditText widthTxt = (EditText) findViewById(R.id.widthT);
        Button berB = (Button) findViewById(R.id.berB);
        berB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int length = 0;
                int width = 0;

                if (lengthTxt.getText().length() > 0) {
                    length = Integer.parseInt(lengthTxt.getText().toString());
                }

                if(widthTxt.getText().length() > 0) {
                    width = Integer.parseInt(widthTxt.getText().toString());
                }
                final String fraktVal = frakt.getSelectedItem().toString();



               priceService = Integer.parseInt(dbhandler.getPriceString("Stenmjöl 0-8"));
               priceTransport = Integer.parseInt(dbhandler.getPriceString(fraktVal));


                double berTransport = priceTransport;
                double berService = priceService;
                int tempAmount = (int) (length*width*0.05*1.4);
                int tempPrice = (int) ((tempAmount*berService) + berTransport);

                priceString = Integer.toString(tempPrice);
                amount = Integer.toString(tempAmount)+ " ton";

                AlertDialog alertDialog = new AlertDialog.Builder(PlattActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Uppskattat pris:");
                alertDialog.setMessage(tempPrice+" SEK");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Beställ", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(PlattActivity.this, BestallActivity.class);
                        i.putExtra("ORDER_SERVICE_TYPE", serviceType);
                        i.putExtra("ORDER_AMOUNT", amount);
                        i.putExtra("ORDER_PRICE", priceString);
                        i.putExtra("ORDER_DESTINATION", fraktVal);
                        startActivity(i);
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Avbryt", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });


                alertDialog.show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }

}