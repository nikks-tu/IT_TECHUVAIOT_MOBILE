package com.techuva.iot.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.Build;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.adapter.GridAdapterMotorChannels;
import com.techuva.iot.adapter.GridAdapterWaterMotor;
import com.techuva.iot.adapter.MotorHistoryAdapter;
import com.techuva.iot.adapter.MotorOnOffSummaryAdapter;
import com.techuva.iot.api_interface.AccountDetailsInterface;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.api_interface.HistoryDataInterface;
import com.techuva.iot.api_interface.HistoryTableGetData;
import com.techuva.iot.app.Constants;
import com.techuva.iot.listener.EndlessScrollListener;
import com.techuva.iot.model.ChannelAllValuesObject;
import com.techuva.iot.model.CurrentDataMainObject;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.CurrentDataResultObject;
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.model.DevicesListPostParameter;
import com.techuva.iot.model.GetChannelDataWMotor;
import com.techuva.iot.model.HistoryDataAllChannelPostParamter;
import com.techuva.iot.model.MotorChannelHistoryData;
import com.techuva.iot.response_model.MotorOnOffSummary;
import com.techuva.iot.utils.CustomTypefaceSpan;
import com.techuva.iot.utils.views.MApplication;
import com.techuva.iot.utils.views.MPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Dashboard_Water_Motor extends BaseActivity {

    LinearLayout ll_top_view, ll_main;
    CardView cv_consumed_units;
    LinearLayout ll_bottom_view;
    ViewPager viewPager_bottom;
    TabLayout tab_layout_pro;
    TextView tv_thermometer, tv_deviceName, tv_tempUnit;
    TextView tv_deviceName1, tv_deviceId, tv_last_sync;
    TextView tv_provisioned_on,tv_provisioned, tv_last_sync_txt;
    Button btn_serverConnect, internetRetry;
    String deviceId, userId;
    Activity context;
    Intent intent;
    String inventoryId;
    String appVersion;
    String fontVersion;
    String FontUrl="";
    Toast exitToast;
    String inventoryName="";
    ArrayList<String> popupItem;
    Boolean doubleBackToExitPressedOnce = true;
    private View decorView;
    RelativeLayout internetConnection, rl_serverError;
    LinearLayout ll_refresh;
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    MPreferences mPreferences;
    int UserId;
    String authorityKey ="";
    String authorityKeyForRefresh ="";
    String grantType = "";
    String grantTypeRefresh = "";
    String refreshToken="";
    String date= "";
    String Label= "";
    LinearLayout ll_motor_on_off, ll_motor_on_off_summary, ll_device_details,ll_device_history;
    TextView tv_motor_on_off, tv_motor_on_off_summary,tv_device_details, tv_device_history;
    LinearLayout ll_bottom;
    ArrayList<GetChannelDataWMotor> channelList;
    ArrayList<GetChannelDataWMotor> channelListForLevel;
    Drawable round_red;
    Drawable round_dg;
    GridView grid_water_level;
    ListView lv_data;
    String fromDateforCall, toDateforCall;
    GridAdapterWaterMotor gridAdapterWaterLevel;
    LinearLayout ll_Bottom_on_off, ll_first, ll_on_off_summary_view, ll_headings, ll_select_date, ll_to_date;
    TextView tv_motor_operation_mode, tv_auto_remote, tv_manual_mode, tv_motor_on_off_status, tv_motor_on, tv_motor_off;
    TextView tv_motor_on_off_reason, tv_manual, tv_auto, tv_remote, tv_motor_airlock,tv_neutral;
    ImageView iv_auto_remote, iv_manual, iv_motor_on, iv_motor_off, iv_manual_reason, iv_auto, iv_remote, iv_motor_airlock, iv_neutral;
    TextView tv_time_heading, tv_motor_on_off_heading, tv_motor_on_off_reason_heading, tv_no_summary, tv_to_date, tv_from_date;
    LinearLayout ll_device_history_view, ll_headings_history;
    TextView tv_time_heading_history, tv_sump_level_head, tv_tank_level_head, tv_nodata_history;
    ListView lv_history_data;

    String fromTime = "00:00";
    String toTime = "23:59";
    String fromTimeForCall = "00:00:00";
    String toTimeforCall = "23:59:59";
    ArrayList<CurrentDataValueObject> currentWaterLevelList;
    ArrayList<ChannelAllValuesObject> all_channel_data;
    ArrayList<GetChannelDataWMotor> listForGrid;
    boolean isManualMode= false;
    int pageNumber = 1;
    int yy, mm, dd;
    static String fromDate;
    static String fromDateV;
    static String toDate;
    int day, year, month;
    int listCount;
    int pagePerCount=50;
    String description="MOTOR_STATUS";
    Calendar calendar;
    HistoryDataInterface service;
    Call<JsonElement> call1;
    ArrayList<MotorOnOffSummary> summaryList;
    ArrayList<MotorChannelHistoryData> historyData;
    MotorOnOffSummaryAdapter summaryAdapter;
    MotorHistoryAdapter historyAdapter;
    Boolean dateChanged = false;
    //-----------
    LinearLayout ll_motor_on_off_heading, ll_provisioned, ll_all_buttons;
    TextView tv_on_off_status_text, tv_on_off_reason_text, tv_on_off_status, tv_on_off_reason;
    TextView tv_date, tv_time_from, tv_time_to;
    String motorStatus="";
    String motorReason="";
    LinearLayout ll_device_details_grid;
    GridView grid_device_details;
    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> {
        // TODO Auto-generated method stub
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(arg1, arg2 + 1, arg3);
    };

    private EndlessScrollListener scrollListener;
    private EndlessScrollListener scrollListenerHistory;
    @Override
    protected void goto_manageThreshold() {

    }

    @Override
    public void initialize() {
    InitViews();
    }

    private void InitViews() {
        context = Dashboard_Water_Motor.this;
        mPreferences = new MPreferences(context);
        llContent.addView(inflater.inflate(R.layout.activity_dashboard_water_motor, null),
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        decorView = getWindow().getDecorView();
        deviceId = MApplication.getString(context, Constants.DeviceID);
        userId = "1001";
        Toolbar toolbar = findViewById(R.id.toolbar_drawer);
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
        tv_provisioned = findViewById(R.id.tv_provisioned);
        tv_provisioned_on = findViewById(R.id.tv_provisioned_on);
        tv_last_sync_txt = findViewById(R.id.tv_last_sync_txt);
        btn_serverConnect = findViewById(R.id.btn_serverConnect);
        internetRetry = findViewById(R.id.internetRetry);
        rl_serverError = findViewById(R.id.rl_serverError);
        ll_motor_on_off = findViewById(R.id.ll_motor_on_off);
        ll_motor_on_off_summary = findViewById(R.id.ll_motor_on_off_summary);
        ll_device_details = findViewById(R.id.ll_device_details);
        ll_device_history = findViewById(R.id.ll_device_history);
        tv_motor_on_off = findViewById(R.id.tv_motor_on_off);
        tv_motor_on_off_summary = findViewById(R.id.tv_motor_on_off_summary);
        tv_device_details = findViewById(R.id.tv_device_details);
        tv_device_history = findViewById(R.id.tv_device_history);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_from_date = findViewById(R.id.tv_from_date);
       
        ll_bottom = findViewById(R.id.ll_bottom);
        ll_on_off_summary_view = findViewById(R.id.ll_on_off_summary_view);
        ll_headings = findViewById(R.id.ll_headings);
        ll_select_date = findViewById(R.id.ll_select_date);
        ll_to_date = findViewById(R.id.ll_to_date);
        lv_data = findViewById(R.id.lv_data);
        inventoryId = MApplication.getString(context, Constants.InventoryTypeId);
        //-----------------------------------------------------
        grid_water_level = findViewById(R.id.grid_water_level);
        ll_Bottom_on_off = findViewById(R.id.ll_Bottom_on_off);
        ll_first = findViewById(R.id.ll_first);
        tv_motor_operation_mode = findViewById(R.id.tv_motor_operation_mode);
        tv_auto_remote = findViewById(R.id.tv_auto_remote);
        tv_manual_mode = findViewById(R.id.tv_manual_mode);
        tv_motor_on_off_status = findViewById(R.id.tv_motor_on_off_status);
        tv_motor_on = findViewById(R.id.tv_motor_on);
        tv_motor_off = findViewById(R.id.tv_motor_off);
        tv_motor_on_off_reason = findViewById(R.id.tv_motor_on_off_reason);
        tv_manual = findViewById(R.id.tv_manual);
        tv_auto = findViewById(R.id.tv_auto);
        tv_remote = findViewById(R.id.tv_remote);
        tv_neutral = findViewById(R.id.tv_neutral);
        tv_motor_airlock = findViewById(R.id.tv_motor_airlock);
        tv_time_heading = findViewById(R.id.tv_time_heading);
        tv_motor_on_off_heading = findViewById(R.id.tv_motor_on_off_heading);
        tv_motor_on_off_reason_heading = findViewById(R.id.tv_motor_on_off_reason_heading);
        tv_no_summary = findViewById(R.id.tv_no_summary);
        //-----------------------------------------------------------------------
        ll_all_buttons = findViewById(R.id.ll_all_buttons);
        ll_provisioned = findViewById(R.id.ll_provisioned);
        ll_motor_on_off_heading = findViewById(R.id.ll_motor_on_off_heading);
        tv_on_off_status_text = findViewById(R.id.tv_on_off_status_text);
        tv_on_off_reason_text = findViewById(R.id.tv_on_off_reason_text);
        tv_on_off_status = findViewById(R.id.tv_on_off_status);
        tv_on_off_reason = findViewById(R.id.tv_on_off_reason);
        iv_auto_remote = findViewById(R.id.iv_auto_remote);
        iv_manual = findViewById(R.id.iv_manual);
        iv_motor_on = findViewById(R.id.iv_motor_on);
        iv_motor_off = findViewById(R.id.iv_motor_off);
        iv_manual_reason = findViewById(R.id.iv_manual_reason);
        iv_auto = findViewById(R.id.iv_auto);
        iv_remote = findViewById(R.id.iv_remote);
        iv_motor_airlock = findViewById(R.id.iv_motor_airlock);
        iv_neutral = findViewById(R.id.iv_neutral);
        //----------------------------------------
        ll_device_details_grid = findViewById(R.id.ll_device_details_grid);
        grid_device_details = findViewById(R.id.grid_device_details);
        ll_device_history_view = findViewById(R.id.ll_device_history_view);
        ll_headings_history = findViewById(R.id.ll_headings_history);
        tv_time_heading_history = findViewById(R.id.tv_time_heading_history);
        tv_sump_level_head = findViewById(R.id.tv_sump_level_head);
        tv_nodata_history = findViewById(R.id.tv_nodata_history);
        tv_tank_level_head = findViewById(R.id.tv_tank_level_head);
        lv_history_data = findViewById(R.id.lv_history_data);
        tv_date = findViewById(R.id.tv_date);
        tv_time_from = findViewById(R.id.tv_time_from);
        tv_time_to = findViewById(R.id.tv_time_to);
        //--------------------------------------
        currentWaterLevelList = new ArrayList<>();
        all_channel_data = new ArrayList<>();
        listForGrid = new ArrayList<>();
        setTypeface();
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        authorityKeyForRefresh = Constants.AuthorizationKey;
        grantType = Constants.GrantType;
        grantTypeRefresh = Constants.GrantTypeRefresh;
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        inventoryName = MApplication.getString(context, Constants.InventoryName);
        channelList = new ArrayList<>();
        channelListForLevel = new ArrayList<>();
        String productName = MApplication.getString(context, Constants.InventoryTypeName);
        Calendar calendar = new GregorianCalendar();
       // Toast.makeText(context, "Yes", Toast.LENGTH_SHORT).show();

        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf5 = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdf6 = new SimpleDateFormat("hh:mm");
        fromDate = sdf3.format(calendar.getTime());
        toDate = fromDate;
        tv_to_date.setText(fromDate);
        tv_from_date.setText(fromDate);
        fromDateforCall = fromDate +" "+fromTime;
        toDateforCall = toDate+" "+toTime;
        date = sdf4.format(calendar.getTime());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        popupItem = new ArrayList<>();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-9);
        round_red = getResources().getDrawable(R.drawable.round_red);
        round_dg = getResources().getDrawable(R.drawable.round_dg);
        summaryList = new ArrayList<>();
        historyData = new ArrayList<>();
        //toTime = sdf5.format(calendar.getTime());
        tv_time_from.setText(fromTime);
        tv_time_to.setText(toTime);
        //calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit TechUva IoT", Toast.LENGTH_SHORT);

        fromDate = sdf4.format(calendar.getTime());
        toDate = sdf4.format(calendar.getTime());
        tv_date.setText(fromDate);
        ll_Bottom_on_off.setVisibility(View.VISIBLE);
        ll_provisioned.setVisibility(View.VISIBLE);
        ll_headings_history.setVisibility(View.GONE);
        if(MApplication.isNetConnected(context))
        {
            internetConnection.setVisibility(View.GONE);
            ll_main.setVisibility(View.VISIBLE);
            appVersion = mPreferences.getStringFromPreference(Constants.AppVersion, "1");
            fontVersion = mPreferences.getStringFromPreference(Constants.FontVersion, "0");
            showLoaderNew();
            serviceCallForChannelInfo();
            new Handler().postDelayed(() -> {
                serviceCall();
            }, 1000);
        }
        else {
            internetConnection.setVisibility(View.VISIBLE);
            ll_main.setVisibility(View.GONE);
        }

        historyAdapter = new MotorHistoryAdapter(R.layout.item_hr_channel_values, context, historyData );
        lv_history_data.setAdapter(historyAdapter);
        summaryAdapter = new MotorOnOffSummaryAdapter(R.layout.item_hr_channel_values, context, summaryList );
        lv_data.setAdapter(summaryAdapter);

        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (pageNumber < listCount) {
                    pageNumber = pageNumber + 1;
                    loadNextDataFromApi(pageNumber, 200);
                    //tabChanged= false;
                } else {
                    pageNumber = 1;
                }
                //Mistake
                return true;
            }
        };

        scrollListenerHistory = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (pageNumber < listCount) {
                    pageNumber = pageNumber + 1;
                    loadNextDataFromApiForHistory(pageNumber, 200);
                    //tabChanged= false;
                } else {
                    pageNumber = 1;
                }
                //Mistake
                return true;
            }
        };

        tv_time_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                tv_time_from.setText(hourOfDay + ":" + minute);
                                fromTimeForCall = hourOfDay+":"+minute+":"+"00";
                            }
                        }, hour, minute, true);
                timePickerDialog.show();
                timePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timePickerDialog.dismiss();
                    }
                });
                timePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        historyData.clear();
                        loadNextDataFromApiForHistory(1, 50);
                        timePickerDialog.dismiss();
                    }
                });
            }
        });
        tv_time_to.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            tv_time_to.setText(hourOfDay + ":" + minute);
                            toTimeforCall = hourOfDay+":"+minute+":"+"00";
                        }
                    }, hour, minute, true);
            timePickerDialog.show();
            timePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerDialog.dismiss();
                }
            });
            timePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyData.clear();
                    loadNextDataFromApiForHistory(1, 50);
                    timePickerDialog.dismiss();
                }
            });
        });

        GridAdapterMotorChannels gridAdapter = new GridAdapterMotorChannels(context, R.layout.grid_item_water_motor, listForGrid);
        grid_device_details.setAdapter(gridAdapter);

        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showDateForHistory(year, month+1, day);
            }
        });

        lv_data.setOnScrollListener(scrollListener);
        ll_select_date.setOnClickListener(v -> {
            if (month < 10) {
                showDate(year, month + 1, day);
            } else
                showDate(year, month, day);
        });
        ll_to_date.setOnClickListener(v -> {
            String date = sdf3.format(calendar.getTime());
            if(!date.equals(fromDate))
            {
                if (month < 10) {
                    showToDate(year, month + 1, day);
                } else
                    showToDate(year, month, day);
            }
        });


        ll_device_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_provisioned.setVisibility(View.VISIBLE);
                tv_last_sync.setVisibility(View.GONE);
                tv_last_sync_txt.setVisibility(View.GONE);
                ll_motor_on_off_heading.setVisibility(View.VISIBLE);
                ll_device_details_grid.setVisibility(View.VISIBLE);
                ll_device_history.setVisibility(View.VISIBLE);
                ll_device_details.setVisibility(View.GONE);
                ll_Bottom_on_off.setVisibility(View.GONE);
                /*listForGrid.clear();
                channelList.clear();
                serviceCallForChannelInfo();
                serviceCall();*/
                if(listForGrid.size()>0)
                {
                    gridAdapter.notifyDataSetChanged();
                }
            }
        });


        ll_device_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_provisioned.setVisibility(View.GONE);
                tv_last_sync.setVisibility(View.GONE);
                tv_last_sync_txt.setVisibility(View.GONE);
                ll_motor_on_off_heading.setVisibility(View.VISIBLE);
                ll_device_details_grid.setVisibility(View.GONE);
                ll_all_buttons.setVisibility(View.GONE);
                ll_device_details.setVisibility(View.GONE);
                ll_Bottom_on_off.setVisibility(View.GONE);
                ll_device_history_view.setVisibility(View.VISIBLE);


               loadNextDataFromApiForHistory(1, 50);
            }
        });

        ll_motor_on_off_summary.setOnClickListener(v -> {
            ll_all_buttons.setVisibility(View.GONE);
            ll_provisioned.setVisibility(View.GONE);
            ll_motor_on_off_heading.setVisibility(View.VISIBLE);
            ll_Bottom_on_off.setVisibility(View.GONE);
            ll_device_history.setVisibility(View.GONE);
            ll_on_off_summary_view.setVisibility(View.VISIBLE);
            ll_device_details.setVisibility(View.VISIBLE);
            ll_device_details_grid.setVisibility(View.GONE);
            ll_Bottom_on_off.setVisibility(View.GONE);
            switch (Integer.parseInt(motorStatus))
            {
                case 0:
                case 6:
                case 3:
                case 2:
                    tv_on_off_status.setText("ON");
                    break;
                case 1:
                case 5:
                case 4:
                    tv_on_off_status.setText("OFF");
                    break;
            }
            switch (Integer.parseInt(motorReason))
            {
                case 0:
                case 1:
                    tv_on_off_reason.setText("Remote");
                    break;
                case 3:
                case 4:
                    tv_on_off_reason.setText("Manual");
                    break;
                case 2:
                    tv_on_off_reason.setText("Neutral");
                    break;
                case 5:
                case 6:
                    tv_on_off_reason.setText("Auto");
                    break;
            }
            //showLoaderNew();
            loadNextDataFromApi(1, 50);
        });

        ll_refresh.setOnClickListener(v -> {
            showLoaderNew();
            channelList = new ArrayList<>();
            currentWaterLevelList = new ArrayList<>();
            channelListForLevel = new ArrayList<>();
            ll_motor_on_off_heading.setVisibility(View.GONE);
            ll_provisioned.setVisibility(View.VISIBLE);
            ll_all_buttons.setVisibility(View.VISIBLE);
            ll_Bottom_on_off.setVisibility(View.VISIBLE);
            ll_on_off_summary_view.setVisibility(View.GONE);
            ll_device_details_grid.setVisibility(View.GONE);
            ll_device_history.setVisibility(View.GONE);
            ll_device_details.setVisibility(View.VISIBLE);
            ll_all_buttons.setVisibility(View.VISIBLE);
            ll_device_history_view.setVisibility(View.GONE);
            serviceCallForChannelInfo();
            serviceCall();
        });

        btn_serverConnect.setOnClickListener(v ->
        {
            showLoaderNew();
            serviceCall();
        });

        internetRetry.setOnClickListener(v ->{
            showLoaderNew();
            serviceCall();
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


        ll_motor_on_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupItem = new ArrayList<>();
                popupItem.add("ON MOTOR");
                popupItem.add("OFF MOTOR");
                showPopupMenu(popupItem, ll_motor_on_off );
            }
        });


    }


    @Override
    public void goto_home() {

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

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void loadNextDataFromApi(int page, int delay) {
        pageNumber = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(pageNumber==1)
                {
                    summaryList.clear();
                    summaryList = new ArrayList<>();
                   // summaryAdapter.notifyDataSetChanged();
                }
                serviceCallForGettingOnOffSummary();
            }
        }, delay);
    }

    private void loadNextDataFromApiForHistory(int page, int delay) {
        pageNumber = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(pageNumber==1)
                {
                    summaryList.clear();
                    summaryList = new ArrayList<>();
                   // summaryAdapter.notifyDataSetChanged();
                }
                serviceCallforChannelData();
            }
        }, delay);
    }



    private void serviceCallForGettingOnOffSummary() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_TOKEN)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(HistoryDataInterface.class);
        call1 = service.getMotorOnOffSummary(UserId, authorityKey, new CurrentDataPostParameter(inventoryName, fromDate, toDate, String.valueOf(pagePerCount), String.valueOf(pageNumber), description));
        call1.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                    if (response.code()==401)
                    {
                        MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                        Intent intent = new Intent(context, TokenExpireActivity.class);
                        startActivity(intent);
                    }
                    else  if (response.body() != null) {
                        hideloader();
                        JsonObject jsonObject = response.body().getAsJsonObject();
                        JsonObject info = jsonObject.get("info").getAsJsonObject();
                        int errorCode = info.get("ErrorCode").getAsInt();
                        String msg = info.get("ErrorMessage").getAsString();
                        if (errorCode==0) {
                            JsonArray array = jsonObject.get("result").getAsJsonArray();
                            for(int i=0; i<array.size(); i++)
                            {
                                MotorOnOffSummary model = new MotorOnOffSummary();
                                JsonObject obj = array.get(i).getAsJsonObject();
                                if(obj.has("RECEIVED_TIME") && !obj.get("RECEIVED_TIME").isJsonNull())
                                {
                                    model.setRECEIVEDTIME(obj.get("RECEIVED_TIME").getAsString());
                                }
                                else {
                                    model.setRECEIVEDTIME("");
                                } if(obj.has("STATUS") && !obj.get("STATUS").isJsonNull())
                                {
                                    model.setSTATUS(obj.get("STATUS").getAsString());
                                }
                                else {
                                    model.setSTATUS("");
                                } if(obj.has("REASON") && !obj.get("REASON").isJsonNull())
                                {
                                    model.setrEASON(obj.get("REASON").getAsString());
                                }
                                else {
                                    model.setrEASON("");
                                }
                                summaryList.add(model);
                            }
                            if(summaryList.size()>0)
                            {
                                lv_data.setVisibility(View.VISIBLE);
                                tv_no_summary.setVisibility(View.GONE);
                                if(pageNumber==1)
                                {
                                    summaryAdapter = new MotorOnOffSummaryAdapter(R.layout.item_hr_channel_values, context, summaryList );
                                    lv_data.setAdapter(summaryAdapter);
                                }
                                else {
                                    summaryAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            lv_data.setVisibility(View.GONE);
                            tv_no_summary.setVisibility(View.VISIBLE);
                            tv_no_summary.setText(msg);
                        }

                    } else {
                        hideloader();
                        lv_data.setVisibility(View.GONE);
                        tv_no_summary.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    hideloader();
                    Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
                    lv_data.setVisibility(View.GONE);
                    tv_no_summary.setVisibility(View.VISIBLE);
                }

            });
        }




    private void serviceCallforChannelData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HistoryTableGetData service = retrofit.create(HistoryTableGetData.class);
        //Call<FullHistoryMainObject> call = service.getStringScalar(new HistoryDataAllChannelPostParamter(deviceId, fromDateforCall, toDateforCall, pagePerCount, String.valueOf(pageNumber)));
        Call<JsonElement> call = service.getStringScalarWithSession(UserId, authorityKey, new HistoryDataAllChannelPostParamter(deviceId, fromDateforCall, toDateforCall, String.valueOf(pagePerCount), String.valueOf(pageNumber)));
        call.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                hideloader();

                if (response.code() == 401) {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                } else if (response.body() != null) {

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    if (!jsonObject.isJsonNull()) {
                        JsonObject jObj = jsonObject.getAsJsonObject("info");
                        if (jObj.get("ErrorCode").getAsInt() == 0) {
                            JsonArray resultArray = jsonObject.getAsJsonArray("result");
                            if(resultArray.size()>0)
                            {

                                for (int i=0; i<resultArray.size(); i++)
                                {
                                    JsonObject object = resultArray.get(i).getAsJsonObject();
                                    MotorChannelHistoryData model = new MotorChannelHistoryData();
                                    if(object.has("BATCH_ID"))
                                    {
                                        model.setBATCHID(object.get("BATCH_ID").getAsInt());
                                    }
                                    if(object.has("INVENTORY_NAME"))
                                    {
                                        model.setINVENTORYNAME(object.get("INVENTORY_NAME").getAsString());
                                    }
                                    if(object.has("1"))
                                    {
                                        model.set1(object.get("1").getAsString());
                                    }
                                    if(object.has("2"))
                                    {
                                        model.set2(object.get("2").getAsString());
                                    }
                                    if(object.has("3"))
                                    {
                                        model.set3(object.get("3").getAsString());
                                    }
                                    if(object.has("4"))
                                    {
                                        model.set4(object.get("4").getAsString());
                                    }
                                    if(object.has("RECEIVED_TIME"))
                                    {
                                        model.setRECEIVEDTIME(object.get("RECEIVED_TIME").getAsString());
                                    }

                                    historyData.add(model);
                                }

                                if(historyData.size()>0 )
                                {
                                    tv_nodata_history.setVisibility(View.GONE);
                                    ll_device_history_view.setVisibility(View.VISIBLE);
                                    lv_history_data.setVisibility(View.VISIBLE);
                                    if(pageNumber==1)
                                    {

                                        historyAdapter = new MotorHistoryAdapter(R.layout.item_hr_channel_values, context, historyData );
                                        lv_history_data.setAdapter(historyAdapter);
                                        historyAdapter = new MotorHistoryAdapter(R.layout.item_hr_channel_values, context, historyData );
                                        lv_history_data.setAdapter(historyAdapter);
                                    }
                                    else {

                                        historyAdapter.notifyDataSetChanged();
                                    }
                                }
                                else {
                                    tv_nodata_history.setVisibility(View.VISIBLE);
                                    lv_history_data.setVisibility(View.GONE);
                                }

                            }


                        }
                        }
                }}

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();

            }
        });

    }


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
        tv_provisioned.setTypeface(faceLight);
        tv_last_sync_txt.setTypeface(faceLight);
        tv_motor_on_off.setTypeface(faceLight);
        tv_motor_on_off_summary.setTypeface(faceLight);
        tv_device_details.setTypeface(faceLight);
        tv_provisioned_on.setTypeface(faceLight);
        tv_motor_operation_mode.setTypeface(faceLight);
        tv_auto_remote.setTypeface(faceLight);
        tv_manual_mode.setTypeface(faceLight);
        tv_motor_on_off_status.setTypeface(faceLight);
        tv_motor_on.setTypeface(faceLight);
        tv_motor_off.setTypeface(faceLight);
        tv_motor_on_off_reason.setTypeface(faceLight);
        tv_manual.setTypeface(faceLight);
        tv_auto.setTypeface(faceLight);
        tv_remote.setTypeface(faceLight);
        tv_motor_airlock.setTypeface(faceLight);
        tv_neutral.setTypeface(faceLight);
        tv_time_heading.setTypeface(faceLight);
        tv_motor_on_off_heading.setTypeface(faceLight);
        tv_motor_on_off_reason_heading.setTypeface(faceLight);
        tv_no_summary.setTypeface(faceLight);
        tv_to_date.setTypeface(faceLight);
        tv_from_date.setTypeface(faceLight);
        tv_on_off_status_text.setTypeface(faceLight);
        tv_on_off_reason_text.setTypeface(faceLight);
        tv_on_off_status.setTypeface(faceLight);
        tv_on_off_reason.setTypeface(faceLight);
        tv_device_history.setTypeface(faceLight);
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

    }


    private void showToDate(int year, final int month, int day) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {

            monthOfYear = monthOfYear+1;
            String date2;
            String date = (year1 + "-" + (monthOfYear) + "-" + dayOfMonth);

            if(monthOfYear<10 && dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(monthOfYear<10)
            {
                date2  = (dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            else {
                date2 =   (dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            tv_to_date.setText(date2);
            toDate = date;
            fromDate = tv_from_date.getText().toString();
            DateFormat  outputFormat= new SimpleDateFormat("yyyy-MM-dd");
            DateFormat inputFormat  = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat yearOnly  = new SimpleDateFormat("yyyy");
            DateFormat monthOnly  = new SimpleDateFormat("MM");
            DateFormat dayOnly  = new SimpleDateFormat("dd");
            String inputDateStr= fromDate;
            Date date1 = null;
            Date yearMax = null;
            Date monthMax = null;
            Date dayMax = null;
            try {
                date1 = inputFormat.parse(inputDateStr);
                yearMax = inputFormat.parse(inputDateStr);
                monthMax = inputFormat.parse(inputDateStr);
                dayMax = inputFormat.parse(inputDateStr);

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr = outputFormat.format(date1);
            yy = Integer.parseInt(yearOnly.format(yearMax));
            mm = Integer.parseInt(monthOnly.format(monthMax));
            dd = Integer.parseInt(dayOnly.format(dayMax));

            /*try {
                date1 = sdf1.parse(fromDate);
                //date3 = sdf1.parse(toDate);
            } catch (ParseException | java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
            fromDate = outputDateStr;
            dateChanged = true;
            lv_data.setSelectionAfterHeaderView();
            if (dateChanged) {
                summaryList.clear();
                summaryAdapter.notifyDataSetChanged();
                summaryList = new ArrayList<>();
                showLoaderNew();
                //serviceCall();
                pageNumber =1;
                loadNextDataFromApi(pageNumber, 0);
            }

        }, year, month, day);
        Calendar c = Calendar.getInstance();
        c.set(yy, mm-1, dd);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void showDate(int year, final int month, int day) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {
            monthOfYear = monthOfYear+1;
            String date2;
            String date = (year1 + "-" + (monthOfYear) + "-" + dayOfMonth);
            yy = year1;
            mm = monthOfYear;
            dd = dayOfMonth;
            if(monthOfYear<10 && dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(monthOfYear<10)
            {
                date2  = (dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            else {
                date2 =   (dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            tv_from_date.setText(date2);
            fromDate = date;
            fromDateV = date2;
            toDate = tv_to_date.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            Date date1;
            Date date3;
            try {
                date1 = sdf1.parse(toDate);
                date3 = sdf1.parse(date);
                toDate = sdf.format(date1);
                //fromDate = sdf.format(date3);
            } catch (ParseException | java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            dateChanged = true;
            lv_data.setSelectionAfterHeaderView();
            if (dateChanged) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = new GregorianCalendar();
                String dateV = sdf3.format(calendar.getTime());
                //String  = sdf3.format(fromDate);
                if(fromDateV.equals(dateV))
                {
                    tv_to_date.setText(fromDateV);
                    summaryList.clear();
                    summaryAdapter.notifyDataSetChanged();
                    summaryList = new ArrayList<>();
                    showLoaderNew();
                    //serviceCall();
                    pageNumber =1;
                    loadNextDataFromApi(pageNumber, 0);
                }
                else {
                    summaryList.clear();
                    summaryAdapter.notifyDataSetChanged();
                    summaryList = new ArrayList<>();
                    showLoaderNew();
                    //serviceCall();
                    pageNumber =1;
                    loadNextDataFromApi(pageNumber, 0);
                }
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    private void showDateForHistory(int year, final int month, int day) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {
            monthOfYear = monthOfYear+1;
            String date2;
            String date = (year1 + "-" + (monthOfYear) + "-" + dayOfMonth);
            yy = year1;
            mm = monthOfYear;
            dd = dayOfMonth;
            if(monthOfYear<10 && dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
                date  = (year1 + "-" + "0"+monthOfYear + "-" + "0"+dayOfMonth );
            }
            else if(monthOfYear<10)
            {
                date2  = (dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" +monthOfYear + "-" + year1);
                date  = ( year1+ "-" +monthOfYear + "-" +"0"+dayOfMonth );
            }
            else {
                date2 =   (dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            tv_date.setText(date2);
            fromDate = date;
            fromDateV = date2;
           // toDate = tv_date.getText().toString();
            toDate = fromDate;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            Date date1;
            Date date3;
          /*  try {
               *//* date1 = sdf1.parse(date2);
                date3 = sdf1.parse(date);
                toDate = sdf.format(date1);
                fromDate = sdf.format(date1);*//*
            } catch (ParseException | java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

            dateChanged = true;
            lv_history_data.setSelectionAfterHeaderView();

            if (dateChanged) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = new GregorianCalendar();
                String dateV = sdf3.format(calendar.getTime());
                //String  = sdf3.format(fromDate);
                if(fromDateV.equals(dateV))
                {
                    tv_date.setText(fromDateV);
                    historyData.clear();
                    historyAdapter.notifyDataSetChanged();
                    historyData = new ArrayList<>();
                    showLoaderNew();
                    //serviceCall();
                    pageNumber =1;/*
                    toTime= tv_time_to.getText().toString();
                    fromTime = tv_time_from.getText().toString();*/
                    fromDateforCall = fromDate+" "+fromTimeForCall;
                    toDateforCall = fromDate +" "+toTimeforCall;
                    loadNextDataFromApiForHistory(pageNumber, 0);
                }
                else {
                    historyData.clear();
                    historyAdapter.notifyDataSetChanged();
                    historyData = new ArrayList<>();
                    showLoaderNew();
                    //serviceCall();
                    pageNumber =1;/*
                    toTime= tv_time_to.getText().toString();
                    fromTime = tv_time_from.getText().toString();*/
                    fromDateforCall = fromDate+" "+fromTimeForCall;
                    toDateforCall = fromDate +" "+toTimeforCall;
                    loadNextDataFromApiForHistory(pageNumber, 0);
                }
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    private void dataResponse(CurrentDataResultObject currentDataResultObject) throws java.text.ParseException {
        //Success message
        tv_deviceId.setText(currentDataResultObject.getInventoryID());
        //Constants.DEVICE_IN_USE= currentDataResultObject.getDeviceName();
        MApplication.setString(context, Constants.DEVICE_IN_USE, currentDataResultObject.getDeviceName());
        Label = currentDataResultObject.getDeviceName();
        MApplication.setString(context, Constants.InventoryName, currentDataResultObject.getInventoryID());
        tv_deviceName1.setText(MApplication.getString(context, Constants.DEVICE_IN_USE));
        if(currentDataResultObject.getValues().size()>0)
        {
            String dateToParse = currentDataResultObject.getValues().get(0).getDate();
            String dateOfProvision = currentDataResultObject.getValues().get(0).getProvisionedOn();
            //dateToParse.replaceAll("AMP", "");
            DateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            //:String inputDateStr = dateToParse;
            Date date = inputFormat.parse(dateToParse);
            Date date1 = inputFormat.parse(dateOfProvision);
            String outputDateStr = outputFormat.format(date);
            String outputProvisionedOn= outputFormat.format(date1);
            tv_last_sync.setText(outputDateStr);
            tv_provisioned_on.setText(outputProvisionedOn);
        }



        if(channelList.size()>0)
        {
            for (int k=0; k<currentDataResultObject.getValues().size(); k++)
            {
                if(currentDataResultObject.getValues().get(k).getChannelNumber()==3)
                {
                    motorStatus = currentDataResultObject.getValues().get(k).getValue();
                    switch (Integer.parseInt(currentDataResultObject.getValues().get(k).getValue()))
                    {
                        case 0:
                        case 6:
                        case 3:
                            iv_motor_on.setImageDrawable(round_red);
                            iv_motor_off.setImageDrawable(round_dg);
                            break;
                        case 1:
                        case 5:
                        case 4:
                            iv_motor_on.setImageDrawable(round_dg);
                            iv_motor_off.setImageDrawable(round_red);
                            break;
                        case 2:
                            iv_motor_on.setImageDrawable(round_dg);
                            iv_motor_off.setImageDrawable(round_dg);
                            break;
                    }
            }
                if(currentDataResultObject.getValues().get(k).getChannelNumber()==4)
                {
                    motorReason = currentDataResultObject.getValues().get(k).getValue();
                    switch (Integer.parseInt(currentDataResultObject.getValues().get(k).getValue()))
                    {
                        case 0:
                        case 2:
                            iv_auto_remote.setImageDrawable(round_red);
                            iv_manual.setImageDrawable(round_dg);
                            isManualMode = false;
                            break;
                        case 1:
                            iv_auto_remote.setImageDrawable(round_dg);
                            iv_manual.setImageDrawable(round_red);
                            isManualMode = true;
                            break;
                        case 3:
                            iv_auto_remote.setImageDrawable(round_dg);
                            iv_manual.setImageDrawable(round_red);
                            isManualMode = false;
                            break;
                    }
                    switch (Integer.parseInt(currentDataResultObject.getValues().get(k).getValue()))
                    {
                        case 0:
                        case 1:
                            iv_remote.setImageDrawable(round_red);
                            iv_manual_reason.setImageDrawable(round_dg);
                            iv_auto.setImageDrawable(round_dg);
                            iv_motor_airlock.setImageDrawable(round_dg);
                            iv_neutral.setImageDrawable(round_dg);
                            break;
                        case 3:
                        case 4:
                            iv_remote.setImageDrawable(round_dg);
                            iv_manual_reason.setImageDrawable(round_red);
                            iv_auto.setImageDrawable(round_dg);
                            iv_motor_airlock.setImageDrawable(round_dg);
                            iv_neutral.setImageDrawable(round_dg);
                            break;
                        case 2:
                        case 5:
                        case 6:
                            iv_auto.setImageDrawable(round_red);
                            iv_remote.setImageDrawable(round_dg);
                            iv_manual_reason.setImageDrawable(round_dg);
                            iv_motor_airlock.setImageDrawable(round_dg);
                            iv_neutral.setImageDrawable(round_dg);
                            break;
                        case 7:
                            iv_motor_airlock.setImageDrawable(round_red);
                            iv_auto.setImageDrawable(round_dg);
                            iv_remote.setImageDrawable(round_dg);
                            iv_manual_reason.setImageDrawable(round_dg);
                            iv_neutral.setImageDrawable(round_dg);
                            break;


                    }
                }
            }
        }

        for (int i=0; i<channelList.size(); i++)
        {
            if(channelList.get(i).getCHANNELDESCRIPTION().contains("LEVEL"))
            {
                for (int k=0; k<currentDataResultObject.getValues().size(); k++)
                {
                    if(currentDataResultObject.getValues().get(k).getChannelNumber()==channelList.get(i).getCHANNELNUMBER())
                    {
                        CurrentDataValueObject object = new CurrentDataValueObject();
                        object.setChannelDefaultValue(channelList.get(i).getCHANNELDEFAULTVALUE());
                        object.setChannelDescription(currentDataResultObject.getValues().get(k).getChannelDescription());
                        object.setChannelNumber(currentDataResultObject.getValues().get(k).getChannelNumber());
                        object.setDate(currentDataResultObject.getValues().get(k).getDate());
                        object.setDisplayOrder(currentDataResultObject.getValues().get(k).getDisplayOrder());
                        object.setIcon(currentDataResultObject.getValues().get(k).getIcon());
                        object.setLabel(currentDataResultObject.getValues().get(k).getLabel());
                        object.setTime(currentDataResultObject.getValues().get(k).getTime());
                        object.setUnit_of_measure(currentDataResultObject.getValues().get(k).getUnit_of_measure());
                        object.setProvisionedOn(currentDataResultObject.getValues().get(k).getProvisionedOn());
                        object.setValue(currentDataResultObject.getValues().get(k).getValue());
                        object.setMinValue(channelList.get(i).getMinValue());
                        object.setMaxValue(channelList.get(i).getMaxValue());
                        currentWaterLevelList.add(object);
                    }

                }

                GetChannelDataWMotor model = new GetChannelDataWMotor();
                model.setCHANNELLABEL(channelList.get(i).getCHANNELLABEL());
                model.setMaxValue(channelList.get(i).getMaxValue());
                model.setMinValue(channelList.get(i).getMinValue());
                model.setCHANNELDEFAULTVALUE(channelList.get(i).getCHANNELDEFAULTVALUE());
                model.setINVENTORYID(channelList.get(i).getINVENTORYID());
                model.setINVENTORYNAME(channelList.get(i).getINVENTORYNAME());
                model.setCHANNELNUMBER(channelList.get(i).getCHANNELNUMBER());
                model.setDISPLAYNAME(channelList.get(i).getDISPLAYNAME());
                model.setCHANNELDESCRIPTION(channelList.get(i).getCHANNELDESCRIPTION());
                model.setCHANNELICON(channelList.get(i).getCHANNELICON());
                model.setUNITOFMEASURE(channelList.get(i).getUNITOFMEASURE());

                listForGrid.add(model);
            }
            if(currentWaterLevelList.size()>0)
            {
                gridAdapterWaterLevel = new GridAdapterWaterMotor(Dashboard_Water_Motor.this, R.layout.grid_item_water_level, currentWaterLevelList);
                grid_water_level.setAdapter(gridAdapterWaterLevel);
            }
        }
    }

    private void goto_login_activity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }


    private void serviceCallForChannelInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.USER_MGMT_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AccountDetailsInterface service = retrofit.create(AccountDetailsInterface.class);
        //Call<AccountsListMainObject> call = service.getUserAccountDetails(UserId);
        Call<JsonElement> call = service.getChannelInfoWaterMotor(UserId, authorityKey, inventoryName);
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
                    if(errorCode==0)
                    {
                        JsonArray result = jsonObject.get("result").getAsJsonArray();
                        if(result.size()>0)
                        {
                            for (int i=0; i<result.size(); i++)
                            {
                                JsonObject object = result.get(i).getAsJsonObject();
                                GetChannelDataWMotor model = new GetChannelDataWMotor();
                                if(object.has("INVENTORY_ID") && !object.get("INVENTORY_ID").isJsonNull())
                                {
                                    model.setINVENTORYID(object.get("INVENTORY_ID").getAsInt());
                                }
                                if(object.has("INVENTORY_NAME") && !object.get("INVENTORY_NAME").isJsonNull())
                                {
                                    model.setINVENTORYNAME(object.get("INVENTORY_NAME").getAsString());
                                }
                                if(object.has("DISPLAY_NAME") && !object.get("DISPLAY_NAME").isJsonNull())
                                {
                                    model.setDISPLAYNAME(object.get("DISPLAY_NAME").getAsString());
                                }
                                if(object.has("CHANNEL_NUMBER") && !object.get("CHANNEL_NUMBER").isJsonNull())
                                {
                                    model.setCHANNELNUMBER(object.get("CHANNEL_NUMBER").getAsInt());
                                }
                                if(object.has("CHANNEL_DESCRIPTION") && !object.get("CHANNEL_DESCRIPTION").isJsonNull())
                                {
                                    model.setCHANNELDESCRIPTION(object.get("CHANNEL_DESCRIPTION").getAsString());
                                }
                                if(object.has("CHANNEL_LABEL") && !object.get("CHANNEL_LABEL").isJsonNull())
                                {
                                    model.setCHANNELLABEL(object.get("CHANNEL_LABEL").getAsString());
                                }
                                if(object.has("CHANNEL_ICON") && !object.get("CHANNEL_ICON").isJsonNull())
                                {
                                    model.setCHANNELICON(object.get("CHANNEL_ICON").getAsString());
                                }
                                else {
                                    model.setCHANNELICON("");
                                }
                                if(object.has("CHANNEL_DEFAULT_VALUE") && !object.get("CHANNEL_DEFAULT_VALUE").isJsonNull())
                                {
                                    model.setCHANNELDEFAULTVALUE(object.get("CHANNEL_DEFAULT_VALUE").getAsString());
                                }
                                else {
                                    model.setCHANNELDEFAULTVALUE("");
                                }
                                if(object.has("MinValue") && !object.get("MinValue").isJsonNull())
                                {
                                    model.setMinValue(object.get("MinValue").getAsInt());
                                }
                                else {
                                    model.setMinValue(0);
                                }
                                if(object.has("MaxValue") && !object.get("MaxValue").isJsonNull())
                                {
                                    model.setMaxValue(object.get("MaxValue").getAsInt());
                                }
                                else {
                                    model.setMaxValue(0);
                                }
                                if(object.has("UNIT_OF_MEASURE") && !object.get("UNIT_OF_MEASURE").isJsonNull())
                                {
                                    model.setUNITOFMEASURE(object.get("UNIT_OF_MEASURE").getAsString());
                                }
                                else {
                                    model.setUNITOFMEASURE("");
                                }
                                channelList.add(model);
                            }
                        }
                    }
                }
                else {

                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
               hideloader();
                Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });
    }


    private void showPopupMenu(ArrayList<String> popupItem, LinearLayout viewClick) {
        PopupMenu popup = new PopupMenu(Dashboard_Water_Motor.this, viewClick);
        for(int x=0; x<popupItem.size(); x++)
        {
            popup.getMenu().add(popupItem.get(x));
        }
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            String itemTitle = String.valueOf(item.getTitle());
            if(!itemTitle.equals(""))
            {
                switch (itemTitle)
                {
                    case "ON MOTOR":
                        if(isManualMode)
                        {
                            Toast.makeText(context, "You cannot switch on motor in manual mode", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //serviceCallForOnOFFMotor("0");
                            showCustomDialog(context.getResources().getString(R.string.motor_on_confirmation), "ON", inventoryName, Label);
                        }
                        break;
                    case "OFF MOTOR":
                        if(isManualMode)
                        {
                            Toast.makeText(context, "You cannot switch off motor in manual mode", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            showCustomDialog(context.getResources().getString(R.string.motor_off_confirmation), "OFF", inventoryName, Label);
                           // serviceCallForOnOFFMotor("1");
                        }
                        break;

                }
            }
            return true;
        });
        Menu m = popup.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        popup.show();
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Light.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }



    private void serviceCallForOnOFFMotor(String onOrOff){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface .class);

        Call<JsonElement> call = service.setOnOffMotor(UserId, authorityKey, new DevicesListPostParameter(inventoryName, onOrOff));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                hideloader();
                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if(response.body()!=null) {
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject info = jsonObject.get("info").getAsJsonObject();
                    int errorCode = info.get("ErrorCode").getAsInt();
                    if(errorCode==0)
                    {
                        String errorMsg = info.get("ErrorMessage").getAsString();
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String errorMsg = info.get("ErrorMessage").getAsString();
                        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                //  Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();
                ll_main.setVisibility(View.GONE);
                rl_serverError.setVisibility(View.VISIBLE);
            }

        });

    }


    private void showCustomDialog(String text, String onOrOff, String deviceNum, String deviceName) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_motor, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        TextView tv_head = dialogView.findViewById(R.id.tv_head);
        TextView tv_alertText = dialogView.findViewById(R.id.tv_alertText);
        TextView tv_device_num = dialogView.findViewById(R.id.tv_device_num);
        TextView tv_device_name = dialogView.findViewById(R.id.tv_device_name);
        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_off_on = dialogView.findViewById(R.id.button_off_on);
        tv_alertText.setText(text);
        tv_head.setTypeface(faceLight);
        tv_alertText.setTypeface(faceLight);
        tv_device_num.setTypeface(faceLight);
        tv_device_name.setTypeface(faceLight);
        button_cancel.setTypeface(faceLight);
        button_off_on.setTypeface(faceLight);

        tv_device_num.setText("Device No. : "+deviceNum);
        tv_device_name.setText("Device Name : "+deviceName);

        if(onOrOff.equals("ON"))
        {
            button_off_on.setText("ON MOTOR");
        }
        else if(onOrOff.equals("OFF"))
        {
            button_off_on.setText("OFF MOTOR");
        }
        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        button_off_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onOrOff.equals("ON"))
                {
                   serviceCallForOnOFFMotor("0");
                    alertDialog.dismiss();
                }
                else if(onOrOff.equals("OFF"))
                {
                    serviceCallForOnOFFMotor("1");
                    alertDialog.dismiss();
                }
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              alertDialog.dismiss();
            }
        });


    }


}
