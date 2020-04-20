package com.techuva.iot.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

class OnSpinnerItemClicked implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent,
                               View view, int pos, long id) {
        Toast.makeText(parent.getContext(), "Clicked : " +
                parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView parent) {
        // Do nothing.
    }
}