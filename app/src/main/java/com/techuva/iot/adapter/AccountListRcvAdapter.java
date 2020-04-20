package com.techuva.iot.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.iot.R;
import com.techuva.iot.activity.UserDevicesListActivity;
import com.techuva.iot.app.Constants;
import com.techuva.iot.holders.AccountsNameRCVHolder;
import com.techuva.iot.model.AccountListResultObject;
import com.techuva.iot.utils.views.MApplication;

import java.util.ArrayList;

public class AccountListRcvAdapter extends RecyclerView.Adapter<AccountsNameRCVHolder>
{
    private ArrayList<AccountListResultObject> arrayList;
    private Context context;
    private AccountsNameRCVHolder listHolder;
    private CompoundButton lastCheckedRB = null;
    private String UserName="";
    private int selectedPosition = -1;
    private OnItemClicked listener;

    public AccountListRcvAdapter(Context context, ArrayList<AccountListResultObject> arrayList, OnItemClicked listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
       // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(AccountsNameRCVHolder holder, int position) {
        AccountListResultObject model = arrayList.get(position);
        UserName = MApplication.getString(context, "UserName");
        if(model.getCompanyId()==0)
        {
            holder.companyName.setText(UserName+" (Personal)");
            holder.nameInitials.setText(firstTwo(UserName));
        }
        else
        {
            holder.companyName.setText(model.getCompanyName());
            holder.nameInitials.setText(firstTwo(model.getCompanyName()));
        }

        holder.companyName.setOnClickListener(v -> {
            //MApplication.setString(context, Constants.UserID, String.valueOf(model.getUserId()));
            int UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
            holder.rb_checked.setChecked(true);
            holder.rb_checked.setTag(position);
            holder.rb_checked.setChecked(position == selectedPosition);
        });
         holder.rb_checked.setOnCheckedChangeListener(ls);
         holder.rb_checked.setTag(position);
         holder.rb_checked.setChecked(position == selectedPosition);

    }

    private CompoundButton.OnCheckedChangeListener ls = ((buttonView, isChecked) -> {
        int tag = (int) buttonView.getTag();
        if (lastCheckedRB == null) {
            lastCheckedRB = buttonView;
            itemCheckChanged(buttonView);

        } else if (tag != (int) lastCheckedRB.getTag()) {
            lastCheckedRB.setChecked(false);
            lastCheckedRB = buttonView;
            itemCheckChanged(buttonView);
        }

    });


    public String firstTwo(String str) {
        return str.length() < 2 ? str : str.substring(0, 2);
    }

    @Override
    public AccountsNameRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_account_list, viewGroup, false);
        listHolder = new AccountsNameRCVHolder(mainGroup);
        return listHolder;

    }


    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        //Toast.makeText(context, "Changed" +selectedPosition, Toast.LENGTH_SHORT).show();

        if(!getSelectedItem().equals(""))
        {
            int i =  arrayList.get(Integer.parseInt(getSelectedItem())).getCompanyId();
            String userId =  arrayList.get(Integer.parseInt(getSelectedItem())).getUserId();
            MApplication.setString(context, Constants.CompanyID, String.valueOf(i));
            MApplication.setBoolean(context, Constants.IsDefaultDeviceSaved, false);
           // Toast.makeText(context, "Changed" + userId, Toast.LENGTH_SHORT).show();
            if(arrayList.size()==1)
            {
                MApplication.setString(context, Constants.UserID, String.valueOf(userId));
            }
            else {
                MApplication.setString(context, Constants.UserIDSelected, String.valueOf(userId));
            }
            Intent intent = new Intent(context, UserDevicesListActivity.class);
            context.startActivity(intent);
        }
        else
            Toast.makeText(context, "Please select account to proceed", Toast.LENGTH_SHORT).show();
        /*
        Intent intent = new Intent(context, Dashboard.class);
        context.startActivity(intent);*/
        notifyDataSetChanged();
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public String getSelectedItem() {
        if ( selectedPosition!= -1) {
           // Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition).getCompanyName(), Toast.LENGTH_SHORT).show();
            return String.valueOf(selectedPosition);
        }
        return "";
    }

}