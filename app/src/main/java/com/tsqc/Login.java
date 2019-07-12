package com.tsqc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tsqc.util.ConnectivityReceiver;
import com.tsqc.util.MyApplication;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

//    private static final String REGISTER_URL ="http://13.228.246.1/app/qlogin.php" ;
    EditText username,password;
    Button signin;
    SharedPreferences sharedpreferences;
    TextView forgotpassword;
    Button signup,lqrcode;
    ImageView showpassword,shownpassword;
    RelativeLayout coordinatorLayout;
    private IntentIntegrator qrScan;
    String s;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        qrScan = new IntentIntegrator(this);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        ImageView relativeLayout=(ImageView)findViewById(R.id.activity_stores);
       // imageLoader.displayImage("drawable://"+R.drawable.bg, relativeLayout);

        coordinatorLayout = (RelativeLayout) findViewById(R.id
                .activity_login);
        username=(EditText)findViewById(R.id.lusername);
        //username.setText("somchaichatry@gmail.com");
        ///username.setEnabled(false);


        password=(EditText)findViewById(R.id.lpassword);
        forgotpassword=(TextView)findViewById(R.id.lforgot);
        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(Login.this, ForgotPassword.class));

            }
        });

        lqrcode=(Button)findViewById(R.id.lqrcode);
        lqrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                qrScan.setPrompt("Scan");
                qrScan.setCameraId(0);
                qrScan.setBarcodeImageEnabled(false);
                qrScan.initiateScan();
            }
        });

        TextView appname=(TextView)findViewById(R.id.appnamess);
      //  Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/argon.otf");
       // appname.setTypeface(custom_font);


        signin=(Button)findViewById(R.id.lsigninn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user=username.getText().toString().trim();
                String pass=password.getText().toString().trim();


                if (user.equals("")&&pass.equals(""))
                {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, getResources().getString(R.string.Fields), Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
                /*else if(user.equals(""))
                {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Please enter username", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                }else if(pass.equals(""))
                {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Please enter password", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();

                }*/else
                {
                    login( user,pass);

                }

            }
        });

        showpassword=(ImageView)findViewById(R.id.lshowpassword);
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                showpassword.setVisibility(View.GONE);
                shownpassword.setVisibility(View.VISIBLE);

            }
        });
        shownpassword=(ImageView)findViewById(R.id.lshownpassword);
        shownpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                showpassword.setVisibility(View.VISIBLE);
                shownpassword.setVisibility(View.GONE);


            }
        });

        checkConnection();


    }

    private void login(final String user, final String pass) {

        final ProgressDialog loading = ProgressDialog.show(this, "Login", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebUrlMethods.REGISTER_URL_Login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        if(response.trim().equals("success"))
                        {

                            //Toast.makeText(Login.this,"success",Toast.LENGTH_LONG).show();
                            sharedpreferences = Login.this.getSharedPreferences(LoginConfig.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean(LoginConfig.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(LoginConfig.admin_email, user);
                            editor.commit();

                            startActivity(new Intent(Login.this, FlashScreen.class));

                        }
                        else if(response.equals("fail"))
                        {
                            Toast.makeText(Login.this,"Fail",Toast.LENGTH_LONG).show();
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, getResources().getString(R.string.mismatch), Snackbar.LENGTH_LONG);
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        if(error instanceof NoConnectionError) {
                            Toast.makeText(Login.this,error.toString(),Toast.LENGTH_LONG).show();

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", user);
                params.put("password", pass);

                Log.e("username",user+pass);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    //Getting the scan results
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(Login.this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {

                try {
                    JSONObject feedObj = new JSONObject(result.getContents());
                    String usernames=feedObj.getString("username");
                    String passwords=feedObj.getString("password");
                    username.setText(usernames);
                    password.setText(passwords);
                    login(usernames,passwords);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }





}
