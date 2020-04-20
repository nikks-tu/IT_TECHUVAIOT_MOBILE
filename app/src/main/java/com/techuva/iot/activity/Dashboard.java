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
import androidx.cardview.widget.CardView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.adapter.GridAdapterDashboard;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.api_interface.DeviceTokenDataInterface;
import com.techuva.iot.api_interface.GetConsumedUnits;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ChannelUnitMeasureDO;
import com.techuva.iot.model.CurrentDataMainObject;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.CurrentDataResultObject;
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.model.DeviceTokenPostParameter;
import com.techuva.iot.model.GetConsumedDataPostParamter;
import com.techuva.iot.utils.views.AppDatabase;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class Dashboard extends BaseActivity implements MyFirebaseMessagingService.EventListener {

    LinearLayout ll_top_view, ll_main;
    CardView cv_consumed_units;
    FrameLayout ll_bottom_view;
    TextView tv_thermometer, tv_deviceName, tv_tempUnit, tv_consumed_units_txt, tv_consumed_units, tv_notification_count;
    TextView tv_deviceName1, tv_deviceId, tv_last_sync, tv_history, tv_forwarning, tv_graph, tv_notification;
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
    String inventoryName="";
    Boolean doubleBackToExitPressedOnce = true;
    private View decorView;
    RelativeLayout internetConnection, rl_serverError;
    ProgressDialog progressDialog;
    GridAdapterDashboard gridAdapterDashboard;
    LinearLayout ll_refresh;
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    MPreferences mPreferences;
    LinearLayout ll_history, ll_graph, ll_forwarning, ll_notification;
    int UserId;
    String authorityKey ="";
    String authorityKeyForRefresh ="";
    String grantType = "";
    String grantTypeRefresh = "";
    String refreshToken="";
    String date= "";

    private void setTypeface() {
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceBook = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Book.otf");
        tv_thermometer.setTypeface(faceBook);
        tv_deviceName.setTypeface(faceLight);
        tv_deviceName1.setTypeface(faceLight);
        tv_deviceId.setTypeface(faceLight);
        tv_last_sync.setTypeface(faceLight);
        tv_tempUnit.setTypeface(faceLight);
        tv_consumed_units.setTypeface(faceLight);
        tv_consumed_units_txt.setTypeface(faceLight);
        tv_history.setTypeface(faceLight);
        tv_forwarning.setTypeface(faceLight);
        tv_graph.setTypeface(faceLight);
        tv_notification.setTypeface(faceLight);
        tv_notification_count.setTypeface(faceBook);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void Init_Views() {
        context = Dashboard.this;
        mPreferences = new MPreferences(context);
        llContent.addView(inflater.inflate(R.layout.chiller_dashboard_v_one, null), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        decorView = getWindow().getDecorView();
        grid_bottom = findViewById(R.id.grid_bottom);
        deviceId = MApplication.getString(context, Constants.DeviceID);
        userId = "1001";
        ll_refresh = findViewById(R.id.ll_refresh);
        ll_top_view = findViewById(R.id.ll_top_view);
        ll_bottom_view = findViewById(R.id.ll_bottom_view);
        ll_main = findViewById(R.id.ll_main);
        cv_consumed_units = findViewById(R.id.cv_consumed_units);
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
        tv_forwarning = findViewById(R.id.tv_forwarning);
        tv_graph = findViewById(R.id.tv_graph);
        tv_notification = findViewById(R.id.tv_notification);
        tv_notification_count = findViewById(R.id.tv_notification_count);
        btn_serverConnect = findViewById(R.id.btn_serverConnect);
        internetRetry = findViewById(R.id.internetRetry);
        rl_serverError = findViewById(R.id.rl_serverError);
        inventoryId = MApplication.getString(context, Constants.InventoryTypeId);
        setTypeface();
        ll_history = findViewById(R.id.ll_history);
        ll_graph = findViewById(R.id.ll_graph);
        ll_notification = findViewById(R.id.ll_notification);
        ll_forwarning = findViewById(R.id.ll_forwarning);
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
        ll_history.setEnabled(true);
        ll_history.setVisibility(View.VISIBLE);
        cv_consumed_units.setVisibility(View.GONE);
        ll_graph.setVisibility(View.GONE);
        ll_notification.setVisibility(View.GONE);

       /* if(productName.equals("HBL"))
        {
            String notificationCount = MApplication.getString(context, Constants.NotificationCount);
            tv_notification_count.setText(notificationCount);
            ll_forwarning.setVisibility(View.GONE);
            ll_graph.setVisibility(View.GONE);
            ll_notification.setVisibility(View.VISIBLE);
        }
        else {
            ll_notification.setVisibility(View.GONE);
        }
        if(productName.equals("AGRICULTURE_MONITORING"))
        {
            ll_forwarning.setVisibility(View.VISIBLE);
            ll_graph.setVisibility(View.VISIBLE);
        }
        else {
            ll_forwarning.setVisibility(View.GONE);
            //ll_graph.setVisibility(View.GONE);
        }

        if (productName.equals("ENERGY_MONITORING") || productName.equals("PREPAID_ENERGY_METER"))
        {
            ll_forwarning.setVisibility(View.GONE);
            ll_graph.setVisibility(View.VISIBLE);
            cv_consumed_units.setVisibility(View.VISIBLE);
        }
        else {
            cv_consumed_units.setVisibility(View.GONE);
        }

        if (productName.equals("AGRICULTURE_MONITORING") || productName.equals("ENERGY_MONITORING") || productName.equals("PREPAID_ENERGY_METER"))
        {
            ll_graph.setVisibility(View.VISIBLE);
        }
        else {
            ll_graph.setVisibility(View.GONE);
        }*/

        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit TechUva IoT", Toast.LENGTH_SHORT);
        if(MApplication.isNetConnected(context))
        {
            internetConnection.setVisibility(View.GONE);
            ll_main.setVisibility(View.VISIBLE);
            appVersion = mPreferences.getStringFromPreference(Constants.AppVersion, "1");
            fontVersion = mPreferences.getStringFromPreference(Constants.FontVersion, "0");
            showLoaderNew();
            new Handler().postDelayed(() ->
            {   serviceCall();
                serviceCallforConsumedData(); }, 1000);
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

        cv_consumed_units.setOnClickListener(v -> {
            Intent intent = new Intent(context, ConsumedUnitDetailsActivity.class);
            startActivity(intent);
        });

        ll_forwarning.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForewarninActivity.class);
            startActivity(intent);
        });


        grid_bottom.setOnItemClickListener((parent, view, position, id) -> {
            int selectedGridChannelNum = gridAdapterDashboard.getChannelNumber(position);
            Intent intent = new Intent(context, History_View_Activity.class);
            intent.putExtra("TabId", selectedGridChannelNum);
            startActivity(intent);
        });

        ll_refresh.setOnClickListener(v -> {
            showLoaderNew();
            String notificationCount = MApplication.getString(context, Constants.NotificationCount);
            //Toast.makeText(context, notificationCount, Toast.LENGTH_SHORT).show();
            tv_notification_count.setText(notificationCount);
            serviceCall();
            serviceCallforConsumedData();
        });

        btn_serverConnect.setOnClickListener(v ->
        {
            serviceCall();
            serviceCallforConsumedData();
        });

        internetRetry.setOnClickListener(v ->{
            serviceCall();
            serviceCallforConsumedData();
        });

        MApplication.setBoolean(context, Constants.IsHomeSelected, true);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("Device ID", "getInstanceId failed", task.getException());
                        return;
                    }
                    String token = task.getResult().getToken();
                    serviceCallForSavingToken(token);
                    Log.d("Device ID", token);
                    });

    }
    private void serviceCallForSavingToken(String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DeviceTokenDataInterface service = retrofit.create(DeviceTokenDataInterface.class);
        Call<JsonElement> call = service.getStringScalarWithSession(UserId, authorityKey, new DeviceTokenPostParameter(token));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hideloader();
                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if(response.body()!=null){
                   JsonObject jsonObject = response.body().getAsJsonObject();
                   JsonObject infoObject = jsonObject.get("info").getAsJsonObject();
                   int errorCode = infoObject.get("ErrorCode").getAsInt();
                   String errorMsg = infoObject.get("ErrorMessage").getAsString();

                }else {
                // Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                ll_main.setVisibility(View.GONE);
                rl_serverError.setVisibility(View.VISIBLE);
            }

        });
    }


    private void serviceCall(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface .class);

        Call<CurrentDataMainObject> call = service.getStringScalarWithSession(UserId, authorityKey, new CurrentDataPostParameter(deviceId,userId));
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
                    //Log.println(1, "Empty", "Else");
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

        ll_graph.setOnClickListener(v -> {
            Intent intent = new Intent(context, DataGraphActivity.class);
            startActivity(intent);
        });

        ll_notification.setOnClickListener(v -> {
            MApplication.setString(context, Constants.NotificationCount, "0");
            Intent intent = new Intent(context, HBLNotificationActivity.class);
            startActivity(intent);
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
        new Handler().postDelayed(() -> serviceCall(), 1000);


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
      //  serviceCallforRefreshToken();
    }

    @Override
    public void goto_home() {
        if(MApplication.getBoolean(context, Constants.IsHomeSelected))
        {
            Log.e("", "");
        }
        else
        {
            intent = new Intent(context, Dashboard.class);
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

                arrayList.add(values.get(i));

                AppDatabase.init(context);
                ChannelUnitMeasureDO channelUnitMeasureDO = new ChannelUnitMeasureDO();
                channelUnitMeasureDO.setChannelNum(values.get(i).getChannelNumber());
                channelUnitMeasureDO.setUnit(values.get(i).getUnit_of_measure());
                AppDatabase.addUnittoTable(channelUnitMeasureDO);
            }

        }
        if(arrayList.size()>0)
        {
            gridAdapterDashboard = new GridAdapterDashboard(Dashboard.this, R.layout.grid_item_dashboard, arrayList);
            grid_bottom.setAdapter(gridAdapterDashboard);
        }
    }

    public void showLoaderNew() {
        runOnUiThread(new Dashboard.Runloader(getResources().getString(R.string.loading)));
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
                if(dialog != null){
                    dialog.show();
                }


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

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "My_IOT_Files");

        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

            }
            else {
             // Toast.makeText(context, "Folder created", Toast.LENGTH_SHORT).show();
            }
        }
        else {

            try {
                File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My_IOT_Files");
                File file = new File(Dir,  "/icomoon.ttf");
                if(file.exists()){
                    file.delete();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            new DownloadFileFromURL().execute(FontUrl);

        }
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
                OutputStream output = new FileOutputStream("/storage/emulated/0/My_IOT_Files/icomoon.ttf");
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
        File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My_IOT_Files");

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

    private void serviceCallforConsumedData() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                //For Production .Net
                //.baseUrl(Constants.Table_Data_URL)
                //For Test
                .client(okHttpClient)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetConsumedUnits service = retrofit.create(GetConsumedUnits.class);
        Call<JsonElement> call = service.getStringScalarWithSession(UserId, authorityKey, new GetConsumedDataPostParamter(date, date, inventoryName));
        call.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
               hideloader();

                if (response.code() == 401) {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                } else if (response.body() != null) {

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    if (!jsonObject.isJsonNull()) {
                     if(jsonObject.has("info"))
                     {
                         JsonObject jObj = jsonObject.getAsJsonObject("info");
                         if (jObj.get("ErrorCode").getAsInt() == 0) {
                             JsonObject resultObject = jsonObject.getAsJsonObject("result");
                             JsonArray dataArray = resultObject.getAsJsonArray("DaywiseConsumption");
                             JsonObject unitObject = dataArray.get(0).getAsJsonObject();
                             if(!unitObject.get("SUM").getAsString().equals("-"))
                             {
                                 tv_consumed_units.setText(unitObject.get("SUM").getAsString());
                             }
                             else {
                                 tv_consumed_units.setText(getResources().getString(R.string.na));
                             }
                             //Toast.makeText(context, ""+unitObject.get("SUM"), Toast.LENGTH_SHORT).show();
                         } else if (jObj.get("ErrorCode").getAsInt() == 1) {

                         } else {

                         }
                     }

                    } else {
                              }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
            }
        });

    }


    @Override
    protected void onPause() {
        if(alertDialog!=null)
        {
            alertDialog.dismiss();
        }
        super.onPause();
    }
}
