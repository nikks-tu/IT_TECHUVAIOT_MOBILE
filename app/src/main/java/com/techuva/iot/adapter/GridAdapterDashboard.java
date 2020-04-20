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
import com.techuva.iot.app.Constants;
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.utils.views.MApplication;
import com.techuva.iot.utils.views.TextViewIcomoon;

import java.util.ArrayList;

public class GridAdapterDashboard extends ArrayAdapter<CurrentDataValueObject> {
    private final int resource;
    private Context mContext;
    ArrayList<CurrentDataValueObject> list;

    public GridAdapterDashboard(@NonNull Context context, int resource,  @NonNull ArrayList<CurrentDataValueObject> objects) {
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
    public CurrentDataValueObject getItem(int position) {
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
        CurrentDataValueObject item = list.get(position);


        String s2;

        if(item.getIcon()== null)
        {
            s2="";
        }
        else {
          s2 = item.getIcon();
        }

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
        }

        if(item.getLabel().equals("KWH"))
        {
            //Toast.makeText(mContext, ""+item.channelNumber, Toast.LENGTH_SHORT).show();
            MApplication.setString(mContext, Constants.ChannelNumKWH, String.valueOf(item.channelNumber));
        }

        Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/AvenirLTStd-Light.otf");

       /* File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My_IOT_Files");
        //  File file = new File(Dir,"nahk.txt");

        File file = new File(Dir,  "/icomoon.ttf");

        if(file.exists()){
            Typeface typeFace = Typeface.createFromFile(
                    new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/My_IOT_Files/icomoon.ttf" ));
            holder.tv_channel_icon.setTypeface(typeFace);
        }
        else {
            // Toast.makeText(context, "No Font", Toast.LENGTH_SHORT).show();
        }
*/
        holder.tv_channel_name.setTypeface(faceLight);
       // holder.tv_channel_icon.setTypeface(ico);
        String s = item.getLabel();
        s = s.replaceAll("_", " ");
        holder.tv_channel_name.setText(s);
        holder.tv_channel_name.setTypeface(faceLight);
        holder.tv_channel_value.setText(item.getValue());
        holder.tv_channel_value.setTypeface(faceLight);
        return row;
    }

    static class RecordHolder {
        TextViewIcomoon tv_channel_icon;
        TextView tv_channel_name;
        TextView tv_channel_value;
    }
    // Keep all Images in array

}
