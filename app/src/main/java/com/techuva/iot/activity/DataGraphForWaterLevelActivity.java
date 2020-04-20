package com.techuva.iot.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.appyvet.materialrangebar.RangeBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.adapter.WaterChannelAdapter;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.api_interface.GetDataForGraph;
import com.techuva.iot.api_interface.HistoryDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ChannelUnitMeasureDO;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.model.GraphDataPostParamter;
import com.techuva.iot.model.HistoryDataInfoObject;
import com.techuva.iot.model.HistoryDataMainObject;
import com.techuva.iot.model.HistoryDataObject;
import com.techuva.iot.model.YearsObject;
import com.techuva.iot.utils.views.MApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DataGraphForWaterLevelActivity extends AppCompatActivity {

    Toolbar toolbar;
    Context context;
    FrameLayout ll_first_layout;
    LineChart chart_dailyData;
    TextView tv_selected_channel;
    LinearLayout ll_bottom_layout, ll_bar;
    CardView ll_selected_channel;
    RangeBar rangebar_final;
    HorizontalCalendar calendarView;
    RecyclerView rcv_channel_select;
    HistoryDataInterface service;
    Call<HistoryDataMainObject> call;
    ArrayList<ChannelUnitMeasureDO> unitList;
    int pageNumber = 1;
    Calendar calendar;
    static String fromDate;
    static String toDate;
    String deviceId, inventoryName, pagePerCount;
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    String fromTime = "00:00:00";
    String toTime = "23:59:59";
    int totalRecords =0;
    ArrayList<HistoryDataObject> arrayList = new ArrayList<>();
    List<HistoryDataObject> list;
    static String fromDateCalender;
    static String toDateCalender;
    static String fromDateforCall;
    static String toDateforCall;
    StringBuilder sb, sb1;
    ArrayList<CurrentDataValueObject> currentWaterLevelList;
    Description description;
    HistoryDataInfoObject info;
    String channelNumber = "";
    XAxis xAxis;
    LinearLayout ll_data_not_found, ll_main, ll_back_btn;
    ArrayList<Entry> yValues = new ArrayList<>();
    int UserId;
    TextView tv_unit_measure;
    String authorityKey ="";
    String grantType = "";
    WaterChannelAdapter channel_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_graph_water_level);
        initialize();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        fromDate = sdf4.format(calendar.getTime());
        toDate = sdf4.format(calendar.getTime());
        fromDateCalender = sdf4.format(calendar.getTime());
        toDateCalender = sdf4.format(calendar.getTime());
        fromDateforCall = sdf4.format(calendar.getTime());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(fromDateforCall);
        sb2.append(" ");
        sb2.append("00:00:00");
        fromDateforCall = sb2.toString();
        toDateforCall = sdf3.format(calendar.getTime());
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -2);
        /*ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 0);
        unitList = new ArrayList<>();
        ll_back_btn.setOnClickListener(v -> {
            finish();
        });
        calendarView = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .selectorColor(getResources().getColor(R.color.hex_color_selected))
                .showBottomText(true)
                .selectedDateBackground(getResources().getDrawable(R.drawable.calendar_selected))
                .end()
                //.defaultSelectedDate(defaultSelectedDate)
                .build();
        calendarView.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                fromDate = DateFormat.format("yyyy-MM-dd", date).toString();
                toDate = DateFormat.format("yyyy-MM-dd", date).toString();
                fromDateCalender = DateFormat.format("yyyy-MM-dd", date).toString();
                toDateCalender = DateFormat.format("yyyy-MM-dd", date).toString();
                getDateTime();
                showLoaderNew();
                yValues.clear();
                chart_dailyData.invalidate();
                serviceCallforChannelData();
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView,
                                         int dx, int dy) {
            }

            @Override
            public boolean onDateLongClicked(Calendar date, int position) {
                return true;
            }
        });


        ll_selected_channel.setOnClickListener(v -> {
            if(currentWaterLevelList!=null && currentWaterLevelList.size()>1)
            {
                showCustomDialog(currentWaterLevelList);
            }
        });

    }

    private void initialize() {
        context = DataGraphForWaterLevelActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        ll_first_layout = findViewById(R.id.ll_first_layout);
        chart_dailyData = findViewById(R.id.chart_dailyData);
        ll_bottom_layout = findViewById(R.id.ll_bottom_layout);
        ll_bar = findViewById(R.id.ll_bar);
        ll_selected_channel = findViewById(R.id.ll_selected_channel);
        tv_selected_channel = findViewById(R.id.tv_selected_channel);
        rangebar_final = findViewById(R.id.rangebar_final);
        rcv_channel_select = findViewById(R.id.rcv_channel_select);
        deviceId = MApplication.getString(context, Constants.DeviceID);
        inventoryName = MApplication.getString(context, Constants.InventoryName);
        ll_data_not_found= findViewById(R.id.ll_data_not_found);
        tv_unit_measure= findViewById(R.id.tv_unit_measure);
        ll_main= findViewById(R.id.ll_main);
        ll_back_btn= findViewById(R.id.ll_back_btn);
        pagePerCount = "20";
        xAxis = chart_dailyData.getXAxis();
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        grantType = Constants.GrantType;
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        chart_dailyData.setNoDataText("");
        channelNumber = MApplication.getString(context, Constants.SelectedChannelWaterLevel);
        showLoaderNew();
        yValues.clear();
        chart_dailyData.invalidate();
        serviceCallforCurrentDataWaterMon();
    }

    public void showLoaderNew() {
        runOnUiThread(new DataGraphForWaterLevelActivity.Runloader(getResources().getString(R.string.loading)));
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

                ImageView imgeView =  dialog.findViewById(R.id.imgeView);
                TextView tvLoading =  dialog.findViewById(R.id.tvLoading);
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

    private void serviceCallforChannelData() {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
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

    private void getDateTime() {
        sb = new StringBuilder();
        sb.append(fromDateCalender);
        sb.append(" ");
        sb.append(fromTime);
        fromDateforCall = sb.toString();
        sb1 = new StringBuilder();
        sb1.append(toDateCalender);
        sb1.append(" ");
        sb1.append(toTime);
        toDateforCall = sb1.toString();
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
                            //:String inputDateStr = dateToParse;
                            JsonArray valueArray = resultObject.getAsJsonArray("Values");
                            if (valueArray.size()>0)
                            {
                                currentWaterLevelList = new ArrayList<>();
                                for(int i =0; i<valueArray.size(); i++)
                                {
                                    JsonObject jobject = valueArray.get(i).getAsJsonObject();
                                    if(!jobject.get("ChannelNumber").isJsonNull())
                                    {
                                         if(jobject.get("channeldescription").getAsString().equals("LEVEL"))
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
                                            currentWaterLevelList.add(object);
                                        }
                                    }
                                }
                                if(currentWaterLevelList.size()>0)
                                {
                                   for (int i=0; i<currentWaterLevelList.size(); i++)
                                   {
                                       int chNumber = Integer.parseInt(channelNumber);
                                       if(chNumber ==currentWaterLevelList.get(i).getChannelNumber())
                                       {
                                           tv_selected_channel.setText(currentWaterLevelList.get(i).getLabel());
                                           tv_unit_measure.setText(currentWaterLevelList.get(i).getUnit_of_measure());
                                       }
                                   }
                                    serviceCallforChannelData();
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


    private void showCustomDialog(ArrayList currentWaterLevelList) {
        //yearsList = new ArrayList<>();
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.year_popup, viewGroup, false);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        ListView lv_years = dialogView.findViewById(R.id.lv_years);
        TextView button_ok = dialogView.findViewById(R.id.button_ok);
        TextView tv_heading = dialogView.findViewById(R.id.tv_heading);
        button_ok.setVisibility(View.GONE);
        tv_heading.setText("Select a channel");
        YearsObject yearsObject = new YearsObject();
        yearsObject.setYear("2019");

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        if(currentWaterLevelList.size()>0){
            channel_adapter = new WaterChannelAdapter(context, R.layout.item_years, currentWaterLevelList);
            lv_years.setAdapter(channel_adapter);
        }

        lv_years.setOnItemClickListener((parent, view, position, id) ->
        {
            channelNumber = channel_adapter.getSelectedChannel(position);
            showLoaderNew();
            yValues.clear();
            chart_dailyData.clear();
            chart_dailyData.invalidate();
            serviceCallforCurrentDataWaterMon();
            serviceCallforChannelData();
            alertDialog.dismiss();
        });
    }
}