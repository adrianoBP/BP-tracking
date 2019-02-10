package pressure.adriano.com.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathDashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import pressure.adriano.com.APIHelper.Pressure;
import pressure.adriano.com.Classes.PressureEntry;
import pressure.adriano.com.R;

import static pressure.adriano.com.Helpers.Util.CreateBasicSnack;

public class Graph extends AppCompatActivity {

    // Global values
    public static int minSystoleValue = 70;
    public static int maxSystoleValue = 190;
    public static int minDiastoleValue = 40;
    public static int maxDiastoleValue = 100;

    // Class elements
    Context context;
    View view;

    // View elements

    public Graph(final Context context, View view){

        this.context = context;
        this.view = view;

        Pressure.GetPressureData(context, view);
    }

    public static void AddGraphData(final List<PressureEntry> pressureEntries, final Context context, View view){

        @SuppressLint("SimpleDateFormat") final DateFormat viewDatetimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        LineChartView lineChart = view.findViewById(R.id.chart);
        List<PointValue> systolicData = new ArrayList<>();
        List<PointValue> diastolicData = new ArrayList<>();
        List<PointValue> systolicReferenceData = new ArrayList<>();
        List<PointValue> diastolicReferenceData = new ArrayList<>();
        List<AxisValue> axisXs = new ArrayList<>();
        List<AxisValue> axisYs = new ArrayList<>();

        int counter = 0;
        for (PressureEntry pe:
            pressureEntries) {
            systolicData.add(new PointValue(counter, pe.getSystole()));
            diastolicData.add(new PointValue(counter, pe.getDiastole()));
            systolicReferenceData.add(new PointValue(counter, 120));
            diastolicReferenceData.add(new PointValue(counter, 80));
            axisXs.add(new AxisValue(counter).setLabel(""));
            counter++;
        }

        for(int i = Graph.minDiastoleValue; i <= Graph.maxSystoleValue; i += 5){
            axisYs.add(new AxisValue(i));
        }

        Line systolic = new Line(systolicData).setColor(context.getResources().getColor(R.color.systolicColor)).setCubic(true);
        Line diastolic = new Line(diastolicData).setColor(context.getResources().getColor(R.color.diastolicColor)).setCubic(true);
        Line systolicReference = new Line(systolicReferenceData).setColor(context.getResources().getColor(R.color.commonGreen)).setCubic(true);
        Line diastolicReference = new Line(diastolicReferenceData).setColor(context.getResources().getColor(R.color.commonOrange)).setCubic(true);

        systolicReference.setHasPoints(false);
        systolicReference.setPathEffect(new DashPathEffect(new float[]{10, 20},00f));
        systolicReference.setStrokeWidth(2);
        diastolicReference.setHasPoints(false);
        diastolicReference.setPathEffect(new DashPathEffect(new float[]{10, 20},0f));
        diastolicReference.setStrokeWidth(2);


        List<Line> lines = new ArrayList<>();
        lines.add(systolicReference);
        lines.add(diastolicReference);
        lines.add(systolic);
        lines.add(diastolic);

        LineChartData data = new LineChartData();
        data.setLines(lines);
        data.setAxisXBottom(new Axis(axisXs).setHasLines(true));
        data.setAxisYLeft(new Axis(axisYs).setHasLines(true));

        lineChart.setLineChartData(data);

        Viewport viewportMax = new Viewport(lineChart.getMaximumViewport());
        viewportMax.bottom = Graph.minDiastoleValue  - 5;
        viewportMax.top = Graph.maxSystoleValue + 5;
        lineChart.setMaximumViewport(viewportMax);

        Viewport viewport = new Viewport(lineChart.getMaximumViewport());
        viewport.left = 0;
        viewport.right = 5;
        viewport.bottom = viewportMax.bottom;
        viewport.top = viewportMax.top;
        lineChart.setCurrentViewport(viewport);
        lineChart.setVerticalScrollBarEnabled(false);
        lineChart.setHorizontalScrollBarEnabled(true);
        lineChart.setZoomEnabled(false);
        lineChart.setCurrentViewportWithAnimation(viewport);

        lineChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                if(i == 0){
                    CreateBasicSnack("Systolic - " + String.valueOf(pressureEntries.get(i1).getSystole()) + " at " + viewDatetimeFormat.format(pressureEntries.get(i1).getCreateTime()) + " at " + pressureEntries.get(i1).getBpm() + " BPM", 5000, context);
                }else{
                    CreateBasicSnack("Diastolic - " + String.valueOf(pressureEntries.get(i1).getDiastole()) + " at " + viewDatetimeFormat.format(pressureEntries.get(i1).getCreateTime()) + " at " + pressureEntries.get(i1).getBpm() + " BPM", 5000, context);
                }
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }
}