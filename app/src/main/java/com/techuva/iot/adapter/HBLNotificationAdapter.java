package com.techuva.iot.adapter;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techuva.iot.R;
import com.techuva.iot.response_model.HblNotificationResultObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HBLNotificationAdapter extends BaseAdapter {
    private final int resource;
    private Context mContext;
    List<HblNotificationResultObject> list;

    public HBLNotificationAdapter(int resource, Context mContext, List<HblNotificationResultObject> list) {
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
            view = inflater.inflate(R.layout.item_hbl_notification, parent, false);

            holder = new ViewHolder();

            holder.tv_channel_id = view.findViewById(R.id.tv_channel_id);
            holder.tv_channel_label =  view.findViewById(R.id.tv_channel_label);
            holder.tv_notification_time =  view.findViewById(R.id.tv_notification_time);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final HblNotificationResultObject model = list.get(position);

        String dateToParse = model.getNotificationTime();
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
        SimpleDateFormat timeFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String sa = timeFormatter.format(date);

        holder.tv_notification_time.setText(sa);
        holder.tv_channel_id.setText(model.getChannelNumber());
        holder.tv_channel_id.setVisibility(View.GONE);
        holder.tv_channel_label.setText(model.getChannelValue());
        return view;
    }

    // View Holder
    private class ViewHolder {
        public TextView  tv_channel_id, tv_channel_label, tv_notification_time;
    }


}
