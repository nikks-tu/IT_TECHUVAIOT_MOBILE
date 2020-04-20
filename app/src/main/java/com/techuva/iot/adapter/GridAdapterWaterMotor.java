package com.techuva.iot.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
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

public class GridAdapterWaterMotor extends ArrayAdapter<CurrentDataValueObject> {
    private final int resource;
    private Context mContext;
    ArrayList<CurrentDataValueObject> list;

    public GridAdapterWaterMotor(@NonNull Context context, int resource, @NonNull ArrayList<CurrentDataValueObject> objects) {
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
            holder.tv_label_name = row.findViewById(R.id.tv_label_name);
            holder.progressBar_water_level = row.findViewById(R.id.progressBar_water_level);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        CurrentDataValueObject item = list.get(position);

        String value = item.getLabel()+": "+item.getValue()+" "+item.getUnit_of_measure()+" ("+item.getChannelDefaultValue()+" "+item.getUnit_of_measure()+")";
        holder.tv_label_name.setText(value);
        if(isDouble(String.valueOf(item.getChannelDefaultValue())))
        {
            int x = Math.round(Float.parseFloat(item.getChannelDefaultValue()));
            holder.progressBar_water_level.setMax(x);
        }
        else
        {
            holder.progressBar_water_level.setMax(item.getMaxValue());
        }
        if(isDouble(String.valueOf(item.getValue())))
        {
            int x = Math.round(Float.parseFloat(item.getValue()));
            holder.progressBar_water_level.setProgress(x);
            holder.progressBar_water_level.setScaleY(3f);

        }
        else
        {
            holder.progressBar_water_level.setProgress(Integer.parseInt(item.getValue()));
            holder.progressBar_water_level.setScaleY(3f);
        }

        BubbleDrawableWhite myBubble = new BubbleDrawableWhite(BubbleDrawable.CENTER);
        myBubble.setCornerRadius(0);
        myBubble.setPointerAlignment(BubbleDrawableWhite.CENTER);
        myBubble.setPadding(15,15, 20,15);
        //holder.ll_main_bubble.setBackground(myBubble);
        Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        holder.tv_label_name.setTypeface(faceLight);
        return row;
    }


    private boolean isDouble(String str) {
        try {
            // check if it can be parsed as any double
            double x = Double.parseDouble(str);
            // check if the double can be converted without loss to an int
            if (x == (int) x)
                // if yes, this is an int, thus return false
                return false;
            // otherwise, this cannot be converted to an int (e.g. "1.2")
            return true;
            // short version: return x != (int) x;
        }
        catch(NumberFormatException e) {
            return false;
        }

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
        TextView  tv_label_name;
        ProgressBar progressBar_water_level;

    }
    // Keep all Images in array

}
