package com.tsqc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Locale myLocale;
    String lang;
    int stopPosition;
    VideoView videoView;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView appname=(TextView)findViewById(R.id.tsname);
      //  Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/argon.otf");
       // appname.setTypeface(custom_font);



        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        ImageView relativeLayout=(ImageView)findViewById(R.id.mactivity_store);
   //     imageLoader.displayImage("drawable://"+R.drawable.bg, relativeLayout);


        SharedPreferences sharedPreferences = getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String userid = sharedPreferences.getString(LoginConfig.admin_email, getResources().getString(R.string.na));
        final String langs = sharedPreferences.getString(LoginConfig.lang, getResources().getString(R.string.na));

        if(langs.equals(getResources().getString(R.string.na))|| userid.equals(""))
        {
            lang="en";
        }
        else
        {
            lang=langs;
        }

        Log.e("language",lang);

        if(userid.equals(getResources().getString(R.string.na))|| userid.equals(""))
        {
            Login();
        }
        else
        {
            HomeRun();
        }

        /*videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Log.e("userid at main",userid);

            }
        });

        videoView.start();*/

    }

    public void HomeRun()
    {
         /*
        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(1*3000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),FlashScreen.class);
                    setLocale(lang);
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

    public void Login()
    {
         /*
        /****** Create Thread that will sleep for 5 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(1*3000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(),Login.class);
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

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }


}
