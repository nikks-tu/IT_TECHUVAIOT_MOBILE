package com.techuva.iot.holders;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;

public class ChannelDataRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
// View holder for gridview recycler view as we used in listview
        public LinearLayout ll_main_months;
        public TextView tv_month;





public ChannelDataRCVHolder(View view, Context context) {
        super(view);
        // Find all views ids

        this.tv_month =  view.findViewById(R.id.tv_month);
        this.ll_main_months =  view.findViewById(R.id.ll_main_months);

        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        tv_month.setTypeface(faceLight);
        }

        @Override
        public void onClick(View v) {
                int position = getAdapterPosition();
        }
}