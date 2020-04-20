package com.techuva.iot.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.adapter.ThresholdChannelDataAdapter;
import com.techuva.iot.adapter.ThresholdListAdapter;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.api_interface.GetThresholdData;
import com.techuva.iot.api_interface.GetThresholdNamesData;
import com.techuva.iot.api_interface.SaveThresholdDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ChannelNameAndValue;
import com.techuva.iot.model.CurrentDataMainObject;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.GetThresholdPostParameter;
import com.techuva.iot.model.SaveThresholdPostParameter;
import com.techuva.iot.response_model.ThresholdResultObject;
import com.techuva.iot.response_model.ThresholdValuesObject;
import com.techuva.iot.utils.views.MApplication;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ManageThresholdActivity extends AppCompatActivity {

    public Dialog dialog;
    Context context;
    Toolbar toolbar;
    LinearLayout ll_back_btn, ll_data_not_found;
    FloatingActionButton fab_add_threshold;
    ExpandableListView exlv_devices;
    ArrayList<ThresholdResultObject> thresholdList = new ArrayList<>();
    ArrayList<ThresholdResultObject> dataList = new ArrayList<>();
    ThresholdResultObject headerInfo = new ThresholdResultObject();
    ThresholdListAdapter adapter;
    String authorityKey="";
    String userId;
    String inventoryId="";
    String pagePerCount ="30";
    int pageNumber= 1;
    String channelNumber;
    String thresholdName;
    String thresholdValue;
    AlertDialog alertDialog;
    TextView tv_nodata;
    ArrayList<ThresholdValuesObject> tempList;
    ThresholdChannelDataAdapter namesAdapter;

    int val = 0;
    ArrayList<ChannelNameAndValue> channelNameAndValues;
    ArrayList<ChannelNameAndValue> thresholdNameList;
    ArrayList<ChannelNameAndValue> nameForThreshold;
    private AnimationDrawable animationDrawable;

    public static boolean isStringOnlyAlphabet(String str)
    {
        return ((!str.equals(""))
                && (str != null)
                && (str.matches("^[a-zA-Z]*$")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_threshold);
        init();
        showLoaderNew();
        channelNameAndValues = new ArrayList<>();
        serviceCallforChannelNames();
        serviceCallForThresholdNames();
        nameForThreshold = new ArrayList<>();

        exlv_devices.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            //get the group header
            headerInfo = thresholdList.get(groupPosition);
            //display it or do something with it
            /*Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getProductName(),
                    Toast.LENGTH_LONG).show();*/

            return false;
        });

        fab_add_threshold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog( "", false);
            }
        });


        exlv_devices.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            exlv_devices.setEnabled(false);
            ThresholdResultObject headerInfo = thresholdList.get(groupPosition);
            //get the child info
            if(childPosition>0){
                ThresholdValuesObject detailInfo =  headerInfo.getThreshold().get(childPosition);
                channelNumber = String.valueOf(headerInfo.getChannelNumber());
                if(!detailInfo.getTitle().equals("Threshold Name")){
                    new Handler().postDelayed(() -> showCustomDialog(detailInfo.getTitle(), true), 300);
                }
            }
            else {
                exlv_devices.setEnabled(true);
            }
            return false;
        });

        exlv_devices.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    exlv_devices.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });

        ll_back_btn.setOnClickListener(v -> finish());
    }

    private void init() {
        context = ManageThresholdActivity.this;
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.threshold_history);
        ll_back_btn = findViewById(R.id.ll_back_btn);
        ll_data_not_found = findViewById(R.id.ll_data_not_found);
        tv_nodata = findViewById(R.id.tv_nodata);
        fab_add_threshold = findViewById(R.id.fab_add_threshold);
        exlv_devices = findViewById(R.id.exlv_devices);
        userId = MApplication.getString(context, Constants.UserID);
        inventoryId = MApplication.getString(context, Constants.DeviceID);
        authorityKey = "Bearer " + MApplication.getString(context, Constants.AccessToken);
        adapter = new ThresholdListAdapter(context, thresholdList);
    }

    private void showCustomDialog(String title, boolean fromEdit) {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_threshold, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        Spinner spin_channel= dialogView.findViewById(R.id.spin_channel);
        Spinner spin_threshold_name= dialogView.findViewById(R.id.spin_threshold_name);
        EditText edt_threshold_name = dialogView.findViewById(R.id.edt_threshold_name);
        EditText edt_threshold_value = dialogView.findViewById(R.id.edt_threshold_value);
        TextView tv_channel_name = dialogView.findViewById(R.id.tv_channel_name);
        TextView tv_alertText = dialogView.findViewById(R.id.tv_alertText);
        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_ok = dialogView.findViewById(R.id.button_ok);
        button_cancel.setVisibility(View.GONE);
        button_ok.setVisibility(View.GONE);
        View divider = dialogView.findViewById(R.id.divider);
        if(fromEdit){
            tv_alertText.setText("Update Threshold");
            tv_channel_name.setVisibility(View.VISIBLE);
            thresholdName = title;
            edt_threshold_name.setVisibility(View.GONE);
            tv_channel_name.setText(title);
            spin_channel.setVisibility(View.GONE);
            spin_threshold_name.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
        }
        else {
            ThresholdChannelDataAdapter adapter = new ThresholdChannelDataAdapter(context, channelNameAndValues);
            spin_channel.setAdapter(adapter);
            spin_channel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    val=0;
                    channelNumber = adapter.getChannelNum(position);
                    tempList = new ArrayList<>();
                    for(int i=0; i<dataList.size(); i++){
                       if(channelNumber.equals(String.valueOf(dataList.get(i).getChannelNumber()))){
                           for(int j=0; j<dataList.get(i).getThreshold().size(); j++){
                               ThresholdValuesObject object = new ThresholdValuesObject();
                               object.setTitle(dataList.get(i).getThreshold().get(j).getTitle());
                               object.setValue(dataList.get(i).getThreshold().get(j).getValue());
                               tempList.add(object);
                           }
                       }
                    }
                    nameForThreshold = new ArrayList<>();
                    if(tempList.size()>0)
                    {

                    for (int k=1; k<tempList.size(); k++)
                    {
                        for (int i=0; i<2; i++)
                        {
                            if(tempList.get(k).getTitle().contains(thresholdNameList.get(i).getChannelValue()))
                            {
                                /*ChannelNameAndValue object = new ChannelNameAndValue();
                                object.setChannelName(thresholdNameList.get(i).getChannelName());
                                object.setChannelValue(thresholdNameList.get(i).getChannelValue());
                                nameForThreshold.add(object);*/
                            }

                            else {
                                ChannelNameAndValue object = new ChannelNameAndValue();
                                object.setChannelName(thresholdNameList.get(i).getChannelName());
                                object.setChannelValue(thresholdNameList.get(i).getChannelValue());
                                nameForThreshold.add(object);
                            }
                        }
                        /*   if(tempList.get(k).getTitle().contains(thresholdNameList.get(1).getChannelValue()))
                          {
                              ChannelNameAndValue object = new ChannelNameAndValue();
                              object.setChannelName(thresholdNameList.get(1).getChannelName());
                              object.setChannelValue(thresholdNameList.get(1).getChannelValue());
                              nameForThreshold.add(object);
                              val = val+1;  }
                           else{

                           }*/

                      }
                        if(nameForThreshold.size()<2)
                        {
                            /*if(nameForThreshold.size()==1)
                            {
                                String name = nameForThreshold.get(0).getChannelValue();
                                for (int i=0; i<2; i++){

                                    nameForThreshold = new ArrayList<>();
                                    if(!thresholdNameList.get(i).getChannelName().equals(name))
                                    {
                                        ChannelNameAndValue object = new ChannelNameAndValue();
                                        object.setChannelName(thresholdNameList.get(i).getChannelName());
                                        object.setChannelValue(thresholdNameList.get(i).getChannelValue());
                                        nameForThreshold.add(object);
                                    }
                                }
                            }*/
                            spin_threshold_name.setVisibility(View.VISIBLE);
                            namesAdapter = new ThresholdChannelDataAdapter(context, nameForThreshold);
                            spin_threshold_name.setAdapter(namesAdapter);
                        }
                        else {
                            alertDialog.dismiss();
                            Toast.makeText(context, "Threshold values already exists for selected channel! You can only update.", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        spin_threshold_name.setVisibility(View.VISIBLE);
                        namesAdapter = new ThresholdChannelDataAdapter(context, thresholdNameList);
                        spin_threshold_name.setAdapter(namesAdapter);
                    }

                    //Toast.makeText(context, ""+ val, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(context, "HI " + adapter.getChannelNum(position), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            spin_threshold_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    thresholdName = namesAdapter.getChannelNum(position);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        builder.setPositiveButton("Save", (dialog, which) -> {

        });

        builder.setNegativeButton("Close", (dialog, which) -> {
            alertDialog.dismiss();
            headerInfo = new ThresholdResultObject();
            thresholdList = new ArrayList<>();
            dataList = new ArrayList<>();
            serviceCall();
        });


        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if(fromEdit){
                thresholdName = title;
            }
            thresholdValue = edt_threshold_value.getText().toString();
            if(!thresholdName.equals("") && isStringOnlyAlphabet(thresholdName))
            {
                if (!thresholdValue.equals("")){
                    if(thresholdValue.matches("^-?[0-9]+"))
                    {
                        if(thresholdValue.length()<=10)
                            {
                                serviceCallForSaveThreshold();
                                alertDialog.dismiss();
                            }
                            else {
                                Boolean wantToCloseDialog = false;
                                //Do stuff, possibly set wantToCloseDialog to true then...
                                if(wantToCloseDialog){
                                    alertDialog.dismiss();
                                }
                                Toast.makeText(context, "Value can not be more than 10 digits!", Toast.LENGTH_SHORT).show();
                            }
                    }
                    else {
                                Boolean wantToCloseDialog = false;
                                //Do stuff, possibly set wantToCloseDialog to true then...
                                if(wantToCloseDialog){
                                    alertDialog.dismiss();
                                }
                                Toast.makeText(context, "Please enter valid value!", Toast.LENGTH_SHORT).show();
                            }

                }
                else {
                    Boolean wantToCloseDialog = false;
                    //Do stuff, possibly set wantToCloseDialog to true then...
                    if(wantToCloseDialog){
                        alertDialog.dismiss();
                    }
                    Toast.makeText(context, "Please enter threshold value!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Boolean wantToCloseDialog = false;
                //Do stuff, possibly set wantToCloseDialog to true then...
                if(wantToCloseDialog){
                    alertDialog.dismiss();
                }
                Toast.makeText(context, "Please enter valid threshold name!", Toast.LENGTH_SHORT).show();
            }  });

        exlv_devices.setEnabled(true);
    }

    private void serviceCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetThresholdData service = retrofit.create(GetThresholdData.class);

        Call<JsonElement> call = service.getStringScalarWithSession(Integer.parseInt(userId), authorityKey, new GetThresholdPostParameter(inventoryId, pagePerCount, String.valueOf(pageNumber)));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    //Toast.makeText(getBaseContext(),response.body().getInfo().getErrorMessage(),Toast.LENGTH_SHORT).show();
                    hideloader();
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject infoObject = jsonObject.get("info").getAsJsonObject();
                    int errorCode = infoObject.get("ErrorCode").getAsInt();
                    String errorMsg = infoObject.get("ErrorMessage").getAsString();

                    if(errorCode==0)
                    {
                        ll_data_not_found.setVisibility(View.GONE);
                        exlv_devices.setVisibility(View.VISIBLE);
                        JsonArray resultObject = jsonObject.get("result").getAsJsonArray();
                        populateDatatoAdapter(resultObject);
                    }
                    else {
                        exlv_devices.setVisibility(View.GONE);
                        ll_data_not_found.setVisibility(View.VISIBLE);
                        tv_nodata.setText(errorMsg);
                        //Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    hideloader();
                    Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
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

    private void populateDatatoAdapter(JsonArray resultArray) {

        if(resultArray!=null)
        {
            for (int a=0; a<resultArray.size(); a++)
            {
                JsonObject result = resultArray.get(a).getAsJsonObject();

                ThresholdResultObject resultObject = new ThresholdResultObject();
                resultObject.setInventoryID(result.get("InventoryID").getAsInt());
                resultObject.setInventoryName(result.get("InventoryName").getAsString());
                resultObject.setChannelLabel(result.get("ChannelLabel").getAsString());
                resultObject.setChannelNumber(result.get("ChannelNumber").getAsInt());

                ArrayList<ThresholdValuesObject> thresholdList = new ArrayList<>();
                ThresholdValuesObject thresholdValuesObject = new ThresholdValuesObject();
                thresholdValuesObject.setTitle("Threshold Name");
                thresholdValuesObject.setValue("Threshold Value");
                thresholdList.add(thresholdValuesObject);
                JsonArray thresholdArray = result.get("Threshold").getAsJsonArray();
                for (int j=0; j<thresholdArray.size(); j++)
                {
                    JsonObject thresholdObject = thresholdArray.get(j).getAsJsonObject();
                    thresholdValuesObject = new ThresholdValuesObject();
                    thresholdValuesObject.setTitle(thresholdObject.get("Title").getAsString());
                    thresholdValuesObject.setValue(thresholdObject.get("Value").getAsString());
                    thresholdList.add(thresholdValuesObject);
                }
                //thresholdList.add(headerInfo);
                resultObject.setThreshold(thresholdList);
                dataList.add(resultObject);
            }

           // dataList.addAll(result.getResult());
        }

        for (int i=0; i<resultArray.size(); i++)
        {
            JsonObject result = resultArray.get(i).getAsJsonObject();
            headerInfo = new ThresholdResultObject();
            headerInfo.setChannelNumber(result.get("ChannelNumber").getAsInt());
            headerInfo.setChannelLabel(result.get("ChannelLabel").getAsString());
            headerInfo.setInventoryID(result.get("InventoryID").getAsInt());
            headerInfo.setInventoryName(result.get("InventoryName").getAsString());

            ArrayList<ThresholdValuesObject> thresholdList1 = new ArrayList<>();
            ThresholdValuesObject thresholdValuesObject = new ThresholdValuesObject();
            thresholdValuesObject.setTitle("Threshold Name");
            thresholdValuesObject.setValue("Threshold Value");
            thresholdList1.add(thresholdValuesObject);
            JsonArray thresholdArray = result.get("Threshold").getAsJsonArray();
            for (int j=0; j<thresholdArray.size(); j++)
            {
                JsonObject thresholdObject = thresholdArray.get(j).getAsJsonObject();
                thresholdValuesObject = new ThresholdValuesObject();
                thresholdValuesObject.setTitle(thresholdObject.get("Title").getAsString());
                thresholdValuesObject.setValue(thresholdObject.get("Value").getAsString());
                thresholdList1.add(thresholdValuesObject);
            }
            //thresholdList.add(headerInfo);
            headerInfo.setThreshold(thresholdList1);
            thresholdList.add(headerInfo);
        }
        adapter = new ThresholdListAdapter(context, dataList);
        exlv_devices.setAdapter(adapter);
        expandAll();

    }

    //method to expand all groups
    private void expandAll() {
        if(adapter!=null)
        {
            int count = adapter.getGroupCount();
            for (int i = 0; i < count; i++){
                exlv_devices.expandGroup(i);
            }
        }

    }

    public void showLoaderNew() {
        runOnUiThread(new ManageThresholdActivity.Runloader(getResources().getString(R.string.loading)));
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

    private void serviceCallForSaveThreshold() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SaveThresholdDataInterface service = retrofit.create(SaveThresholdDataInterface.class);

        Call<JsonElement> call = service.getStringScalarWithSession(Integer.parseInt(userId), authorityKey, new SaveThresholdPostParameter(inventoryId, thresholdValue, thresholdName, channelNumber));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    //Toast.makeText(getBaseContext(),response.body().getInfo().getErrorMessage(),Toast.LENGTH_SHORT).show();
                    hideloader();
                    showLoaderNew();
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject infoObject = jsonObject.get("info").getAsJsonObject();
                    String errorMsg = infoObject.get("ErrorMessage").getAsString();
                    Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    dataList = new ArrayList<>();
                    headerInfo = new ThresholdResultObject();
                    thresholdList = new ArrayList<>();
                    serviceCall();

                }else {
                    hideloader();
                    Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
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

    private void serviceCallForThresholdNames() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetThresholdNamesData service = retrofit.create(GetThresholdNamesData.class);

        Call<JsonElement> call = service.getStringScalarWithSession(Integer.parseInt(userId), authorityKey);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    //Toast.makeText(getBaseContext(),response.body().getInfo().getErrorMessage(),Toast.LENGTH_SHORT).show();
                    hideloader();
                    showLoaderNew();
                    thresholdNameList = new ArrayList<>();

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject infoObject = jsonObject.get("info").getAsJsonObject();
                    int errorCode = infoObject.get("ErrorCode").getAsInt();
                    if(errorCode==0){
                        JsonArray resultArray = jsonObject.get("result").getAsJsonArray();
                        for (int i=0; i<resultArray.size(); i++){
                            ChannelNameAndValue channelNameAndValue = new ChannelNameAndValue();
                            JsonObject object = resultArray.get(i).getAsJsonObject();
                            channelNameAndValue.setChannelName(object.get("DESCRIPTION").getAsString());
                            channelNameAndValue.setChannelValue(object.get("SHORT_TEXT").getAsString());
                            thresholdNameList.add(channelNameAndValue);
                        }


                    }

                }else {
                    hideloader();
                    Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
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

    private void serviceCallforChannelNames(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface .class);

        Call<CurrentDataMainObject> call = service.getStringScalarWithSession(Integer.parseInt(userId), authorityKey, new CurrentDataPostParameter(inventoryId ,userId));
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
                            headerInfo = new ThresholdResultObject();
                            dataList = new ArrayList<>();
                            thresholdList = new ArrayList<>();
                            serviceCall();
                            channelNameAndValues = new ArrayList<>();

                            for (int i=0; i<response.body().getResult().getValues().size(); i++)
                            {
                                ChannelNameAndValue object = new ChannelNameAndValue();
                                object.setChannelName(response.body().getResult().getValues().get(i).getLabel());
                                object.setChannelValue(String.valueOf(response.body().getResult().getValues().get(i).getChannelNumber()));
                                channelNameAndValues.add(object);
                            }

                        }
                    }
                    else {
                        if (response.body().getInfo().getErrorMessage().equals(Constants.TokenExpireMsg)) {
                            Toast.makeText(context, "Token Expire, Plz Relogin", Toast.LENGTH_SHORT).show();

                        }
                        else if (response.body().getInfo().getErrorMessage().equals(Constants.NoUserRole)) {
                            Toast.makeText(context, "No Role Assigned to User", Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<CurrentDataMainObject> call, Throwable t) {
                hideloader();
                //  Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();

            }

        });

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
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
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
            } catch (Exception e)
            {

            }
        }
    }

}
