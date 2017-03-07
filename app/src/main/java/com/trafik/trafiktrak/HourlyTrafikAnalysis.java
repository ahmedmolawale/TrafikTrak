package com.trafik.trafiktrak;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;

import com.github.mikephil.charting.charts.BarChart;

public class HourlyTrafikAnalysis extends AppCompatActivity {

    private TableLayout tableLayout;

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hourly_trafik_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hourly_trafik_analysis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //lets get the serializable object passed
        TableData tableData = (TableData) getIntent().getSerializableExtra(HourlyTrafik.HOUR_INTENT_ID);

        tableLayout = (TableLayout) findViewById(R.id.hourly_table);
        barChart = (BarChart) findViewById(R.id.hourly_bar_chart);
        tableData.constructTable(this, tableLayout);
        tableData.constructBarChart(barChart);


    }


}
