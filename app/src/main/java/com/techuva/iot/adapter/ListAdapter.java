package com.techuva.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.techuva.iot.R;
import com.techuva.iot.activity.WaterMonValueDetailsActivity;
import com.techuva.iot.listener.Update;
import com.techuva.iot.model.YearsObject;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<YearsObject> {

    private int resourceLayout;
    private Context mContext;
    ArrayList<YearsObject> items;
    String year;
    Update update;

    public ListAdapter(Context context, int resource, ArrayList<YearsObject> items, String selectedYear, WaterMonValueDetailsActivity activity) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
        this.items = items;
        this.year = selectedYear;
        this.update = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.item_years, null);
        }


        YearsObject p = getItem(position);
        TextView tv_year = (TextView) v.findViewById(R.id.tv_year);
        if (p != null) {

            tv_year.setText(p.getYear());
        }

         if(p.getYear().equals(year))
            {
                tv_year.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                tv_year.setTextColor(getContext().getResources().getColor(R.color.white));
            }
         else {
             {
                 tv_year.setBackgroundColor(getContext().getResources().getColor(R.color.white));
                 tv_year.setTextColor(getContext().getResources().getColor(R.color.dark_gray));
             }
         }

         tv_year.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 year = tv_year.getText().toString();
                 tv_year.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                 tv_year.setTextColor(getContext().getResources().getColor(R.color.white));
                 update.selectedYear(year);
                 notifyDataSetChanged();
             }
         });


        return v;
    }

    @Nullable
    @Override
    public YearsObject getItem(int position) {
        return super.getItem(position);
    }

    public String getSelectedYear(int position)
    {
        return items.get(position).getYear();
    }



}