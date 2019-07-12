package com.tsqc.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.LoginConfig;
import com.tsqc.R;
import com.tsqc.WebUrlMethods;
import com.tsqc.adapters.PurchaseOrder;
import com.tsqc.util.AppController;
import com.tsqc.util.OrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by someo on 10-03-2017.
 */

public class PurchasedItems extends Fragment {

//    private static final String REGISTER_URL ="http://13.228.246.1/app/updatetotal.php" ;
//    private  final String url="http://13.228.246.1/app/getbyorder.php?id=";
    String orderid;
    FloatingActionButton pst;
    ListView listView;
    private ArrayList<OrderItem> feedItems;
    private PurchaseOrder listAdapter;

    double sum=0;
    String namess;
    TextView totalmoney,cart;
    EditText adv,pickupdate,picktime;
    Button next;
    ImageView pick,pickt;
    private int mYear, mMonth, mDay, mHour, mMinute;
    TextView items;
    String joined;
    String types;
    String tag="0";
    String storename,storeaddres,storeemail,storenumber,storetrip,storesign,cemail;
    ArrayList<String> list;

    public PurchasedItems()
    {

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchaseditems, container, false);

   SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        listView=(ListView)view.findViewById(R.id.mygroups);
        cart=(TextView) view.findViewById(R.id.textView29);
        next=(Button)view.findViewById(R.id.button);

         orderid = getArguments().getString("orderId");
         Log.e("OrderId",orderid);
         tag = getArguments().getString("tag");

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        storetrip = sharedPreferences.getString(LoginConfig.trip, "Not Available");
        storename = sharedPreferences.getString(LoginConfig.store_name, "Not Available");
        storenumber = sharedPreferences.getString(LoginConfig.store_number, "Not Available");
        storesign = sharedPreferences.getString(LoginConfig.sign, "Not Available");
        storeemail = sharedPreferences.getString(LoginConfig.store_email, "Not Available");
        storeaddres = sharedPreferences.getString(LoginConfig.address, "Not Available");
        cemail = sharedPreferences.getString(LoginConfig.cemail, "Not Available");



        if (tag.equals("3"))
        {
///             pst.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            cart.setVisibility(View.GONE);
        }
        if (tag.equals("4"))
        {
            //pst.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            cart.setVisibility(View.GONE);
            next.setVisibility(View.VISIBLE);
        }
        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
        ImageView relativeLayout=(ImageView)view.findViewById(R.id.psitems);
        imageLoader.displayImage("drawable://"+R.drawable.bg, relativeLayout);

        totalmoney=(TextView) view.findViewById(R.id.tot);


        adv=(EditText) view.findViewById(R.id.adv);
        pickupdate=(EditText) view.findViewById(R.id.pickupdate);
        picktime=(EditText) view.findViewById(R.id.picktime);
        pickt=(ImageView)view.findViewById(R.id.ptime);
        pick=(ImageView)view.findViewById(R.id.pick);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                pickupdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });



        pickt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                picktime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();


            }
        });

        adv.setText("0");
        if (tag.equals("0"))
        {
            next.setVisibility(View.GONE);
            cart.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

        }
        else if (tag.equals("1"))
        {
            next.setVisibility(View.VISIBLE);
            cart.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);

        }



        feedItems = new ArrayList<OrderItem>();
        listAdapter = new PurchaseOrder(getActivity(), feedItems,this,orderid);
        registerForContextMenu(listView);
        listView.setAdapter(listAdapter);




        fetchOrder(WebUrlMethods.url_PurchasedItems+orderid);
       // fetchOrder("https://www.obpoomail.com/app/getbyorder.php?id=34691424");



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
            JSONArray feedArray = response.getJSONArray("Order");
            list = new ArrayList<String>();

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                list.add(feedObj.getString("name"));

                OrderItem item = new OrderItem();

                item.setId(feedObj.getString("cid"));
                item.setCid(orderid);
                item.setName(feedObj.getString("name"));
                item.setQty(feedObj.getString("qty"));
                item.setPrice(feedObj.getString("price"));
                item.setSubtotal(feedObj.getString("subtotal"));
                item.setFabric_one(feedObj.getString("fabric_one"));
                item.setFabric_image1(feedObj.getString("fabric_image1"));
                item.setFab_1_meters(feedObj.getString("fab_1_meters"));
                item.setFab_1_price(feedObj.getString("fab_1_price"));
                item.setFabric_two(feedObj.getString("fabric_two"));
                item.setFabric_image2(feedObj.getString("fabric_image2"));
                item.setFab_2_meters(feedObj.getString("fab_2_meters"));
                item.setFab_2_price(feedObj.getString("fab_2_price"));
                item.setType(feedObj.getString("type"));
                item.setQc_check(feedObj.getString("qc_check"));

                String styles=feedObj.getString("styles");
                styles=styles.replaceAll("\\\\", "");


                item.setStyles(styles);
                item.setExtra_info(feedObj.getString("extra_info"));
                item.setStylus_image(feedObj.getString("stylus_image"));
                item.setCid(feedObj.getString("cid"));

                if(feedObj.getString("type").equals("2")){

                }else{
                    feedItems.add(item);
                }


                listAdapter.notifyDataSetChanged();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        sum = 0;
        Integer size = feedItems.size();
        for (int i = 0; i < size; ++i) {
            sum += Double.parseDouble(feedItems.get(i).getSubtotal());
        }
        Log.e("Total:",""+sum);


        joined = TextUtils.join(", ", list);
        Log.e("joined",joined);

    }


}
