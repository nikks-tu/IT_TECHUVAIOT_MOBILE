package com.techuva.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.techuva.iot.R;
import com.techuva.iot.model.CurrentDataValueObject;

import java.util.ArrayList;

public class WaterChannelAdapter extends ArrayAdapter<CurrentDataValueObject> {

    private int resourceLayout;
    private Context mContext;
    ArrayList<CurrentDataValueObject> items;

    public WaterChannelAdapter(Context context, int resource, ArrayList<CurrentDataValueObject> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(R.layout.item_years, null);
        }


        CurrentDataValueObject p = getItem(position);

        if (p != null) {
            TextView tv_year = (TextView) v.findViewById(R.id.tv_year);

            tv_year.setText(p.getLabel());
        }

        return v;
    }

    @Nullable
    @Override
    public CurrentDataValueObject getItem(int position) {
        return super.getItem(position);
    }
    public String getSelectedChannel(int position)
    {
        return String.valueOf(items.get(position).getChannelNumber());
    }
}