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

public class DailyTrafikAnalysis extends AppCompatActivity {

    BarChart barChart;
    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_trafik_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_daily_trafik_analysis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //lets get the serializable object passed
        TableData tableData = (TableData) getIntent().getSerializableExtra(DailyTrafik.DAILY_INTENT_ID);
        //getting the day selected
        String daySelected = getIntent().getStringExtra(DailyTrafik.DAY_SELECTED_INTENT_ID);
        barChart = (BarChart) findViewById(R.id.daily_bar_chart);
        lineChart = (LineChart) findViewById(R.id.daily_line_chart);
        TableLayout tableLayout = (TableLayout) findViewById(R.id.daily_table1);
        tableData.constructTable(this, tableLayout);
        tableData.constructBarChart(barChart);
        tableData.constructDailyLineChart(this,daySelected,lineChart);

    }

}
