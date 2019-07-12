package com.tsqc.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.Login;
import com.tsqc.LoginConfig;
import com.tsqc.R;
import com.tsqc.WebUrlMethods;
import com.tsqc.adapters.PurchaseOrder;
import com.tsqc.util.AppController;
import com.tsqc.util.MyApplication;
import com.tsqc.util.OrderItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by someo on 14-03-2017.
 */

public class ViewOrder extends Fragment {

    public TextView name,date,ordertotal,orderno,address,mobile,email,gender,ddates,dtimes,advances,leftovers;
    public ImageView overflow;
    public NetworkImageView imageView;
    ImageLoader imageLoader;
    String admin_id,id,OrderNo,OrderDate,OrderTotal,names,genders,photo,addr,emails,mobiles,aid;


    String ddate;
    String dtime;
    String advance;
    String leftover;


    private ArrayList<OrderItem> feedItems;
    private PurchaseOrder listAdapter;
    Button vpi;

    String oid;
    TextView derro,merro;




    String details_error="No Error",details_errorr_status="0",tag="0";
    String measure_error_status="0",measure_error="No Error";


    @SuppressLint("ValidFragment")
    public ViewOrder(String oid) {
        this.oid=oid;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_vieworder, container, false);

        com.nostra13.universalimageloader.core.ImageLoader imageLoadesr = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoadesr.init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageView relativeLayout=(ImageView)view.findViewById(R.id.rty);
        imageLoadesr.displayImage("drawable://"+R.drawable.bg, relativeLayout);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        aid = sharedPreferences.getString(LoginConfig.admin_id,  getResources().getString(R.string.na));
        Button derror=(Button)view.findViewById(R.id.derror);
        derror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag="1";
                unumber();
            }
        });
        Button dcorrect=(Button)view.findViewById(R.id.dcorrect);
        dcorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag="1";
                details();
            }
        });




        imageLoader = MyApplication.getInstance().getImageLoader();
        vpi=(Button)view.findViewById(R.id.vpi);
        vpi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(LoginConfig.orderid, oid);
                editor.commit();


                FragmentManager fmq = getFragmentManager();
                FragmentTransaction fqt = fmq.beginTransaction();

                PurchasedItems subjectHome = new PurchasedItems();
                Bundle args = new Bundle();
                args.putString("orderId", oid);
                args.putString("tag", "3");
                subjectHome.setArguments(args);
                fqt.replace(R.id.frame, subjectHome);
                fqt.addToBackStack(null);
                fqt.commit();
            }
        });




        derro = (TextView) view.findViewById(R.id.derro);



        name = (TextView) view.findViewById(R.id.vname);
        orderno = (TextView) view.findViewById(R.id.vordernum);
        date = (TextView) view.findViewById(R.id.vdate);
        ordertotal = (TextView) view.findViewById(R.id.vsales);
        address = (TextView) view.findViewById(R.id.vaddr);
        mobile = (TextView) view.findViewById(R.id.vphone);
        email = (TextView) view.findViewById(R.id.vemail);
        imageView = (NetworkImageView) view.findViewById(R.id.vImage);


        ddates = (TextView) view.findViewById(R.id.delieverydates);
        dtimes = (TextView) view.findViewById(R.id.deliverytimes);
        leftovers = (TextView) view.findViewById(R.id.leftovers);
        advances = (TextView) view.findViewById(R.id.advpaids);



        feedItems = new ArrayList<OrderItem>();
        listAdapter = new PurchaseOrder(getActivity(), feedItems,this,oid);

        fetchOrder(WebUrlMethods.url_ViewOrder+oid);
        Log.e("oid",oid);
        fetchOrders(WebUrlMethods.url_PurchasedItems+oid);

        return view;

    }





    public void fetchOrder(String url)
    {
        // We first check for cached request
        Log.e("url",url);
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null)
        {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseCommentFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {

            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    url, (String)null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d("Error", "Response: " + response.toString());
                    if (response != null) {
                        parseCommentFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }
    private void parseCommentFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("Orders");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                admin_id=feedObj.getString("admin_id");
                id=feedObj.getString("id");
                OrderNo=feedObj.getString("OrderNo");
                OrderDate=feedObj.getString("OrderDate");
                OrderTotal=feedObj.getString("OrderTotal");
                names=feedObj.getString("name");
                genders=feedObj.getString("gender");
                photo=feedObj.getString("photo");
                addr=feedObj.getString("addr");
                emails=feedObj.getString("email");
                mobiles=feedObj.getString("mobile");
                ddate=feedObj.getString("pickup_date");
                dtime=feedObj.getString("pickup_time");
                advance=feedObj.getString("adavace");
                leftover=feedObj.getString("leftover");


            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        name.setText(names);
        orderno.setText("#"+OrderNo);
        imageView.setImageUrl(WebUrlMethods.url_ViewOrder+photo,imageLoader);
        address.setText(addr);
        mobile.setText(mobiles);
        email.setText(emails);
        ordertotal.setText(OrderTotal);
        date.setText(OrderDate);

        ddates.setText("Delivery Date:"+ddate);
        dtimes.setText("Delivery Time:"+dtime);
        advances.setText("Advance Paid:"+advance);
        leftovers.setText("Left Balance:"+leftover);


    }

    public void fetchOrders(String url)
    {
        // We first check for cached request
        Log.e("url",url);
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        if (entry != null)
        {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseCommentFeeds(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else {

            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    url, (String)null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d("Error", "Response: " + response.toString());
                    if (response != null) {
                        parseCommentFeeds(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }
    private void parseCommentFeeds(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("Order");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);


                OrderItem item = new OrderItem();
                item.setId(feedObj.getString("id"));
                item.setName(feedObj.getString("name"));
                item.setQty(feedObj.getString("qty"));
                item.setPrice(feedObj.getString("price"));
                item.setSubtotal(feedObj.getString("subtotal"));

                feedItems.add(item);
                listAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void details() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.detailsurl_ViewOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response",response);
                        if(response.trim().equals("success"))
                        {


                            Toast.makeText(getActivity(),"Updated",Toast.LENGTH_SHORT).show();

                        }
                        else if(response.equals("fail"))
                        {
                            Toast.makeText(getActivity(),"Fail",Toast.LENGTH_LONG).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("oid", oid);
                params.put("aid",aid);
                params.put("details_errorr_status",details_errorr_status);
                params.put("details_error",details_error);


                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    public void measure() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.mesurl_ViewOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("response",response);
                        if(response.trim().equals("success"))
                        {


                            Toast.makeText(getActivity(),"Updated",Toast.LENGTH_SHORT).show();

                        }
                        else if(response.equals("fail"))
                        {
                            Toast.makeText(getActivity(),"Fail",Toast.LENGTH_LONG).show();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error instanceof NoConnectionError) {
                            Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("oid", oid);
                params.put("measure_error_status",measure_error_status);
                params.put("measure_error",measure_error);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


    public void unumber()
    {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.uprompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.edittTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                details_error=userInput.getText().toString();
                                derro.setVisibility(View.VISIBLE);
                                derro.setText("Error:"+details_error);
                                details_errorr_status="1";
                                details();


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    public void mnumber()
    {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.uprompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.edittTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                measure_error=userInput.getText().toString();
                                merro.setVisibility(View.VISIBLE);
                                merro.setText("Error:"+measure_error);
                                measure_error_status="1";
                                measure();


                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }







}
