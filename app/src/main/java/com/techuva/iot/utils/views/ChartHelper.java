package com.techuva.iot.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.techuva.iot.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikita on 03-04-2019.
 * Modified by Nikita on 03-04-2019.
 * Reviewed by Nikita on 03-04-2019.
 */

public class ChartHelper {

       public static void generateLineChart(Context context, LineChart chart, List<String> xValues, List<Float> yValues,Description desc,  String heading) {
        if (chart == null)
            chart = new LineChart(context);
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        ArrayList<Entry> lineEntries = new ArrayList<>();
        for (int i = 0; i < yValues.size(); i++) {
            lineEntries.add(new Entry(yValues.get(i), i));
        }
       /* LineDataSet lineDataSet = new LineDataSet(lineEntries, "");
        lineDataSet.setColors(colors);
        lineDataSet.enableDashedLine(10f, 5f, 0f);
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(9f);
        lineDataSet.setFillAlpha(65);
        lineDataSet.setFillColor(Color.BLACK);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        chart.setData(data);*/
        chart.setExtraOffsets(0, 0, 0, 8);
        chart.setDescription(desc);
        chart.setDrawGridBackground(false);
        chart.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        /*chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTextSize(12f);
        chart.getLegend().setEnabled(false);
        chart.setY(5);*/
        //chart.setDescription(heading);
        //chart.getAxisRight().setDrawZeroLine(true);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.setContentDescription(heading);
        chart.getCenter();


           YAxis leftAxis = chart.getAxisLeft();
           leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
           leftAxis.setTextColor(ColorTemplate.getHoloBlue());
           leftAxis.setDrawGridLines(false);
           leftAxis.setGranularityEnabled(false);
           leftAxis.setAxisMinimum(0f);
           leftAxis.setAxisMaximum(170f);
           leftAxis.setYOffset(-9f);
           leftAxis.setTextColor(Color.rgb(255, 192, 56));

           YAxis rightAxis = chart.getAxisRight();
           rightAxis.setEnabled(false);
           rightAxis.disableGridDashedLine();
           rightAxis.setDrawGridLines(false);
        //chart.setDescriptionTextSize(26f);
        //chart.setDescriptionColor(ContextCompat.getColor(context, R.color.colorAccent_light));
        chart.setContentDescription(heading);
        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        //chart.setDescriptionPosition(chart.getLayoutParams().width - chart.getViewPortHandler().offsetLeft() - 10.0F, actionBarSize / 2 - 10.0F);
    }
}

