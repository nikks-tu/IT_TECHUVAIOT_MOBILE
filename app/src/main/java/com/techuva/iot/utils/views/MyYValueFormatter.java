package com.techuva.iot.utils.views;

import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class MyYValueFormatter extends ValueFormatter implements IValueFormatter {
    private DecimalFormat mFormat;
    public MyYValueFormatter() {
        mFormat = new DecimalFormat("00"); // use one decimal
    }
    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value)+"am";
    }
}