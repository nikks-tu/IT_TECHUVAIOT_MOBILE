package com.techuva.iot.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.techuva.iot.R;
import com.techuva.iot.adapter.GridAdapterDashboard;
import com.techuva.iot.api_interface.CurrentAgriDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ChannelUnitMeasureDO;
import com.techuva.iot.model.CurrentDataAgriPostParameter;
import com.techuva.iot.model.CurrentDataMainObject;
import com.techuva.iot.model.CurrentDataResultObject;
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.utils.views.AppDatabase;
import com.techuva.iot.utils.views.MApplication;
import com.techuva.iot.utils.views.MPreferences;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Dashboard_Agri extends BaseActivity {

    LinearLayout ll_top_view, ll_main, ll_1, ll_thermostat, ll_single_phasing,  ll_low_pressure, ll_high_pressure;
    LinearLayout ll_2, ll_3, ll_compressor_olr, ll_primary_pump_olr;
    FrameLayout ll_bottom_view;
    //TextViewIcomoon tv_thermometer_icon;
    TextView tv_thermometer, tv_deviceName, tv_tempUnit, tv_thermostat, tv_single_phasing;
    TextView tv_deviceName1, tv_deviceId, tv_last_sync;
    Button btn_serverConnect, internetRetry;
    GridView grid_bottom;
    String deviceId, userId;
    Activity context;
    Intent intent;
    String inventoryId;
    String appVersion;
    String fontVersion;
    String FontUrl="";
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;
    private View decorView;
    RelativeLayout internetConnection, rl_serverError;
    ProgressDialog progressDialog;
    GridAdapterDashboard gridAdapterDashboard;
    LinearLayout ll_refresh;
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    MPreferences mPreferences;
    LinearLayout ll_history, ll_graph, ll_forwarning;
    int UserId;
    String authorityKey ="";
    String authorityKeyForRefresh ="";
    String grantType = "";
    String grantTypeRefresh = "";
    String refreshToken="";

 /*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.chiller_dashboard_v_one);

        Init_Views();
    }
*/
    private void setTypeface() {
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceBook = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Book.otf");
        Typeface faceMedium = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        tv_thermometer.setTypeface(faceBook);
        tv_deviceName.setTypeface(faceLight);
        tv_deviceId.setTypeface(faceLight);
        tv_last_sync.setTypeface(faceLight);
        tv_tempUnit.setTypeface(faceLight);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Init_Views() {
        context = Dashboard_Agri.this;
        mPreferences = new MPreferences(context);
        llContent.addView(inflater.inflate(R.layout.agri_dashboard_v_one, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        decorView = getWindow().getDecorView();
        grid_bottom = findViewById(R.id.grid_bottom);
        deviceId = MApplication.getString(context, Constants.DeviceID);
        userId = "1001";
        ll_refresh = findViewById(R.id.ll_refresh);
        ll_top_view = findViewById(R.id.ll_top_view);
        ll_bottom_view = findViewById(R.id.ll_bottom_view);
        ll_main = findViewById(R.id.ll_main);
        internetConnection =  findViewById(R.id.internetConnection);
        tv_thermometer = findViewById(R.id.tv_thermometer);
        tv_deviceName = findViewById(R.id.tv_deviceName);
        tv_tempUnit = findViewById(R.id.tv_tempUnit);
        tv_deviceName1 = findViewById(R.id.tv_deviceName1);
        tv_deviceId = findViewById(R.id.tv_deviceId);
        tv_last_sync = findViewById(R.id.tv_last_sync);
        btn_serverConnect = findViewById(R.id.btn_serverConnect);
        internetRetry = findViewById(R.id.internetRetry);
        rl_serverError = findViewById(R.id.rl_serverError);
        inventoryId = MApplication.getString(context, Constants.InventoryTypeId);
        setTypeface();
        ll_history = findViewById(R.id.ll_history);
        ll_graph = findViewById(R.id.ll_graph);
        ll_forwarning = findViewById(R.id.ll_forwarning);
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        authorityKeyForRefresh = Constants.AuthorizationKey;
        grantType = Constants.GrantType;
        grantTypeRefresh = Constants.GrantTypeRefresh;
        refreshToken = MApplication.getString(context, Constants.RefreshToken);
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        String productName = MApplication.getString(context, Constants.InventoryTypeName);
        ll_history.setEnabled(true);
        if(productName.equals("AGRICULTURE_MONITORING"))
        {
            ll_forwarning.setVisibility(View.VISIBLE);
        }
        else {
            ll_forwarning.setVisibility(View.GONE);
        }
        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit TechUva IoT", Toast.LENGTH_SHORT);
        if(MApplication.isNetConnected(context))
        {
            internetConnection.setVisibility(View.GONE);
            ll_main.setVisibility(View.VISIBLE);
            appVersion = mPreferences.getStringFromPreference(Constants.AppVersion, "1");
            fontVersion = mPreferences.getStringFromPreference(Constants.FontVersion, "0");
            showLoaderNew();
            //serviceCallforVersionInfo();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    serviceCall();
                }
            }, 1000);
        }
        else {
            internetConnection.setVisibility(View.VISIBLE);
            ll_main.setVisibility(View.GONE);
        }

        ll_history.setOnClickListener(v -> {
            ll_history.setEnabled(false);
            Intent intent = new Intent(context, HistoryDataAgriTableActivity.class);
            startActivity(intent);
            /*new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(context, HistoryDataAgriTableActivity.class);
                    startActivity(intent);
                }
            }, 1000);*/
        });


        ll_forwarning.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForewarninActivity.class);
            startActivity(intent);
        });


       /* grid_bottom.setOnItemClickListener((parent, view, position, id) -> {
            int selectedGridChannelNum = gridAdapterDashboard.getChannelNumber(position);
            Intent intent = new Intent(context, History_View_Activity.class);
            intent.putExtra("TabId", selectedGridChannelNum);
            startActivity(intent);
        });*/

         // ll_refresh.setBackgroundDrawable(myFunction());
         // ll_refresh.setBackgroundDrawable(getResources().getDrawable(R.drawable.hexagon, null));
         // Toast.makeText(getBaseContext(), "isLoogedIn"+MApplication.isLoggedIn,Toast.LENGTH_SHORT).show();
         // Typeface font = Typeface.createFromAsset(getApplicationContext().getResources().getAssets(), "fonts/ico_moon.ttf" );
         // tv_thermometer.setTypeface(font);

        ll_refresh.setOnClickListener(v -> {
            showLoaderNew();
            serviceCall();
        });

        btn_serverConnect.setOnClickListener(v -> serviceCall());

        internetRetry.setOnClickListener(v -> serviceCall());

        MApplication.setBoolean(context, Constants.IsHomeSelected, true);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Device ID", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        // Log and toast
                        //String msg = getResources().getString(R.string.app_name, token);
                        Log.d("Device ID", token);
                       // Toast.makeText(Dashboard.this, "IOT" +token, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void serviceCall(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentAgriDataInterface service = retrofit.create(CurrentAgriDataInterface.class);

        //Call<CurrentDataMainObject> call = service.getStringScalar(new CurrentDataPostParameter(deviceId,userId));
        Call<CurrentDataMainObject> call = service.getStringScalarWithSession(UserId, authorityKey, new CurrentDataAgriPostParameter(deviceId));
        call.enqueue(new Callback<CurrentDataMainObject>() {
            @Override
            public void onResponse(Call<CurrentDataMainObject> call, Response<CurrentDataMainObject> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                hideloader();
                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }

                 else if(response.body()!=null){
                    if (response.body().getInfo().getErrorCode()==0)
                    {
                        if(response.body().getResult()!=null)
                        {
                            try {
                                ll_main.setVisibility(View.VISIBLE);
                                rl_serverError.setVisibility(View.GONE);
                                dataResponse(response.body().getResult());
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        if (response.body().getInfo().getErrorMessage().equals(Constants.TokenExpireMsg)) {
                            Toast.makeText(context, "Token Expire, Plz Relogin", Toast.LENGTH_SHORT).show();
                            goto_login_activity();
                        }
                       else if (response.body().getInfo().getErrorMessage().equals(Constants.NoUserRole)) {
                            Toast.makeText(context, "No Role Assigned to User", Toast.LENGTH_SHORT).show();
                            goto_login_activity();
                        }
                    }

                }else {
                    //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                  //  Toast.makeText(getBaseContext(), "Data Error",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<CurrentDataMainObject> call, Throwable t) {
            hideloader();
              //  Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();
                ll_main.setVisibility(View.GONE);
                rl_serverError.setVisibility(View.VISIBLE);
            }

        });


        ll_graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DataGraphActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();
        mPreferences = new MPreferences(context);
        // Web request fro the personal info
     /*   appVersion = MPreferences.getAppVersion();
        fontVersion = MPreferences.getFontVersion();*/
        appVersion = mPreferences.getStringFromPreference(Constants.AppVersion, "1");
        fontVersion = mPreferences.getStringFromPreference(Constants.FontVersion, "0");
        ll_history.setEnabled(true);
       // serviceCallforVersionInfo();
        new Handler().postDelayed(() -> serviceCall(), 1000);


    }

    @Override
    protected void goto_manageThreshold() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initialize() {
        Init_Views();
      //  serviceCallforRefreshToken();
    }

    @Override
    public void goto_home() {
        if(MApplication.getBoolean(context, Constants.IsHomeSelected))
        {

        }
        else
        {
            intent = new Intent(context, Dashboard_Agri.class);
            startActivity(intent);
        }

    }


    @Override
    public void goto_devices() {
        intent = new Intent(context, UserDevicesListActivity.class);
        startActivity(intent);
    }

    @Override
    public void goto_devicesHisory() {
        // For other device history
        /*intent = new Intent(context, HistoryDataTableActivity.class);
        startActivity(intent);*/
        // For Agri product History
        intent = new Intent(context, HistoryDataAgriTableActivity.class);
        startActivity(intent);
    }

    @Override
    public void goto_forewarningDetails() {
    }

    @Override
    public void goto_aboutApp() {

    }

    @Override
    public void goto_TermsandCondition() {

    }

    @Override
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
        //MApplication.clearAllPreference();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }


    private void dataResponse(CurrentDataResultObject currentDataResultObject) throws java.text.ParseException {
        //Success message
         tv_deviceId.setText(currentDataResultObject.getInventoryID());
         //Constants.DEVICE_IN_USE= currentDataResultObject.getDeviceName();
        MApplication.setString(context, Constants.DEVICE_IN_USE, currentDataResultObject.getDeviceName());
        MApplication.setString(context, Constants.InventoryName, currentDataResultObject.getInventoryID());
        tv_deviceName1.setText(MApplication.getString(context, Constants.DEVICE_IN_USE));
        if(currentDataResultObject.getValues().size()>0)
        {
            String dateToParse = currentDataResultObject.getValues().get(0).getDate();
            //dateToParse.replaceAll("AMP", "");
            DateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            //:String inputDateStr = dateToParse;
            Date date = inputFormat.parse(dateToParse);
            String outputDateStr = outputFormat.format(date);
            tv_last_sync.setText(outputDateStr);

        }
        //tv_last_sync.setText(currentDataResultObject.getValues().get(0).getDate());
        List<CurrentDataValueObject> list = currentDataResultObject.getValues();
        generateDataList(list);
    }

    private void generateDataList(List<CurrentDataValueObject> values)
    {

         ArrayList<CurrentDataValueObject> arrayList = new ArrayList<>();
         AppDatabase.init(context);
         AppDatabase.clearUnitTable();
        if(values.size()>0)
        {

            for (int i =0; i<values.size() ; i++)

            {
                //tv_thermometer.setText(values.get(0).getValue());
                String s2 = values.get(0).getIcon();

                String s1 = s2.replaceAll("&#x", "");

                s1 = s1.replaceAll(";", "");

              /*  String icon = new String(Character.toChars(Integer.parseInt(
                        s1, 16)));*/
               // tv_thermometer_icon.setText(icon);

                arrayList.add(values.get(i));

                AppDatabase.init(context);
                ChannelUnitMeasureDO channelUnitMeasureDO = new ChannelUnitMeasureDO();
                channelUnitMeasureDO.setChannelNum(values.get(i).getChannelNumber());
                channelUnitMeasureDO.setUnit(values.get(i).getUnit_of_measure());
                AppDatabase.addUnittoTable(channelUnitMeasureDO);
            }

        }
        //Toast.makeText(context, ""+AppDatabase.getUnitlistCount(), Toast.LENGTH_SHORT).show();
        if(arrayList.size()>0)
        gridAdapterDashboard = new GridAdapterDashboard(Dashboard_Agri.this, R.layout.grid_item_dashboard, arrayList);
        grid_bottom.setAdapter(gridAdapterDashboard);

    }

    public void showLoaderNew() {
        runOnUiThread(new Dashboard_Agri.Runloader(getResources().getString(R.string.loading)));
    }

    class Runloader implements Runnable {
        private String strrMsg;

        public Runloader(String strMsg) {
            this.strrMsg = strMsg;
        }

        @SuppressWarnings("ResourceType")
        @Override
        public void run() {
            try {
                if (dialog == null)
                {
                    dialog = new Dialog(context,R.style.Theme_AppCompat_Light_DarkActionBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
                }
                dialog.show();

                ImageView imgeView = dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading = dialog
                        .findViewById(R.id.tvLoading);
                if (!strrMsg.equalsIgnoreCase(""))
                    tvLoading.setText(strrMsg);

                imgeView.setBackgroundResource(R.drawable.frame);

                animationDrawable = (AnimationDrawable) imgeView
                        .getBackground();
                imgeView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (animationDrawable != null)
                            animationDrawable.start();
                    }
                });
            } catch (Exception e)
            {

            }
        }
    }

    public void hideloader() {
        runOnUiThread(() -> {
            try
            {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }); }



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
    private void download () {

       try {
           File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
           //  File file = new File(Dir,"nahk.txt");
           File file = new File(Dir,  "/icomoon.ttf");
           if(file.exists()){
               file.delete();
           }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
        new DownloadFileFromURL().execute(FontUrl); // Downlod LINK !
    }


    // File download process from URL
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/storage/emulated/0/Download/icomoon.ttf");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            // Display the custom font after the File was downloaded !
          //  loadfont();
        }
    }


    public boolean isFontFileExist()
    {
        File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
        //  File file = new File(Dir,"nahk.txt");

        File file = new File(Dir,  "/icomoon.ttf");

        if(file.exists()){
            return true;
        }
        else {
            return false;
        }
    }

    private void goto_login_activity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }


}
