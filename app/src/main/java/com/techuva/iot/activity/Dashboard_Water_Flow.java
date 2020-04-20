package com.techuva.iot.activity;

import android.annotation.SuppressLint;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.adapter.GridAdapterChannelWaterFlow;
import com.techuva.iot.adapter.GridAdapterWaterLevel;
import com.techuva.iot.adapter.GridAdapterWaterMon;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.api_interface.GetDataForGraph;
import com.techuva.iot.api_interface.VersionInfoDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.model.GraphDataPostParamter;
import com.techuva.iot.model.VersionInfoMainObject;
import com.techuva.iot.model.VersionInfoPostParameters;
import com.techuva.iot.model.WaterMonCurrentDataPostParameter;
import com.techuva.iot.response_model.WaterMonMainObject;
import com.techuva.iot.response_model.WaterMonResultObject;
import com.techuva.iot.response_model.WaterMonValueWithDateObject;
import com.techuva.iot.utils.views.MApplication;
import com.techuva.iot.utils.views.MPreferences;
import com.techuva.iot.utils.views.MyFirebaseMessagingService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Dashboard_Water_Flow extends BaseActivity implements MyFirebaseMessagingService.EventListener {

    LinearLayout ll_top_view, ll_main;
    LinearLayout ll_manjeera_water, ll_ground_water;
    LinearLayout ll_manjeera_txt, ll_ground_txt;
    FrameLayout ll_bottom_view;
    LineChart chart_dailyData;
    FrameLayout ll_first_layout;
    int totalRecords =0;
    String measurementUnit = "";
    Description description;
    XAxis xAxis;
    TextView tv_thermometer, tv_deviceName, tv_tempUnit, tv_consumed_units_txt, tv_consumed_units, tv_notification_count;
    TextView tv_deviceName1, tv_deviceId, tv_last_sync, tv_history, tv_provisioned, tv_last_sync_txt, tv_unit_measure;
    Button btn_serverConnect, internetRetry;
    GridView grid_bottom;
    GridView grid_water_level;
    GridView grid_channel_total_value;
    String deviceId, userId;
    Activity context;
    Intent intent;
    String inventoryId;
    String appVersion;
    String fontVersion;
    String FontUrl="";
    Toast exitToast;
    String inventoryName="";
    Boolean doubleBackToExitPressedOnce = true;
    private View decorView;
    RelativeLayout internetConnection, rl_serverError;
    ProgressDialog progressDialog;
    GridAdapterWaterMon gridAdapterDashboard;
    GridAdapterChannelWaterFlow gridAdapterChannelValues;
    GridAdapterWaterLevel gridAdapterWaterLevel;
    LinearLayout ll_refresh;
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    MPreferences mPreferences;
    LinearLayout ll_history, ll_graph;
    int UserId;
    String authorityKey ="";
    String authorityKeyForRefresh ="";
    String grantType = "";
    String grantTypeRefresh = "";
    String refreshToken="";
    String date= "";
    String fromDate="";
    String toDate="";
    String channelNumber = "";
    ArrayList<CurrentDataValueObject> currentDataValueList;
    ArrayList<CurrentDataValueObject> currentWaterLevelList;
    String provisionDate;
    TextView tv_history_btn;
    ArrayList<Entry> yValues = new ArrayList<>();
    int pageNumber = 1;
    int Num = 0;

    private void setTypeface() {
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceBook = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Book.otf");
        Typeface faceMedium = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        tv_thermometer.setTypeface(faceBook);
        tv_deviceName.setTypeface(faceLight);
        tv_deviceName1.setTypeface(faceLight);
        tv_deviceId.setTypeface(faceLight);
        tv_last_sync.setTypeface(faceLight);
        tv_tempUnit.setTypeface(faceLight);
        tv_history.setTypeface(faceLight);
        tv_provisioned.setTypeface(faceLight);
        tv_last_sync_txt.setTypeface(faceLight);
        tv_unit_measure.setTypeface(faceLight);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Init_Views() {
        context = Dashboard_Water_Flow.this;
        mPreferences = new MPreferences(context);
        llContent.addView(inflater.inflate(R.layout.waterflow_dashboard_v_one, null),
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        decorView = getWindow().getDecorView();
        grid_bottom = findViewById(R.id.grid_bottom);
        grid_water_level = findViewById(R.id.grid_water_level);
        grid_channel_total_value = findViewById(R.id.grid_channel_total_value);
        deviceId = MApplication.getString(context, Constants.DeviceID);
        userId = "1001";
        Toolbar toolbar = findViewById(R.id.toolbar_drawer);
        tv_history_btn  = toolbar.findViewById(R.id.tv_history_btn);
        tv_history_btn.setVisibility(View.VISIBLE);
        ll_refresh = findViewById(R.id.ll_refresh);
        ll_top_view = findViewById(R.id.ll_top_view);
        ll_bottom_view = findViewById(R.id.ll_bottom_view);
        ll_first_layout = findViewById(R.id.ll_first_layout);
        chart_dailyData = findViewById(R.id.chart_dailyData);
        ll_main = findViewById(R.id.ll_main);
        ll_manjeera_water = findViewById(R.id.ll_manjeera_water);
        ll_ground_water = findViewById(R.id.ll_ground_water);
        ll_manjeera_txt = findViewById(R.id.ll_manjeera_txt);
        ll_ground_txt = findViewById(R.id.ll_ground_txt);
        internetConnection =  findViewById(R.id.internetConnection);
        tv_thermometer = findViewById(R.id.tv_thermometer);
        tv_deviceName = findViewById(R.id.tv_deviceName);
        tv_tempUnit = findViewById(R.id.tv_tempUnit);
        tv_deviceName1 = findViewById(R.id.tv_deviceName1);
        tv_deviceId = findViewById(R.id.tv_deviceId);
        tv_last_sync = findViewById(R.id.tv_last_sync);
        tv_consumed_units_txt = findViewById(R.id.tv_consumed_units_txt);
        tv_consumed_units = findViewById(R.id.tv_consumed_units);
        tv_history = findViewById(R.id.tv_history);
        tv_notification_count = findViewById(R.id.tv_notification_count);
        tv_provisioned = findViewById(R.id.tv_provisioned);
        tv_last_sync_txt = findViewById(R.id.tv_last_sync_txt);
        tv_unit_measure = findViewById(R.id.tv_unit_measure);
        btn_serverConnect = findViewById(R.id.btn_serverConnect);
        internetRetry = findViewById(R.id.internetRetry);
        rl_serverError = findViewById(R.id.rl_serverError);
        inventoryId = MApplication.getString(context, Constants.InventoryTypeId);
        setTypeface();
        ll_history = findViewById(R.id.ll_history);
        ll_graph = findViewById(R.id.ll_graph);
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        authorityKeyForRefresh = Constants.AuthorizationKey;
        grantType = Constants.GrantType;
        grantTypeRefresh = Constants.GrantTypeRefresh;
        refreshToken = MApplication.getString(context, Constants.RefreshToken);
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        inventoryName = MApplication.getString(context, Constants.InventoryName);
        String productName = MApplication.getString(context, Constants.InventoryTypeName);
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(calendar.getTime());
        fromDate = sdf.format(calendar.getTime());
        xAxis = chart_dailyData.getXAxis();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-9);
        toDate = sdf.format(cal.getTime());
        ll_history.setEnabled(true);
        ll_graph.setEnabled(true);
        ll_history.setVisibility(View.GONE);
        ll_graph.setVisibility(View.GONE);
        currentDataValueList = new ArrayList<>();
        currentWaterLevelList = new ArrayList<>();
        chart_dailyData.setNoDataText("");
        channelNumber = MApplication.getString(context, Constants.SelectedChannelWaterLevel);
        //showLoaderNew();
        yValues.clear();
        chart_dailyData.invalidate();
        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit TechUva IoT", Toast.LENGTH_SHORT);
        if(MApplication.isNetConnected(context))
        {
            internetConnection.setVisibility(View.GONE);
            ll_main.setVisibility(View.VISIBLE);
            appVersion = mPreferences.getStringFromPreference(Constants.AppVersion, "1");
            fontVersion = mPreferences.getStringFromPreference(Constants.FontVersion, "0");
            showLoaderNew();
            //serviceCallforVersionInfo();
            new Handler().postDelayed(() -> {
                serviceCall();
                serviceCallforCurrentDataWaterMon();
            }, 1000);
        }
        else {
            internetConnection.setVisibility(View.VISIBLE);
            ll_main.setVisibility(View.GONE);
        }


        ll_history.setOnClickListener(v -> {
            ll_history.setEnabled(false);
            Intent intent = new Intent(context, HistoryDataTableActivity.class);
            startActivity(intent);

        });

        tv_history_btn.setOnClickListener(v -> {
            ll_history.setEnabled(false);
            Intent intent = new Intent(context, HistoryDataTableActivity.class);
            startActivity(intent);

        });

        grid_bottom.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDate = gridAdapterDashboard.getSelectedDate(position);
            String day = gridAdapterDashboard.getDay(position);
            String month = gridAdapterDashboard.getMonth(position);
            String year = gridAdapterDashboard.getYear(position);
            Intent intent = new Intent(context, WaterMonValueDetailsActivity.class);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("Day", day);
            intent.putExtra("Month", month);
            intent.putExtra("Year", year);
            startActivity(intent);
        });

        grid_water_level.setOnItemClickListener((parent, view, position, id) -> {
            int channelNum = currentWaterLevelList.get(position).getChannelNumber();
            MApplication.setString(context, Constants.SelectedChannelWaterLevel, String.valueOf(channelNum));
            Intent intent = new Intent(context, DataGraphForWaterLevelActivity.class);
            startActivity(intent);

        });

        ll_refresh.setOnClickListener(v -> {
            showLoaderNew();
            String notificationCount = MApplication.getString(context, Constants.NotificationCount);
            //Toast.makeText(context, notificationCount, Toast.LENGTH_SHORT).show();
            showLoaderNew();
            tv_notification_count.setText(notificationCount);
            serviceCall();
            serviceCallforCurrentDataWaterMon();
        });

        btn_serverConnect.setOnClickListener(v ->
        {
            showLoaderNew();
            serviceCall();
            serviceCallforCurrentDataWaterMon();
        });

        internetRetry.setOnClickListener(v ->{
            showLoaderNew();
            serviceCall();
            serviceCallforCurrentDataWaterMon();
        });

        MApplication.setBoolean(context, Constants.IsHomeSelected, true);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("Device ID", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    //serviceCallForSavingToken(token);
                    // Log and toast
                    //String msg = getResources().getString(R.string.app_name, token);
                    Log.d("Device ID", token);
                   // Toast.makeText(Dashboard.this, "IOT" +token, Toast.LENGTH_SHORT).show();
                });

    }
    private void serviceCall(){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(80, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                //.client(okHttpClient)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface .class);
        Call<WaterMonMainObject> call = service.getWaterMonValues(UserId, authorityKey, new WaterMonCurrentDataPostParameter(deviceId, toDate, fromDate));
        call.enqueue(new Callback<WaterMonMainObject>() {
            @Override
            public void onResponse(Call<WaterMonMainObject> call, Response<WaterMonMainObject> response) {
              /*  Num = Num+1;
               Toast.makeText(context, "serviceCall "+Num, Toast.LENGTH_SHORT).show();*/
                if (response.code()==401)
                {
                    hideloader();
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
                    else if (response.body().getInfo().getErrorCode()==1)
                    {
                        hideloader();
                        ll_main.setVisibility(View.GONE);
                        rl_serverError.setVisibility(View.VISIBLE);
                    }
                    else {
                        hideloader();
                        if (response.body().getInfo().getErrorMessage().equals(Constants.TokenExpireMsg)) {
                            Toast.makeText(context, "Token Expire, Please Re-login", Toast.LENGTH_SHORT).show();
                            goto_login_activity();
                        }
                        else if (response.body().getInfo().getErrorMessage().equals(Constants.NoUserRole)) {
                            Toast.makeText(context, "No Role Assigned to User", Toast.LENGTH_SHORT).show();
                            goto_login_activity();
                        }
                    }

                }else {
                    //Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<WaterMonMainObject> call, Throwable t) {
                hideloader();
                // Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();
                ll_main.setVisibility(View.GONE);
                rl_serverError.setVisibility(View.VISIBLE);
            }

        });

    }


    private void serviceCallforVersionInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VersionInfoDataInterface service = retrofit.create(VersionInfoDataInterface.class);
        Call<VersionInfoMainObject> call = service.getStringScalar(new VersionInfoPostParameters(inventoryId, appVersion, fontVersion));
        call.enqueue(new Callback<VersionInfoMainObject>() {
            @Override
            public void onResponse(Call<VersionInfoMainObject> call, Response<VersionInfoMainObject> response) {
                //hideloader();
               /* Num = Num+1;
                Toast.makeText(context, "serviceCallforVersionInfo "+Num, Toast.LENGTH_SHORT).show();*/
                if(response.body()!=null){

                    if(response.body().getInfo().getErrorCode()==0)
                    {
                       if(!response.body().getResult().getFontVersionStatus())
                        {
                            FontUrl = response.body().getResult().getFontLink();
                            mPreferences.saveString(Constants.AppVersion, String.valueOf(response.body().getResult().getCurrentAppVersion()));
                            mPreferences.saveString(Constants.FontVersion, String.valueOf(response.body().getResult().getVersionNumber()));
                            //Toast.makeText(context, "Font Downloading.." + "DeviceId" +deviceId, Toast.LENGTH_SHORT).show();
                            download();
                        }
                        else
                       {
                           if(isFontFileExist())
                           {
                               Log.e("", "");
                           }
                           else {
                               recreate();
                               fontVersion ="0";
                               serviceCallforVersionInfo();
                           }
                       }
                    }
                    else
                    {
                        //Log.println(1, "Empty", "Else");
                        }
                }else {
                    hideloader();
                }
            }
            @Override
            public void onFailure(Call<VersionInfoMainObject> call, Throwable t) {
                hideloader();
                ll_main.setVisibility(View.GONE);
                rl_serverError.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String notificationCount = MApplication.getString(context, Constants.NotificationCount);
        //Toast.makeText(context, notificationCount, Toast.LENGTH_SHORT).show();
        tv_notification_count.setText(notificationCount);
        mPreferences = new MPreferences(context);
        // Web request fro the personal info
        /*   appVersion = MPreferences.getAppVersion();
        fontVersion = MPreferences.getFontVersion();*/
        appVersion = mPreferences.getStringFromPreference(Constants.AppVersion, "1");
        fontVersion = mPreferences.getStringFromPreference(Constants.FontVersion, "0");
        ll_history.setEnabled(true);
    }

    @Override
    protected void goto_manageThreshold() {
        intent = new Intent(context, ManageThresholdActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initialize() {
        Init_Views();
    }

    @Override
    public void goto_home() {
        if(MApplication.getBoolean(context, Constants.IsHomeSelected))
        {
            Log.e("", "");
        }
        else
        {
            intent = new Intent(context, Dashboard_Water_Flow.class);
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
        intent = new Intent(context, HistoryDataTableActivity.class);
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
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }


    private void dataResponse(WaterMonResultObject currentDataResultObject) throws java.text.ParseException {
        hideloader();
         tv_deviceId.setText(currentDataResultObject.getInventoryID());
         //Constants.DEVICE_IN_USE= currentDataResultObject.getDeviceName();
        MApplication.setString(context, Constants.DEVICE_IN_USE, currentDataResultObject.getDeviceName());
        MApplication.setString(context, Constants.InventoryName, currentDataResultObject.getInventoryID());
        tv_deviceName1.setText(MApplication.getString(context, Constants.DEVICE_IN_USE));
        ArrayList<WaterMonValueWithDateObject> list = currentDataResultObject.getValuesByDate();
        if(list.size()>0)
        {
           if(list.get(0).getValues().size()>0)
           {
               gridAdapterDashboard = new GridAdapterWaterMon(Dashboard_Water_Flow.this, R.layout.grid_item_water_mon, list);
               grid_bottom.setAdapter(gridAdapterDashboard);
           }
        }
    }


    @Override
    public void onEvent(Boolean data) {
        String notificationCount = MApplication.getString(context, Constants.NotificationCount);
        //Toast.makeText(context, notificationCount, Toast.LENGTH_SHORT).show();
        tv_notification_count.setText(notificationCount);
    }

    @Override
    public void onDelete(Boolean data) {

    }

    public void showLoaderNew() {
        runOnUiThread(new Dashboard_Water_Flow.Runloader(getResources().getString(R.string.loading)));
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
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
                }
                assert dialog != null;
                dialog.show();
                ImageView imgeView = dialog.findViewById(R.id.imgeView);
                TextView tvLoading = dialog.findViewById(R.id.tvLoading);
                if (!strrMsg.equalsIgnoreCase(""))
                    tvLoading.setText(strrMsg);
                imgeView.setBackgroundResource(R.drawable.frame);
                animationDrawable = (AnimationDrawable) imgeView
                        .getBackground();
                imgeView.post(() -> {
                    if (animationDrawable != null)
                        animationDrawable.start();
                });
            } catch (Exception e)
            {
                Log.e("", "");
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
           File file = new File(Dir,  "/icomoon.ttf");
           if(file.exists()){
               file.delete();
           }
       }
       catch (Exception e)
       {
           e.printStackTrace();
       }
        new DownloadFileFromURL().execute(FontUrl); // Download LINK !
    }


    // File download process from URL
    @SuppressLint("StaticFieldLeak")
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

    private void serviceCallforCurrentDataWaterMon() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface.class);
        Call<JsonElement> call = service.getCurrentDataForWaterMon(UserId, authorityKey, new CurrentDataPostParameter(deviceId));
        call.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //hideloader();
              /*  Num = Num+1;
                Toast.makeText(context, "serviceCallforCurrentDataWaterMon"+Num, Toast.LENGTH_SHORT).show();*/
                if (response.code() == 401) {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                } else if (response.body() != null) {

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    if (!jsonObject.isJsonNull()) {
                        JsonObject jObj = jsonObject.getAsJsonObject("info");
                        if (jObj.get("ErrorCode").getAsInt() == 0) {
                            JsonObject resultObject = jsonObject.getAsJsonObject("result");
                            String lastSyncDateTime = resultObject.get("date").getAsString();
                            provisionDate = resultObject.get("ProvisionedOn").getAsString();
                            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                            DateFormat onlyDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            //:String inputDateStr = dateToParse;
                            Date date = null;
                            Date provisionedDate = null;
                            try {
                                date = inputFormat.parse(lastSyncDateTime);
                                provisionedDate = inputFormat.parse(provisionDate);
                                if(!provisionDate.equals("--"))
                                {
                                    provisionedDate = inputFormat.parse(provisionDate);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            lastSyncDateTime = outputFormat.format(date);
                            tv_provisioned.setText("Provisioned On: "+onlyDateFormat.format(provisionedDate));
                            tv_last_sync.setText(lastSyncDateTime);
                            JsonArray valueArray = resultObject.getAsJsonArray("Values");
                            if (valueArray.size()>0)
                            {
                                currentDataValueList = new ArrayList<>();
                                currentWaterLevelList = new ArrayList<>();
                                for(int i =0; i<valueArray.size(); i++)
                                {
                                     JsonObject jobject = valueArray.get(i).getAsJsonObject();
                                    if(!jobject.get("ChannelNumber").isJsonNull())
                                    {
                                        if(jobject.get("channeldescription").getAsString().equals("FLOW"))
                                        {
                                            CurrentDataValueObject object = new CurrentDataValueObject();
                                            object.setChannelNumber(jobject.get("ChannelNumber").getAsInt());
                                            object.setLabel(jobject.get("Label").getAsString());
                                            object.setValue(jobject.get("Value").getAsString());
                                            object.setIcon(jobject.get("icon").getAsString());
                                            object.setDate(jobject.get("date").getAsString());
                                            object.setTime(jobject.get("time").getAsString());
                                            object.setChannelDescription(jobject.get("channeldescription").getAsString());
                                            object.setChannelDefaultValue(jobject.get("channeldefaultvalue").getAsString());
                                            object.setDisplayOrder(jobject.get("display_order").getAsInt());
                                            object.setUnit_of_measure(jobject.get("unit_of_measure").getAsString());
                                            if(!jobject.get("ProvisionedOn").isJsonNull())
                                            {
                                                object.setProvisionedOn(jobject.get("ProvisionedOn").getAsString());
                                            }
                                            else
                                            {
                                                object.setProvisionedOn("--");
                                            }
                                            currentDataValueList.add(object);
                                        }
                                       else if(jobject.get("channeldescription").getAsString().equals("LEVEL"))
                                        {
                                            CurrentDataValueObject object = new CurrentDataValueObject();
                                            object.setChannelNumber(jobject.get("ChannelNumber").getAsInt());
                                            object.setLabel(jobject.get("Label").getAsString());
                                            object.setValue(jobject.get("Value").getAsString());
                                            object.setIcon(jobject.get("icon").getAsString());
                                            object.setDate(jobject.get("date").getAsString());
                                            object.setTime(jobject.get("time").getAsString());
                                            object.setChannelDescription(jobject.get("channeldescription").getAsString());
                                            object.setChannelDefaultValue(jobject.get("channeldefaultvalue").getAsString());
                                            object.setDisplayOrder(jobject.get("display_order").getAsInt());
                                            object.setUnit_of_measure(jobject.get("unit_of_measure").getAsString());
                                            tv_unit_measure.setText(jobject.get("unit_of_measure").getAsString());
                                            if(!jobject.get("ProvisionedOn").isJsonNull())
                                            {
                                                object.setProvisionedOn(jobject.get("ProvisionedOn").getAsString());
                                            }
                                            else
                                            {
                                                object.setProvisionedOn("--");
                                            }
                                            currentWaterLevelList.add(object);
                                        }
                                    }
                                }
                                if(currentDataValueList.size()>0)
                                {
                                    String dateToParse = currentDataValueList.get(0).getDate();
                                    provisionDate = currentDataValueList.get(0).getProvisionedOn();
                                    if(provisionDate.equals("--"))
                                    {
                                        provisionDate = currentWaterLevelList.get(0).getProvisionedOn();
                                    }
                                    gridAdapterChannelValues = new GridAdapterChannelWaterFlow(Dashboard_Water_Flow.this, R.layout.grid_item_channel_water, currentDataValueList);
                                    grid_channel_total_value.setAdapter(gridAdapterChannelValues);
                                }
                                if(currentWaterLevelList.size()>0)
                                {
                                    if(currentWaterLevelList.size()==1 && currentDataValueList.size()==0)
                                    {
                                        channelNumber = String.valueOf(currentWaterLevelList.get(0).getChannelNumber());
                                        ll_bottom_view.setVisibility(View.VISIBLE);
                                        grid_bottom.setVisibility(View.GONE);
                                        chart_dailyData.setVisibility(View.VISIBLE);
                                        ll_first_layout.setVisibility(View.VISIBLE);
                                        yValues.clear();
                                        chart_dailyData.invalidate();
                                        showLoaderNew();
                                        serviceCallforChannelData();
                                    }
                                    else {
                                        chart_dailyData.setVisibility(View.GONE);
                                        ll_first_layout.setVisibility(View.GONE);
                                    }
                                    gridAdapterWaterLevel = new GridAdapterWaterLevel(Dashboard_Water_Flow.this, R.layout.grid_item_water_level, currentWaterLevelList);
                                    grid_water_level.setAdapter(gridAdapterWaterLevel);
                                }
                            }

                        } else if (jObj.get("ErrorCode").getAsInt() == 1) {

                        } else {

                        }

                    } else {}

                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
            }
        });
    }


    private void serviceCallforChannelData() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetDataForGraph service = retrofit.create(GetDataForGraph.class);

        //Call<JsonElement> call = service.getStringScalar(new GraphDataPostParamter(fromDate, inventoryName, channelNumber));
        Call<JsonElement> call = service.getStringScalarWithSession(UserId, authorityKey, new GraphDataPostParamter(fromDate, inventoryName, channelNumber));
        call.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )

                /*Num = Num+1;
                Toast.makeText(context, "serviceCallforChannelData "+Num, Toast.LENGTH_SHORT).show();*/
                hideloader();
                if (response.body() != null) {

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    if(!jsonObject.isJsonNull())
                    {
                        JsonObject jObj = jsonObject.getAsJsonObject("info");
                        totalRecords =jObj.get("TotalRecords").getAsInt();
                        if(jObj.get("ErrorCode").getAsString().equals("1"))
                        {
                            if(jObj.get("ErrorMessage").getAsString().equals(Constants.TokenExpireMsg)) {
                                goto_login_activity();
                            }
                            else
                            {
                                yValues.clear();
                                chart_dailyData.invalidate();
                                chart_dailyData.clear();
                                chart_dailyData.setNoDataText(getResources().getString(R.string.nodata));
                                chart_dailyData.setNoDataTextColor(getResources().getColor(R.color.white));
                            }
                        }
                        else if(jObj.get("ErrorCode").getAsString().equals("0")){
                            // Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show();
                            /* ll_data_not_found.setVisibility(View.GONE);
                            ll_main.setVisibility(View.VISIBLE);*/
                            JsonObject resultObj =null;
                            JsonArray jsonArray = null;
                            resultObj =  jsonObject.getAsJsonObject("result");
                            String graphName = "graphData_"+channelNumber;
                            jsonArray = resultObj.getAsJsonArray(graphName);
                            List<Float> listValue = new ArrayList<>();
                            List<Float> listValueTime = new ArrayList<>();
                            if(jsonArray != null)
                            {
                                for (int i=0; i<jsonArray.size(); i++)
                                {
                                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                                    //listValue.add(Float.valueOf(jsonObject1.get(channelNumber).toString()));
                                    String s = String.valueOf(jsonObject1.get("DATA"));
                                    //listValue.add(Float.valueOf(s));
                                    //listValueTime.add(String.valueOf(jsonObject1.get("RECEIVED_TIME")));
                                    s= s.replace("\"", "");
                                    if(!s.equals("null"))
                                    {
                                        listValue.add(Float.valueOf(s));
                                    }
                                }
                                for (int i=0; i<jsonArray.size(); i++)
                                {
                                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                                    String s = String.valueOf(jsonObject1.get("TIME"));
                                    s = s.replace("\"", "");
                                    String originalString = s;
                                    Date date = null;
                                    try {
                                        date = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a").parse(s);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if(date!=null)
                                    {
                                        String newString = new SimpleDateFormat("HH:mm").format(date); //9:00
                                        String s2 = newString.replace("\"", "");
                                        s2 = s2.replace(":", ".");
                                        float f2 = Float.parseFloat(s2);
                                        listValueTime.add(f2);
                                    }
                                }
                            }
                            description = new Description();
                            description.setText("Time");
                            description.setTextColor(getResources().getColor(R.color.white));
                            chart_dailyData.setDescription(description);
                            chart_dailyData.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                            chart_dailyData.getXAxis().setTextSize(8f);
                            chart_dailyData.getAxisRight().setEnabled(false);
                            chart_dailyData.setScaleEnabled(true);
                            chart_dailyData.setDrawGridBackground(false);
                            chart_dailyData.setDoubleTapToZoomEnabled(false);
                            chart_dailyData.getAxisLeft().setDrawAxisLine(true);
                            chart_dailyData.getAxisLeft().setAxisLineColor(getResources().getColor(R.color.white));
                            chart_dailyData.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            chart_dailyData.getAxisLeft().setDrawGridLines(false);
                            chart_dailyData.getXAxis().setDrawGridLines(false);
                            chart_dailyData.getXAxis().setAxisLineColor(getResources().getColor(R.color.white));
                            chart_dailyData.getXAxis().setTextColor(getResources().getColor(R.color.white));
                            chart_dailyData.getLegend().setTextColor(getResources().getColor(R.color.white));
                            chart_dailyData.setTouchEnabled(true);
                            xAxis.setDrawLabels(true);
                            xAxis.setAxisLineColor(getResources().getColor(R.color.white));
                            xAxis.setAvoidFirstLastClipping(true);
                            xAxis.setEnabled(true);
                            chart_dailyData.getAxisLeft().setZeroLineColor(R.color.white);
                            chart_dailyData.getLegend().setEnabled(false);
                            xAxis.setAxisMinValue(0f);
                            //xAxis.setSpaceMin(5f);
                            //xAxis.setLabelCount(listValueTime.size(), true);
                            chart_dailyData.setVisibleXRangeMaximum(10);
                            chart_dailyData.moveViewToX(10);
                            // let the chart know it's data has changed
                            //chart_dailyData.notifyDataSetChanged();
                            ///chart_dailyData.setVisibleXRangeMaximum(chart_dailyData.getVisibleXRange());
                            chart_dailyData.invalidate();
                            if(pageNumber==1)
                            {
                                yValues = new ArrayList<>();
                            }
                            pageNumber++;
                            /*  Collections.sort(listValue, new Comparator<Float>() {
                                @Override
                                public int compare(Float o1, Float o2) {
                                    return Float.compare(o1,o2);
                                }
                            });*/
                            for (int i=0; i<listValueTime.size(); i++)
                            {
                                yValues.add(new Entry(listValueTime.get(i), listValue.get(i)));
                            }
                            Collections.sort(yValues, (entry, t1) -> Float.compare(entry.getX(),t1.getX()));
                            ArrayList<ILineDataSet> dataSet = new ArrayList<>();
                            LineDataSet set1;
                            set1 = new LineDataSet(yValues, "");
                            set1.setFillAlpha(120);
                            set1.setFillColor(getResources().getColor(R.color.white));
                            set1.setLabel("");/*
                            set1.setCubicIntensity(0f);*/
                            set1.setDrawCircles(false);
                            set1.setDrawCircleHole(false);
                            set1.setDrawValues(false);
                            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                            dataSet.add(set1);
                            LineData data = new LineData(dataSet);
                            xAxis.setGranularityEnabled(true);
                            chart_dailyData.setData(data);
                            data.notifyDataChanged();
                            //chart_dailyData.setViewPortOffsets(60, 0, 50, 60);
                            if(yValues.size()<totalRecords)
                            {
                                serviceCallforChannelData();
                            }

                        }
                    }

                } else {
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
            }
        });
    }


    @Override
    protected void onStop() {
        if (dialog != null) { dialog.dismiss(); dialog = null; }
        super.onStop();
    }
}
