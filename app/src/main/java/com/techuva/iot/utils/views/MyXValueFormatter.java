package com.techuva.iot.utils.views;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.List;

public class MyXValueFormatter extends ValueFormatter implements IValueFormatter {
    private List<Float> mValues;

    public MyXValueFormatter(List<Float> values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)

        return String.valueOf(mValues.get((int) value));
    }

}