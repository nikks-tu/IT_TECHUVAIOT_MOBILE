package com.techuva.iot.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.iot.R;
import com.techuva.iot.api_interface.SaveThresholdDataInterface;
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.GetChannelDataWMotor;
import com.techuva.iot.model.UpdateChannelPostParameters;
import com.techuva.iot.utils.views.MApplication;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GridAdapterMotorChannels extends ArrayAdapter<GetChannelDataWMotor> {
    private final int resource;
    private Context mContext;
    ArrayList<GetChannelDataWMotor> list;
    AlertDialog alertDialog;
    String authorityKey="";
    String userId;
    String CHANNEL_LABEL;
    String MinValue;
    String MaxValue;
    String UNIT_OF_MEASURE;
    public GridAdapterMotorChannels(@NonNull Context context, int resource, @NonNull ArrayList<GetChannelDataWMotor> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.mContext = context;
        this.list = objects;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public GetChannelDataWMotor getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getChannelNumber(int position)
    {
        return list.get(position).getCHANNELNUMBER();
    }

    // create a new ImageView for each item referenced by the Adapter
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        RecordHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new RecordHolder();
            holder.tv_channel_icon = row.findViewById(R.id.tv_channel_icon);
            holder.tv_channel_name_motor = row.findViewById(R.id.tv_channel_name_motor);
            holder.tv_channel_value = row.findViewById(R.id.tv_channel_value);
            holder.iv_editChannel = row.findViewById(R.id.iv_editChannel);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        GetChannelDataWMotor item = list.get(position);

        userId = MApplication.getString(mContext, Constants.UserID);
        authorityKey = "Bearer " + MApplication.getString(mContext, Constants.AccessToken);
        Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        holder.tv_channel_value.setTypeface(faceLight);
        holder.tv_channel_icon.setTypeface(faceLight);
        holder.tv_channel_icon.setText(item.getUNITOFMEASURE());
        String s = item.getCHANNELLABEL();
        s = s.replaceAll("_", " ");
        holder.tv_channel_name_motor.setText(s);
        holder.tv_channel_name_motor.setTypeface(faceLight);
        holder.tv_channel_value.setText(item.getCHANNELDEFAULTVALUE());

        holder.iv_editChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(item, convertView, parent);
            }
        });


        return row;
    }

    static class RecordHolder {
        TextView tv_channel_icon;
        TextView tv_channel_name_motor;
        TextView tv_channel_value;
        ImageView iv_editChannel;
    }
    // Keep all Images in array

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showCustomDialog(GetChannelDataWMotor item, View view, ViewGroup viewGroup) {
     //   ViewGroup viewGroup = view.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog_update_channel, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(dialogView);
        TextView tv_head = dialogView.findViewById(R.id.tv_head);
        TextView tv_channel_label = dialogView.findViewById(R.id.tv_channel_label);
        EditText edt_channel_label = dialogView.findViewById(R.id.edt_channel_label);
        TextView tv_channel_max_value = dialogView.findViewById(R.id.tv_channel_max_value);
        EditText edt_channel_max_value = dialogView.findViewById(R.id.edt_channel_max_value);
        TextView tv_channel_min_value = dialogView.findViewById(R.id.tv_channel_min_value);
        EditText edt_channel_min_value = dialogView.findViewById(R.id.edt_channel_min_value);
        TextView tv_unit_measure = dialogView.findViewById(R.id.tv_unit_measure);
        EditText edt_unit_measure = dialogView.findViewById(R.id.edt_unit_measure);
        TextView button_cancel = dialogView.findViewById(R.id.button_cancel);
        TextView button_update = dialogView.findViewById(R.id.button_update);
        button_cancel.setVisibility(View.VISIBLE);
        button_update.setVisibility(View.VISIBLE);
        View divider = dialogView.findViewById(R.id.divider);


        edt_channel_min_value.setText(String.valueOf(item.getMinValue()));
        edt_channel_max_value.setText(String.valueOf(item.getMaxValue()));
        edt_unit_measure.setText(item.getUNITOFMEASURE());
        edt_channel_label.setText(item.getCHANNELLABEL());


        //finally creating the alert dialog and displaying it
        alertDialog = builder.create();
        alertDialog.show();


        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        CHANNEL_LABEL = edt_channel_label.getText().toString();
        MinValue = edt_channel_min_value.getText().toString();
        MaxValue = edt_channel_max_value.getText().toString();
        UNIT_OF_MEASURE = edt_unit_measure.getText().toString();




        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CHANNEL_LABEL = edt_channel_label.getText().toString();
                MinValue = edt_channel_min_value.getText().toString();
                MaxValue = edt_channel_max_value.getText().toString();
                UNIT_OF_MEASURE = edt_unit_measure.getText().toString();

                if(!CHANNEL_LABEL.equals(""))
                {
                    if(!MinValue.equals(""))
                    {
                        if(!MaxValue.equals(""))
                        {
                            if(!UNIT_OF_MEASURE.equals(""))
                            {

                                GetChannelDataWMotor itemNew = new GetChannelDataWMotor();
                                itemNew.setINVENTORYID(item.getINVENTORYID());
                                itemNew.setINVENTORYNAME(item.getINVENTORYNAME());
                                itemNew.setDISPLAYNAME(item.getDISPLAYNAME());
                                itemNew.setCHANNELNUMBER(item.getCHANNELNUMBER());
                                itemNew.setCHANNELDESCRIPTION(item.getCHANNELDESCRIPTION());
                                itemNew.setCHANNELLABEL(CHANNEL_LABEL);
                                itemNew.setCHANNELICON(item.getCHANNELICON());
                                itemNew.setCHANNELDEFAULTVALUE(item.getCHANNELDEFAULTVALUE());
                                itemNew.setMinValue(Integer.parseInt(MinValue));
                                itemNew.setMaxValue(Integer.parseInt(MaxValue));
                                itemNew.setUNITOFMEASURE(UNIT_OF_MEASURE);

                                serviceCallForUpdateChannel(itemNew);
                            }
                            else {
                                Toast.makeText(mContext, "Please enter unit of measure", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(mContext, "Please enter maximum value of channel", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(mContext, "Please enter minimum value of channel", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(mContext, "Please enter channel Label", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void serviceCallForUpdateChannel(GetChannelDataWMotor item) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SaveThresholdDataInterface service = retrofit.create(SaveThresholdDataInterface.class);

        Call<JsonElement> call = service.updateChannelData(Integer.parseInt(userId), authorityKey, new UpdateChannelPostParameters(item.getINVENTORYID(), item.getINVENTORYNAME(),
                item.getDISPLAYNAME(), item.getCHANNELNUMBER(), item.getCHANNELDESCRIPTION(), item.getCHANNELLABEL(), item.getCHANNELICON(), item.getCHANNELDEFAULTVALUE(), item.getMinValue(), item.getMaxValue(), item.getUNITOFMEASURE()));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){
                    //Toast.makeText(getBaseContext(),response.body().getInfo().getErrorMessage(),Toast.LENGTH_SHORT).show();
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject infoObject = jsonObject.get("info").getAsJsonObject();
                    String errorMsg = infoObject.get("ErrorMessage").getAsString();
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();

                }else {
                    Toast toast = Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast toast = Toast.makeText(mContext, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });


    }
}
