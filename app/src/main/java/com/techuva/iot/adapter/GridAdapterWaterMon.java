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
import com.techuva.iot.response_model.WaterMonValueWithDateObject;
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

public class GridAdapterWaterMon extends ArrayAdapter<WaterMonValueWithDateObject> {
    private final int resource;
    private Context mContext;
    ArrayList<WaterMonValueWithDateObject> list;

    public GridAdapterWaterMon(@NonNull Context context, int resource, @NonNull ArrayList<WaterMonValueWithDateObject> objects) {
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
    public WaterMonValueWithDateObject getItem(int position) {
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
            holder.ll_date = row.findViewById(R.id.ll_date);
            holder.ll_manjeera = row.findViewById(R.id.ll_manjeera);
            holder.ll_ground = row.findViewById(R.id.ll_ground);
            holder.ll_other = row.findViewById(R.id.ll_other);
            holder.ll_main_bubble = row.findViewById(R.id.ll_main_bubble);
            holder.tv_date = row.findViewById(R.id.tv_date);
            holder.tv_month = row.findViewById(R.id.tv_month);
            holder.tv_manjeera_value = row.findViewById(R.id.tv_manjeera_value);
            holder.tv_manjeera_txt = row.findViewById(R.id.tv_manjeera_txt);
            holder.tv_ground_value = row.findViewById(R.id.tv_ground_value);
            holder.tv_ground_txt = row.findViewById(R.id.tv_ground_txt);
            holder.tv_other_value = row.findViewById(R.id.tv_other_value);
            holder.tv_other_txt = row.findViewById(R.id.tv_other_txt);
            holder.view_divider_one = row.findViewById(R.id.view_divider_one);
            holder.view_divider_two = row.findViewById(R.id.view_divider_two);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        WaterMonValueWithDateObject item = list.get(position);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        SimpleDateFormat sdf_month = new SimpleDateFormat("MMM");
        String inputText = item.getDate();
        Date date = null;
        try {
            date = inputFormat.parse(inputText);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputText = sdf.format(date);
        String month  = sdf_month.format(date);
        holder.tv_date.setText(outputText);
        holder.tv_month.setText(capitalize(month));
        if(item.getValues().size()>0)
        {
            holder.ll_manjeera.setVisibility(View.VISIBLE);
            if(item.getValues().get(0).getLabel()!=null && !item.getValues().get(0).getLabel().isEmpty())
            {
                if(!String.valueOf(item.getValues().get(0).getValue()).equals("faulty"))
                {
                    Double value = item.getValues().get(0).getValue();
                    holder.tv_manjeera_txt.setText(item.getValues().get(0).getLabel());
                    holder.tv_manjeera_value.setText(String.format("%.2f", value) +" "+item.getValues().get(0).getUnitOfMeasure());
                }
                else {
                    holder.tv_manjeera_txt.setText(item.getValues().get(0).getLabel());
                    holder.tv_manjeera_value.setText(String.valueOf(item.getValues().get(0).getValue()));
                }
            }
        }
        if(item.getValues().size()>1)
        {
            holder.ll_ground.setVisibility(View.VISIBLE);



            if(item.getValues().get(1).getLabel()!=null && !item.getValues().get(1).getLabel().isEmpty())
            {
                holder.tv_ground_txt.setText(item.getValues().get(1).getLabel());
                if(!String.valueOf(item.getValues().get(1).getValue()).equals("faulty"))
                {
                    Double value = item.getValues().get(1).getValue();
                    holder.tv_ground_value.setText(String.format("%.2f", value) + " "+item.getValues().get(1).getUnitOfMeasure());
                }
                else {
                    holder.tv_ground_value.setText(String.valueOf(item.getValues().get(1).getValue()));
                }
 }
        }
        if(item.getValues().size()>2)
        {
            holder.ll_other.setVisibility(View.VISIBLE);
            if(item.getValues().get(2).getLabel()!=null && !item.getValues().get(2).getLabel().isEmpty())
            {
                holder.tv_other_txt.setText(item.getValues().get(2).getLabel());
                if(!String.valueOf(item.getValues().get(2).getValue()).equals("faulty"))
                {
                    Double value = item.getValues().get(2).getValue();
                    holder.tv_other_value.setText(String.format("%.2f", value) + " "+item.getValues().get(2).getUnitOfMeasure());
                }
                else {
                    holder.tv_other_value.setText(String.valueOf(item.getValues().get(2).getValue()));
                }
            }
        }

        if(item.getValues().size()>1)
        {
            holder.view_divider_one.setVisibility(View.VISIBLE);
        }
        if(item.getValues().size()>2)
        {
            holder.view_divider_two.setVisibility(View.VISIBLE);
        }
        BubbleDrawableWhite myBubble = new BubbleDrawableWhite(BubbleDrawable.CENTER);
        myBubble.setCornerRadius(0);
        myBubble.setPointerAlignment(BubbleDrawableWhite.CENTER);
        myBubble.setPadding(15,15, 20,15);
        //holder.ll_main_bubble.setBackground(myBubble);
        Typeface faceLight = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        holder.tv_date.setTypeface(faceLight);
        holder.tv_month.setTypeface(faceLight);
        holder.tv_manjeera_value.setTypeface(faceLight);
        holder.tv_manjeera_txt.setTypeface(faceLight);
        holder.tv_ground_value.setTypeface(faceLight);
        holder.tv_ground_txt.setTypeface(faceLight);
        holder.tv_other_value.setTypeface(faceLight);
        holder.tv_other_txt.setTypeface(faceLight);
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
        LinearLayout ll_date, ll_manjeera, ll_ground, ll_main_bubble, ll_other;
        TextView tv_date, tv_month, tv_manjeera_value, tv_manjeera_txt, tv_ground_value, tv_ground_txt, tv_other_value, tv_other_txt;
        View view_divider_one, view_divider_two;

    }
    // Keep all Images in array

}
