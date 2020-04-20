package com.techuva.iot.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.techuva.iot.R;
import com.techuva.iot.model.NavDrawerItem;

import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private LayoutInflater inflator;
    ViewHolder holder;
    NavDrawerItem model;

    //FontType fontType;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;

        inflator= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder=new ViewHolder();
            Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                    "fonts/AvenirLTStd-Light.otf");
            convertView = inflator.inflate(R.layout.row_drawer_list_item,parent,false);

            holder.navdrawerlayout= (LinearLayout) convertView.findViewById(R.id.navdrawerlayout);

            holder.nav_imgicon= (ImageView) convertView.findViewById(R.id.drawericon);

            holder.nav_tvtitle= (TextView) convertView.findViewById(R.id.drawertitle);
            holder.nav_tvtitle.setTypeface(faceLight);

            holder.nav_tvcount= (TextView) convertView.findViewById(R.id.counter);
           /* holder.nav_tvtitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
*/
            convertView.setTag(holder);

        }
        else
        {
            holder= (ViewHolder) convertView.getTag();
        }

        model= (NavDrawerItem) getItem(position);

        holder.nav_imgicon.setImageResource(model.icon);

        holder.nav_tvtitle.setText(model.title);


        return convertView;
    }

    private class ViewHolder
    {
        ImageView nav_imgicon;
        TextView nav_tvtitle,nav_tvcount;
        LinearLayout navdrawerlayout;
    }

}
