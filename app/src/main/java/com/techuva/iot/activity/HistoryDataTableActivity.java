package com.techuva.iot.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.appyvet.materialrangebar.RangeBar;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.api_interface.HistoryTableGetData;
import com.techuva.iot.app.Constants;
import com.techuva.iot.listener.SimpleGestureListener;
import com.techuva.iot.model.CurrentDataMainObject;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.CurrentDataResultObject;
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.model.HistoryDataAllChannelPostParamter;
import com.techuva.iot.model.HistoryDataValueObject;
import com.techuva.iot.utils.views.MApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class HistoryDataTableActivity extends AppCompatActivity {

    List<CurrentDataValueObject> list;
    TabLayout tabLayout;
    Context context;
    static String fromDateCalender;
    static String toDateCalender;
    static String fromDate;
    static String toDate;
    static String fromDateforCall;
    static String toDateforCall;
    String fromTime = "00:00:00";
    String toTime = "23:59:59";
    RecyclerView rcv_Dates;
    String deviceId, pagePerCount;
    int pageNumber = 1;
    ArrayList<CurrentDataValueObject> arrayList = new ArrayList<>();
    HorizontalScrollView hscrll1;
    LinearLayout RelativeLayout1, ll_table_data, ll_bootom_btn_view, ll_data_not_found;
    TableLayout table_main;
    TableLayout table_time;
    TableLayout table_headers;
    LinearLayout ll_serverError, ll_back;
    Button btn_retry_connect_server, btn_retry;
    String maxTime = "00:00";
    String minTime = "00:00";
    CrystalRangeSeekbar seekbar;
    RangeBar rangebar_final;
    TextView tv_search;
    SimpleGestureListener simpleGestureListener;
    ImageView iv_search, iv_back_btn;
    TextView tv_device_name, tv_data_not_found;
    TextView tv_prev_button, tv_next_btn, tv_search_btn;
    HorizontalCalendar horizontalCalendar;
    StringBuilder sb;
    StringBuilder sb1;
    Boolean channelNames = false;
    ArrayList<String> channelList = new ArrayList<>();
    int inPixels;
    int inPixelslarge;
    int inPixelsExtralarge;
    int inPixelsForTime;
    int marginTop;
    int marginBottom;
    int marginLeftRight;

    public Dialog dialog;
    private AnimationDrawable animationDrawable;

    TableLayout.LayoutParams params;

    int UserId;
    String authorityKey ="";
    String grantType = "";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint({"ClickableViewAccessibility", "WrongConstant"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data_table);
        init();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();

        inPixels = (int) context.getResources().getDimension(R.dimen.table_row_width);
        inPixelslarge = (int) context.getResources().getDimension(R.dimen.table_row_large_width);
        inPixelsExtralarge = (int) context.getResources().getDimension(R.dimen.table_row_xxlarge_width);
        inPixelsForTime = (int) context.getResources().getDimension(R.dimen.top_ten_layout_height);
        marginTop = (int) context.getResources().getDimension(R.dimen.margin_eight);
        marginBottom = (int) context.getResources().getDimension(R.dimen.margin_five);
        marginLeftRight = (int) context.getResources().getDimension(R.dimen.margin_three);
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

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.DAY_OF_MONTH, 0);
        //final Calendar defaultSelectedDate = Calendar.getInstance();

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
                fromDateCalender = DateFormat.format("yyyy-MM-dd", date).toString();
                toDateCalender = DateFormat.format("yyyy-MM-dd", date).toString();
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


        tv_next_btn.setOnClickListener(v -> {
            pageNumber = pageNumber + 1;
            tv_prev_button.setEnabled(true);
            tv_prev_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            showLoaderNew();
            refreshViews();
            setTimeHeader();
            serviceCallforChannelData();
        });

        btn_retry.setOnClickListener(v -> {
            pageNumber = 1;
            serviceCallforChannelData();
        });

        btn_retry_connect_server.setOnClickListener(v -> {
            pageNumber = 1;
            serviceCallforChannelData();
        });

        tv_prev_button.setOnClickListener(v -> {
            if (pageNumber >= 1) {
                pageNumber = pageNumber - 1;
                refreshViews();
                setTimeHeader();
                showLoaderNew();
                serviceCallforChannelData();
            }

        });

        tv_search_btn.setOnClickListener(v -> {
            showLoaderNew();
            getDateTime();
            pageNumber = 1;
            tv_search_btn.setEnabled(false);

            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            if (channelNames) {
                                refreshViews();
                                setTimeHeader();
                                serviceCallforChannelData();
                            } else {
                                refreshViews();
                                serviceCallforChannelNames();
                                serviceCallforChannelData();
                            }
                        }
                    }, 500);
        });

        iv_back_btn.setOnClickListener(v -> {
            finish();
        });

        ll_back.setOnClickListener(v -> {
            finish();
        });

        rangebar_final.setOnRangeBarChangeListener((rangeBar, leftPinIndex, rightPinIndex, leftPinValue, rightPinValue) -> {
            minTime = leftPinValue;
            if (rightPinValue.equals("24")) {
                maxTime = "23";
            } else {
                maxTime = rightPinValue;
            }
            if (minTime.length() <= 1) {
                sb = new StringBuilder();
                sb.append("0");
                sb.append(minTime);
                minTime = sb.toString();
            }
            if (maxTime.length() <= 1) {
                sb = new StringBuilder();
                sb.append("0");
                sb.append(maxTime);
                maxTime = sb.toString();
            }
            sb = new StringBuilder();
            sb.append(minTime);
            sb.append(":");
            sb.append("00");
            sb.append(":");
            sb.append("00");
            fromTime = sb.toString();
            sb1 = new StringBuilder();
            if(maxTime.equals("23"))
            {
                sb1.append(maxTime);
                sb1.append(":");
                sb1.append("59");
                sb1.append(":");
                sb1.append("59");
                toTime = sb1.toString();
            }
            else
            { sb1.append(maxTime);
                sb1.append(":");
                sb1.append("00");
                sb1.append(":");
                sb1.append("00");
                toTime = sb1.toString();}
            //  Toast.makeText(context, fromTime + toTime , Toast.LENGTH_SHORT).show();
        });
        serviceCallforChannelNames();
        }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setTimeHeader() {
        TableRow tbrow1 = new TableRow(this);
        tbrow1.removeAllViews();
        tbrow1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView tv1 = new TextView(this);
        tv1.setWidth(inPixelsForTime);
        tv1.setMaxLines(2);
        tv1.setText(getResources().getString(R.string.time_txt_caps));
        tv1.setGravity(Gravity.CENTER);
        tv1.setTextAlignment(Gravity.CENTER);
        tv1.setTextColor(Color.WHITE);
        tv1.setPadding(marginLeftRight, marginTop, marginLeftRight, marginBottom);
        tbrow1.addView(tv1);
        tbrow1.setGravity(Gravity.CENTER);
        table_time.addView(tbrow1);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongConstant")
    private void refreshViews() {
        table_main.removeAllViews();
        table_time.removeAllViews();
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


    private void serviceCallforChannelData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HistoryTableGetData service = retrofit.create(HistoryTableGetData.class);
        //Call<FullHistoryMainObject> call = service.getStringScalar(new HistoryDataAllChannelPostParamter(deviceId, fromDateforCall, toDateforCall, pagePerCount, String.valueOf(pageNumber)));
        Call<JsonElement> call = service.getStringScalarWithSession(UserId, authorityKey, new HistoryDataAllChannelPostParamter(deviceId, fromDateforCall, toDateforCall, pagePerCount, String.valueOf(pageNumber)));
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
                            //Toast.makeText(context, ""+resultArray.size(), Toast.LENGTH_SHORT).show();
                            if (resultArray.size() > 0) {
                                ll_table_data.setVisibility(View.VISIBLE);
                                ll_serverError.setVisibility(View.GONE);
                                ll_data_not_found.setVisibility(View.GONE);
                                dataResponseforChannelData(resultArray);
                                tv_next_btn.setEnabled(true);
                                tv_next_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                if (jObj.get("PageNumber").getAsString().equals("1")) {
                                    tv_prev_button.setEnabled(false);
                                    tv_prev_button.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                                } else {
                                    tv_prev_button.setEnabled(true);
                                    tv_prev_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                }

                            } else {
                                ll_table_data.setVisibility(View.GONE);
                                ll_data_not_found.setVisibility(View.VISIBLE);
                                tv_data_not_found.setText(jObj.get("ErrorMessage").getAsString());
                                tv_next_btn.setEnabled(false);
                                tv_next_btn.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                                tv_search_btn.setEnabled(true);
                                if (pageNumber <= 1) {
                                    tv_prev_button.setEnabled(false);
                                    tv_prev_button.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                                }
                            }
                        } else if (jObj.get("ErrorCode").getAsInt() == 1) {
                            ll_table_data.setVisibility(View.GONE);
                            ll_data_not_found.setVisibility(View.VISIBLE);
                            tv_next_btn.setEnabled(false);
                            tv_next_btn.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                            tv_search_btn.setEnabled(true);
                            tv_data_not_found.setText(jObj.get("ErrorMessage").getAsString());
                            if (pageNumber <= 1) {
                                tv_prev_button.setEnabled(false);
                                tv_prev_button.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                            } else {
                                tv_prev_button.setEnabled(true);
                                tv_prev_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            }
                        } else {
                            //scrollView1.setVisibility(View.GONE);
                            ll_table_data.setVisibility(View.GONE);
                            //ll_bootom_btn_view.setVisibility(View.GONE);
                            ll_data_not_found.setVisibility(View.VISIBLE);
                            tv_data_not_found.setText(jObj.get("ErrorMessage").getAsString());
                            tv_next_btn.setEnabled(false);
                            tv_next_btn.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                            tv_search_btn.setEnabled(true);

                            if (pageNumber <= 1) {
                                tv_prev_button.setEnabled(false);
                                tv_prev_button.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                            }
                        }

                    } else {
                        ll_table_data.setVisibility(View.GONE);
                        tv_search_btn.setEnabled(true);
                        ll_data_not_found.setVisibility(View.VISIBLE);
                        tv_next_btn.setEnabled(false);
                        tv_next_btn.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                        tv_prev_button.setEnabled(false);
                        tv_prev_button.setBackgroundColor(getResources().getColor(R.color.App_Grey));
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                tv_search_btn.setEnabled(true);
                ll_table_data.setVisibility(View.GONE);
                ll_data_not_found.setVisibility(View.VISIBLE);
                tv_data_not_found.setText(R.string.data_not_found);
                tv_next_btn.setEnabled(false);
                tv_next_btn.setBackgroundColor(getResources().getColor(R.color.App_Grey));

            }
        });

    }

    private void serviceCallforChannelNames() {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // HistoryDataInterface service = retrofit.create(HistoryDataInterface.class);
        CurrentDataInterface service = retrofit.create(CurrentDataInterface.class);

        //Call<HistoryDataMainObject> call = service.getStringScalar(new HistoryDataPostParamter(deviceId, fromDate, toDate, pagePerCount, String.valueOf(pageNumber)));
        // Call<HistoryDataMainObject> call = service.getStringScalarWithSession(UserId, authorityKey, new HistoryDataPostParamter(deviceId, fromDate, toDate, pagePerCount, String.valueOf(pageNumber)));
        Call<CurrentDataMainObject> call = service.getStringScalarWithSession(UserId, authorityKey, new CurrentDataPostParameter(deviceId, "1001"));
        call.enqueue(new Callback<CurrentDataMainObject>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<CurrentDataMainObject> call, Response<CurrentDataMainObject> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )


                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if (response.body() != null) {
                    if (response.body().getInfo().getErrorCode().equals(0)) {

                        if (response.body().getResult() != null) {
                            hideloader();
                            channelNames = true;
                            ll_table_data.setVisibility(View.VISIBLE);
                            //ll_bootom_btn_view.setVisibility(View.VISIBLE);
                            ll_serverError.setVisibility(View.GONE);
                            ll_data_not_found.setVisibility(View.GONE);
                            dataResponse(response.body().getResult());
                            serviceCallforChannelData();
                        } else {
                            tv_search_btn.setEnabled(true);
                            ll_table_data.setVisibility(View.GONE);
                            ll_data_not_found.setVisibility(View.VISIBLE);

                        }
                    } else if (response.body().getInfo().getErrorCode().equals(1)) {
                        tv_search_btn.setEnabled(true);
                        ll_table_data.setVisibility(View.GONE);
                        ///ll_bootom_btn_view.setVisibility(View.GONE);
                        ll_data_not_found.setVisibility(View.VISIBLE);
                        tv_data_not_found.setText(response.body().getInfo().getErrorMessage().toString());
                    } else {
                        tv_search_btn.setEnabled(true);
                        ll_table_data.setVisibility(View.GONE);
                        ll_serverError.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onFailure(Call<CurrentDataMainObject> call, Throwable t) {
                // Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
            }

        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("WrongConstant")
    private void dataResponseforChannelData(JsonArray resultArray) {
        TableRow tbrow1 = new TableRow(this);
        tbrow1.setBackgroundColor(Color.WHITE);
        for (int a=0; a<resultArray.size(); a++)
        {
            tbrow1 = new TableRow(this);
            //for (int i = 0; i < size+2; i++) {
            for (int i = 0; i < channelList.size(); i++) {
                TextView tv = new TextView(this);
                if (channelList.size() <=1) {
                    tv.setWidth(inPixelsExtralarge);
                } else if (channelList.size()==2) {
                    tv.setWidth(inPixelslarge);
                }
                else {
                    tv.setWidth(inPixels);
                }
                tv.setGravity(Gravity.CENTER);
                tv.setTextAlignment(Gravity.CENTER);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                tv.setTextAlignment(Gravity.CENTER);
                tv.setPadding(marginLeftRight, marginTop, marginLeftRight, marginBottom);
                tv.setTextColor(getResources().getColor(R.color.text_color_table));
                String s = channelList.get(i);
                JsonObject jsonObject = resultArray.get(a).getAsJsonObject();
                if (!jsonObject.get(s).isJsonNull())
                {
                    tv.setText(jsonObject.get(s).getAsString());
                }
                tbrow1.setGravity(Gravity.CENTER);
                tbrow1.addView(tv);
            }
            table_main.addView(tbrow1);
        }

        if (channelNames) {

            for (int a=0; a<resultArray.size(); a++)
            {
                TableRow tbrow2 = new TableRow(this);
                tbrow2.removeAllViews();
                tbrow2.setGravity(Gravity.CENTER);
                tbrow2.setBackgroundColor(Color.WHITE);
                TextView tv6;
                JsonObject jsonObject = resultArray.get(a).getAsJsonObject();
                if(jsonObject.has("RECEIVED_TIME"))
                {
                    tv6 = new TextView(this);
                    if (channelList.size() < 2) {
                        tv6.setWidth(inPixels);
                    } else {
                        tv6.setWidth(inPixelsForTime);
                    }
                   /* if (channelList.size() <=1) {
                        tv6.setWidth(inPixelsExtralarge);
                    } else if (channelList.size()==2) {
                        tv6.setWidth(inPixelslarge);
                    }
                    else {
                        tv6.setWidth(inPixels);
                    }*/
                    tv6.setGravity(Gravity.CENTER);
                    tv6.setTextAlignment(Gravity.CENTER);
                    tv6.setTextColor(getResources().getColor(R.color.dark_blue));
                    tv6.setPadding(marginLeftRight, marginTop, marginLeftRight, marginBottom);
                    String dateToParse = jsonObject.get("RECEIVED_TIME").getAsString();
                    dateToParse = dateToParse.replaceAll("T", " ");
                    @SuppressLint("SimpleDateFormat")
                    //Feb 11, 2019 12:04:43 AM
                            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                    Date date = null;
                    try {
                        date = sdf.parse(dateToParse);
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        String dateToStr = format.format(date);
                        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                        date = sdf.parse(dateToParse);
                        String sa = timeFormatter.format(date);
                        tv6.setText(sa);

                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                    tbrow2.addView(tv6);
                }
                table_time.addView(tbrow2);
                tv_search_btn.setEnabled(true);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void dataResponse(CurrentDataResultObject resultObject) {
        //Success message
        // tv_deviceName.setText(historyResultObject.getDeviceName());
        list = resultObject.getValues();
        // generateDataList(list);
        generateDataListforChannelNames(list);

    }
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void generateDataListforChannelNames(List<CurrentDataValueObject> data) {
        setTimeHeader();
        arrayList = new ArrayList<>();
        List<HistoryDataValueObject> valuelist = new ArrayList<>();
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                channelList.add(String.valueOf(data.get(i).getChannelNumber()));
                arrayList.add(data.get(i));
            }
        }
        TableRow tbrow0 = new TableRow(this);
/*      params = new TableLayout.LayoutParams(30, 40);
        params.leftMargin = 50;
        params.topMargin = 60;
        tbrow0.setLayoutParams(params);*/
        tbrow0.removeAllViews();
        tbrow0.setGravity(Gravity.CENTER);
        tbrow0.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        for (int i = 0; i < arrayList.size(); i++) {
            TextView tv0 = new TextView(this);
            tv0.setTextColor(Color.WHITE);
            tv0.setGravity(Gravity.CENTER);
            tv0.setTextAlignment(Gravity.CENTER);
            tv0.setPadding(marginLeftRight, marginTop, marginLeftRight, marginBottom);
           /* if (channelList.size() < 2) {
                tv0.setWidth(inPixelslarge);
            } else {
                tv0.setWidth(inPixels);
            }*/
            if (channelList.size() <=1) {
                tv0.setWidth(inPixelsExtralarge);
            } else if (channelList.size()==2) {
                tv0.setWidth(inPixelslarge);
            }
            else {
                tv0.setWidth(inPixels);
            }
            String s = arrayList.get(i).getLabel();
            s = s.replaceAll("_", " " +
                    "");
            tv0.setText(s);
            tbrow0.addView(tv0);
        }
        tbrow0.setGravity(Gravity.CENTER);
        table_headers.addView(tbrow0);

    }


    private void init() {
        context = HistoryDataTableActivity.this;
        deviceId = MApplication.getString(context, Constants.DeviceID);
        pagePerCount = "30";
        rcv_Dates = findViewById(R.id.rcv_Dates);
        hscrll1 = findViewById(R.id.hscrll1);
        ///RelativeLayout1 = findViewById(R.id.RelativeLayout1);
        ll_table_data = findViewById(R.id.ll_table_data);
        ll_bootom_btn_view = findViewById(R.id.ll_bootom_btn_view);
        ll_data_not_found = findViewById(R.id.ll_data_not_found);
        table_main = findViewById(R.id.table_main);
        table_time = findViewById(R.id.table_time);
        table_headers = findViewById(R.id.table_headers);
        ll_serverError = findViewById(R.id.ll_serverError);
        btn_retry_connect_server = findViewById(R.id.btn_retry_connect_server);
        btn_retry = findViewById(R.id.btn_retry);
        rangebar_final = findViewById(R.id.rangebar_final);
        iv_search = findViewById(R.id.iv_search);
        iv_back_btn = findViewById(R.id.iv_back_btn);
        tv_device_name = findViewById(R.id.tv_device_name);
        tv_data_not_found = findViewById(R.id.tv_data_not_found);
        tv_prev_button = findViewById(R.id.tv_prev_button);
        tv_next_btn = findViewById(R.id.tv_next_btn);
        tv_search_btn = findViewById(R.id.tv_search_btn);
        tv_search_btn.setEnabled(false);
        ll_back = findViewById(R.id.ll_back);
        iv_search.setVisibility(View.GONE);
        simpleGestureListener = new SimpleGestureListener();
        tv_device_name.setText(MApplication.getString(context, Constants.DEVICE_IN_USE));
        table_time.removeAllViews();
        table_headers.removeAllViews();
        table_main.removeAllViews();
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        grantType = Constants.GrantType;
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        if (pageNumber ==1) {
            tv_prev_button.setEnabled(false);
            tv_prev_button.setBackgroundColor(getResources().getColor(R.color.App_Grey));
        } else {
            tv_prev_button.setEnabled(true);
            tv_prev_button.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void showLoaderNew() {
        runOnUiThread(new Runloader(getResources().getString(R.string.loading)));
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


    private void goto_login_activity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

}
