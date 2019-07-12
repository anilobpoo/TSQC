package com.tsqc;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.util.AppController;
import com.tsqc.util.ConnectivityReceiver;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class FlashScreen extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    String admin_name,sign,admin_email,store_name,admin_pic,store_email,admin_id,store_number,store_pic,type,url,address,fac,ta;
    //private String profileurl="http://13.228.246.1/app/profile.php?id=";
    //private String picurl="http://13.228.246.1/app/photos/";
    ProgressBar mprogressBar;
    String ordercount,salescount,fabriccount,obpoo,currency,website;

    TextView storename,adminname,adminmail,adminnumber,storeaddress,storetype,trip;
    NetworkImageView storepic,adminpic;
    ImageLoader imageLoader;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);
        imageLoader = AppController.getInstance().getImageLoader();


        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        ImageView relativeLayout=(ImageView)findViewById(R.id.factivity_storess);
        imageLoader.displayImage("drawable://"+R.drawable.bg, relativeLayout);

        mprogressBar = (ProgressBar) findViewById(R.id.circular_progress_bar);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        admin_email = sharedPreferences.getString(LoginConfig.admin_email, getResources().getString(R.string.na));
        admin_id = sharedPreferences.getString(LoginConfig.admin_id, getResources().getString(R.string.na));
        admin_pic = sharedPreferences.getString(LoginConfig.admin_pic, getResources().getString(R.string.na));
        ta = sharedPreferences.getString(LoginConfig.trip, getResources().getString(R.string.na));
        address = sharedPreferences.getString(LoginConfig.address, getResources().getString(R.string.na));
        store_name = sharedPreferences.getString(LoginConfig.store_name, getResources().getString(R.string.na));
        store_email = sharedPreferences.getString(LoginConfig.store_email, getResources().getString(R.string.na));
        store_number = sharedPreferences.getString(LoginConfig.store_number, getResources().getString(R.string.na));
        type = sharedPreferences.getString(LoginConfig.type, getResources().getString(R.string.na));
        admin_name = sharedPreferences.getString(LoginConfig.admin_name, getResources().getString(R.string.na));


        storename=(TextView)findViewById(R.id.storeName);
        adminname=(TextView)findViewById(R.id.adminName);
        adminmail=(TextView)findViewById(R.id.storeEmail);
        adminnumber=(TextView)findViewById(R.id.storeNumber);
        trip=(TextView)findViewById(R.id.triplink);
        storepic=(NetworkImageView)findViewById(R.id.imageView2) ;
        adminpic=(NetworkImageView)findViewById(R.id.imageView3) ;
        storeaddress=(TextView)findViewById(R.id.storeaddress);
        storetype=(TextView)findViewById(R.id.storetype);

        storename.setVisibility(View.INVISIBLE);
        adminname.setVisibility(View.INVISIBLE);
        adminmail.setVisibility(View.INVISIBLE);
        adminnumber.setVisibility(View.INVISIBLE);
        trip.setVisibility(View.INVISIBLE);
        storepic.setVisibility(View.INVISIBLE);
        adminpic.setVisibility(View.INVISIBLE);
        storeaddress.setVisibility(View.INVISIBLE);
        storetype.setVisibility(View.INVISIBLE);

        trip.setClickable(true);
        trip.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href="+""+"'"+ta+"'> TripAdvisor Link </a>";
        trip.setText(Html.fromHtml(text));



        checkConnection();
        progress();



    }


    public void HomeRun()
    {
         /*
        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(1*1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),Home.class);
                    startActivity(i);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();
    }


    public void progress()
    {
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(10000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                visible();
            }
        }, 1000);


    }

    private void visible() {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mprogressBar.setVisibility(View.GONE);

                storename.setVisibility(View.VISIBLE);
                adminname.setVisibility(View.VISIBLE);
                adminmail.setVisibility(View.VISIBLE);
                adminnumber.setVisibility(View.VISIBLE);
                trip.setVisibility(View.VISIBLE);
                storepic.setVisibility(View.VISIBLE);
                adminpic.setVisibility(View.VISIBLE);
                storeaddress.setVisibility(View.VISIBLE);
                storetype.setVisibility(View.VISIBLE);

                HomeRun();

            }
        });

    }

    private void getProfile()
    {
        url=WebUrlMethods.profileurl_Splashscreen+admin_email;
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
            JSONArray feedArray = response.getJSONArray("Profile");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject userData = (JSONObject) feedArray.get(i);

                admin_name=userData.getString("admin_name");
                admin_email=userData.getString("admin_email");
                store_name=userData.getString("store_name");
                admin_pic=userData.getString("admin_pic");
                store_email=userData.getString("store_email");
                admin_id=userData.getString("admin_id");
                store_number=userData.getString("store_number");
                store_pic=userData.getString("store_pic");
                type=userData.getString("type");
                address=userData.getString("address");
                fac=userData.getString("fac");
                ta=userData.getString("trip");
                sign=userData.getString("sign");
                obpoo=userData.getString("obpoo");
                currency=userData.getString("currency");
                website=userData.getString("website");

                salescount=userData.getString("salescount");
                ordercount=userData.getString("ordercount");
                fabriccount=userData.getString("fabriccount");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedpreferences = FlashScreen.this.getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(LoginConfig.LOGGEDIN_SHARED_PREF, true);
        editor.putString(LoginConfig.admin_name, admin_name);
        editor.putString(LoginConfig.admin_email, admin_email);
        editor.putString(LoginConfig.store_name, store_name);
        editor.putString(LoginConfig.admin_pic, admin_pic);

        editor.putString(LoginConfig.store_email, store_email);
        editor.putString(LoginConfig.admin_id, admin_id);
        editor.putString(LoginConfig.store_number, store_number);
        editor.putString(LoginConfig.store_pic, store_pic);
        editor.putString(LoginConfig.type, type);
        editor.putString(LoginConfig.address, address);
        editor.putString(LoginConfig.fac, fac);
        editor.putString(LoginConfig.trip, ta);
        editor.putString(LoginConfig.sign, sign);

        editor.putString(LoginConfig.salescount, salescount);
        editor.putString(LoginConfig.fabriccount, fabriccount);
        editor.putString(LoginConfig.ordercount, ordercount);
        editor.putString(LoginConfig.obpoo, obpoo);
        editor.putString(LoginConfig.currency, currency);
        editor.putString(LoginConfig.website, website);

        editor.commit();





        try {

            Log.e("url",WebUrlMethods.PHOTOUPLOADURL+admin_pic);
            Log.e("SP","saved!!!");

            String url=admin_pic.replaceAll(" ", "%20");
            String ur11=store_pic.replaceAll(" ", "%20");

            storepic.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+ur11,imageLoader);
            adminpic.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url,imageLoader);

            adminname.setText(admin_name.toUpperCase());
            storename.setText(store_name.toUpperCase());
            adminname.setText(admin_name);
            adminnumber.setText(store_number);
            adminmail.setText(store_email);
            storeaddress.setText(address);

            if (type.equals("2"))
            {
                storetype.setText(getResources().getString(R.string.salesman));

            }else if (type.equals("4"))
            {
                storetype.setText("QC GUY");

            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


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
            getProfile();
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;

            storepic.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+store_pic,imageLoader);
            adminpic.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+admin_pic,imageLoader);

            adminname.setText(admin_name.toUpperCase());
            storename.setText(store_name.toUpperCase());
            adminname.setText(admin_name);
            adminnumber.setText(store_number);
            adminmail.setText(store_email);
            storeaddress.setText(address);

            if (type.equals("2"))
            {
                storetype.setText("Logged in as Salesman");

            }else
            {
                storetype.setText("Logged in as Admin");

            }

        }


    }







    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
