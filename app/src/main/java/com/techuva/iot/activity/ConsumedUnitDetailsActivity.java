package com.techuva.iot.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.adapter.ConsumedDataDayRcvAdapter;
import com.techuva.iot.adapter.ConsumedDataHistoryAdapter;
import com.techuva.iot.api_interface.GetConsumedUnits;
import com.techuva.iot.api_interface.HistoryTableGetData;
import com.techuva.iot.app.Constants;
import com.techuva.iot.listener.EndlessScrollListener;
import com.techuva.iot.listener.RecyclerItemClickListener;
import com.techuva.iot.model.GetConsumedDataPostParamter;
import com.techuva.iot.model.HistoryDataAllChannelPostParamter;
import com.techuva.iot.response_model.ConsumedDataHistoryObject;
import com.techuva.iot.response_model.ConsumedDataValueObject;
import com.techuva.iot.utils.views.MApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ConsumedUnitDetailsActivity extends AppCompatActivity {

    Context context;
    String authorityKey ="";
    String toDate="";
    String fromDate="";
    String inventoryName, userId;
    String inventoryId ="";
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    HorizontalCalendar horizontalCalendar;
    ListView rcv_consumed_data;
    RecyclerView rcv_days_data;
    LinearLayout ll_back_btn;
    ArrayList<ConsumedDataValueObject> arrayList;
    ConsumedDataValueObject valueObject;
    ConsumedDataDayRcvAdapter dayDataAdapter;
    ConsumedDataHistoryObject historyObject;
    ArrayList<ConsumedDataHistoryObject> historyList;
    ConsumedDataHistoryAdapter historyAdapter;
    String pagePerCount= "20";
    int pageNumber= 1;
    String fromDateCall="";
    String toDateCall="";
    TextView tv_nodata;
    int listCount = 0;
    Toolbar toolbar;
    ImageView iv_back_btn;
    int toRecord =0;
    private EndlessScrollListener scrollListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumed_unit_details);
        init();
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.YEAR, -2);
        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 0);
        horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .selectorColor(getResources().getColor(R.color.app_orange))
                .showBottomText(false)
                .end()
                //.defaultSelectedDate(defaultSelectedDate)
                .build();
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                fromDate = DateFormat.format("yyyy-MM-dd", date).toString();
                toDate = DateFormat.format("yyyy-MM-dd", date).toString();
                historyList.clear();
                showLoaderNew();
                serviceCallforConsumedData();
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
        rcv_consumed_data.setOnScrollListener(scrollListener);
        rcv_days_data.addOnItemTouchListener(new RecyclerItemClickListener(context, rcv_days_data, new RecyclerItemClickListener.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(View view, int position) {
                //String i = dayDataAdapter.getSelectedItem();
                //Toast.makeText(context, ""+arrayList.get(position).getDATE(), Toast.LENGTH_SHORT).show();
                String dateToParse = arrayList.get(position).getDATE();
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                Date date = null;
                String dateToStr="";
                try {
                    date = sdf.parse(dateToParse);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    dateToStr = format.format(date);
                    //System.out.println(dateToStr);
                    //holder.tv_date_value.setText(dateToStr);
                } catch (android.net.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                StringBuffer sb = new StringBuffer();
                sb.append(dateToStr);
                sb.append(" ");
                sb.append("00:00:00");
                fromDateCall = sb.toString();
                StringBuffer sb1 = new StringBuffer();
                sb1.append(dateToStr);
                sb1.append(" ");
                sb1.append("23:59:59");
                toDateCall = sb1.toString();
                historyList = new ArrayList<>();
                scrollListener.resetValues();
                pageNumber = 1;
                showLoaderNew();
                serviceCallforChannelData();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        ll_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        context = ConsumedUnitDetailsActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iv_back_btn = findViewById(R.id.iv_back_btn);
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        userId = MApplication.getString(context, Constants.UserID);
        inventoryId = MApplication.getString(context, Constants.DeviceID);
        rcv_consumed_data = findViewById(R.id.rcv_consumed_data);
        rcv_days_data = findViewById(R.id.rcv_days_data);
        inventoryName = MApplication.getString(context, Constants.InventoryName);
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        fromDate = sdf4.format(calendar.getTime());
        toDate = sdf4.format(calendar.getTime());
        tv_nodata = findViewById(R.id.tv_nodata);
        ll_back_btn = findViewById(R.id.ll_back_btn);
        historyList = new ArrayList<>();
        showLoaderNew();
        serviceCallforConsumedData();
    }

    private void serviceCallforConsumedData() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        GetConsumedUnits service = retrofit.create(GetConsumedUnits.class);
        //Call<FullHistoryMainObject> call = service.getStringScalar(new HistoryDataAllChannelPostParamter(deviceId, fromDateforCall, toDateforCall, pagePerCount, String.valueOf(pageNumber)));
        Call<JsonElement> call = service.getStringScalarWithSession(Integer.parseInt(userId), authorityKey, new GetConsumedDataPostParamter(fromDate, toDate, inventoryName));
        call.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                //hideloader();
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
                            JsonArray dataArray = resultObject.getAsJsonArray("DaywiseConsumption");
//                            JsonObject unitObject = dataArray.get(0).getAsJsonObject();
//                            //Toast.makeText(context, ""+unitObject.get("SUM"), Toast.LENGTH_SHORT).show();
                            arrayList = new ArrayList<>();
                            for (int i=0; i<dataArray.size(); i++)
                            {
                                JsonObject unitObject = dataArray.get(i).getAsJsonObject();
                                valueObject = new ConsumedDataValueObject();
                                valueObject.setDATE(unitObject.get("DATE").getAsString());
                                valueObject.setSUM(unitObject.get("SUM").getAsString());
                                arrayList.add(valueObject);
                            }
                            dayDataAdapter = new ConsumedDataDayRcvAdapter(context, arrayList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                            rcv_days_data.setLayoutManager(linearLayoutManager);
                            rcv_days_data.setAdapter(dayDataAdapter);
                            String dateToParse = arrayList.get(0).getDATE();
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                            Date date = null;
                            String dateToStr="";
                            try {
                                date = sdf.parse(dateToParse);
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                dateToStr = format.format(date);
                                //System.out.println(dateToStr);
                                //holder.tv_date_value.setText(dateToStr);
                            } catch (ParseException | java.text.ParseException e) {
                                //TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            StringBuffer sb = new StringBuffer();
                            sb.append(dateToStr);
                            sb.append(" ");
                            sb.append("00:00:00");
                            fromDateCall = sb.toString();
                            StringBuffer sb1 = new StringBuffer();
                            sb1.append(dateToStr);
                            sb1.append(" ");
                            sb1.append("23:59:59");
                            toDateCall = sb1.toString();
                            historyList.clear();
                            pageNumber = 1;
                            //showLoaderNew();
                            scrollListener.resetValues();
                            serviceCallforChannelData();

                        } else if (jObj.get("ErrorCode").getAsInt() == 1) {

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

    public void showLoaderNew() {
        runOnUiThread(new ConsumedUnitDetailsActivity.Runloader(getResources().getString(R.string.loading)));
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
                            new ColorDrawable(Color.TRANSPARENT));
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
                imgeView.post(() -> {
                    if (animationDrawable != null)
                        animationDrawable.start();
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
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HistoryTableGetData service = retrofit.create(HistoryTableGetData.class);
        //Call<FullHistoryMainObject> call = service.getStringScalar(new HistoryDataAllChannelPostParamter(deviceId, fromDateforCall, toDateforCall, pagePerCount, String.valueOf(pageNumber)));
        Call<JsonElement> call = service.getStringScalarWithSession(Integer.parseInt(userId), authorityKey, new HistoryDataAllChannelPostParamter(inventoryId, fromDateCall, toDateCall, pagePerCount, String.valueOf(pageNumber)));
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
                        listCount = jObj.get("ListCount").getAsInt();
                        if (jObj.get("ErrorCode").getAsInt() == 0) {
                            JsonArray resultArray = jsonObject.getAsJsonArray("result");
                            //Toast.makeText(context, ""+resultArray.size(), Toast.LENGTH_SHORT).show();
                            if (resultArray.size() > 0) {
                                tv_nodata.setVisibility(View.GONE);
                                rcv_consumed_data.setVisibility(View.VISIBLE);
                                rcv_consumed_data.setSelectionAfterHeaderView();
                                dataResponseforChannelData(resultArray);

                            } else {

                            }
                        } else if (jObj.get("ErrorCode").getAsInt() == 1) {
                            tv_nodata.setVisibility(View.VISIBLE);
                            rcv_consumed_data.setVisibility(View.GONE);
                        } else {

                        }

                    } else { }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongConstant")
    private void dataResponseforChannelData(JsonArray resultArray) {
        double units =0;
        for (int i=0; i<resultArray.size(); i++)
        {
            String channelNum = MApplication.getString(context, Constants.ChannelNumKWH);
            JsonObject valueObject = resultArray.get(i).getAsJsonObject();
            if(i<=18)
            {
                int k=i+1;
                JsonObject unitObject = resultArray.get(k).getAsJsonObject();
                if(!valueObject.get(channelNum).isJsonNull())
                {
                    units =valueObject.get(channelNum).getAsDouble()-unitObject.get(channelNum).getAsDouble();
                }
            }
            else {
                units = 0;
            }
            historyObject = new ConsumedDataHistoryObject();
            historyObject.setDate(valueObject.get("RECEIVED_TIME").getAsString());
            historyObject.setChannelValue(valueObject.get(channelNum).getAsString());
            if(units>0)
            {
                historyObject.setUnits(String.format("%.2f", units));
            }
            else {
                historyObject.setUnits(String.valueOf(units));
            }
            historyList.add(historyObject);
        }
        rcv_consumed_data.setSelectionAfterHeaderView();
        rcv_consumed_data.setSelection(0);
        if(pageNumber ==1)
        {
            historyAdapter = new ConsumedDataHistoryAdapter(R.layout.item_hr_channel_values, context, historyList);
            rcv_consumed_data.setAdapter(historyAdapter);
        }
        else {
            historyAdapter.notifyDataSetChanged();
        }
    }

    private void loadNextDataFromApi(int page, int delay) {
        pageNumber = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
               // rcv_consumed_data.setSelectionAfterHeaderView();
                serviceCallforChannelData();
            }
        }, delay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
