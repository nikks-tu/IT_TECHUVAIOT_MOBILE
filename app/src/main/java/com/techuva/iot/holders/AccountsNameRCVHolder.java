package com.techuva.iot.holders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.techuva.iot.R;

import androidx.recyclerview.widget.RecyclerView;

public class AccountsNameRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView companyName;
public TextView nameInitials;
public RadioButton rb_checked;
public int mSelectedItem;
public AccountsNameRCVHolder(View view) {
        super(view);
        // Find all views ids

        this.companyName =  view.findViewById(R.id.tv_userNames);
        this.nameInitials = view.findViewById(R.id.tv_name_initials);
        this.rb_checked = view.findViewById(R.id.rb_accountUsed);
        Typeface faceLight = Typeface.createFromAsset(view.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        this.companyName.setTypeface(faceLight);
        this.nameInitials.setTypeface(faceLight);
        }

        @Override
        public void onClick(View v) {

        mSelectedItem = getAdapterPosition();
        }

        private void setTypeface() {

        }
}