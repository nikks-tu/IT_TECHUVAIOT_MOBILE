package com.techuva.iot.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;

public class ConsumedDataDayRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public LinearLayout ll_main;
public CardView cv_day_view;
public TextView tv_date_value;
public TextView tv_consumed_units;




public ConsumedDataDayRCVHolder(View view) {
        super(view);
        this.ll_main =  view.findViewById(R.id.ll_main);
        this.cv_day_view =  view.findViewById(R.id.cv_day_view);
        this.tv_date_value =  view.findViewById(R.id.tv_date_value);
        this.tv_consumed_units =  view.findViewById(R.id.tv_consumed_units);

        }

        @Override
        public void onClick(View v) {
                int position = getAdapterPosition();
        }
}