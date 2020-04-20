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
import com.techuva.iot.model.CurrentDataValueObject;
import com.techuva.iot.utils.BubbleDrawable;
import com.techuva.iot.utils.BubbleDrawableWhite;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GridAdapterChannelWaterFlow extends ArrayAdapter<CurrentDataValueObject> {
    private final int resource;
    private Context mContext;
    ArrayList<CurrentDataValueObject> list;

    public GridAdapterChannelWaterFlow(@NonNull Context context, int resource, @NonNull ArrayList<CurrentDataValueObject> objects) {
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
    public CurrentDataValueObject getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getSelectedDate(int position)
    {
        return list.get(position).getDate();
    }

    public String getMonth(int position)
    {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf_month = new SimpleDateFormat("MM");
        String inputText = list.get(position).getDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String month  = sdf_month.format(date);
        return month;
    }

    public String getDay(int position)
    {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String inputText = list.get(position).getDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String day  = sdf.format(date);
        return day;
    }


    public String getYear(int position)
    {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String inputText = list.get(position).getDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String year  = sdf.format(date);
        return year;
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

        CurrentDataValueObject item = list.get(position);

        holder.tv_water_txt.setText(item.getLabel());
        if(!item.getValue().equals("faulty")){
            holder.tv_water_value.setText(String.format("%.2f", Double.parseDouble(item.getValue())));
        }
        else {
            holder.tv_water_value.setText(item.getValue());
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
