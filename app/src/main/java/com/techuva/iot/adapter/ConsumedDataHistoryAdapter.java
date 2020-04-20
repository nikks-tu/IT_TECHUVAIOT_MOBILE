package com.techuva.iot.adapter;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techuva.iot.R;
import com.techuva.iot.response_model.ConsumedDataHistoryObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConsumedDataHistoryAdapter extends BaseAdapter {
    private final int resource;
    private Context mContext;
    List<ConsumedDataHistoryObject> list;

    public ConsumedDataHistoryAdapter(int resource, Context mContext, List<ConsumedDataHistoryObject> list) {
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
            view = inflater.inflate(R.layout.item_hr_channel_unit_values, parent, false);

            holder = new ViewHolder();

            holder.tv_time_value = (TextView) view.findViewById(R.id.tv_time_value);
            holder.tv_history_channel_values = (TextView) view.findViewById(R.id.tv_history_channel_values);
            holder.tv_unit_value = (TextView) view.findViewById(R.id.tv_unit_value);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final ConsumedDataHistoryObject model = list.get(position);

        String dateToParse = model.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

        Date date = null;
        try {
            date = sdf.parse(dateToParse);
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String dateToStr = format.format(date);
            //System.out.println(dateToStr);
            //holder.tv_date_value.setText(dateToStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        String sa = timeFormatter.format(date);

       holder.tv_time_value.setText(sa);
       holder.tv_unit_value.setText(String.valueOf(model.getUnits()));
        holder.tv_history_channel_values.setText(model.getChannelValue());

        return view;
    }

    // View Holder
    private class ViewHolder {
        public TextView tv_time_value, tv_history_channel_values, tv_unit_value;
    }
   /* @Override
    public int getViewTypeCount() {
        if(list.size()==0){
            return 1;
        }
        return list.size();
    }*/




}
