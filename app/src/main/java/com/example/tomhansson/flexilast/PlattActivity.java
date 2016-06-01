package com.example.tomhansson.flexilast;

import android.app.AlertDialog;
import android.content.Context;
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
public class PlattActivity extends AppCompatActivity  {
    private int sprice;
    private String gravelType;
    private String priceString;
    private String amount;
    private DatabaseHandler dbhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platt);

        dbhandler = new DatabaseHandler(this);

        dbhandler.getPrice();


        gravelType = "Sand";

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
                String stad = frakt.getSelectedItem().toString();



               sprice = Integer.parseInt(dbhandler.getPriceString(stad));

               // double ber = ((length*width*0.05*1.4*67) + fraktp);
                //int price = (int)ber;

                double ber = sprice;
                int tempPrice = (int) (length*width*0.05*1.4);

                priceString = Integer.toString(sprice);
                amount = Integer.toString(tempPrice);

                AlertDialog alertDialog = new AlertDialog.Builder(PlattActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Uppskattat pris:");
                alertDialog.setMessage(sprice+" SEK");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Beställ", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(PlattActivity.this, BestallActivity.class);
                        i.putExtra("ORDER_GRAVEL_TYPE", gravelType);
                        i.putExtra("ORDER_AMOUNT", amount);
                        i.putExtra("ORDER_PRICE", priceString);
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