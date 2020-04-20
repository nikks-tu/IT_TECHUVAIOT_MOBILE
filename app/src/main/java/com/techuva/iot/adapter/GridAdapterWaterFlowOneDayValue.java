package com.techuva.iot.adapter;

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

import com.techuva.iot.R;
import com.techuva.iot.response_model.WaterMonValuesObject;
import com.techuva.iot.utils.BubbleDrawable;
import com.techuva.iot.utils.BubbleDrawableWhite;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GridAdapterWaterFlowOneDayValue extends ArrayAdapter<WaterMonValuesObject> {
    private final int resource;
    private Context mContext;
    ArrayList<WaterMonValuesObject> list;

    public GridAdapterWaterFlowOneDayValue(@NonNull Context context, int resource, @NonNull ArrayList<WaterMonValuesObject> objects) {
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
    public WaterMonValuesObject getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
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
            holder.ll_manjeera_water = row.findViewById(R.id.ll_manjeera_water);
            holder.ll_water_txt = row.findViewById(R.id.ll_water_txt);
            holder.tv_water_txt = row.findViewById(R.id.tv_water_txt);
            holder.tv_water_value = row.findViewById(R.id.tv_water_value);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        WaterMonValuesObject item = list.get(position);

        holder.tv_water_txt.setText(item.getLabel());

        if(!item.getValue().equals("faulty"))
        {
            holder.tv_water_value.setText(String.format("%.2f", item.getValue()) +" "+item.getUnitOfMeasure());
        }
        else {
            holder.tv_water_value.setText(String.valueOf(item.getValue()));
        }


        BubbleDrawableWhite myBubble = new BubbleDrawableWhite(BubbleDrawable.CENTER);
        myBubble.setCornerRadius(0);
        myBubble.setPointerAlignment(BubbleDrawableWhite.CENTER);
        myBubble.setPadding(15,15, 20,15);
        //holder.ll_main_bubble.setBackground(myBubble);
        Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        holder.tv_water_txt.setTypeface(faceLight);
        holder.tv_water_value.setTypeface(faceLight);
        return row;
    }
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }
        return capMatcher.appendTail(capBuffer).toString();
    }

    static class RecordHolder {
        LinearLayout ll_manjeera_water, ll_water_txt;
        TextView  tv_water_txt, tv_water_value;

    }
    // Keep all Images in array

}
