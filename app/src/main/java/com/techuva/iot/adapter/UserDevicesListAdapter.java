package com.techuva.iot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.techuva.iot.R;
import com.techuva.iot.model.DevicesInventoryListObject;
import com.techuva.iot.model.DevicesListResultObject;

import java.util.ArrayList;
import java.util.List;

public class UserDevicesListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<DevicesListResultObject> deptList;
    private CompoundButton lastCheckedRB = null;
    private int selectedPosition = -1;
    DevicesInventoryListObject detailInfo = new DevicesInventoryListObject();
    ArrayList<DevicesInventoryListObject> inventoryArray = new ArrayList<>();

    public UserDevicesListAdapter(Context context, ArrayList<DevicesListResultObject> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<DevicesInventoryListObject> devicesList = deptList.get(groupPosition).getInventoryList();
        return devicesList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        detailInfo = (DevicesInventoryListObject) getChild(groupPosition, childPosition);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.item_devices_list, null);
        }

        TextView tv_deviceName = (TextView) view.findViewById(R.id.tv_deviceName);
        TextView tv_deviceId = (TextView) view.findViewById(R.id.tv_deviceId);
        RadioButton rb_selectedDevice = view.findViewById(R.id.rb_deviceUsed);
        tv_deviceName.setText(detailInfo.getDisplayName());
        tv_deviceId.setText(detailInfo.getInventoryName());
        rb_selectedDevice.setOnCheckedChangeListener(ls);
        rb_selectedDevice.setTag(childPosition);
        return view;

    }

    @Override
    public int getChildrenCount(int groupPosition) {

        List<DevicesInventoryListObject> productList = deptList.get(groupPosition).getInventoryList();
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

        DevicesListResultObject headerInfo = (DevicesListResultObject) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_header_device_list, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.tv_device_headers);
        heading.setText(headerInfo.getProductName());

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




   /* public String getSelectedItem() {
        if ( selectedPosition!= -1) {
            // Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition).getCompanyName(), Toast.LENGTH_SHORT).show();
            for (int i= 0; i<deptList.size(); i++)
            {
                inventoryArray.addAll(deptList.get(i).getInventoryList());
            }
          //  return String.valueOf(selectedPosition);

            return inventoryArray.get(selectedPosition).getDisplayName();
        }
        else {
            return "";
        }

    }
*/

}