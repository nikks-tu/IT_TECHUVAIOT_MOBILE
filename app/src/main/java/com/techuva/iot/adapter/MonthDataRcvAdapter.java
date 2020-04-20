package com.techuva.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.holders.ChannelDataRCVHolder;
import com.techuva.iot.model.MonthsObject;

import java.util.ArrayList;

public class MonthDataRcvAdapter extends RecyclerView.Adapter<ChannelDataRCVHolder>
{// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<MonthsObject> list;
    private Context context;
    private ChannelDataRCVHolder listHolder;
    int selectedPosition=0;
    int defaultMonth=0;

    public MonthDataRcvAdapter(Context context, ArrayList<MonthsObject> list, int position) {
        this.context = context;
        this.list = list;
        this.defaultMonth = position;
    }

    @Override
    public int getItemCount() {
       // return (null != arrayList ? arrayList.size() : 0);
        return list.size();

    }

    @Override
    public void onBindViewHolder(ChannelDataRCVHolder holder, int position) {
        MonthsObject model = list.get(position);
        holder.tv_month.setText(model.getMonthName());

        if(selectedPosition==position){
            holder.ll_main_months.setBackgroundColor(context.getResources().getColor(R.color.text_color_dark));
            holder.tv_month.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else {
            holder.ll_main_months.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.tv_month.setTextColor(context.getResources().getColor(R.color.text_color_dark));
        }

    }

    @Override
    public ChannelDataRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_months, viewGroup, false);

        listHolder = new ChannelDataRCVHolder(mainGroup, context);
        return listHolder;

    }

   public void setDefaultSelection(int position)
    {
        selectedPosition = position;
    }




}