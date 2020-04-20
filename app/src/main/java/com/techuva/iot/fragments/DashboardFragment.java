package com.techuva.iot.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.activity.TokenExpireActivity;
import com.techuva.iot.api_interface.ChangeDGStatusInterface;
import com.techuva.iot.api_interface.CurrentDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.ChangeDGStatusPostParameter;
import com.techuva.iot.model.CurrentDataMainObject;
import com.techuva.iot.model.CurrentDataPostParameter;
import com.techuva.iot.model.CurrentDataResultObject;
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.utils.views.AppDatabase;
import com.techuva.iot.utils.views.MApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DashboardFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    LinearLayout ll_dg;
    ImageView iv_auto_semi, iv_manual, iv_dg_on, iv_dg_off, iv_voltage_main_l1, iv_voltage_main_l2, iv_voltage_main_l3;
    ImageView iv_voltage_dg_l1, iv_voltage_dg_l2, iv_voltage_dg_l3, iv_current_l1, iv_current_l2, iv_current_l3;
    TextView tv_dg_on, tv_dg_off, tv_dg_reset, tv_dg_operation_mode, tv_dg_on_off, tv_voltage_main;
    TextView tv_auto_semi_mode, tv_manual_mode, tv_dg_on_status, tv_dg_off_status, tv_voltage_l1;
    TextView tv_voltage_main_l3, tv_voltage_l3_value, tv_voltage_dg_l1,tv_voltage_dg_l1_value;
    TextView  tv_voltage_l1_value, tv_voltage_l2_value, tv_voltage_dg, tv_voltage_dg_l2, tv_voltage_dg_l2_value;
    TextView tv_voltage_dg_l3, tv_voltage_dg_l3_value, tv_current, tv_current_l1, tv_current_l1_value;
    TextView tv_current_l2, tv_current_l2_value, tv_current_l3, tv_current_l3_value;
    TextView tv_voltage_main_no_data, tv_dg_voltage_main_no_data, tv_current_no_data;
    LinearLayout ll_voltage_mains, ll_dg_voltage, ll_current_details;
    LinearLayout ll_voltage_mains_1, ll_voltage_mains_2, ll_voltage_mains_3;
    LinearLayout ll_voltage_dg_1, ll_voltage_dg_2, ll_voltage_dg_3;
    LinearLayout ll_current_1, ll_current_2, ll_current_3;
    Context context;
    String deviceId, userId, deviceName;
    int UserId;
    String authorityKey ="";
    String onDG = "1";
    String offDG = "0";
    String resetDG = "2";
    Drawable round_red;
    Drawable round_dg;
    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dashboard, null, false);
        initialize(contentView);
        setTypeFace();
        serviceCall();

        tv_dg_on.setOnClickListener(v -> showDialogBox(context.getResources().getString(R.string.dg_on_txt), onDG));

        tv_dg_off.setOnClickListener(v -> showDialogBox(context.getResources().getString(R.string.dg_off_txt), offDG));

        tv_dg_reset.setOnClickListener(v -> showDialogBox(context.getResources().getString(R.string.dg_reset_txt), resetDG));
        return contentView;
    }

    private void showDialogBox(String msg, String status) {

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        /*adb.setTitle("Title of alert dialog");
        adb.setIcon(android.R.drawable.ic_dialog_alert);*/
        adb.setMessage(msg);
        adb.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                serviceCallToChangeDGStatus(status);
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.show();


    }

    private void setTypeFace() {
        Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        tv_dg_on.setTypeface(faceLight);
        tv_dg_off.setTypeface(faceLight);
        tv_dg_reset.setTypeface(faceLight);
        tv_dg_operation_mode.setTypeface(faceLight);
        tv_dg_on_off.setTypeface(faceLight);
        tv_voltage_main.setTypeface(faceLight);
        tv_auto_semi_mode.setTypeface(faceLight);
        tv_manual_mode.setTypeface(faceLight);
        tv_dg_on_status.setTypeface(faceLight);
        tv_dg_off_status.setTypeface(faceLight);
        tv_voltage_l1.setTypeface(faceLight);
        tv_voltage_main_l3.setTypeface(faceLight);
        tv_voltage_l3_value.setTypeface(faceLight);
        tv_voltage_dg_l1.setTypeface(faceLight);
        tv_voltage_dg_l1_value.setTypeface(faceLight);
        tv_voltage_l1_value.setTypeface(faceLight);
        tv_voltage_l2_value.setTypeface(faceLight);
        tv_voltage_dg.setTypeface(faceLight);
        tv_voltage_dg_l2.setTypeface(faceLight);
        tv_voltage_dg_l2_value.setTypeface(faceLight);
        tv_voltage_dg_l3.setTypeface(faceLight);
        tv_voltage_dg_l3_value.setTypeface(faceLight);
        tv_current.setTypeface(faceLight);
        tv_current_l1.setTypeface(faceLight);
        tv_current_l1_value.setTypeface(faceLight);
        tv_current_l2.setTypeface(faceLight);
        tv_current_l2_value.setTypeface(faceLight);
        tv_current_l3.setTypeface(faceLight);
        tv_current_l3_value.setTypeface(faceLight);
        tv_voltage_main_no_data.setTypeface(faceLight);
        tv_dg_voltage_main_no_data.setTypeface(faceLight);
        tv_current_no_data.setTypeface(faceLight);
    }

    private void initialize(View contentView) {
        context = getContext();
        deviceId = MApplication.getString(context, Constants.DeviceID);
        deviceName = MApplication.getString(context, Constants.InventoryName);
        userId = "1001";
        authorityKey = "Bearer "+ MApplication.getString(context, Constants.AccessToken);
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        ll_dg = contentView.findViewById(R.id.ll_dg);
        ll_voltage_mains = contentView.findViewById(R.id.ll_voltage_mains);
        ll_current_details = contentView.findViewById(R.id.ll_current_details);
        ll_dg_voltage = contentView.findViewById(R.id.ll_dg_voltage);
        ll_voltage_mains_1 = contentView.findViewById(R.id.ll_voltage_mains_1);
        ll_voltage_mains_2 = contentView.findViewById(R.id.ll_voltage_mains_2);
        ll_voltage_mains_3 = contentView.findViewById(R.id.ll_voltage_mains_3);
        ll_voltage_dg_1 = contentView.findViewById(R.id.ll_voltage_dg_1);
        ll_voltage_dg_2 = contentView.findViewById(R.id.ll_voltage_dg_2);
        ll_voltage_dg_3 = contentView.findViewById(R.id.ll_voltage_dg_3);
        ll_current_1 = contentView.findViewById(R.id.ll_current_1);
        ll_current_2 = contentView.findViewById(R.id.ll_current_2);
        ll_current_3 = contentView.findViewById(R.id.ll_current_3);
        iv_auto_semi = contentView.findViewById(R.id.iv_auto_semi);
        iv_manual = contentView.findViewById(R.id.iv_manual);
        iv_dg_on = contentView.findViewById(R.id.iv_dg_on);
        iv_dg_off = contentView.findViewById(R.id.iv_dg_off);
        iv_voltage_main_l1 = contentView.findViewById(R.id.iv_voltage_main_l1);
        iv_voltage_main_l2 = contentView.findViewById(R.id.iv_voltage_main_l2);
        iv_voltage_main_l3 = contentView.findViewById(R.id.iv_voltage_main_l3);
        iv_voltage_dg_l1 = contentView.findViewById(R.id.iv_voltage_dg_l1);
        iv_voltage_dg_l2 = contentView.findViewById(R.id.iv_voltage_dg_l2);
        iv_voltage_dg_l3 = contentView.findViewById(R.id.iv_voltage_dg_l3);
        iv_current_l1 = contentView.findViewById(R.id.iv_current_l1);
        iv_current_l2 = contentView.findViewById(R.id.iv_current_l2);
        iv_current_l3 = contentView.findViewById(R.id.iv_current_l3);
        tv_dg_on = contentView.findViewById(R.id.tv_dg_on);
        tv_dg_off = contentView.findViewById(R.id.tv_dg_off);
        tv_dg_reset = contentView.findViewById(R.id.tv_dg_reset);
        tv_dg_operation_mode = contentView.findViewById(R.id.tv_dg_operation_mode);
        tv_dg_on_off = contentView.findViewById(R.id.tv_dg_on_off);
        tv_voltage_main = contentView.findViewById(R.id.tv_voltage_main);
        tv_auto_semi_mode = contentView.findViewById(R.id.tv_auto_semi_mode);
        tv_manual_mode = contentView.findViewById(R.id.tv_manual_mode);
        tv_dg_on_status = contentView.findViewById(R.id.tv_dg_on_status);
        tv_dg_off_status = contentView.findViewById(R.id.tv_dg_off_status);
        tv_voltage_l1 = contentView.findViewById(R.id.tv_voltage_l1);
        tv_voltage_main_l3 = contentView.findViewById(R.id.tv_voltage_main_l3);
        tv_voltage_l3_value = contentView.findViewById(R.id.tv_voltage_l3_value);
        tv_voltage_dg_l1 = contentView.findViewById(R.id.tv_voltage_dg_l1);
        tv_voltage_dg_l1_value = contentView.findViewById(R.id.tv_voltage_dg_l1_value);
        tv_voltage_l1_value = contentView.findViewById(R.id.tv_voltage_l1_value);
        tv_voltage_l2_value = contentView.findViewById(R.id.tv_voltage_l2_value);
        tv_voltage_dg = contentView.findViewById(R.id.tv_voltage_dg);
        tv_voltage_dg_l2 = contentView.findViewById(R.id.tv_voltage_dg_l2);
        tv_voltage_dg_l2_value = contentView.findViewById(R.id.tv_voltage_dg_l2_value);
        tv_voltage_dg_l3 = contentView.findViewById(R.id.tv_voltage_dg_l3);
        tv_voltage_dg_l3_value = contentView.findViewById(R.id.tv_voltage_dg_l3_value);
        tv_current = contentView.findViewById(R.id.tv_current);
        tv_current_l1 = contentView.findViewById(R.id.tv_current_l1);
        tv_current_l1_value = contentView.findViewById(R.id.tv_current_l1_value);
        tv_current_l2 = contentView.findViewById(R.id.tv_current_l2);
        tv_current_l2_value = contentView.findViewById(R.id.tv_current_l2_value);
        tv_current_l3 = contentView.findViewById(R.id.tv_current_l3);
        tv_current_l3_value = contentView.findViewById(R.id.tv_current_l3_value);
        tv_voltage_main_no_data = contentView.findViewById(R.id.tv_voltage_main_no_data);
        tv_dg_voltage_main_no_data = contentView.findViewById(R.id.tv_dg_voltage_main_no_data);
        tv_current_no_data = contentView.findViewById(R.id.tv_current_no_data);
        round_red = getResources().getDrawable(R.drawable.round_red);
        round_dg = getResources().getDrawable(R.drawable.round_dg);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        DashboardFragment tabFragment = new DashboardFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    private void serviceCall(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CurrentDataInterface service = retrofit.create(CurrentDataInterface .class);

        //Call<CurrentDataMainObject> call = service.getStringScalar(new CurrentDataPostParameter(deviceId,userId));
        Call<CurrentDataMainObject> call = service.getStringScalarWithSession(UserId, authorityKey, new CurrentDataPostParameter(deviceId,userId));
        call.enqueue(new Callback<CurrentDataMainObject>() {
            @Override
            public void onResponse(Call<CurrentDataMainObject> call, Response<CurrentDataMainObject> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
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
                            try {
                                dataResponse(response.body().getResult());
                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }else {
                    Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<CurrentDataMainObject> call, Throwable t) {
            }

        });

    }

    private void dataResponse(CurrentDataResultObject currentDataResultObject) throws java.text.ParseException {
        //Success message
           if(currentDataResultObject.getValues().size()>0)
        {
            String dateToParse = currentDataResultObject.getValues().get(0).getDate();
            //dateToParse.replaceAll("AMP", "");
            DateFormat inputFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            DateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            //:String inputDateStr = dateToParse;
            Date date = inputFormat.parse(dateToParse);
            String outputDateStr = outputFormat.format(date);
                  }
        //tv_last_sync.setText(currentDataResultObject.getValues().get(0).getDate());
        List<CurrentDataValueObject> list = currentDataResultObject.getValues();
        generateDataList(list);
    }

    private void generateDataList(List<CurrentDataValueObject> values)
    {
        ArrayList<CurrentDataValueObject> voltageArray = new ArrayList<>();
        for (int i=0; i<values.size(); i++)
        {
            if(values.get(i).channelNumber==66)
            {
                if(values.get(i).getValue().equals("A/S"))
                {
                    iv_auto_semi.setImageDrawable(round_red);
                    iv_manual.setImageDrawable(round_dg);
                }
                else {
                    iv_manual.setImageDrawable(round_red);
                    iv_auto_semi.setImageDrawable(round_dg);
                }
            }
            for (int j=4; j<7; j++)
            {
                if(values.get(i).channelNumber==j)
                {
                    voltageArray.add(values.get(i));
                }
            }
        }

        //Toast.makeText(context, ""+voltageArray.size(), Toast.LENGTH_SHORT).show();
        setVoltageMainValues(voltageArray);
        ArrayList<CurrentDataValueObject> voltageDGArray = new ArrayList<>();
        for (int i=0; i<values.size(); i++)
        {
            for (int j=29; j<32; j++)
            {
                if(values.get(i).channelNumber==j)
                {
                    voltageDGArray.add(values.get(i));
                }
            }
        }
        //Toast.makeText(context, ""+voltageArray.size(), Toast.LENGTH_SHORT).show();
        setVoltageDGValues(voltageDGArray);
        ArrayList<CurrentDataValueObject> currentArray = new ArrayList<>();
        for (int i=0; i<values.size(); i++)
        {
            for (int j=1; j<4; j++)
            {
                if(values.get(i).channelNumber==j)
                {
                   currentArray.add(values.get(i));
                }
            }
        }
        //Toast.makeText(context, ""+voltageArray.size(), Toast.LENGTH_SHORT).show();
        setCurrentValues(currentArray);
        AppDatabase.init(context);
        AppDatabase.clearUnitTable();

    }

    private void setVoltageDGValues(ArrayList<CurrentDataValueObject> voltageDGArray) {

        if(voltageDGArray.size()>0)
        {
            tv_dg_voltage_main_no_data.setVisibility(View.GONE);
            ll_dg_voltage.setVisibility(View.VISIBLE);
            if(Double.parseDouble(voltageDGArray.get(0).getValue())>0)
            {
                iv_voltage_dg_l1.setImageDrawable(round_red);
                iv_dg_on.setImageDrawable(round_red);
                iv_dg_off.setImageDrawable(round_dg);
                tv_voltage_dg_l1_value.setText(voltageDGArray.get(0).getValue()+" V");

            }
            else {
                iv_voltage_dg_l1.setImageDrawable(round_dg);
                iv_dg_on.setImageDrawable(round_dg);
                tv_voltage_dg_l1_value.setText(voltageDGArray.get(0).getValue()+" V");
            }
           if(voltageDGArray.size()>1)
           {
               if(Double.parseDouble(voltageDGArray.get(1).getValue())>0)
               {
                   iv_voltage_dg_l2.setImageDrawable(round_red);
                   iv_dg_on.setImageDrawable(round_red);
                   iv_dg_off.setImageDrawable(round_dg);
                   tv_voltage_dg_l2_value.setText(voltageDGArray.get(1).getValue()+" V");
               }
               else {
                   iv_dg_on.setImageDrawable(round_dg);
                   iv_voltage_dg_l2.setImageDrawable(round_dg);
                   tv_voltage_dg_l2_value.setText(voltageDGArray.get(1).getValue()+" V");
               }
           }
           else {
               ll_voltage_dg_2.setVisibility(View.GONE);
           }
            if(voltageDGArray.size()>2)
            {
                if(Double.parseDouble(voltageDGArray.get(2).getValue())>0)
                {
                    iv_voltage_dg_l3.setImageDrawable(round_red);
                    iv_dg_on.setImageDrawable(round_red);
                    iv_dg_off.setImageDrawable(round_dg);
                    tv_voltage_dg_l3_value.setText(voltageDGArray.get(2).getValue()+" V");
                }
                else {
                    iv_voltage_dg_l3.setImageDrawable(round_dg);
                    iv_dg_on.setImageDrawable(round_dg);
                    tv_voltage_dg_l3_value.setText(voltageDGArray.get(2).getValue()+" V");
                }

                if(Double.parseDouble(voltageDGArray.get(0).getValue())>0 || Double.parseDouble(voltageDGArray.get(1).getValue())>0 || Double.parseDouble(voltageDGArray.get(2).getValue())>0)
                {
                    iv_dg_on.setImageDrawable(round_red);
                }
                else {
                    iv_dg_off.setImageDrawable(round_red);
                }
            }
            else {
                ll_voltage_dg_3.setVisibility(View.INVISIBLE);
            }





        }
    }

    private void setVoltageMainValues(ArrayList<CurrentDataValueObject> voltageArray) {
        if(voltageArray.size()>0)
        {
            tv_voltage_main_no_data.setVisibility(View.GONE);
            ll_voltage_mains.setVisibility(View.VISIBLE);
             if(Double.parseDouble(voltageArray.get(0).getValue())>0)
            {
                iv_voltage_main_l1.setImageDrawable(round_red);
                tv_voltage_l1_value.setText(voltageArray.get(0).getValue()+" V");
            }
             else {
                 iv_voltage_main_l1.setImageDrawable(round_dg);
                 tv_voltage_l1_value.setText(voltageArray.get(0).getValue()+" V");
             }
            if(voltageArray.size()>1)
            {
                if(Double.parseDouble(voltageArray.get(1).getValue())>0)
                {
                    iv_voltage_main_l2.setImageDrawable(round_red);
                    tv_voltage_l2_value.setText(voltageArray.get(1).getValue()+" V");
                }
                else {
                    iv_voltage_main_l2.setImageDrawable(round_dg);
                    tv_voltage_l2_value.setText(voltageArray.get(1).getValue()+" V");
                }
            }
            else {
                ll_voltage_mains_2.setVisibility(View.GONE);
            }
            if (voltageArray.size()>2)
            {
                if(Double.parseDouble(voltageArray.get(2).getValue())>0)
                {
                    iv_voltage_main_l3.setImageDrawable(round_red);
                    tv_voltage_l3_value.setText(voltageArray.get(2).getValue()+" V");
                }
                else {
                    iv_voltage_main_l3.setImageDrawable(round_dg);
                    tv_voltage_l3_value.setText(voltageArray.get(2).getValue()+" V");
                }
            }
            else {
                ll_voltage_mains_3.setVisibility(View.INVISIBLE);
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private void setCurrentValues(ArrayList<CurrentDataValueObject> currentArray) {
       if(currentArray.size()>0)
       {
           tv_current_no_data.setVisibility(View.GONE);
           ll_current_details.setVisibility(View.VISIBLE);
           if(Double.parseDouble(currentArray.get(0).getValue())>0.0)
           {
               iv_current_l1.setImageDrawable(round_red);
               tv_current_l1_value.setText(currentArray.get(0).getValue()+" amps");
           }
           else {
               iv_current_l1.setImageDrawable(round_dg);
               tv_current_l1_value.setText(currentArray.get(0).getValue()+" amps");
           }
           if(currentArray.size()>1)
           {
               if(Double.parseDouble(currentArray.get(1).getValue())>0.0)
               {
                   iv_current_l2.setImageDrawable(round_red);
                   tv_current_l2_value.setText(currentArray.get(1).getValue()+" amps");
               }
               else {
                   iv_current_l2.setImageDrawable(round_dg);
                   tv_current_l2_value.setText(currentArray.get(1).getValue()+" amps");
               }
           }
           else {
               ll_current_2.setVisibility(View.GONE);
                }
          if(currentArray.size()>2)
          {
              if(Double.parseDouble(currentArray.get(2).getValue())>0)
              {
                  iv_current_l3.setImageDrawable(round_red);
                  tv_current_l3_value.setText(currentArray.get(2).getValue()+" amps");
              }
              else {
                  iv_current_l3.setImageDrawable(round_dg);
                  tv_current_l3_value.setText(currentArray.get(2).getValue()+" amps");
              }
          }
          else {
              ll_current_3.setVisibility(View.INVISIBLE);
          }
       }
    }


    private void serviceCallToChangeDGStatus(String status){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChangeDGStatusInterface service = retrofit.create(ChangeDGStatusInterface.class);

        //Call<CurrentDataMainObject> call = service.getStringScalar(new CurrentDataPostParameter(deviceId,userId));
        Call<JsonElement> call = service.getStringScalarWithSession(UserId, authorityKey, new ChangeDGStatusPostParameter(deviceName, status));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }
                else if(response.body()!=null){
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject info = jsonObject.get("info").getAsJsonObject();
                    String msg = info.get("ErrorMessage").getAsString();
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                }else {
                    Log.println(1, "Empty", "Else");
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
            }

        });

    }


}
