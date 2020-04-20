package com.techuva.iot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.techuva.iot.R;
import com.techuva.iot.app.Constants;
import com.techuva.iot.utils.views.MApplication;

public class TokenExpireActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tv_session_expire;
    TextView tv_click_here;
    TextView tv_relogin;
    Context context;
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_expire);
        init();
        tv_click_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goto_logout_method();
            }
        });

    }

    private void init() {
        context = TokenExpireActivity.this;
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        tv_session_expire = findViewById(R.id.tv_session_expire);
        tv_click_here = findViewById(R.id.tv_click_here);
        tv_relogin = findViewById(R.id.tv_relogin);
        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit TechUva IoT", Toast.LENGTH_SHORT);
    }
    public void goto_logout_method() {
        MApplication.setString(context, Constants.UserID, "");
        MApplication.setString(context, Constants.UserIDSelected, "");
        MApplication.setString(context, Constants.DeviceID, "");
        MApplication.setString(context, Constants.DEVICE_IN_USE, "");
        MApplication.setString(context, Constants.AccessToken, "");
        MApplication.setString(context, Constants.DateToExpireToken, "");
        MApplication.setString(context, Constants.SecondsToExpireToken, "");
        MApplication.setBoolean(context, Constants.IsLoggedIn, false);
        MApplication.setBoolean(context, Constants.SingleAccount, false);
        MApplication.setBoolean(context, Constants.IsSessionExpired, true);
        //MApplication.clearAllPreference();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            // Do what ever you want
            exitToast.show();
            doubleBackToExitPressedOnce = false;
        } else{
            finishAffinity();
            finish();
            // Do exit app or back press here
            super.onBackPressed();
        }
    }

}
