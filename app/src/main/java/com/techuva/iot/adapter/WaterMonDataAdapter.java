package com.techuva.iot.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techuva.iot.R;
import com.techuva.iot.response_model.WaterMonDataResultObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WaterMonDataAdapter extends BaseAdapter {
    private final int resource;
    private Context mContext;
    List<WaterMonDataResultObject> list;
    String flag;
    Boolean isTwoChannel;

    public WaterMonDataAdapter(int resource, Context mContext, String flag, List<WaterMonDataResultObject> list, Boolean isTwoChannel) {
        this.resource = resource;
        this.mContext = mContext;
        this.flag = flag;
        this.list = list;
        this.isTwoChannel = isTwoChannel;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        ViewHolder holder = null;

        // Inflater for custom layout
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.item_water_mon_data, parent, false);

            holder = new ViewHolder();

            holder.tv_receive_date =  view.findViewById(R.id.tv_receive_date);
            holder.tv_manjeera =  view.findViewById(R.id.tv_manjeera);
            holder.tv_ground =  view.findViewById(R.id.tv_ground);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Typeface faceLight = Typeface.createFromAsset(mContext.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        holder.tv_receive_date.setTypeface(faceLight);
        holder.tv_manjeera.setTypeface(faceLight);
        holder.tv_ground.setTypeface(faceLight);
        final WaterMonDataResultObject model = list.get(position);
        String dateToParse = model.getTime();
        //Aug 27, 2019 3:48:56
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

        Date date = null;
        String dateToStr= null;
        try {
            date = sdf.parse(dateToParse);
            SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
            dateToStr = format.format(date);
            //System.out.println(dateToStr);
            //holder.tv_date_value.setText(dateToStr);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        String sa = timeFormatter.format(date);
        SimpleDateFormat onlyHour= new SimpleDateFormat("HH");
        String hour = onlyHour.format(date);

        if(flag.equals("0"))
        {
            holder.tv_receive_date.setText(sa);
        }
        else {
            int hourOfDay = Integer.parseInt(hour)+1;
            if(String.valueOf(hourOfDay).length()<2)
            {
                String dayHour = "0"+hourOfDay;
                holder.tv_receive_date.setText(dayHour);
            }
            else
            holder.tv_receive_date.setText(String.valueOf(Integer.parseInt(hour)+1));
        }


        if(isTwoChannel)
        {
            holder.tv_ground.setVisibility(View.VISIBLE);
        }
        else {
            holder.tv_ground.setVisibility(View.GONE);
        }

        holder.tv_manjeera.setText(model.getData1());
        holder.tv_ground.setText(model.getData2());

        return view;
    }

    // View Holder
    private class ViewHolder {
        public TextView tv_receive_date, tv_manjeera, tv_ground;
    }


   /* @Override
    public int getViewTypeCount() {
        if(list.size()==0){
            return 1;
        }
        return list.size();
    }*/




}
