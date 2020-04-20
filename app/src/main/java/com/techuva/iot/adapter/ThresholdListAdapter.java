package com.techuva.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.techuva.iot.R;
import com.techuva.iot.response_model.ThresholdResultObject;
import com.techuva.iot.response_model.ThresholdValuesObject;

import java.util.ArrayList;
import java.util.List;

public class ThresholdListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<ThresholdResultObject> deptList;
    private CompoundButton lastCheckedRB = null;
    private int selectedPosition = -1;
    ThresholdValuesObject detailInfo = new ThresholdValuesObject();
    ArrayList<ThresholdValuesObject> inventoryArray = new ArrayList<>();
    Boolean zero = false;

    public ThresholdListAdapter(Context context, ArrayList<ThresholdResultObject> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ThresholdValuesObject> devicesList = deptList.get(groupPosition).getThreshold();
        return devicesList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        detailInfo = (ThresholdValuesObject) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.item_threshold_list, null);
        }

        TextView tv_deviceName =  view.findViewById(R.id.tv_deviceName);
        TextView tv_deviceId =  view.findViewById(R.id.tv_deviceId);
        RadioButton rb_selectedDevice = view.findViewById(R.id.rb_deviceUsed);
        ImageView v_device_icon = view.findViewById(R.id.v_device_icon);
        LinearLayout ll_headings = view.findViewById(R.id.ll_headings);
        LinearLayout ll_main = view.findViewById(R.id.ll_main);
        tv_deviceName.setText(detailInfo.getTitle());
        tv_deviceId.setText(detailInfo.getValue());
        rb_selectedDevice.setOnCheckedChangeListener(ls);
        rb_selectedDevice.setTag(childPosition);

        if(detailInfo.getTitle().equals("Threshold Name")){

            //Toast.makeText(context, ""+childPosition, Toast.LENGTH_SHORT).show();
            v_device_icon.setVisibility(View.GONE);
            ll_main.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            tv_deviceName.setTextColor(context.getResources().getColor(R.color.white));
            tv_deviceId.setTextColor(context.getResources().getColor(R.color.white));
        }
        else{
           v_device_icon.setVisibility(View.VISIBLE);
            ll_main.setBackgroundColor(context.getResources().getColor(R.color.white));
            tv_deviceName.setTextColor(context.getResources().getColor(R.color.text_color_dark));
            tv_deviceId.setTextColor(context.getResources().getColor(R.color.text_color_dark));
            //ll_headings.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return view;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ThresholdValuesObject> productList = deptList.get(groupPosition).getThreshold();
        return productList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        ThresholdResultObject headerInfo = (ThresholdResultObject) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_threshold_device_list, null);
        }

        TextView heading =  view.findViewById(R.id.tv_device_headers);
        heading.setText(headerInfo.getChannelLabel());

        ImageView iv_expand_list = view.findViewById(R.id.iv_expand_list);
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private CompoundButton.OnCheckedChangeListener ls = ((buttonView, isChecked) -> {
        int tag = (int) buttonView.getTag();
        if (lastCheckedRB == null) {
        } else if (tag != (int) lastCheckedRB.getTag()) {
            lastCheckedRB.setChecked(false);
        }
        lastCheckedRB = buttonView;
        itemCheckChanged(buttonView);
    });

    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
         notifyDataSetChanged();
    }


}