package com.techuva.iot.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.utils.views.TextViewIcomoon;

public class DashboardChannelRCVHolder extends RecyclerView.ViewHolder {
// View holder for gridview recycler view as we used in listview
public LinearLayout ll_main;
public View view_line_one;
public TextView tv_channel_name_one;
public TextView tv_channel_name_two;
public TextView tv_channel_value;
public TextView tv_channel_update;
public TextView tv_blank;
public TextViewIcomoon tv_channel_icon;

public DashboardChannelRCVHolder(View view) {
        super(view);
        this.ll_main =  view.findViewById(R.id.ll_main);
        this.view_line_one =  view.findViewById(R.id.view_line_one);
        this.tv_channel_name_one =  view.findViewById(R.id.tv_channel_name_one);
        this.tv_channel_name_two =  view.findViewById(R.id.tv_channel_name_two);
        this.tv_channel_value =  view.findViewById(R.id.tv_channel_value);
        this.tv_channel_update =  view.findViewById(R.id.tv_channel_update);
        this.tv_blank =  view.findViewById(R.id.tv_blank);
        this.tv_channel_icon =  view.findViewById(R.id.tv_channel_icon);
     }
}