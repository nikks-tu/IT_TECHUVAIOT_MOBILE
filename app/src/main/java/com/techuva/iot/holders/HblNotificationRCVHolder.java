package com.techuva.iot.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;

public class HblNotificationRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public LinearLayout ll_main;
public TextView tv_channel_id;
public TextView tv_channel_label;
public TextView tv_notification_time;




public HblNotificationRCVHolder(View view) {
        super(view);
        this.ll_main =  view.findViewById(R.id.ll_main);
        this.tv_channel_id =  view.findViewById(R.id.tv_channel_id);
        this.tv_channel_label =  view.findViewById(R.id.tv_channel_label);
        this.tv_notification_time =  view.findViewById(R.id.tv_notification_time);

        }

        @Override
        public void onClick(View v) {
                int position = getAdapterPosition();
        }
}