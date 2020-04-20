package com.techuva.iot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.adapter.HBLNotificationAdapter;
import com.techuva.iot.api_interface.HblNotificationDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.listener.EndlessScrollListener;
import com.techuva.iot.model.HblNotificationPostParameter;
import com.techuva.iot.response_model.HblNotificationResultObject;
import com.techuva.iot.utils.views.MApplication;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HBLNotificationActivity extends AppCompatActivity {
    Context context;
    Toolbar toolbar;
    FrameLayout fl_main;
    LinearLayout ll_back_btn;
    TextView tv_notification_heading, tv_notification_count;
    TextView tv_channel_id, tv_channel_label, tv_notification_time;
    ListView rcv_threshold;
    String pagePerCount= "20";
    int pageNumber= 1;
    String authorityKey ="";
    String inventoryName, userId;
    String inventoryId ="";
    ArrayList<HblNotificationResultObject> arrayList;
    HblNotificationResultObject valueObject;
    HBLNotificationAdapter notificationAdapter;
    private EndlessScrollListener scrollListener;
    int listCount = 0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hblnotification);
        init();
        serviceCall();
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
        rcv_threshold.setOnScrollListener(scrollListener);

        ll_back_btn.setOnClickListener(v -> {
            finish();
        });

    }

    private void loadNextDataFromApi(int page, int delay) {
        pageNumber = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // rcv_consumed_data.setSelectionAfterHeaderView();
                serviceCall();
            }
        }, delay);
    }

    private void serviceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HblNotificationDataInterface service = retrofit.create(HblNotificationDataInterface.class);
        Call<JsonElement> call = service.getStringScalarWithSession(Integer.parseInt(userId), authorityKey, new HblNotificationPostParameter(inventoryId, "", pagePerCount, String.valueOf(pageNumber)));
        call.enqueue(new Callback<JsonElement>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body()
                // hideloader();
                if (response.code() == 401) {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                } else if (response.body() != null) {
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    if (!jsonObject.isJsonNull()) {
                        JsonObject jObj = jsonObject.getAsJsonObject("info");
                        int totalRecords = 0;
                        totalRecords =  jObj.get("TotalRecords").getAsInt();
                        tv_notification_count.setText(String.valueOf(totalRecords));
                        if (jObj.get("ErrorCode").getAsInt() == 0) {
                            JsonArray dataArray = jsonObject.getAsJsonArray("result");
                            arrayList = new ArrayList<>();
                            for (int i=0; i<dataArray.size(); i++)
                            {
                                JsonObject unitObject = dataArray.get(i).getAsJsonObject();
                                valueObject = new HblNotificationResultObject();
                                valueObject.setNotificationTime(unitObject.get("notificationTime").getAsString());
                                valueObject.setPushNotoficationFlag(unitObject.get("PushNotoficationFlag").getAsBoolean());
                                valueObject.setInventoryID(unitObject.get("InventoryID").getAsInt());
                                valueObject.setInventoryName(unitObject.get("InventoryName").getAsString());
                                valueObject.setChannelNumber(unitObject.get("ChannelNumber").getAsString());
                                valueObject.setChannelLabel(unitObject.get("ChannelLabel").getAsString());
                                tv_channel_label.setText(unitObject.get("ChannelLabel").getAsString());
                                if(unitObject.has("ChannelValue") && !unitObject.get("ChannelValue").isJsonNull()){
                                    valueObject.setChannelValue(unitObject.get("ChannelValue").getAsString());
                                }
                                else valueObject.setChannelValue("");
                                arrayList.add(valueObject);
                            }
                            notificationAdapter = new HBLNotificationAdapter(R.layout.item_hbl_notification,context, arrayList);
                            rcv_threshold.setAdapter(notificationAdapter);
                            pageNumber = 1;
                            //showLoaderNew();
                            scrollListener.resetValues();
                        } else if (jObj.get("ErrorCode").getAsInt() == 1) {
                        } else {}
                    } else {}
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void init() {
        context = HBLNotificationActivity.this;
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        fl_main = findViewById(R.id.fl_main);
        ll_back_btn = findViewById(R.id.ll_back_btn);
        tv_notification_heading = findViewById(R.id.tv_notification_heading);
        tv_notification_count  = findViewById(R.id.tv_notification_count);
        tv_channel_id   = findViewById(R.id.tv_channel_id);
        tv_channel_id.setVisibility(View.GONE);
        tv_channel_label   = findViewById(R.id.tv_channel_label);
        tv_notification_time   = findViewById(R.id.tv_notification_time);
        rcv_threshold   = findViewById(R.id.rcv_threshold);
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        userId = MApplication.getString(context, Constants.UserID);
        inventoryId = MApplication.getString(context, Constants.DeviceID);
    }
}
