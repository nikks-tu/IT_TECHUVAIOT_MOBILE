package com.techuva.iot.holders;

import android.view.View;
import android.widget.TextView;

import com.techuva.iot.R;

import androidx.recyclerview.widget.RecyclerView;

public class ChannelValuesRCVHolder extends RecyclerView.ViewHolder {
// View holder for gridview recycler view as we used in listview

public TextView tv_channel_data;
public TextView tv_channel_name;


public ChannelValuesRCVHolder(View view) {
        super(view);
        // Find all views ids
        this.tv_channel_data =  view.findViewById(R.id.tv_channel_data);
        this.tv_channel_name =  view.findViewById(R.id.tv_channel_name);
        }
}