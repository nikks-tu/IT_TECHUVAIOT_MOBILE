package com.techuva.iot.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.utils.views.TextViewIcomoon;

public class HexagonChannelRCVHolder extends RecyclerView.ViewHolder {
// View holder for gridview recycler view as we used in listview

public TextViewIcomoon tv_channel_icon;
public LinearLayout ll_main;
public LinearLayout ll_icon;
public TextView tv_channel_name;

public HexagonChannelRCVHolder(View view) {
        super(view);
        this.tv_channel_icon =  view.findViewById(R.id.tv_channel_icon);
        this.ll_main =  view.findViewById(R.id.ll_main);
        this.ll_icon =  view.findViewById(R.id.ll_icon);
        this.tv_channel_name =  view.findViewById(R.id.tv_channel_name);
     }
}