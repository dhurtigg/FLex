package com.example.tomhansson.flexilast;

import android.content.Context;

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
 * Created by Tom Hansson on 2016-05-06.
 *
 * Handles data from and to the database by using a intermediate php script.
 *
 */
public class DatabaseHandler {
    private String email = "";
    private String gravelType = "";
    private String amount = "";
    private String price = "";

    private String insertUrl;
    private String showUrl;

    private RequestQueue requestQueue;

    private Context c;

    DatabaseHandler(Context context) {
        c = context;

        insertUrl = "http://192.168.2.7/flexilast/AndroidPHP/insertOrder.php";
        showUrl = "http://192.168.2.7/flexilast/AndroidPHP/showOrders.php";
    }

    /* Makes a request to a php script for inserting email, gravelType, amount, price into the
     * orders table in the database.
     */

    public void insertIntoOrders(String emailInput, String gravelTypeInput, String amountInput, String priceInput) {
        email = emailInput;
        gravelType = gravelTypeInput;
        amount = amountInput;
        price = priceInput;

        requestQueue = Volley.newRequestQueue(c.getApplicationContext());

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
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("gravelType", gravelType);
                params.put("amount", amount);
                params.put("price", price);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public String getPrice() {
        return " ";
    }


}
