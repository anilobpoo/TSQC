package com.tsqc.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.LoginConfig;
import com.tsqc.R;
import com.tsqc.WebUrlMethods;
import com.tsqc.adapters.OrderAdapter;
import com.tsqc.util.AppController;
import com.tsqc.util.ConnectivityReceiver;
import com.tsqc.util.MyOrder;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by someo on 07-03-2017.
 */

public class MyOrders extends Fragment implements SearchView.OnQueryTextListener,ConnectivityReceiver.ConnectivityReceiverListener {

//    String url="http://13.228.246.1/app/qc/myorders.php?id=";
//    String urls="http://13.228.246.1/app/freecount.php?id=";

    private ArrayList<MyOrder> feedItems;
    private OrderAdapter listAdapter;
    FloatingActionButton fab;
    String userid;
    ImageView click;
    ImageLoader imageLoader;
    private RecyclerView recyclerView;
    private SearchView mSearchView;
    CoordinatorLayout coordinatorLayout;
    SharedPreferences sharedpreferences;
    String myorders;
    String ordercount,oc;

    private int CALL_PHONE = 25;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myorder, container, false);

        com.nostra13.universalimageloader.core.ImageLoader imageLoaders = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoaders.init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageView relativeLayout=(ImageView)view.findViewById(R.id.activity_storehs);
        imageLoaders.displayImage("drawable://"+R.drawable.bg, relativeLayout);
       // click=(ImageView)view.findViewById(R.id.lefticon);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userid = sharedPreferences.getString(LoginConfig.store_email, "Not Available");
        ordercount = sharedPreferences.getString(LoginConfig.ordercount, "Not Available");
        fetchcount(WebUrlMethods.urls_MyOrders+userid);
        Log.i("userId_tsqc",userid);
        myorders = sharedPreferences.getString(LoginConfig.myorders, "Not Available");
        Log.e("My orders",myorders)  ;
        mSearchView = (SearchView) view.findViewById(R.id.search);

        final EditText searchEditText = (EditText)mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.QRCodeWhiteColor));
        searchEditText.setHintTextColor(getResources().getColor(R.color.QRCodeWhiteColor));



        coordinatorLayout = (CoordinatorLayout)view.findViewById(R.id.livecf);
        storagestate();


        imageLoader = AppController.getInstance().getImageLoader();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewfeed);
        feedItems = new ArrayList<MyOrder>();
        listAdapter = new OrderAdapter(getActivity(), feedItems,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(listAdapter);

        checkConnection();

        setupSearchView();

        return view;

    }




    public void fetchorders(String url)
    {
        // We first check for cached request
        Log.e("url_orders",url);
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
                    Log.e("Error", "Response: " + response.toString());


                    try {

                        sharedpreferences = getActivity().getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        //editor.putBoolean(LoginConfig.LOGGEDIN_SHARED_PREF, true);
                        editor.putString(LoginConfig.myorders, response.toString());
                        editor.commit();

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }




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

                MyOrder item = new MyOrder();
                item.setId(feedObj.getString("id"));
                item.setName(feedObj.getString("name"));
                item.setPhoto(feedObj.getString("photo"));
                item.setEmail(feedObj.getString("email"));
                item.setMobile(feedObj.getString("mobile"));
                item.setGender(feedObj.getString("gender"));
                item.setOrdertotal(feedObj.getString("OrderTotal"));
                item.setOrderno(feedObj.getString("OrderNo"));
                item.setOrderdate(feedObj.getString("OrderDate"));
                item.setAddr(feedObj.getString("addr"));
                item.setOid(feedObj.getString("oid"));
                item.setItems(feedObj.getString("items"));
                item.setDdate(feedObj.getString("pickup_date"));
                item.setDtime(feedObj.getString("pickup_time"));
                item.setAdvance(feedObj.getString("adavace"));
                item.setLeftover(feedObj.getString("leftover"));
                item.setOtype(feedObj.getString("otype"));
                item.setStatus(feedObj.getString("status"));
                item.setCid(feedObj.getString("cid"));


                feedItems.add(item);
                listAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void setupSearchView() {
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        listAdapter.getFilter().filter(newText);
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }


    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
            clear();
            fetchorders(WebUrlMethods.url_MyOrders+userid);
            Log.i("tsqc_userId",userid);

        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
            clear();

            try {
                JSONObject jsonObj=new JSONObject(myorders);
                parseCommentFeed(jsonObj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();

    }


    public void clear() {
        int size = this.feedItems.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.feedItems.remove(0);
            }

            listAdapter.notifyItemRangeRemoved(0,size);
        }
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void fetchcount(String url)
    {
        Log.e("url",url);
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);

        if (entry != null)
        {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
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
                    VolleyLog.d("Error2", "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error1", "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("Count");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject userData = (JSONObject) feedArray.get(i);

                oc=userData.getString("ordercount");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void salesalert()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title
        alertDialogBuilder.setTitle("You reached your limit");

        // set dialog message
        alertDialogBuilder
                .setMessage("You cant add orders more than "+ordercount+" in a month ! \n Do you want Upgrade?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.tailorssoftware.com/about/pro.html"));
                        startActivity(browserIntent);

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }



    public void storagestate()
    {
        if(isstorageAllowed()){
            //Toast.makeText(getActivity(),"You already have the permission", Toast.LENGTH_LONG).show();

            return;
        }

        requestStoragePermission();
    }


    private boolean isstorageAllowed() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;
        return false;
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE)){

        }

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},CALL_PHONE);
    }



    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CALL_PHONE){

            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Toast.makeText(getActivity(),"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(),"Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }


    }



}
