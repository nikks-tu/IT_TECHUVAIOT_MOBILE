package com.techuva.iot.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.techuva.iot.R;
import com.techuva.iot.adapter.ChannelDataAdapter;
import com.techuva.iot.adapter.ChannelNamesRcvAdapter;
import com.techuva.iot.api_interface.HistoryDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.listener.EndlessScrollListener;
import com.techuva.iot.model.HistoryDataInfoObject;
import com.techuva.iot.model.HistoryDataMainObject;
import com.techuva.iot.model.HistoryDataObject;
import com.techuva.iot.model.HistoryDataPostParamter;
import com.techuva.iot.model.HistoryDataResultObject;
import com.techuva.iot.model.HistoryDataValueObject;
import com.techuva.iot.model.HistoryViewPaginationObject;
import com.techuva.iot.utils.views.MApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class History_View_Activity extends AppCompatActivity {

    TextView tv_to_date;
    static String fromDate;
    static String toDate;
    public Dialog dialog;
    LinearLayout ll_day_view, ll_select_date, ll_tab_data_not_found, ll_headings, ll_back_btn;
    RelativeLayout ll_root_day_view;
    TextView tv_from_date;
    TextView tv_date_heading;
    TextView tv_time_heading;
    TextView tv_value_heading;
    TextView tv_data_not_found;
    ImageView iv_to_date, iv_from_date;
    Button btn_retry;
    RecyclerView rcv_day_data;
    HorizontalScrollView hv_list_channels;
    ListView channel_list;
    ListView lv_data;
    String deviceId, pagePerCount;
    int pageNumber = 1;
    Context context;
    ChannelNamesRcvAdapter channelNamesAdapter;
    int channelPosition = 0;
    int selectedTabValue = 1;
    Boolean channelNames = false;
    Boolean dateChanged = false;
    RecyclerView rcv_channel_names;
    List<HistoryDataObject> list;
    List<HistoryDataObject> listforValues;
    TabLayout tabLayout;
    ArrayList<HistoryDataObject> arrayList = new ArrayList<>();
    ArrayList<HistoryDataObject> arrayListforValues = new ArrayList<>();
    List<HistoryDataValueObject> valuelist = new ArrayList<>();
    ArrayList<HistoryDataValueObject> valueObjectArrayList = new ArrayList<>();
    List<HistoryViewPaginationObject> getDataPageVise = new ArrayList<>();
    ChannelDataAdapter adapter;
    Toolbar toolbar;
    ImageView iv_back_btn;
    int listCount = 0;
    int day, year, month;
    Calendar calendar;
    List<HistoryDataMainObject> arrayInfo = new ArrayList<>();
    HistoryDataInfoObject info;
    HistoryViewPaginationObject pageViseData;
    Boolean isSecond = false;
    HistoryDataInterface service;
    Call<HistoryDataMainObject> call;
    Call<HistoryDataMainObject> call1;
    int UserId;
    String authorityKey = "";
    String grantType = "";
    private boolean loading = true;
    private boolean tabChanged = false;
    private AnimationDrawable animationDrawable;
    private EndlessScrollListener scrollListener;
    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> {
        // TODO Auto-generated method stub
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(arg1, arg2 + 1, arg3);
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_view);
        Init_Views();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {

            //displayValue = 1;
        } else {
            // displayValue = extras.getInt("TabId");
            //selectedTabId = displayValue;
            selectedTabValue = extras.getInt("TabId");
        }
        setTypeface();
        showLoaderNew();
        serviceCallforChannelNames();
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

        adapter = new ChannelDataAdapter(R.layout.item_hr_channel_data, this, valuelist);
        lv_data.setAdapter(adapter);
        lv_data.setOnScrollListener(scrollListener);
        ll_select_date.setOnClickListener(v -> {
            if (month < 10) {
                showDate(year, month + 1, day);
            } else
                showDate(year, month, day);
        });

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        ll_back_btn.setOnClickListener(v -> {
            finish();
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!((Activity) context).isFinishing()) {
                    new Handler().postDelayed(
                            () -> {
                                showLoaderNew();
                            }, 150);
                    if (call1 != null) {
                        call1.cancel();
                        pageNumber = 0;
                        scrollListener.resetValues();
                        new Handler().postDelayed(
                                () -> {
                                    selectedTabValue = Integer.parseInt(Objects.requireNonNull(tab.getContentDescription()).toString());
                                    scrollListener.onLoadMore(pageNumber, 0);
                                }, 200);
                    } else {
                        if (!isSecond) {
                            pageNumber = 1;
                            loadNextDataFromApi(pageNumber, 500);
                        }

                    }
                }
                tabChanged = true;
                lv_data.setSelectionAfterHeaderView();
                listforValues.clear();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }


            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


    }

    private void loadNextDataFromApi(int page, int delay) {
        pageNumber = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                serviceCall();
            }
        }, delay);
    }

    private void showDate(int year, final int month, int day) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                String date = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                String date2 = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                tv_to_date.setText(date2);
                fromDate = date;
                toDate = date;
                dateChanged = true;
                lv_data.setSelectionAfterHeaderView();
                if (dateChanged) {
                    if (channelNames) {
                        listforValues.clear();
                        /* scrollListener.resetValues();
                         */
                        showLoaderNew();
                        loadNextDataFromApi(1, 0);
                    } else {
                        showLoaderNew();
                        serviceCallforChannelNames();
                    }
                }

            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void Init_Views() {

        context = History_View_Activity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iv_back_btn = findViewById(R.id.iv_back_btn);
        info = new HistoryDataInfoObject();
        pageViseData = new HistoryViewPaginationObject();
        list = new ArrayList<>();
        listforValues = new ArrayList<>();
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = new GregorianCalendar();
        fromDate = sdf4.format(calendar.getTime());
        toDate = fromDate;
        ll_day_view = findViewById(R.id.ll_day_view);
        ll_select_date = findViewById(R.id.ll_select_date);
        ll_tab_data_not_found = findViewById(R.id.ll_tab_data_not_found);
        ll_headings = findViewById(R.id.ll_headings);
        ll_root_day_view = findViewById(R.id.ll_root_day_view);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_from_date = findViewById(R.id.tv_from_date);
        iv_to_date = findViewById(R.id.iv_to_date);
        iv_from_date = findViewById(R.id.iv_from_date);
        rcv_day_data = findViewById(R.id.rcv_day_data);
        hv_list_channels = findViewById(R.id.hv_list_channels);
        channel_list = findViewById(R.id.channel_list);
        lv_data = findViewById(R.id.lv_data);
        ll_back_btn = findViewById(R.id.ll_back_btn);
        rcv_channel_names = findViewById(R.id.rcv_channel_names);
        tv_date_heading = findViewById(R.id.tv_date_heading);
        tv_time_heading = findViewById(R.id.tv_time_heading);
        tv_value_heading = findViewById(R.id.tv_value_heading);
        tv_data_not_found = findViewById(R.id.tv_data_not_found);
        btn_retry = findViewById(R.id.btn_retry);
        deviceId = MApplication.getString(context, Constants.DeviceID);
        pagePerCount = "20";
        String date = sdf3.format(calendar.getTime());
        tv_to_date.setText(date);
        tabLayout = findViewById(R.id.tabs);
        authorityKey = "Bearer " + MApplication.getString(context, Constants.AccessToken);
        grantType = Constants.GrantType;
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));

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

    //Test Db url http://182.18.177.27:8590/getDataDataByDate

    private void serviceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(HistoryDataInterface.class);

        //call1 = service.getStringScalar(new HistoryDataPostParamter(deviceId, fromDate, toDate, pagePerCount, String.valueOf(pageNumber)));
        call1 = service.getStringScalarWithSession(UserId, authorityKey, new HistoryDataPostParamter(deviceId, fromDate, toDate, pagePerCount, String.valueOf(pageNumber)));
        if (call1.isCanceled()) {
            call1.clone().enqueue(new Callback<HistoryDataMainObject>() {
                @Override
                public void onResponse(Call<HistoryDataMainObject> call, Response<HistoryDataMainObject> response) {
                    //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )

                    if (response.body() != null) {

                        if (response.body().getINFO().getErrorCode().equals(0)) {
                            listCount = response.body().getINFO().getListCount();
                            if (response.body().getRESULT() != null) {
                                ll_tab_data_not_found.setVisibility(View.GONE);
                                lv_data.setVisibility(View.VISIBLE);
                                temp(response.body());
                            }
                        } else if (response.body().getINFO().getErrorCode().equals(1)) {
                            hideloader();
                                lv_data.setVisibility(View.GONE);
                                ll_tab_data_not_found.setVisibility(View.VISIBLE);
                                tv_data_not_found.setText(response.body().getINFO().getErrorMessage());
                        }

                    } else {
                        hideloader();
                        lv_data.setVisibility(View.GONE);
                        ll_tab_data_not_found.setVisibility(View.VISIBLE);

                    }

                }

                @Override
                public void onFailure(Call<HistoryDataMainObject> call1, Throwable t) {
                    hideloader();
                /*lv_data.setVisibility(View.GONE);
                ll_tab_data_not_found.setVisibility(View.VISIBLE);*/
                }

            });
        } else if (!call1.isExecuted()) {
            call1.enqueue(new Callback<HistoryDataMainObject>() {
                @Override
                public void onResponse(Call<HistoryDataMainObject> call, Response<HistoryDataMainObject> response) {
                    //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )

                    if (response.body() != null) {
                        //  hideloader();
                        if (response.body().getINFO().getErrorCode().equals(0)) {
                            listCount = response.body().getINFO().getListCount();
                            if (response.body().getRESULT() != null) {
                                ll_tab_data_not_found.setVisibility(View.GONE);
                                lv_data.setVisibility(View.VISIBLE);
                                temp(response.body());
                            }
                        } else if (response.body().getINFO().getErrorCode().equals(1)) {
                            hideloader();
                            if (response.body().getINFO().getErrorMessage().equals(Constants.TokenExpireMsg)) {
                                Toast.makeText(context, "Token Expire, Plz Relogin", Toast.LENGTH_SHORT).show();
                                goto_login_activity();
                            } else {
                                lv_data.setVisibility(View.GONE);
                                ll_tab_data_not_found.setVisibility(View.VISIBLE);
                                tv_data_not_found.setText(response.body().getINFO().getErrorMessage());
                            }
                        }

                    } else {
                        hideloader();
                        lv_data.setVisibility(View.GONE);
                        ll_tab_data_not_found.setVisibility(View.VISIBLE);

                    }

                }

                @Override
                public void onFailure(Call<HistoryDataMainObject> call1, Throwable t) {
                    hideloader();
                /*lv_data.setVisibility(View.GONE);
                ll_tab_data_not_found.setVisibility(View.VISIBLE);*/
                }

            });
        }
    }

    private void serviceCallforChannelNames() {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(HistoryDataInterface.class);
        //call = service.getStringScalar(new HistoryDataPostParamter(deviceId, fromDate, toDate, pagePerCount, String.valueOf(pageNumber)));
        call = service.getStringScalarWithSession(UserId, authorityKey, new HistoryDataPostParamter(deviceId, fromDate, toDate, pagePerCount, String.valueOf(pageNumber)));
        call.enqueue(new Callback<HistoryDataMainObject>() {
            @Override
            public void onResponse(Call<HistoryDataMainObject> call, Response<HistoryDataMainObject> response) {

                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else  if (response.body() != null) {
                    hideloader();

                    if (response.body().getINFO().getErrorCode()==0) {
                        if (response.body().getRESULT() != null) {
                            ll_tab_data_not_found.setVisibility(View.GONE);
                            lv_data.setVisibility(View.VISIBLE);
                            channelNames = true;
                            dataResponse(response.body().getRESULT());
                        } else {
                            lv_data.setVisibility(View.GONE);
                            tv_data_not_found.setText(response.body().getINFO().getErrorMessage());
                            ll_tab_data_not_found.setVisibility(View.VISIBLE);
                        }
                    } else {
                        lv_data.setVisibility(View.GONE);
                        ll_tab_data_not_found.setVisibility(View.VISIBLE);
                        tv_data_not_found.setText(response.body().getINFO().getErrorMessage());
                    }

                } else {
                    hideloader();
                    lv_data.setVisibility(View.GONE);
                    ll_tab_data_not_found.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<HistoryDataMainObject> call, Throwable t) {
                hideloader();
                Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();
                lv_data.setVisibility(View.GONE);
                ll_tab_data_not_found.setVisibility(View.VISIBLE);
            }

        });
    }


    private void temp(HistoryDataMainObject result) {

        if (result.getINFO().getErrorCode() == 0) {
            info = result.getINFO();
            dataResponseforValues(result.getRESULT());
        }
    }

    private void dataResponseforValues(HistoryDataResultObject result) {
        if (!((Activity) context).isFinishing()) {
            listforValues.addAll(result.getData());
            if (listforValues.size() > 0) {
                getDataPageVise = new ArrayList<>();
                for (int s = 0; s < listforValues.size(); s++) {
                    pageViseData = new HistoryViewPaginationObject();
                    pageViseData.setDisplayValue(listforValues.get(s).getDisplayOrder());
                    pageViseData.setChannelNumber(Integer.parseInt(listforValues.get(s).getChannelNumber()));
                    pageViseData.setData(listforValues.get(s).getValues());
                    getDataPageVise.add(pageViseData);
                }
            }
            hideloader();
            if (getDataPageVise.size() > 0) {
                valuelist.clear();
                for (int i = 0; i < getDataPageVise.size(); i++) {
                    if (selectedTabValue == getDataPageVise.get(i).getChannelNumber()) {
                        valuelist.addAll(getDataPageVise.get(i).getData());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    private void dataResponse(HistoryDataResultObject result) {
        list = new ArrayList<>();
        list = result.getData();
        arrayList = new ArrayList<>();

        hideloader();
        arrayList.addAll(list);

        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                String s = arrayList.get(i).getLabel();
                s = s.replaceAll("_", " ");
                if (selectedTabValue == Integer.parseInt(arrayList.get(i).getChannelNumber())) {
                    selectedTabValue = Integer.parseInt(arrayList.get(i).getChannelNumber());
                    channelPosition = i;
                }
                tabLayout.addTab(tabLayout.newTab().setText(s).setContentDescription(arrayList.get(i).getChannelNumber()));
            }
        }
        new Handler().postDelayed(
                () -> {
                    isSecond = true;
                    Objects.requireNonNull(tabLayout.getTabAt(channelPosition)).select();
                }, 100);
        channelNamesAdapter = new ChannelNamesRcvAdapter(History_View_Activity.this, arrayList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        rcv_channel_names.setLayoutManager(mLayoutManager);
        rcv_channel_names.setAdapter(channelNamesAdapter);
    }

    private void setTypeface() {
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceBook = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Book.otf");
        Typeface faceMedium = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        tv_date_heading.setTypeface(faceMedium);
        tv_time_heading.setTypeface(faceMedium);
        tv_value_heading.setTypeface(faceMedium);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showLoaderNew() {
        runOnUiThread(new History_View_Activity.Runloader(getResources().getString(R.string.loading)));
    }

    public void hideloader() {
        runOnUiThread(() -> {
            try {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        //serviceCall();
        //lv_data.setOnScrollListener(scrollListener);
        super.onResume();

    }

    private void goto_login_activity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
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
                if (dialog == null) {
                    dialog = new Dialog(context, R.style.Theme_AppCompat_Light_DarkActionBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog.show();

                ImageView imgeView = (ImageView) dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading = (TextView) dialog
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
            } catch (Exception e) {

            }
        }
    }
}

