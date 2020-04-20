package com.techuva.iot.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.techuva.iot.R;
import com.techuva.iot.response_model.ProcomChannelDataResultObject;
import com.techuva.iot.utils.views.TextViewIcomoon;

import java.util.ArrayList;

public class GridAdapterFaultInfo extends ArrayAdapter<ProcomChannelDataResultObject> {
    private final int resource;
    private Context mContext;
    ArrayList<ProcomChannelDataResultObject> list;

    public GridAdapterFaultInfo(@NonNull Context context, int resource, @NonNull ArrayList<ProcomChannelDataResultObject> objects) {
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
    public ProcomChannelDataResultObject getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getChannelNumber(int position)
    {
        return list.get(position).getChannelNumber();
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
            holder.tv_channel_name = row.findViewById(R.id.tv_channel_name);
            holder.tv_channel_value = row.findViewById(R.id.tv_channel_value);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }
        ProcomChannelDataResultObject item = list.get(position);
        String s2;

  /*      if(item.getIcon()== null)
        {
            //Toast.makeText(mContext, "Null", Toast.LENGTH_SHORT).show();
            s2="";
        }
        else {
          s2 = item.getIcon();
        }*/
/*
        if(!s2.equals(""))
        {
            String s1 = s2.replaceAll("&#x", "");

            s1 = s1.replaceAll(";", "");

            String icon = new String(Character.toChars(Integer.parseInt(
                    s1, 16)));

            //String temp = capitalize(item.getLabel());
            if (!icon.equals(""))
            {
                holder.tv_channel_icon.setText(icon);
            }else {
                holder.tv_channel_icon.setVisibility(View.GONE);
            }
        }*/

      /*  if(item.getLabel().equals("KWH"))
        {
            //Toast.makeText(mContext, ""+item.channelNumber, Toast.LENGTH_SHORT).show();
            MApplication.setString(mContext, Constants.ChannelNumKWH, String.valueOf(item.channelNumber));
        }*/
        Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        holder.tv_channel_name.setTypeface(faceLight);
        //holder.tv_channel_icon.setTypeface(faceLight);
        String s = item.getLabel();
        s = s.replaceAll("_", " ");
        holder.tv_channel_name.setText(s);
        holder.tv_channel_name.setTypeface(faceLight);
        int value = Integer.parseInt(item.getValue());
        if (value == 1) {
            holder.tv_channel_value.setText("Yes");
            holder.tv_channel_value.setTextColor(getContext().getResources().getColor(R.color.text_color_red));
        } else {
            holder.tv_channel_value.setText("No");
            holder.tv_channel_value.setTextColor(getContext().getResources().getColor(R.color.text_color_green));
        }
        holder.tv_channel_value.setTypeface(faceLight);
        return row;
    }

    static class RecordHolder {
        TextViewIcomoon tv_channel_icon;
        TextView tv_channel_name;
        TextView tv_channel_value;
    }

}
