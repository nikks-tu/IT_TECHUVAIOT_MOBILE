package com.techuva.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.holders.ChannelNameRCVHolder;
import com.techuva.iot.model.HistoryDataObject;

import java.util.ArrayList;

public class ChannelNamesRcvAdapter  extends RecyclerView.Adapter<ChannelNameRCVHolder>
{
    private ArrayList<HistoryDataObject> arrayList;
    private Context context;
    private ChannelNameRCVHolder listHolder;

    public ChannelNamesRcvAdapter(Context context, ArrayList<HistoryDataObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
       // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(ChannelNameRCVHolder holder, int position) {
        HistoryDataObject model = arrayList.get(position);

         holder.title.setText(model.getLabel());


       // mainHolder.imageview.setImageBitmap(image);
    }

    @Override
    public ChannelNameRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_hr_channel_names, viewGroup, false);

        listHolder = new ChannelNameRCVHolder(mainGroup);
        return listHolder;

    }


}