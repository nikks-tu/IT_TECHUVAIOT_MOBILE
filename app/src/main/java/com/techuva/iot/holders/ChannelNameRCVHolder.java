package com.techuva.iot.holders;

import android.view.View;
import android.widget.TextView;

import com.techuva.iot.R;

import androidx.recyclerview.widget.RecyclerView;

public class ChannelNameRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
// View holder for gridview recycler view as we used in listview
public TextView title;
public View view;




public ChannelNameRCVHolder(View view) {
        super(view);
        // Find all views ids

        this.title =  view.findViewById(R.id.tv_history_channel_name);
        }

        @Override
        public void onClick(View v) {
                int position = getAdapterPosition();
        }
}