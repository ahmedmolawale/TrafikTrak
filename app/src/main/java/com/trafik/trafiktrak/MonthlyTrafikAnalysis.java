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

public class MonthlyTrafikAnalysis extends AppCompatActivity {

    private TableLayout tableLayout;
    private BarChart barChart;
    private LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_trafik_analysis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_monthly_analysis);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tableLayout = (TableLayout) findViewById(R.id.monthly_table1);
        barChart = (BarChart) findViewById(R.id.monthly_bar_chart);
        lineChart  = (LineChart) findViewById(R.id.monthly_line_chart);

        TableData tableData = (TableData) getIntent().getSerializableExtra(MonthlyTrafik.MONTH_INTENT_ID);
        int daysNo = getIntent().getIntExtra(MonthlyTrafik.DAYS_IN_MONTH,0);
        String month = getIntent().getStringExtra(MonthlyTrafik.MONTH_SELECTED);
        String year = getIntent().getStringExtra(MonthlyTrafik.YEAR_SELECTED);
        tableData.constructTable(this,tableLayout);
        tableData.constructBarChart(barChart);
        tableData.constructMonthlyLineChart(this,lineChart,daysNo,month,year);



    }

}
