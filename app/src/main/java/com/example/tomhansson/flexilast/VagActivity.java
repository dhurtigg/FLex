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
    private DatabaseHandler dbhandler;
    private int priceTransport, priceService;
    private String amount, priceString, serviceType, destination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vag);



            dbhandler = new DatabaseHandler(this);

            dbhandler.getPrice();



            final VideoView videoView =
                    (VideoView) findViewById(R.id.videoVag);

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
                            MediaController mediaController = new MediaController(VagActivity.this);
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

        final Spinner service = (Spinner)findViewById(R.id.serviceM);
        String[] serv = new String[]{"Hyvling", "Spridning", "Dammbindning"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, serv);
        service.setAdapter(adapter1);

        final EditText lengthTxt = (EditText) findViewById(R.id.lengthT);


        Button berB = (Button) findViewById(R.id.berB);
        berB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int length = 0;

                if (lengthTxt.getText().length() > 0) {
                    length = Integer.parseInt(lengthTxt.getText().toString());
                }

                String serviceVal = service.getSelectedItem().toString();
                String fraktVal = frakt.getSelectedItem().toString();

                destination = fraktVal;
                serviceType = serviceVal;

                priceService = Integer.parseInt(dbhandler.getPriceString(serviceVal));

                priceTransport = Integer.parseInt(dbhandler.getPriceString(fraktVal));


                double berTransport = priceTransport;
                double berService = priceService;
                int tempAmount = length;
                int tempPrice = (int) ((length * berService) + berTransport);

                priceString = Integer.toString(tempPrice);
                amount = Integer.toString(tempAmount)+ " km";

                AlertDialog alertDialog = new AlertDialog.Builder(VagActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Uppskattat pris:");
                alertDialog.setMessage(tempPrice+" SEK");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Beställ", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(VagActivity.this, BestallActivity.class);
                        i.putExtra("ORDER_SERVICE_TYPE", serviceType);
                        i.putExtra("ORDER_AMOUNT", amount);
                        i.putExtra("ORDER_PRICE", priceString);
                        i.putExtra("ORDER_DESTINATION", destination);
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

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
        this.finish();
    }
}
