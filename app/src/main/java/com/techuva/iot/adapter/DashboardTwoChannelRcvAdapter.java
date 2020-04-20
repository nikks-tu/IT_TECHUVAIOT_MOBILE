package com.techuva.iot.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.activity.History_View_Activity;
import com.techuva.iot.holders.DashboardChannelRCVHolder;
import com.techuva.iot.model.CurrentDataValueObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class DashboardTwoChannelRcvAdapter extends RecyclerView.Adapter<DashboardChannelRCVHolder>
{// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<CurrentDataValueObject> arrayList;
    private Context context;
    private DashboardChannelRCVHolder listHolder;
    private static final Pattern SPACE = Pattern.compile(" ");

    public DashboardTwoChannelRcvAdapter(Context context, ArrayList<CurrentDataValueObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
       // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();

    }

    public int getChannelNumber(int position)
    {
        return arrayList.get(position).getChannelNumber();
    }


    @Override
    public void onBindViewHolder(DashboardChannelRCVHolder holder, int position) {
        CurrentDataValueObject model = arrayList.get(position);

        String s2 = model.getIcon();

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
        if (position%2 == 0) {
            holder.view_line_one.setVisibility(View.GONE);
            holder.tv_blank.setVisibility(View.VISIBLE);
        } else {
            holder.view_line_one.setVisibility(View.VISIBLE);
            holder.tv_blank.setVisibility(View.GONE);
        }
        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        String s = model.getLabel();
        s = s.replaceAll("_", " ");
        String[] arr = SPACE.split(s);
       if (arr.length>1)
       {
           holder.tv_channel_name_one.setText(arr[0]);
           holder.tv_channel_name_two.setText(arr[1]);
       }
       else  holder.tv_channel_name_one.setText(arr[0]);
        holder.tv_channel_update.setVisibility(View.GONE);
        if(!model.getUnit_of_measure().equals(""))
        {
            if(model.getUnit_of_measure().equals("o"))
            {
                StringBuilder sb = new StringBuilder();
                sb.append(model.getValue());
                sb.append("");
                sb.append("\u00B0");
                holder.tv_channel_value.setText(sb.toString());
            }
            else if(model.getUnit_of_measure().equals("oC"))
            {
                StringBuilder sb = new StringBuilder();
                sb.append(model.getValue());
                sb.append("");
                sb.append("\u00B0");
                sb.append("C");
                holder.tv_channel_value.setText(sb.toString());
            }
            else if(model.getUnit_of_measure().equals("oF"))
            {
                StringBuilder sb = new StringBuilder();
                sb.append(model.getValue());
                sb.append("");
                sb.append("\u00B0");
                sb.append("F");
                holder.tv_channel_value.setText(sb.toString());
            }
            else {
                StringBuilder sb = new StringBuilder();
                sb.append(model.getValue());
                sb.append("");
                sb.append(model.getUnit_of_measure());
                holder.tv_channel_value.setText(sb.toString());
            }
        }
        else {
            holder.tv_channel_value.setText(model.getValue());
        }

        holder.tv_channel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGridChannelNum = model.getChannelNumber();
                Intent intent = new Intent(context, History_View_Activity.class);
                intent.putExtra("TabId", selectedGridChannelNum);
                context.startActivity(intent);
            }
        });
        holder.tv_channel_name_one.setTypeface(faceLight);
        holder.tv_channel_name_two.setTypeface(faceLight);
        holder.tv_channel_update.setTypeface(faceLight);
        holder.tv_channel_value.setTypeface(faceLight);

    }

    @Override
    public DashboardChannelRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_dashboard_two_channel_value, viewGroup, false);

        listHolder = new DashboardChannelRCVHolder(mainGroup);
        return listHolder;

    }


}