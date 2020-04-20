package com.techuva.iot.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.holders.HexagonChannelRCVHolder;
import com.techuva.iot.model.HistoryDataObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class HexagonChannelRcvAdapter extends RecyclerView.Adapter<HexagonChannelRCVHolder>
{// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<HistoryDataObject> arrayList;
    private Context context;
    private HexagonChannelRCVHolder listHolder;
    private static final Pattern SPACE = Pattern.compile(" ");
    private int selectedPosition = 0;

    public HexagonChannelRcvAdapter(Context context, ArrayList<HistoryDataObject> arrayList) {
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
        return Integer.parseInt(arrayList.get(position).getChannelNumber());
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(HexagonChannelRCVHolder holder, int position) {
        HistoryDataObject model = arrayList.get(position);

        if(position == selectedPosition){
            holder.itemView.setSelected(true);
        } else {
            holder.itemView.setSelected(false);
        }

        holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
        if(selectedPosition==position){
            holder.ll_icon.setBackground(context.getResources().getDrawable(R.drawable.hexagon_selected));
        }
        else
        {
            holder.ll_icon.setBackground(context.getResources().getDrawable(R.drawable.hexagon));
        }

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
        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        String s = model.getLabel();
        s = s.replaceAll("_", " ");
        String[] arr = SPACE.split(s);
        holder.tv_channel_name.setText(s);


       /* holder.tv_channel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*int selectedGridChannelNum = Integer.parseInt(model.getChannelNumber());
                Intent intent = new Intent(context, History_View_Activity.class);
                intent.putExtra("TabId", selectedGridChannelNum);
                context.startActivity(intent);
                //listener.onItemClick(v, position);*//*
                selectedPosition = Integer.parseInt(model.getChannelNumber());
               // Toast.makeText(context, ""+selectedPosition, Toast.LENGTH_SHORT).show();

                MApplication.setString(context, Constants.ChannelNumGraph, model.getChannelNumber());
                //selectedPosition = selectedGridChannelNum;

            }
        });*/

    }

    @Override
    public HexagonChannelRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_hexagon_channel, viewGroup, false);

        listHolder = new HexagonChannelRCVHolder(mainGroup);

        return listHolder;

    }



    public String getSelectedItem() {
        if ( selectedPosition!= -1) {
            // Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition).getCompanyName(), Toast.LENGTH_SHORT).show();
            return String.valueOf(selectedPosition);
        }
        else
        return "";
    }
}