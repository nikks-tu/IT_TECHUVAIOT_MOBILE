package com.techuva.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techuva.iot.R;
import com.techuva.iot.model.ChannelNameAndValue;

import java.util.List;

public class ThresholdChannelDataAdapter extends BaseAdapter {
    private Context mContext;
    List<ChannelNameAndValue> list;

    public ThresholdChannelDataAdapter(Context mContext, List<ChannelNameAndValue> list) {
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

    public String getChannelNum(int position) {

        return String.valueOf(list.get(position).getChannelValue());
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
            view = inflater.inflate(R.layout.item_threshold_values, parent, false);

            holder = new ViewHolder();
            holder.divider = view.findViewById(R.id.divider);
            holder.divider.setVisibility(View.GONE);
            holder.tv_time_value = (TextView) view.findViewById(R.id.tv_time_value);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final ChannelNameAndValue model = list.get(position);


        // bitmap

        // setting data over views
       // holder.tv_time_value.setText(model.getTime());

        holder.tv_time_value.setText(String.valueOf(model.getChannelName()));

        return view;
    }

    // View Holder
    private class ViewHolder {
        public TextView tv_time_value;
        View divider;
    }
   /* @Override
    public int getViewTypeCount() {
        if(list.size()==0){
            return 1;
        }
        return list.size();
    }*/




}
