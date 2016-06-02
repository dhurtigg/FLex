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
    private int priceAvfall, tempPrice, priceTransport;
    private String serviceType;
    private String priceString;
    private String amount;
    private DatabaseHandler dbhandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avfall);

        dbhandler = new DatabaseHandler(this);

        dbhandler.getPrice();

        final VideoView videoView =
                (VideoView) findViewById(R.id.videoAvfall);

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
                        //videoView.start();

                    }
                });
            }
        });

        final Spinner frakt = (Spinner)findViewById(R.id.fraktM);
        String[] kommun = new String[]{"Stor säck Eslöv", "Stor säck Höör", "Stor säck Lund", "Stor säck Hässleholm",
        "Container Eslöv", "Container Höör", "Container Lund", "Container Hässleholm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kommun);
        frakt.setAdapter(adapter);

        final Spinner avfall = (Spinner)findViewById(R.id.avfallM);
        String[] avf = new String[]{"Trädgårdsavfall", "Ris", "Tryckt virke", "Virke", "Blandat"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, avf);
        avfall.setAdapter(adapter1);


        Button berB = (Button) findViewById(R.id.berB);
        berB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String fraktVal = frakt.getSelectedItem().toString();
                priceTransport = Integer.parseInt(dbhandler.getPriceString(fraktVal));
                String avfallVal = avfall.getSelectedItem().toString();
                priceAvfall = Integer.parseInt(dbhandler.getPriceString(avfallVal));

                double berTransport = priceTransport;
                double berAvfall = priceAvfall;

                tempPrice = (int) (berAvfall + berTransport);

                serviceType = avfallVal+" "+fraktVal;
                amount = "0";
                priceString = Integer.toString(tempPrice);

                AlertDialog alertDialog = new AlertDialog.Builder(AvfallActivity.this).create();
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Uppskattat pris:");
                alertDialog.setMessage(tempPrice+" SEK");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Beställ", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(AvfallActivity.this, BestallActivity.class);
                        i.putExtra("ORDER_SERVICE_TYPE", serviceType);
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
