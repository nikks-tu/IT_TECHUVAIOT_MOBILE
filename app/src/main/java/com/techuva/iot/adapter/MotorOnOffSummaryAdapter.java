package com.techuva.iot.adapter;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techuva.iot.R;
import com.techuva.iot.response_model.MotorOnOffSummary;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MotorOnOffSummaryAdapter extends BaseAdapter {
    private final int resource;
    private Context mContext;
    ArrayList<MotorOnOffSummary> list;

    public MotorOnOffSummaryAdapter(int resource, Context mContext, ArrayList<MotorOnOffSummary> list) {
        this.resource = resource;
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        ViewHolder holder = null;

        // Inflater for custom layout
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.item_summary_values, parent, false);

            holder = new ViewHolder();

            holder.tv_date_value = (TextView) view.findViewById(R.id.tv_date_value);
            holder.tv_time_value = (TextView) view.findViewById(R.id.tv_time_value);
            holder.tv_history_channel_values = (TextView) view.findViewById(R.id.tv_history_channel_values);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final MotorOnOffSummary model = list.get(position);


        // bitmap

        // setting data over views
       // holder.tv_time_value.setText(model.getTime());

        String dateToParse = model.getRECEIVEDTIME();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

        Date date = null;
        try {
            date = sdf.parse(dateToParse);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String dateToStr = format.format(date);
            //System.out.println(dateToStr);
            //holder.tv_date_value.setText(dateToStr);
        } catch (ParseException | java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat timeFormatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String sa = timeFormatter.format(date);
        holder.tv_date_value.setVisibility(View.VISIBLE);
        holder.tv_date_value.setText(sa);


        switch (Integer.parseInt(model.getSTATUS()))
        {
            case 0:
            case 6:
            case 3:
            case 2:
                holder.tv_time_value.setText("ON");
                break;
            case 1:
            case 5:
            case 4:
                holder.tv_time_value.setText("OFF");
                break;
        }

        switch (Integer.parseInt(model.getSTATUS()))
        {
            case 0:
            case 1:
                holder.tv_history_channel_values.setText("Remote");
                break;
            case 3:
            case 4:
                holder.tv_history_channel_values.setText("Manual");
                break;
            case 2:
                holder.tv_history_channel_values.setText("Neutral");
                break;
            case 5:
            case 6:
                holder.tv_history_channel_values.setText("Auto");
                break;
        }

        return view;
    }

    private class ViewHolder {
        public TextView tv_date_value, tv_time_value, tv_history_channel_values;
    }
}
