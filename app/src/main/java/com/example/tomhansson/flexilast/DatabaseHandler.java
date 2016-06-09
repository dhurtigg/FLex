package com.example.tomhansson.flexilast;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tom Hansson on 2016-05-06.
 *
 * Handles data from and to the database by using a intermediate php script.
 *
 */
public class DatabaseHandler {
    private String mEmail = "";
    private String mServiceType = "";
    private String mAmount = "";
    private String mDestination = "";
    private String mPriceInsert = "";


    private ArrayList<String> mPriceList;
    private ArrayList<String> mServiceList;

    private String insertUrl;
    private String showUrl;

    private RequestQueue requestQueue;
    private JSONArray jsonArrayServices = null;
    public static final String JSON_ARRAY = "services";
    private Context c;

    DatabaseHandler(Context context) {
        c = context;

        mPriceList = new ArrayList<String>();
        mServiceList = new ArrayList<String>();

        insertUrl = "http://192.168.1.144/flexilast/AndroidPHP/insertOrder.php";
        showUrl = "http://192.168.1.144/flexilast/AndroidPHP/showOrders.php";
    }

    /* Makes a request to a php script for inserting email, gravelType, amount, price into the
     * orders table in the database.
     */

    public void insertIntoOrders(String emailInput, String serviceInput, String destinationInput ,String amountInput, String priceInput) {
        mEmail = emailInput;
        mServiceType = serviceInput;
        mAmount = amountInput;
        mPriceInsert = priceInput;
        mDestination = destinationInput;
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
                params.put("email", mEmail);
                params.put("service", mServiceType);
                params.put("destination", mDestination);
                params.put("amount", mAmount);
                params.put("price", mPriceInsert);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    /* Sends a variable type to a php script that makes a query to the services table in the database.
     * And receives the corresponding price for the type */

    public void getPrice()
    {

        requestQueue = Volley.newRequestQueue(c.getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, showUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {

                            jsonObject = new JSONObject(response);
                            jsonArrayServices = jsonObject.getJSONArray(JSON_ARRAY);

                            for(int i=0;i<jsonArrayServices.length();i++){
                                JSONObject jo = jsonArrayServices.getJSONObject(i);
                                mPriceList.add(i,jo.getString("price"));
                                mServiceList.add(i, jo.getString("service"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
        };
        requestQueue.add(stringRequest);
    }

    /* Returns the corresponding price from serviceInput.*/

    public String getPriceString(String serviceInput)
    {
        int i = 0;

        if(mServiceList != null && mPriceList != null)
            while (serviceInput.compareTo(mServiceList.get(i)) != 0 && i < mServiceList.size())
            {
                i++;
            }

        return mPriceList.get(i);
    }
}
