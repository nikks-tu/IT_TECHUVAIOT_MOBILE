package com.techuva.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.holders.ConsumedDataDayRCVHolder;
import com.techuva.iot.response_model.ConsumedDataValueObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConsumedDataDayRcvAdapter extends RecyclerView.Adapter<ConsumedDataDayRCVHolder>
{
    private ArrayList<ConsumedDataValueObject> arrayList;
    private Context context;
    private ConsumedDataDayRCVHolder listHolder;
    int selectedPosition = 0;
    public String selectedDate="";

    public ConsumedDataDayRcvAdapter(Context context, ArrayList<ConsumedDataValueObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
       // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(ConsumedDataDayRCVHolder holder, int position) {
        ConsumedDataValueObject model = arrayList.get(position);
        if(selectedPosition==position)
            holder.cv_day_view.setBackgroundColor(context.getResources().getColor(R.color.text_color_light_op));
        else
            holder.cv_day_view.setBackgroundColor(context.getResources().getColor(R.color.white));
        //"DATE": "Mar 10, 2019",
        String dateToParse = model.getDATE();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

        Date date = null;
        String dateToStr="";
        try {
            date = sdf.parse(dateToParse);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
             dateToStr= format.format(date);
            //System.out.println(dateToStr);
            //holder.tv_date_value.setText(dateToStr);
        } catch (android.net.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        //SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");

         holder.tv_date_value.setText(dateToStr);
         holder.tv_consumed_units.setText(model.getSUM()+" "+context.getResources().getString(R.string.units));
         holder.ll_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                selectedDate = model.getDATE();
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public ConsumedDataDayRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_consumed_data_day, viewGroup, false);

        listHolder = new ConsumedDataDayRCVHolder(mainGroup);
        return listHolder;

    }

    public String getSelectedItem() {
        if ( selectedPosition!= -1) {
            // Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition).getCompanyName(), Toast.LENGTH_SHORT).show();
            return String.valueOf(selectedDate);
        }
        else
            return "";
    }
}