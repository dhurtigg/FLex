package com.example.tomhansson.flexilast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Daniel on 2016-05-19.
 */
public class BestallActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    String insertUrl = "http://192.168.1.144/flexilast/AndroidPHP/insertOrder.php";
    String showUrl = "http://192.168.1.144/flexilast/AndroidPHP/showOrders.php";

    String email = "hejhej@hotmail.com";
    String gravelType = "Jord";
    String amount = "50";
    String price = "5000";



   private Button orderButton;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bestall);

        textview = (TextView) findViewById(R.id.textview);
        orderButton = (Button) findViewById(R.id.button1);
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("email",email);
                        params.put("gravelType",gravelType);
                        params.put("amount", amount);
                        params.put("price", price);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, PlattActivity.class);
        startActivity(i);
        this.finish();
    }

}
