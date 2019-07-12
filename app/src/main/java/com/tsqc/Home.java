package com.tsqc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.android.volley.toolbox.NetworkImageView;
import com.tsqc.fragments.About;
import com.tsqc.fragments.MyOrders;
import com.tsqc.util.AppController;
import com.tsqc.util.ConnectivityReceiver;
import com.tsqc.util.MyApplication;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Home extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    TextView fullname;
    String admin_email,admin_pic,admin_id,admin_name,admin_type;
    ImageView click;

    NetworkImageView imageView;
    ImageLoader imageLoader;
    //private String picurl="  http://13.228.246.1/app/photos/";
    DrawerLayout coordinatorLayout;

    String ordercount,salescount,fabricount,store_email;
    //String urls="http://13.228.246.1/app/freecount.php?id=";
    String o;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Drawable d=getResources().getDrawable(R.drawable.gradient);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(d);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        // mTitle.setText(toolbar.getTitle());
     //   Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/argon.otf");
      //  mTitle.setTypeface(custom_font);



        coordinatorLayout = (DrawerLayout) findViewById(R.id
                .drawer);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        imageLoader = AppController.getInstance().getImageLoader();


        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        admin_email = sharedPreferences.getString(LoginConfig.admin_email,  getResources().getString(R.string.na));
        admin_id = sharedPreferences.getString(LoginConfig.admin_id,  getResources().getString(R.string.na));
        admin_pic = sharedPreferences.getString(LoginConfig.admin_pic,  getResources().getString(R.string.na));
        admin_name = sharedPreferences.getString(LoginConfig.admin_name,  getResources().getString(R.string.na));
        admin_type = sharedPreferences.getString(LoginConfig.type,  getResources().getString(R.string.na));
        store_email = sharedPreferences.getString(LoginConfig.store_email,  getResources().getString(R.string.na));
        ordercount = sharedPreferences.getString(LoginConfig.ordercount,  getResources().getString(R.string.na));
        String sn = sharedPreferences.getString(LoginConfig.store_name,  getResources().getString(R.string.na));
        o = sharedPreferences.getString(LoginConfig.obpoo,  getResources().getString(R.string.na));


        Log.e("username",admin_email+admin_id+admin_pic);
        checkConnection();
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View v1 = LayoutInflater.from(this).inflate(R.layout.header, navigationView);
        imageView=(NetworkImageView)v1.findViewById(R.id.imageView) ;
        String url=admin_pic.replaceAll(" ", "%20");
        imageView.setImageUrl(WebUrlMethods.PHOTOUPLOADURL+url,imageLoader);
        fullname = (TextView) v1.findViewById(R.id.hname);
        fullname.setText(sn);


        MyOrders myOrderts = new MyOrders();
        android.support.v4.app.FragmentTransaction myGrouptcs = getSupportFragmentManager().beginTransaction();
        myGrouptcs.replace(R.id.frame, myOrderts);
        myGrouptcs.addToBackStack(null);
        myGrouptcs.commit();

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    if (menuItem.isChecked()) {
                        menuItem.setChecked(false);
                    }
                    else {
                        menuItem.setChecked(true);
                    }

                    drawerLayout.closeDrawers();
                    switch (menuItem.getItemId())
                    {





                        case R.id.orders:

                            MyOrders myOrderts = new MyOrders();
                            android.support.v4.app.FragmentTransaction myGrouptcs = getSupportFragmentManager().beginTransaction();
                            myGrouptcs.replace(R.id.frame, myOrderts);
                            myGrouptcs.addToBackStack(null);
                            myGrouptcs.commit();

                            return true;


                        case R.id.delivery:

                            return true;

                        case R.id.about:

                            About myOrderss = new About();
                            android.support.v4.app.FragmentTransaction myGroupscss = getSupportFragmentManager().beginTransaction();
                            myGroupscss.replace(R.id.frame, myOrderss);
                            myGroupscss.addToBackStack(null);
                            myGroupscss.commit();

                            return true;

                        case R.id.logout:
                            logout();
                            return true;

                        default:
                            //  Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                            return true;
                    }
                }
            });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we don't want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we don't want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        //getProfile();


    }



    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.Logout));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.Yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(LoginConfig.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(LoginConfig.admin_pic, "");
                        editor.putString(LoginConfig.admin_id, "");
                        editor.putString(LoginConfig.admin_email, "");
                        editor.putString(LoginConfig.address, "");
                        editor.putString(LoginConfig.admin_name, "");
                        editor.putString(LoginConfig.store_email, "");
                        editor.putString(LoginConfig.store_name, "");
                        editor.putString(LoginConfig.store_number, "");
                        editor.putString(LoginConfig.store_pic, "");
                        editor.putString(LoginConfig.type, "");
                        editor.putString(LoginConfig.fac, "");
                        editor.putString(LoginConfig.trip, "");
                        editor.putString(LoginConfig.sign, "");


                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(Home.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.No),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

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
            message = getResources().getString(R.string.Internet);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            message = getResources().getString(R.string.Internet1);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 1) {
                finish();
            } else {
                if (getFragmentManager().getBackStackEntryCount() > 1) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

}
