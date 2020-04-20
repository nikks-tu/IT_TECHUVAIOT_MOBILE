package com.techuva.iot.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.api_interface.ForwarningDataInterface;
import com.techuva.iot.api_interface.LeafwetnessDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ForwarningPostParameters;
import com.techuva.iot.model.LeafWetnessPostParameters;
import com.techuva.iot.response_model.ForwarningMainObject;
import com.techuva.iot.response_model.forwarningResultObject;
import com.techuva.iot.utils.views.MApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ForewarninActivity extends AppCompatActivity {
    static String fromDate;
    static String toDate;
    public Dialog dialog;
    LinearLayout ll_select_date;
    TextView tv_to_date;
    TextView tv_nodata;
    ImageView iv_to_date;
    boolean dateChanged;
    int day, year, month;
    ImageView iv_back_btn;
    Calendar calendar;
    GridView rcv_channel_data;
    Context context;
    String deviceId;
    String channelId;
    String channelIdforLeafWetness;
    int UserId;
    String authorityKey = "";
    String grantType = "";
    Toolbar toolbar;
    String inventoryID="";
    LinearLayout ll_main_values, ll_fw_item_1, ll_fw_item_2, ll_fw_item_3, ll_leafwetness;
    CardView cv_fw_item_1, cv_fw_item_2, cv_fw_item_3, cv_item_leafwetness;
    TextView tv_channel_data_one, tv_channel_name_one, tv_channel_data_two, tv_channel_name_two;
    TextView tv_channel_data_three, tv_channel_name_three, tv_channel_data_lw, tv_channel_name_lw;
    ArrayList<forwarningResultObject> dataList;
    forwarningResultObject resultObject;
    private AnimationDrawable animationDrawable;
    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> {
        // TODO Auto-generated method stub
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(arg1, arg2 + 1, arg3);
    };

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forewarnin);
        init();
        showLoaderNew();
        inventoryID = MApplication.getString(context, Constants.InventoryName);
        if(inventoryID.equals("TUAGRI01201900005"))
        {
            channelId = "53";
        }
        else {
            channelId = "50,51,52";
        }
        serviceCall();
        serviceCallforLeafwetness();

        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_select_date.setOnClickListener(v -> {
            if (month < 10) {
                showDate(year, month + 1, day);
            } else
                showDate(year, month, day);
        });


    }

    private void init() {
        context = ForewarninActivity.this;
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Forewarning Details");
        setSupportActionBar(toolbar);
        ll_select_date = findViewById(R.id.ll_select_date);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_nodata = findViewById(R.id.tv_nodata);
        iv_to_date = findViewById(R.id.iv_to_date);
        iv_back_btn = findViewById(R.id.iv_back_btn);
        rcv_channel_data = findViewById(R.id.rcv_channel_data);
        ll_main_values = findViewById(R.id.ll_main_values);
        ll_fw_item_1 = findViewById(R.id.ll_fw_item_1);
        ll_fw_item_2 = findViewById(R.id.ll_fw_item_2);
        ll_fw_item_3 = findViewById(R.id.ll_fw_item_3);
        ll_leafwetness = findViewById(R.id.ll_leafwetness);
        cv_fw_item_1 = findViewById(R.id.cv_fw_item_1);
        cv_fw_item_2 = findViewById(R.id.cv_fw_item_2);
        cv_fw_item_3 = findViewById(R.id.cv_fw_item_3);
        cv_item_leafwetness = findViewById(R.id.cv_item_leafwetness);
        tv_channel_data_one = findViewById(R.id.tv_channel_data_one);
        tv_channel_name_one = findViewById(R.id.tv_channel_name_one);
        tv_channel_data_two = findViewById(R.id.tv_channel_data_two);
        tv_channel_name_two = findViewById(R.id.tv_channel_name_two);
        tv_channel_data_three = findViewById(R.id.tv_channel_data_three);
        tv_channel_name_three = findViewById(R.id.tv_channel_name_three);
        tv_channel_data_lw = findViewById(R.id.tv_channel_data_lw);
        tv_channel_name_lw = findViewById(R.id.tv_channel_name_lw);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf3.format(calendar.getTime());
        tv_to_date.setText(date);
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        fromDate = sdf4.format(calendar.getTime());
        toDate = fromDate;
        deviceId = MApplication.getString(context, Constants.DeviceID);
        channelIdforLeafWetness = "3";
        authorityKey = "Bearer " + MApplication.getString(context, Constants.AccessToken);
        grantType = Constants.GrantType;
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        dataList = new ArrayList<>();
    }

    private void showDate(int year, final int month, int day) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                String date = (year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                String date2 = (dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
                tv_to_date.setText(date2);
                fromDate = date;
                toDate = date;
                dateChanged = true;
                dataList.clear();
                serviceCall();
                serviceCallforLeafwetness();
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
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

    private void serviceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                //.baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ForwarningDataInterface service = retrofit.create(ForwarningDataInterface.class);

        //Call<ForwarningMainObject> call = service.getStringScalar(new ForwarningPostParameters(channelId, deviceId, toDate));
        Call<ForwarningMainObject> call = service.getStringScalarWithSession(UserId, authorityKey, new ForwarningPostParameters(channelId, deviceId, toDate));
        call.enqueue(new Callback<ForwarningMainObject>() {
            @Override
            public void onResponse(Call<ForwarningMainObject> call, Response<ForwarningMainObject> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                hideloader();

                if (response.code() == 401) {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                } else if (response.body() != null) {
                    if (response.body().getInfo().getErrorCode() == 0) {
                        if (response.body().getResult() != null) {
                            // Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            //rcv_channel_data.setVisibility(View.VISIBLE);
                            tv_nodata.setVisibility(View.GONE);
                            dataList = new ArrayList<>();

                            for (int i = 0; i < response.body().getResult().size(); i++) {
                                resultObject = new forwarningResultObject();
                                resultObject.setCHANNELICON(response.body().getResult().get(i).getCHANNELICON());
                                resultObject.setCHANNELLABEL(response.body().getResult().get(i).getCHANNELLABEL());
                                resultObject.setUNITOFMEASURE(response.body().getResult().get(i).getUNITOFMEASURE());
                                resultObject.setCHANNELNUMBER(response.body().getResult().get(i).getCHANNELNUMBER());
                                if (response.body().getResult().get(i).getValue().isEmpty()) {
                                    // Toast.makeText(context, "blank", Toast.LENGTH_SHORT).show();
                                    resultObject.setValue("-NA-");

                                } else {
                                    String channelValue = response.body().getResult().get(i).getValue();
                                    resultObject.setValue(channelValue);
                                }
                                dataList.add(resultObject);
                            }
                            //dataResponse(response.body().getResult());
                            dataResponse(dataList);
                        }
                    }

                } else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                    //  Toast.makeText(getBaseContext(), "Data Error",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ForwarningMainObject> call, Throwable t) {
                hideloader();
                //  Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();
                rcv_channel_data.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
            }

        });


    }

    private void dataResponse(ArrayList<forwarningResultObject> result) {
        @SuppressLint("WrongConstant") LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
       /* GridAdapterForwarning adapter = new GridAdapterForwarning(context, R.layout.item_forwarning_channel_data, result);
        rcv_channel_data.setAdapter(adapter);*/
        cv_fw_item_1.setVisibility(View.INVISIBLE);
        cv_fw_item_2.setVisibility(View.INVISIBLE);
        cv_fw_item_3.setVisibility(View.INVISIBLE);

        for (int i = 0; i < result.size(); i++) {
            if (i == 0) {
                cv_fw_item_1.setVisibility(View.VISIBLE);
                if (result.get(i).getValue().equals("-NA-")) {
                    tv_channel_data_one.setText(result.get(i).getValue());
                    tv_channel_name_one.setText(result.get(i).getCHANNELLABEL());
                    ll_fw_item_1.setBackgroundColor(context.getResources().getColor(R.color.bug_green));
                } else {
                    int value;
                    if (result.get(i).getValue().contains("-")) {
                        value = -1;
                    } else {
                        double x = Double.parseDouble(result.get(i).getValue());
                        value = (int) x;
                    }
                    tv_channel_data_one.setText(result.get(i).getValue() + "%");
                    tv_channel_name_one.setText(result.get(i).getCHANNELLABEL());
                    if (value <= 10) {
                        ll_fw_item_1.setBackgroundColor(context.getResources().getColor(R.color.bug_green));
                    } else if (value <= 50) {
                        ll_fw_item_1.setBackgroundColor(context.getResources().getColor(R.color.bug_orange));
                    } else if (value > 51) {
                        ll_fw_item_1.setBackgroundColor(context.getResources().getColor(R.color.bug_red));
                    }
                }
            } else if (i == 1) {
                cv_fw_item_2.setVisibility(View.VISIBLE);
                if (result.get(i).getValue().equals("-NA-")) {
                    tv_channel_data_two.setText(result.get(i).getValue());
                    tv_channel_name_two.setText(result.get(i).getCHANNELLABEL());
                    ll_fw_item_2.setBackgroundColor(context.getResources().getColor(R.color.bug_green));
                } else {
                    int value;
                    if (result.get(i).getValue().contains("-")) {
                        value = -1;
                    } else {

                        double x = Double.parseDouble(result.get(i).getValue());
                        value = (int) x;
                    }
                    tv_channel_data_two.setText(result.get(i).getValue() + "%");
                    tv_channel_name_two.setText(result.get(i).getCHANNELLABEL());
                    if (value <= 10) {
                        ll_fw_item_2.setBackgroundColor(context.getResources().getColor(R.color.bug_green));
                    } else if (value <= 50) {
                        ll_fw_item_2.setBackgroundColor(context.getResources().getColor(R.color.bug_orange));
                    } else if (value > 51) {
                        ll_fw_item_2.setBackgroundColor(context.getResources().getColor(R.color.bug_red));
                    }
                }
            } else if (i == 2) {
                cv_fw_item_3.setVisibility(View.VISIBLE);
                if (result.get(i).getValue().equals("-NA-")) {
                    tv_channel_data_three.setText(result.get(i).getValue());
                    tv_channel_name_three.setText(result.get(i).getCHANNELLABEL());
                    ll_fw_item_3.setBackgroundColor(context.getResources().getColor(R.color.bug_green));
                } else {
                    int value;
                    if (result.get(i).getValue().contains("-")) {
                        value = -1;
                    } else {
                        double x = Double.parseDouble(result.get(i).getValue());
                        value = (int) x;
                    }
                    tv_channel_data_three.setText(result.get(i).getValue() + "%");
                    tv_channel_name_three.setText(result.get(i).getCHANNELLABEL());
                    if (value <= 10) {
                        ll_fw_item_3.setBackgroundColor(context.getResources().getColor(R.color.bug_green));
                    } else if (value <= 50) {
                        ll_fw_item_3.setBackgroundColor(context.getResources().getColor(R.color.bug_orange));
                    } else if (value > 51) {
                        ll_fw_item_3.setBackgroundColor(context.getResources().getColor(R.color.bug_red));
                    }
                }
            }
        }

    }

    private void serviceCallforLeafwetness() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LeafwetnessDataInterface service = retrofit.create(LeafwetnessDataInterface.class);
        Call<JsonElement> call = service.getStringScalarWithSession(UserId, authorityKey, new LeafWetnessPostParameters(channelIdforLeafWetness, deviceId, toDate));
        call.enqueue(new Callback<JsonElement>() {

            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hideloader();
                if (response.code() == 401) {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                } else if (response.body() != null) {

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject infoObject = jsonObject.get("info").getAsJsonObject();

                    if (infoObject.get("ErrorCode").getAsInt()==0) {
                        tv_nodata.setVisibility(View.GONE);
                        JsonObject result = jsonObject.get("result").getAsJsonObject();
                        setValuesLeafWetness(result);
                    } else {
                        cv_item_leafwetness.setVisibility(View.INVISIBLE);
                    }

                } else {
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                cv_item_leafwetness.setVisibility(View.INVISIBLE);
            }

        });
    }

    private void setValuesLeafWetness(JsonObject result) {
          /* @SerializedName("CHANNEL_NUMBER")
    @Expose
    private Integer cHANNELNUMBER;
    @SerializedName("CHANNEL_ICON")
    @Expose
    private String cHANNELICON;
    @SerializedName("Value")
    @Expose
    private Integer value;
    @SerializedName("CHANNEL_LABEL")
    @Expose
    private String cHANNELLABEL;
    @SerializedName("UNIT_OF_MEASURE")*/
        cv_item_leafwetness.setVisibility(View.VISIBLE);
        tv_channel_name_lw.setText(result.get("CHANNEL_LABEL").getAsString());
        if (result.get("Value").getAsString().equals("")) {
            tv_channel_data_lw.setText("-NA-");
        } else {
            String value = result.get("Value").getAsString();
            tv_channel_data_lw.setText(value);
        }

    }

    public void showLoaderNew() {
        runOnUiThread(new ForewarninActivity.Runloader(getResources().getString(R.string.loading)));
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
            } catch (Exception e) {

            }
        }
    }

}
