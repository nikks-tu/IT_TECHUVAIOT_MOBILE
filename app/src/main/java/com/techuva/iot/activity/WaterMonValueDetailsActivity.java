package com.techuva.iot.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.adapter.GridAdapterWaterFlowOneDayValue;
import com.techuva.iot.adapter.ListAdapter;
import com.techuva.iot.adapter.MonthDataRcvAdapter;
import com.techuva.iot.adapter.WaterMonDataAdapter;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.listener.EndlessScrollListener;
import com.techuva.iot.listener.RecyclerItemClickListener;
import com.techuva.iot.listener.Update;
import com.techuva.iot.model.MonthsObject;
import com.techuva.iot.model.WaterMonCurrentDataPostParameter;
import com.techuva.iot.model.YearsObject;
import com.techuva.iot.response_model.WaterMonDataResultObject;
import com.techuva.iot.response_model.WaterMonMainObject;
import com.techuva.iot.response_model.WaterMonValuesObject;
import com.techuva.iot.utils.BubbleDrawable;
import com.techuva.iot.utils.views.MApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WaterMonValueDetailsActivity extends AppCompatActivity implements Update {

    LinearLayout toolbar_layout, ll_data_not_found, ll_back_btn;
    CardView cv_year;
    Toolbar toolbar;
    GridView grid_channel_total_value;
    HorizontalCalendar horizontalCalendar;
    static String fromDate;
    static String toDate;
    String selectedDate;
    TextView tv_year, tv_minute_flow, tv_hour_flow;
    LinearLayout ll_manjeera_water, ll_ground_water;
    LinearLayout ll_manjeera_txt, ll_ground_txt, ll_main, ll_bottom_view;
    LinearLayout ll_minute, ll_hour;
    TextView tv_manjeera_txt, tv_manjeera_value, tv_ground_txt, tv_ground_value;
    TextView tv_receive_date, tv_manjeera, tv_ground;
    RecyclerView rcv_months;
    String deviceId, userId;
    Context context;
    int UserId;
    String authorityKey ="";
    String authorityKeyForRefresh ="";
    String grantType = "";
    String grantTypeRefresh = "";
    String refreshToken="";
    ListView lv_data;
    View view_minute_data, view_hour_data;
    WaterMonDataAdapter adapter;
    private EndlessScrollListener scrollListener;
    private AnimationDrawable animationDrawable;
    public Dialog dialog;
    GridAdapterWaterFlowOneDayValue gridAdapterChannelValues;
    int pageNumber;
    ArrayList<WaterMonDataResultObject> resultList;
    ArrayList<WaterMonValuesObject> topDataList;
    String day="";
    String month ="";
    String year ="";
    String selectedYear="";
    int listCount = 0;
    ListAdapter years_adapter;
    ArrayList<YearsObject> yearsList;
    ArrayList<MonthsObject> monthsList;
    MonthDataRcvAdapter monthsAdapter;
    String channelNum = "1,2";
    String flag = "0";
    Boolean isTwoChannel= false;
    String pagePerCount = "50";
    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_mon_value_details);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        } else {
            selectedDate = extras.getString("selectedDate");
            day = extras.getString("Day");
            month = extras.getString("Month");
            year = extras.getString("Year");
        }
        initialize();
        showLoaderNew();
        serviceCall();
        serviceCallforData();

        ll_hour.setOnClickListener(v -> {
            view_hour_data.setBackgroundColor(context.getResources().getColor(R.color.white));
            view_minute_data.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            pageNumber = 1;
            flag = "1";
            listCount = 0;
            topDataList = new ArrayList<>();
            resultList = new ArrayList<>();
            resultList.clear();
            adapter.notifyDataSetInvalidated();
            showLoaderNew();
            serviceCallforData();
            /* new Handler().postDelayed(() ->
            {
                serviceCall();
                serviceCallforData(); }, 1000);*/

        });

        ll_minute.setOnClickListener(v -> {
            view_hour_data.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            view_minute_data.setBackgroundColor(context.getResources().getColor(R.color.white));
            pageNumber = 1;
            flag = "0";
            listCount = 0;
            topDataList = new ArrayList<>();
            resultList = new ArrayList<>();
            resultList.clear();
            adapter.notifyDataSetChanged();
            showLoaderNew();
            serviceCallforData();/*
            new Handler().postDelayed(() ->
            {
                serviceCall();
                serviceCallforData(); }, 1000);*/
        });

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                fromDate = DateFormat.format("yyyy-MM-dd", date).toString();
                toDate = DateFormat.format("yyyy-MM-dd", date).toString();
                selectedDate = DateFormat.format("yyyy-MM-dd", date).toString();
                month = DateFormat.format("MM", date).toString();
                //Toast.makeText(context, selectedDate, Toast.LENGTH_SHORT).show();
                monthsAdapter.setDefaultSelection(Integer.parseInt(month)-1);
                monthsAdapter.notifyDataSetChanged();
                topDataList.clear();
                resultList.clear();
                topDataList = new ArrayList<>();
                resultList = new ArrayList<>();
                listCount = 0;
                pageNumber = 1;
                showLoaderNew();
                topDataList.clear();
                resultList.clear();
                serviceCall();
                serviceCallforData();
               /* if(adapter!=null)
                {
                    adapter.notifyDataSetChanged();
                }*/
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {
                //Toast.makeText(context, selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });



        cv_year.setOnClickListener(v -> {
            showCustomDialog(Calendar.getInstance().get(Calendar.YEAR));
           // createDialogWithoutDateField().show();
        });
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
        adapter = new WaterMonDataAdapter(R.layout.item_water_mon_data, this, flag,  resultList, isTwoChannel);
        lv_data.setAdapter(adapter);
        lv_data.setOnScrollListener(scrollListener);

        BubbleDrawable myBubble = new BubbleDrawable(BubbleDrawable.CENTER);
        myBubble.setCornerRadius(0);
        myBubble.setPointerAlignment(BubbleDrawable.CENTER);
        myBubble.setPadding(15,20, 15,0);
        ll_ground_txt.setBackground(myBubble);
        ll_ground_txt.setGravity(View.TEXT_ALIGNMENT_CENTER);
        ll_manjeera_txt.setBackground(myBubble);
        ll_manjeera_txt.setGravity(View.TEXT_ALIGNMENT_CENTER);

        ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
            }
        });

        rcv_months.addOnItemTouchListener(new RecyclerItemClickListener(context, rcv_months, new RecyclerItemClickListener.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(View view, int position) {
                monthsAdapter.setDefaultSelection(position);
                month = String.valueOf(position+1);
                monthsAdapter.notifyDataSetChanged();
                Calendar defaultDate = Calendar.getInstance();
                defaultDate.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
                startDate.add(Calendar.MONTH, 0);
                endDate.add(Calendar.MONTH, 0);
                horizontalCalendar.setRange(startDate, endDate);
                horizontalCalendar.selectDate(defaultDate,  true);
                selectedDate = year+"-"+month+"-"+day;
                pageNumber = 1;
                resultList.clear();
                topDataList.clear();
                serviceCall();
                serviceCallforData();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        ll_back_btn.setOnClickListener(v -> finish());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initialize() {
        context = WaterMonValueDetailsActivity.this;
        toolbar = findViewById(R.id.toolbar);
        toolbar_layout = findViewById((R.id.toolbar_layout));
        cv_year = findViewById(R.id.cv_year);
        ll_minute = findViewById(R.id.ll_minute);
        ll_hour = findViewById(R.id.ll_hour);
        ll_bottom_view = findViewById(R.id.ll_bottom_view);
        ll_back_btn = findViewById(R.id.ll_back_btn);
        ll_manjeera_water = findViewById(R.id.ll_manjeera_water);
        ll_ground_water = findViewById(R.id.ll_ground_water);
        ll_manjeera_txt = findViewById(R.id.ll_manjeera_txt);
        ll_ground_txt = findViewById(R.id.ll_ground_txt);
        ll_data_not_found = findViewById(R.id.ll_data_not_found);
        ll_main = findViewById(R.id.ll_main);
        tv_manjeera_txt = findViewById(R.id.tv_manjeera_txt);
        tv_manjeera_value = findViewById(R.id.tv_manjeera_value);
        tv_ground_txt = findViewById(R.id.tv_ground_txt);
        tv_ground_value = findViewById(R.id.tv_ground_value);
        tv_receive_date = findViewById(R.id.tv_receive_date);
        tv_manjeera = findViewById(R.id.tv_manjeera);
        tv_ground = findViewById(R.id.tv_ground);
        tv_year = findViewById(R.id.tv_year);
        tv_minute_flow = findViewById(R.id.tv_minute_flow);
        tv_hour_flow = findViewById(R.id.tv_hour_flow);
        cv_year = findViewById(R.id.cv_year);
        lv_data= findViewById(R.id.lv_data);
        rcv_months = findViewById(R.id.rcv_months);
        view_minute_data = findViewById(R.id.view_minute_data);
        view_hour_data = findViewById(R.id.view_hour_data);
        grid_channel_total_value = findViewById(R.id.grid_channel_total_value);
        deviceId = MApplication.getString(context, Constants.DeviceID);
        userId = "1001";
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        authorityKeyForRefresh = Constants.AuthorizationKey;
        grantType = Constants.GrantType;
        grantTypeRefresh = Constants.GrantTypeRefresh;
        refreshToken = MApplication.getString(context, Constants.RefreshToken);
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        pageNumber = 1;
        resultList  = new ArrayList<>();
        topDataList = new ArrayList<>();
        yearsList = new ArrayList<>();
        monthsList = new ArrayList<>();
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setTypeface();
        startDate.add(Calendar.YEAR, -1 );
        endDate.add(Calendar.DAY_OF_MONTH, 0);
        addMonthsData();
        Calendar defaultDate = Calendar.getInstance();
        defaultDate.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
        tv_year.setText(year);
        selectedYear = year;
        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .textSize(20f, 20f, 20f)
                .colorTextBottom(0, context.getResources().getColor(R.color.white))
                .colorTextTop(0, context.getResources().getColor(R.color.white))
                .selectorColor(getResources().getColor(R.color.colorPrimary))
                .showBottomText(false)
                .showTopText(false)
                .end()
                .defaultSelectedDate(defaultDate)
                .build();
    }

    private void addMonthsData() {

        for (int i=1; i<13; i++)
        {
            MonthsObject object = new MonthsObject();
            if(i==1)
            {
                object.setMonthName("JAN");
                object.setMonthId("01");
            }
            else if(i==2)
            {
                object.setMonthName("FEB");
                object.setMonthId("02");
            }
            else if(i==3)
            {
                object.setMonthName("MAR");
                object.setMonthId("03");
            }
            else if(i==4)
            {
                object.setMonthName("APR");
                object.setMonthId("04");
            }
            else if(i==5)
            {
                object.setMonthName("MAY");
                object.setMonthId("05");
            }
            else if(i==6)
            {
                object.setMonthName("JUN");
                object.setMonthId("06");
            }
            else if(i==7)
            {
                object.setMonthName("JUL");
                object.setMonthId("07");
            }
            else if(i==8)
            {
                object.setMonthName("AUG");
                object.setMonthId("08");
            }
            else if(i==9)
            {
                object.setMonthName("SEP");
                object.setMonthId("09");
            }
            else if(i==10)
            {
                object.setMonthName("OCT");
                object.setMonthId("10");
            }
            else if(i==11)
            {
                object.setMonthName("NOV");
                object.setMonthId("11");
            }
            else {
                object.setMonthName("DEC");
                object.setMonthId("12");
            }
            monthsList.add(object);
        }

        if(monthsList.size()>0)
        {
            monthsAdapter = new MonthDataRcvAdapter(context, monthsList, Integer.parseInt(month)-1);
            LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            manager.scrollToPosition(Integer.parseInt(month)-1);
            rcv_months.setLayoutManager(manager);
            monthsAdapter.setDefaultSelection(Integer.parseInt(month)-1);
            rcv_months.setAdapter(monthsAdapter);
        }
        rcv_months.getLayoutManager().scrollToPosition(Integer.parseInt(month)-1);
        rcv_months.getLayoutManager().setItemPrefetchEnabled(true);
    }

    private void setTypeface() {
        Typeface faceLight = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Light.otf");
        tv_manjeera_txt.setTypeface(faceLight);
        tv_manjeera_value.setTypeface(faceLight);
        tv_ground_txt.setTypeface(faceLight);
        tv_ground_value.setTypeface(faceLight);
        tv_receive_date.setTypeface(faceLight);
        tv_manjeera.setTypeface(faceLight);
        tv_ground.setTypeface(faceLight);
        tv_year.setTypeface(faceLight);
        tv_minute_flow.setTypeface(faceLight);
        tv_minute_flow.setTypeface(faceLight);
        tv_hour_flow.setTypeface(faceLight);
    }

    public void showLoaderNew() {
        runOnUiThread(new WaterMonValueDetailsActivity.Runloader(getResources().getString(R.string.loading)));
    }

    @Override
    public void selectedYear(String number) {
        selectedYear = number;
       // Toast.makeText(context, ""+ selectedYear, Toast.LENGTH_SHORT).show();
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


    private void serviceCall(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface .class);
        Call<WaterMonMainObject> call = service.getWaterMonValues(UserId, authorityKey, new WaterMonCurrentDataPostParameter(deviceId, selectedDate, selectedDate));
        call.enqueue(new Callback<WaterMonMainObject>() {
            @Override
            public void onResponse(Call<WaterMonMainObject> call, Response<WaterMonMainObject> response) {
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
                        //Toast.makeText(context, ""+response.body().getInfo().getErrorCode(), Toast.LENGTH_SHORT).show();

                        if(response.body().getResult()!=null)
                        {
                           if(response.body().getResult().getValuesByDate().size()>0)
                           {
                               topDataList = response.body().getResult().getValuesByDate().get(0).getValues();
                               if(topDataList.size()>0)
                               {
                                   gridAdapterChannelValues = new GridAdapterWaterFlowOneDayValue(WaterMonValueDetailsActivity.this, R.layout.grid_item_channel_water, topDataList);
                                   grid_channel_total_value.setAdapter(gridAdapterChannelValues);
                                   tv_manjeera.setText(topDataList.get(0).getLabel()+" (L)");
                               }
                               if(topDataList.size()>1)
                               {
                                   isTwoChannel = true;
                                   tv_ground.setVisibility(View.VISIBLE);
                                   tv_ground.setText(topDataList.get(1).getLabel()+" (L)");
                               }

                           }
                        }
                    }
                    else {
                        if (response.body().getInfo().getErrorMessage().equals(Constants.TokenExpireMsg)) {
                            Toast.makeText(context, "Token Expire, Plz Re-login", Toast.LENGTH_SHORT).show();
                            //goto_login_activity();
                        }
                        else if (response.body().getInfo().getErrorMessage().equals(Constants.NoUserRole)) {
                            Toast.makeText(context, "No Role Assigned to User", Toast.LENGTH_SHORT).show();
                            //goto_login_activity();
                        }
                    }

                }else {
                 //   Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<WaterMonMainObject> call, Throwable t) {
                hideloader();
            }

        });

    }

    private void serviceCallforData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface.class);
        Call<JsonElement> call = service.getAllDataWaterMon(UserId, authorityKey, new WaterMonCurrentDataPostParameter(deviceId, channelNum, selectedDate, pagePerCount, String.valueOf(pageNumber), flag));
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
                        JsonObject jObj = jsonObject.getAsJsonObject("info");
                        if (jObj.get("ErrorCode").getAsInt() == 0) {
                            listCount = jObj.get("ListCount").getAsInt();
                            int page = jObj.get("PageNumber").getAsInt();
                            if(page==1)
                            {
                                resultList.clear();
                                }
                            lv_data.setVisibility(View.VISIBLE);
                            ll_data_not_found.setVisibility(View.GONE);
                            JsonArray resultObject = jsonObject.getAsJsonArray("result");
                            for (int i=0; i<resultObject.size(); i++)
                            {
                                JsonObject object = resultObject.get(i).getAsJsonObject();
                                WaterMonDataResultObject result = new WaterMonDataResultObject();
                                result.setTime(object.get("Time").getAsString());
                                if(object.has("Data1"))
                                {
                                    result.setData1(object.get("Data1").getAsString());
                                }
                                else {
                                    result.setData1("--");
                                }
                                if(object.has("Data2"))
                                {
                                    result.setData2(object.get("Data2").getAsString());
                                }
                                else {
                                    result.setData2("--");
                                }
                                resultList.add(result);
                            }
                            if(page==1)
                            {
                                adapter = new WaterMonDataAdapter(R.layout.item_water_mon_data, context, flag, resultList, isTwoChannel);
                                lv_data.setAdapter(adapter);
                            }
                            if(adapter!=null)
                            {
                                adapter.notifyDataSetChanged();
                            }
                            else
                            {
                                adapter.notifyDataSetChanged();
                            }

                        } else if (jObj.get("ErrorCode").getAsInt() == 1) {
                            ll_data_not_found.setVisibility(View.VISIBLE);
                            lv_data.setVisibility(View.GONE);

                        } else {

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

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                     for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                            datePicker.findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                            datePicker.findViewById(Resources.getSystem().getIdentifier("month", "id", "android")).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
        }
        return dpd;
    }

    private void loadNextDataFromApi(int page, int delay) {
        pageNumber = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                serviceCallforData();
            }
        }, delay);
    }

    private void showCustomDialog(int text) {
        //text = Integer.parseInt(year);
        yearsList = new ArrayList<>();
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.year_popup, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);
        ListView lv_years = dialogView.findViewById(R.id.lv_years);
        TextView button_ok = dialogView.findViewById(R.id.button_ok);
        YearsObject yearsObject = new YearsObject();
        //text = 2024;
        yearsObject.setYear(String.valueOf(text));
        yearsList.add(yearsObject);
        int tempYear=2019;

        for(int i=1; i<text; i++)
        {
            if(tempYear<text)
            {
                yearsObject = new YearsObject();
                yearsObject.setYear(String.valueOf(text-1));
                text = text-1;
                yearsList.add(yearsObject);
            }

            /*if(tempYear==0)
            {
                yearsObject = new YearsObject();
                yearsObject.setYear(String.valueOf(2019+1));
                tempYear = 2019+1;
                yearsList.add(yearsObject);
            }

            else {
                yearsObject = new YearsObject();
                yearsObject.setYear(String.valueOf(tempYear+1));
                tempYear = tempYear+1;
                yearsList.add(yearsObject);
            }*/
        }

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        years_adapter = new ListAdapter(context, R.layout.item_years, yearsList, year, this);

        lv_years.setAdapter(years_adapter);
        lv_years.setSelector(R.color.colorPrimary);

        button_ok.setOnClickListener(v -> {
            if(!selectedYear.equals(""))
            {
                tv_year.setText(selectedYear);
                year = selectedYear;
                selectedDate = year+"-"+month+"-"+day;
                //month = String.valueOf(Integer.parseInt(month)-1);
                monthsAdapter.notifyDataSetChanged();
               // Toast.makeText(context, ""+isTwoChannel, Toast.LENGTH_SHORT).show();
                showLoaderNew();
                serviceCall();
                serviceCallforData();
            }
            else {
                tv_year.setText(year);
                year = year;

            }
            alertDialog.dismiss();
        });

    }


}
