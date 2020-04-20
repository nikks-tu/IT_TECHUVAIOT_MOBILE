package com.techuva.iot.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;

import com.techuva.iot.R;
import com.techuva.iot.response_model.forwarningResultObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GridAdapterForwarning extends ArrayAdapter<forwarningResultObject> {
    private final int resource;
    private Context mContext;
    ArrayList<forwarningResultObject> list;

    public GridAdapterForwarning(@NonNull Context context, int resource, @NonNull ArrayList<forwarningResultObject> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.mContext = context;
        this.list = objects;
    }

    // Constructor


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public forwarningResultObject getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
/*
    public int getChannelNumber(int position)
    {
        return list.get(position).getChannelNumber();
    }*/

    // create a new ImageView for each item referenced by the Adapter
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View getView(int position, View convertView, ViewGroup parent) {


        View row = convertView;
        RecordHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new RecordHolder();
            holder.tv_channel_data = row.findViewById(R.id.tv_channel_data);
            holder.tv_channel_name = row.findViewById(R.id.tv_channel_name);
            holder.cv_item = row.findViewById(R.id.cv_item);
            holder.ll_main = row.findViewById(R.id.ll_main);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        forwarningResultObject item = list.get(position);

        Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        holder.tv_channel_data.setTypeface(faceLight);
        holder.tv_channel_name.setText(item.getCHANNELLABEL());
        holder.tv_channel_name.setTypeface(faceLight);
        DecimalFormat df = new DecimalFormat("#.###");
        String channelValue = df.format(item.getValue());
        channelValue = channelValue+" %";
        int value = (int) round(Double.parseDouble(item.getValue()), 2);
        if(value<=10)
        {
           holder.ll_main.setBackgroundColor(getContext().getResources().getColor(R.color.bug_green));
        }
        else if(value<=50) {
            holder.ll_main.setBackgroundColor(getContext().getResources().getColor(R.color.bug_orange));
        }
        else if(value>51) {
            holder.ll_main.setBackgroundColor(getContext().getResources().getColor(R.color.bug_red));
        }
        holder.tv_channel_data.setText(channelValue);

        holder.tv_channel_data.setTypeface(faceLight);
        return row;
    }

    static class RecordHolder {

        TextView tv_channel_data;
        TextView tv_channel_name;
        CardView cv_item;
        LinearLayout ll_main;
    }
    // Keep all Images in array
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
