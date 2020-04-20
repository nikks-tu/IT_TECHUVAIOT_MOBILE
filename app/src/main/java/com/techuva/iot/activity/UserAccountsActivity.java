package com.techuva.iot.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.adapter.AccountListRcvAdapter;
import com.techuva.iot.api_interface.AccountDetailsInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.AccountListResultObject;
import com.techuva.iot.model.AccountsListMainObject;
import com.techuva.iot.utils.views.MApplication;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UserAccountsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tv_btn_continue, tv_success_txt, tv_chooseAccounts, tv_chooseAccount2;
    LinearLayout ll_header, ll_back_btn;
    CardView cv_successMsg;
    RecyclerView rcv_user_accounts;
    Context mContext;
    int UserId;
    AccountListRcvAdapter adapter;
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;
    ArrayList<AccountListResultObject> accountList;
    ListView lv_data_list;
    String authorityKey ="";
    String grantType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_accounts);
        init();
        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit Techuva IoT", Toast.LENGTH_SHORT);
        serviceCall();
        setTypeface();
        onClickEvent();

        ll_back_btn.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void setTypeface() {
        Typeface faceLight = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceBook = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Book.otf");
        Typeface faceMedium = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        tv_btn_continue.setTypeface(faceMedium);
        tv_success_txt.setTypeface(faceMedium);
        tv_chooseAccounts.setTypeface(faceLight);
        tv_chooseAccount2.setTypeface(faceLight);
    }

    private void init() {
        mContext = UserAccountsActivity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        accountList = new ArrayList<>();
        tv_btn_continue  = findViewById(R.id.tv_btn_continue);
        tv_chooseAccounts  = findViewById(R.id.tv_chooseAccounts);
        tv_success_txt  = findViewById(R.id.tv_success_txt);
        tv_chooseAccount2  = findViewById(R.id.tv_chooseAccount2);
        ll_header  = findViewById(R.id.ll_header);
        ll_back_btn = findViewById(R.id.ll_back_btn);
        cv_successMsg  = findViewById(R.id.cv_successMsg);
        rcv_user_accounts  = findViewById(R.id.rcv_user_accounts);
        lv_data_list  = findViewById(R.id.lv_data_list);
        UserId = Integer.parseInt(MApplication.getString(mContext, Constants.UserID));
        //authorityKey = Constants.AuthorizationKey;
        authorityKey = "Bearer "+ MApplication.getString(mContext, Constants.AccessToken);
        grantType = Constants.GrantType;
        //Toast.makeText(mContext, "UserId"+ UserId, Toast.LENGTH_SHORT).show();
    }

    private void serviceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.USER_MGMT_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AccountDetailsInterface service = retrofit.create(AccountDetailsInterface.class);
        //Call<AccountsListMainObject> call = service.getUserAccountDetails(UserId);
        Call<AccountsListMainObject> call = service.getUserAccountDetailsWithSession(UserId, authorityKey, UserId);
        call.enqueue(new Callback<AccountsListMainObject>() {
            @Override
            public void onResponse(Call<AccountsListMainObject> call, Response<AccountsListMainObject> response) {

                if (response.code()==401)
                {
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(mContext, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if(response.body()!=null){
                    // Toast.makeText(getBaseContext(),response.body().getInfo().getErrorMessage(),Toast.LENGTH_SHORT).show();

                    if(response.body().getInfo().getErrorCode()==0)
                    {
                        /*Intent intent = new Intent(mContext, UserDevicesListActivity.class);
                        startActivity(intent);*/
                        setDataAdapterforList(response.body());
                    }
                    else if(response.body().getInfo().getErrorCode().equals(1))
                    {
                        Toast toast = Toast.makeText(mContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                        toast.show();
                   }
                }else {

                }
            }
            @Override
            public void onFailure(Call<AccountsListMainObject> call, Throwable t) {
                Toast toast = Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });
    }

    private void setDataAdapterforList(AccountsListMainObject result) {

        if(result!=null)
        {
            accountList.addAll(result.getResult());
        }
        if(accountList.size()==1)
        {
           // Toast.makeText(mContext, "Single", Toast.LENGTH_SHORT).show();
            MApplication.setBoolean(mContext, Constants.SingleAccount, true);
        }
        else
        {
            MApplication.setBoolean(mContext, Constants.SingleAccount, false);

        }
        adapter = new AccountListRcvAdapter(mContext, accountList, new AccountListRcvAdapter.OnItemClicked () {
            @Override
            public void onItemClick(View view, int position) {
              //  Toast.makeText(mContext, "HI"+ accountList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_user_accounts.setLayoutManager(linearLayoutManager);
        rcv_user_accounts.setAdapter(adapter);

    }

    private void onClickEvent() {
        tv_btn_continue.setOnClickListener(view -> {
            //Get the selected position
             adapter.getSelectedItem();

            if(!adapter.getSelectedItem().equals(""))
            {
                int i =  accountList.get(Integer.parseInt(adapter.getSelectedItem())).getCompanyId();
                MApplication.setString(mContext, Constants.CompanyID, String.valueOf(i));
                MApplication.setBoolean(mContext, Constants.IsDefaultDeviceSaved, false);
                Intent intent = new Intent(mContext, UserDevicesListActivity.class);
                startActivity(intent);
            }
           else
               Toast.makeText(mContext, "Please select account to proceed", Toast.LENGTH_SHORT).show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            // Do what ever you want
            exitToast.show();
            doubleBackToExitPressedOnce = false;
        } else{
            finishAffinity();
            finish();
            // Do exit app or back press here
            super.onBackPressed();
        }
    }
}
