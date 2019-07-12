package com.tsqc.fragments;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.LoginConfig;
import com.tsqc.R;
import com.tsqc.WebUrlMethods;
import com.tsqc.adapters.FabricAdapter;
import com.tsqc.util.AppController;
import com.tsqc.util.FabricItem;
import com.tsqc.util.OrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by someo on 15-09-2017.
 */

public class Styles extends Fragment {


    ListView listView;
    private ArrayList<FabricItem> feedItems;
    private FabricAdapter listAdapter;
    String email;
    Button scan;

//    private  final String mesurl="http://13.228.246.1/app/qc/verifysdetails.php";
//    String url="http://13.228.246.1/app/qc/getstyles.php?id=";
//    String ourl="http://13.228.246.1/app/qc/oupdate.php";
    String measure_error_status="0",measure_error="No Error",qc_id,qc_name;

    Button next;
    TextView merro;



    String orderid,tag="0";


    String id;
    public Styles(String id) {

        this.id=id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_styles, container, false);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        orderid = sharedPreferences.getString(LoginConfig.orderid, getResources().getString(R.string.na));
        qc_id = sharedPreferences.getString(LoginConfig.admin_id, getResources().getString(R.string.na));
        qc_name = sharedPreferences.getString(LoginConfig.admin_name, getResources().getString(R.string.na));

        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageView relativeLayout=(ImageView)view.findViewById(R.id.ootttt);
        imageLoader.displayImage("drawable://"+R.drawable.bg, relativeLayout);

        merro = (TextView) view.findViewById(R.id.textView);

        listView=(ListView)view.findViewById(R.id.getfabric);
        feedItems = new ArrayList<FabricItem>();
        listAdapter = new FabricAdapter(getActivity(), feedItems,this);
        registerForContextMenu(listView);
        listView.setAdapter(listAdapter);

        next=(Button)view.findViewById(R.id.scanfabric);
        next.setText("NEXT");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               update();

            }
        });


        Button derror=(Button)view.findViewById(R.id.dserror);
        derror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag="1";
                mnumber();
            }
        });
        Button dcorrect=(Button)view.findViewById(R.id.dscorrect);
        dcorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tag="1";
                measure();
            }
        });




        //fetchOrders(url+"34666");
        fetchOrders(WebUrlMethods.url_Styles+orderid);

        return view;

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
            JSONArray feedArray = response.getJSONArray("styles");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FabricItem item = new FabricItem();

                item.setName(feedObj.getString("name"));
                item.setPic(feedObj.getString("pic"));

                feedItems.add(item);
                listAdapter.notifyDataSetChanged();

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void update()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.ourl_Styles,
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
                params.put("oid", orderid);
                params.put("qc_id",qc_id);
                params.put("qc_name",qc_name);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
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


    public void measure() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.mesurl_Styles,
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
                params.put("oid", orderid);
                params.put("oitems_error_status",measure_error_status);
                params.put("oitems_error",measure_error);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}
