package com.techuva.iot.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.techuva.iot.R;
import com.techuva.iot.adapter.UserDevicesListAdapter;
import com.techuva.iot.api_interface.ListofDevicesInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.DevicesInventoryListObject;
import com.techuva.iot.model.DevicesListMainObject;
import com.techuva.iot.model.DevicesListPostParameter;
import com.techuva.iot.model.DevicesListResultObject;
import com.techuva.iot.utils.views.MApplication;
import com.techuva.iot.utils.views.MPreferences;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserDevicesListActivity extends AppCompatActivity {

    ExpandableListView exlv_devices;
    String userId;
    String accessToken;
    int companyId;
    String refType;
    Context mContext;
    Toolbar toolbar;
    UserDevicesListAdapter adapter;
    ArrayList<DevicesListResultObject> dataList;
    LinearLayout ll_main, ll_error, ll_back_btn;
    ImageView iv_back_btn;
    TextView tv_error;
    MPreferences preferences;
    DevicesListResultObject headerInfo = new DevicesListResultObject();
    private LinkedHashMap<String, DevicesListResultObject> devices = new LinkedHashMap<>();
    private ArrayList<DevicesListResultObject> deptList = new ArrayList<>();
    public Dialog dialog;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_devices_list);
        init();
        adapter = new UserDevicesListAdapter(mContext, deptList);
        showLoaderNew();
        serviceCall();
        ll_back_btn.setOnClickListener(v -> {
            onBackPressed();
        });

        exlv_devices.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            //get the group header
            headerInfo = deptList.get(groupPosition);
            //display it or do something with it
            /*Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getProductName(),
                    Toast.LENGTH_LONG).show();*/

            return false;
        });


        exlv_devices.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                DevicesListResultObject headerInfo = deptList.get(groupPosition);
                //get the child info
                DevicesInventoryListObject detailInfo =  headerInfo.getInventoryList().get(childPosition);
                for(int i =0;i<deptList.get(groupPosition).getInventoryList().size();i++)
                {
                    if(childPosition!=i)
                        deptList.get(groupPosition).getProductId();
                }
                adapter.notifyDataSetChanged();
               if(detailInfo.getTemplateRef().equals("CHILLER_DASHBOARD_V_ONE"))
                {
                    Intent intent = new Intent(mContext, Dashboard.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);
                } else if(detailInfo.getTemplateRef().equals("PROCOM_DASHBOARD_V_ONE"))
                {
                    Intent intent = new Intent(mContext, Dashboard_Procom.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);
                }
                else if(detailInfo.getTemplateRef().equals("CHILLER_DASHBOARD_V_TWO"))
                {
                    Intent intent = new Intent(mContext, Dashboard_Two.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);
                }
                else if(detailInfo.getTemplateRef().equals("AGRI_DASHBOARD_V_ONE"))
                {
                    Intent intent = new Intent(mContext, Dashboard_Agri.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);
                }
                else if(detailInfo.getTemplateRef().equals("WATER_MON_DASHBOARD_V_ONE"))
                {
                    Intent intent = new Intent(mContext, Dashboard_Water_Flow.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);
                }
                else if(detailInfo.getTemplateRef().equals("ENERGY_DASHBOARD_V_ONE"))
                {
                    Intent intent = new Intent(mContext, Dashboard_Energy.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);
                }
                else if(detailInfo.getTemplateRef().equals("HBL_DASHBOARD_V_ONE"))
                {
                    Intent intent = new Intent(mContext, Dashboard_HBL.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);
                }
                else if(detailInfo.getTemplateRef().equals("WATERMOTOR_DASHBOARD_V_ONE"))
                {
                    Intent intent = new Intent(mContext, Dashboard_Water_Motor.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(mContext, Dashboard.class);
                    MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, true);
                    MApplication.setString(mContext, Constants.UserID, userId);
                    MApplication.setString(mContext, Constants.DeviceID, detailInfo.getInventoryId().toString());
                    MApplication.setString(mContext, Constants.InventoryTypeId, String.valueOf(detailInfo.getInventoryTypeId()));
                    MApplication.setString(mContext, Constants.DEVICE_IN_USE, detailInfo.getDisplayName());
                    MApplication.setString(mContext, Constants.InventoryName, detailInfo.getInventoryName());
                    MApplication.setString(mContext, Constants.InventoryTypeName, detailInfo.getInventoryTypeName());
                    MApplication.setString(mContext, Constants.InventoryProvisionedOn, detailInfo.getProvisionedOn());
                    MApplication.setString(mContext, Constants.Template, detailInfo.getTemplateRef());
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, false);
                    preferences.setDefaultDeviceSelected(true);
                    startActivity(intent);

                }

                return false;
            }
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

    }

    private void init() {
        mContext = UserDevicesListActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        exlv_devices = findViewById(R.id.exlv_devices);
        refType = Constants.Ref_Type;
        ll_main = findViewById(R.id.ll_main);
        ll_error = findViewById(R.id.ll_error);
        tv_error = findViewById(R.id.tv_error);
        iv_back_btn = findViewById(R.id.iv_back_btn);
        preferences = new MPreferences(mContext);
        if(MApplication.getBoolean(mContext, Constants.SingleAccount))
        {
            userId = MApplication.getString(mContext, Constants.UserID);
        }
        else {
            userId = MApplication.getString(mContext, Constants.UserIDSelected);
        }
        companyId = Integer.parseInt(MApplication.getString(mContext, Constants.CompanyID));
        dataList = new ArrayList<>();
        ll_back_btn = findViewById(R.id.ll_back_btn);
        boolean singleAccount = MApplication.getBoolean(mContext, Constants.SingleAccount);
        if(singleAccount)
        {
            ll_back_btn.setVisibility(View.GONE);
        }
        else {
            ll_back_btn.setVisibility(View.VISIBLE);
        }
        accessToken = "Bearer "+MApplication.getString(mContext, Constants.AccessToken);
       // Toast.makeText(mContext, "companyId"+ companyId, Toast.LENGTH_SHORT).show();

    }


    private void serviceCall() {

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.USER_MGMT_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ListofDevicesInterface service = retrofit.create(ListofDevicesInterface.class);

        Call<DevicesListMainObject> call = service.getStringScalar(Integer.parseInt(userId), accessToken, new DevicesListPostParameter(userId, companyId, refType));
        call.enqueue(new Callback<DevicesListMainObject>() {
            @Override
            public void onResponse(Call<DevicesListMainObject> call, Response<DevicesListMainObject> response) {
                if(response.body()!=null){
                     //Toast.makeText(getBaseContext(),response.body().getInfo().getErrorMessage(),Toast.LENGTH_SHORT).show();
                    hideloader();
                    if(response.body().getInfo().getErrorCode()==0)
                    {
                        ll_error.setVisibility(View.GONE);
                        ll_main.setVisibility(View.VISIBLE);
                        populateDatatoAdapter(response.body());
                    }
                    else if(response.body().getInfo().getErrorCode().equals(1))
                    {
                        ll_main.setVisibility(View.GONE);
                        ll_error.setVisibility(View.VISIBLE);
                        tv_error.setText(response.body().getInfo().getErrorMessage());
                       /* Toast toast = Toast.makeText(mContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                        toast.show();*/
                        // Toast.makeText(loginContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    hideloader();
                    ll_main.setVisibility(View.GONE);
                    ll_error.setVisibility(View.VISIBLE);
                    Toast toast = Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<DevicesListMainObject> call, Throwable t) {
                hideloader();
                Toast toast = Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });


    }

    private void populateDatatoAdapter(DevicesListMainObject result) {

        if(result!=null)
        {
            dataList.addAll(result.getResult());
        }
        for (int i=0; i<result.getResult().size(); i++)
        {

            headerInfo = new DevicesListResultObject();
            headerInfo.setInventoryList(result.getResult().get(i).getInventoryList());
            headerInfo.setProductId(result.getResult().get(i).getProductId());
            headerInfo.setProductName(result.getResult().get(i).getProductName());
            headerInfo.setVersion(result.getResult().get(i).getVersion());
            deptList.add(headerInfo);
        }
        adapter = new UserDevicesListAdapter(mContext, dataList);
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

    //method to collapse all groups
    private void collapseAll() {
        int count = adapter.getGroupCount();
        for (int i = 0; i < count; i++){
            exlv_devices.collapseGroup(i);
        }
    }

    @Override
    public void onBackPressed() {
        if(MApplication.getBoolean(this, Constants.IsDefaultDeviceSaved))
        {
            finish();
            // Do exit app or back press here
            super.onBackPressed();
        }
        else {
            Intent intent = new Intent(this, UserAccountsActivity.class);
            startActivity(intent);
            // Do exit app or back press here
            super.onBackPressed();
        }



    }


    public void showLoaderNew() {
        runOnUiThread(new UserDevicesListActivity.Runloader(getResources().getString(R.string.loading)));
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
                    dialog = new Dialog(mContext,R.style.Theme_AppCompat_Light_DarkActionBar);
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


}
