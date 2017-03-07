package com.trafik.trafiktrak;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

public class WeeklyTrafikAnalysis extends AppCompatActivity {

    private TableLayout tableLayout;
    private BarChart barChart;
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_trafik_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_weekly_analysis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting the UI components
        tableLayout = (TableLayout) findViewById(R.id.weekly_table1);
        barChart = (BarChart) findViewById(R.id.weekly_bar_chart);
        lineChart = (LineChart) findViewById(R.id.weekly_line_chart);

        TableData tableData = (TableData) getIntent().getSerializableExtra(WeeklyTrafik.WEEKLY_INTENT_ID);
        ArrayList<String> datesOfTheWeek = getIntent().getStringArrayListExtra(WeeklyTrafik.DATES_OF_THE_WEEK);
        tableData.constructTable(this, tableLayout);
        tableData.constructBarChart(barChart);
        tableData.constructWeeklyLineChart(this,datesOfTheWeek,lineChart);


    }

}
