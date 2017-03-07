package com.trafik.trafiktrak;

import android.content.Context;
import android.database.Cursor;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 14/08/2016.
 */
public class TableData implements Serializable {
    private static final String PASSENGER_CAR = "Passenger Car";
    private static final String BUS = "Bus";
    private static final String TWO_AXLE_TRAILER = "2-axle Trailer";
    private static final String THREE_AXLE_TRAILER = "3-axle Trailer";
    private static final String ENTRY = "Entry";
    private static final String EXIT = "Exit";

    private static final String CATEGORY = "Category";
    private static final String TOTAL = "Total";
    private static final String SN = "S/N";
    private static final String ID = "ID";
    private int passengerCarEntryCount, passengerCarExitCount;
    private int busEntryCount, busExitCount ;
    private int twoAxleTrailerEntryCount,twoAxleTrailerExitCount;


    private int threeAxleTrailerEntryCount, threeAxleTrailerExitCount;


    private int passengerCarTotal;
    private int busTotal;
    private int twoAxleTrailerTotal;
    private int threeAxleTrailerTotal;

    private int entryTotal, exitTotal;

    private int grandTotal;

    private Database db;

    public TableData(Cursor cursor){

       setUpValues(cursor);
    }
    public TableData(){

    }
    private void setUpValues(Cursor cursor){

            for(int i =0; i<cursor.getCount();i++){
                cursor.moveToPosition(i);
            if (cursor.getString(cursor.getColumnIndex(Database.CATEGORY)).equals(PASSENGER_CAR)
                    && cursor.getString(cursor.getColumnIndex(Database.DIRECTION)).equals(ENTRY)) {
                passengerCarEntryCount += 1;
            } else if (cursor.getString(cursor.getColumnIndex(Database.CATEGORY)).equals(PASSENGER_CAR)
                    && cursor.getString(cursor.getColumnIndex(Database.DIRECTION)).equals(EXIT)) {
                passengerCarExitCount += 1;
            } else if (cursor.getString(cursor.getColumnIndex(Database.CATEGORY)).equals(BUS)
                    && cursor.getString(cursor.getColumnIndex(Database.DIRECTION)).equals(ENTRY)) {
                busEntryCount += 1;
            }else if (cursor.getString(cursor.getColumnIndex(Database.CATEGORY)).equals(BUS)
                    && cursor.getString(cursor.getColumnIndex(Database.DIRECTION)).equals(EXIT)) {
                busExitCount += 1;
            }else if (cursor.getString(cursor.getColumnIndex(Database.CATEGORY)).equals(TWO_AXLE_TRAILER)
                    && cursor.getString(cursor.getColumnIndex(Database.DIRECTION)).equals(ENTRY)) {
                twoAxleTrailerEntryCount += 1;
            }else if (cursor.getString(cursor.getColumnIndex(Database.CATEGORY)).equals(TWO_AXLE_TRAILER)
                    && cursor.getString(cursor.getColumnIndex(Database.DIRECTION)).equals(EXIT)) {
                twoAxleTrailerExitCount += 1;
            }else if (cursor.getString(cursor.getColumnIndex(Database.CATEGORY)).equals(THREE_AXLE_TRAILER)
                    && cursor.getString(cursor.getColumnIndex(Database.DIRECTION)).equals(ENTRY)) {
                threeAxleTrailerEntryCount += 1;
            }else if (cursor.getString(cursor.getColumnIndex(Database.CATEGORY)).equals(THREE_AXLE_TRAILER)
                    && cursor.getString(cursor.getColumnIndex(Database.DIRECTION)).equals(EXIT)) {
                threeAxleTrailerExitCount += 1;
            }
        }

             helper();

    }

    private void helper(){
        passengerCarTotal = passengerCarEntryCount + passengerCarExitCount;
        busTotal = busEntryCount + busExitCount;
        twoAxleTrailerTotal = twoAxleTrailerEntryCount + twoAxleTrailerExitCount;
        threeAxleTrailerTotal = threeAxleTrailerEntryCount + threeAxleTrailerExitCount;

        //getting the total entry count and total exit count

        entryTotal = passengerCarEntryCount + busEntryCount + twoAxleTrailerEntryCount +threeAxleTrailerEntryCount;
        exitTotal = passengerCarExitCount + busExitCount + twoAxleTrailerExitCount + threeAxleTrailerExitCount;

        //getting the grandtotal
        grandTotal = entryTotal + exitTotal;


    }
    public void setPassengerCarEntryCount(int passengerCarEntryCount) {
        this.passengerCarEntryCount = passengerCarEntryCount;
    }

    public void setPassengerCarExitCount(int passengerCarExitCount) {
        this.passengerCarExitCount = passengerCarExitCount;
    }

    public void setBusEntryCount(int busEntryCount) {
        this.busEntryCount = busEntryCount;
    }

    public void setBusExitCount(int busExitCount) {
        this.busExitCount = busExitCount;
    }

    public void setTwoAxleTrailerEntryCount(int twoAxleTrailerEntryCount) {
        this.twoAxleTrailerEntryCount = twoAxleTrailerEntryCount;
    }

    public void setTwoAxleTrailerExitCount(int twoAxleTrailerExitCount) {
        this.twoAxleTrailerExitCount = twoAxleTrailerExitCount;
    }

    public void setThreeAxleTrailerEntryCount(int threeAxleTrailerEntryCount) {
        this.threeAxleTrailerEntryCount = threeAxleTrailerEntryCount;
    }

    public void setThreeAxleTrailerExitCount(int threeAxleTrailerExitCount) {
        this.threeAxleTrailerExitCount = threeAxleTrailerExitCount;
    }

    public void setPassengerCarTotal(int passengerCarTotal) {
        this.passengerCarTotal = passengerCarTotal;
    }

    public void setBusTotal(int busTotal) {
        this.busTotal = busTotal;
    }

    public void setTwoAxleTrailerTotal(int twoAxleTrailerTotal) {
        this.twoAxleTrailerTotal = twoAxleTrailerTotal;
    }

    public void setThreeAxleTrailerTotal(int threeAxleTrailerTotal) {
        this.threeAxleTrailerTotal = threeAxleTrailerTotal;
    }

    public void setEntryTotal(int entryTotal) {
        this.entryTotal = entryTotal;
    }

    public void setExitTotal(int exitTotal) {
        this.exitTotal = exitTotal;
    }

    public void setGrandTotal(int grandTotal) {
        this.grandTotal = grandTotal;
    }

    public int getBusTotal() {
        return busTotal;
    }

    public int getPassengerCarTotal() {
        return passengerCarTotal;
    }

    public int getTwoAxleTrailerTotal() {
        return twoAxleTrailerTotal;
    }

    public int getThreeAxleTrailerTotal() {
        return threeAxleTrailerTotal;
    }

    public int getEntryTotal() {
        return entryTotal;
    }

    public int getExitTotal() {
        return exitTotal;
    }

    public int getGrandTotal() {
        return grandTotal;
    }


    public int getThreeAxleTrailerEntryCount() {
        return threeAxleTrailerEntryCount;
    }

    public int getPassengerCarEntryCount() {
        return passengerCarEntryCount;
    }

    public int getPassengerCarExitCount() {
        return passengerCarExitCount;
    }

    public int getBusEntryCount() {
        return busEntryCount;
    }

    public int getBusExitCount() {
        return busExitCount;
    }

    public int getTwoAxleTrailerEntryCount() {
        return twoAxleTrailerEntryCount;
    }

    public int getTwoAxleTrailerExitCount() {
        return twoAxleTrailerExitCount;
    }

    public int getThreeAxleTrailerExitCount() {
        return threeAxleTrailerExitCount;
    }

    public void constructTable(Context context, TableLayout tableLayout){

        TableRow header = new TableRow(context);
        header.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));




        //adding the headers
        TextView column = new TextView(context);
        column.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column.setBackgroundResource(R.drawable.cell_shape_for_header);
        column.setPadding(3,3,3,3);
        column.setText(SN);
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column);


        TextView column0 = new TextView(context);
        column0.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column0.setBackgroundResource(R.drawable.cell_shape_for_header);
        column0.setPadding(3,3,3,3);
        column0.setText(ID);
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column0);



        TextView column1 = new TextView(context);
        column1.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column1.setBackgroundResource(R.drawable.cell_shape_for_header);
        column1.setPadding(3,3,3,3);
        column1.setText(CATEGORY);
        //column1.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column1);

        TextView column2 = new TextView(context);
        column2.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column2.setBackgroundResource(R.drawable.cell_shape_for_header);
        column2.setPadding(3,3,3,3);
        column2.setText(ENTRY);
      //  column2.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column2);

        //third column
        TextView column3 = new TextView(context);
        column3.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column3.setBackgroundResource(R.drawable.cell_shape_for_header);
        column3.setPadding(3,3,3,3);
        column3.setText(EXIT);
    //    column3.setTextColor(context.getResources().getColor(R.color.black));
        header.addView(column3);

        //fourth column
        TextView column4 = new TextView(context);
        column4.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column4.setBackgroundResource(R.drawable.cell_shape_for_header);
        column4.setPadding(3,3,3,3);
        column4.setText(TOTAL);
  //      column4.setTextColor(context.getResources().getColor(R.color.black));

        header.addView(column4);
        tableLayout.addView(header);


        //adding the other rows
        tableLayout.addView(addRowData(context,"1","A",PASSENGER_CAR,getPassengerCarEntryCount(),getPassengerCarExitCount(),getPassengerCarTotal()));
        tableLayout.addView(addRowData(context,"2","B",BUS,getBusEntryCount(),getBusExitCount(),getBusTotal()));
        tableLayout.addView(addRowData(context,"3","C",TWO_AXLE_TRAILER,getTwoAxleTrailerEntryCount(),getTwoAxleTrailerExitCount(),getTwoAxleTrailerTotal()));
        tableLayout.addView(addRowData(context,"4","D",THREE_AXLE_TRAILER,getThreeAxleTrailerEntryCount(),getThreeAxleTrailerExitCount(),getThreeAxleTrailerTotal()));
        tableLayout.addView(addRowData(context,"","",TOTAL,getEntryTotal(),getExitTotal(),getGrandTotal()));


    }


    private TableRow addRowData(Context context, String sn,String id,String category, int entry, int exit, int total) {

        TableRow row = new TableRow(context);
        row.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


        //adding the data
        TextView column = new TextView(context);
        column.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column.setBackgroundResource(R.drawable.cell_shape);
        column.setPadding(3,3,3,3);
        column.setText(sn);
        //column1.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column);

        TextView column0 = new TextView(context);
        column0.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column0.setBackgroundResource(R.drawable.cell_shape);
        column0.setPadding(3,3,3,3);
        column0.setText(id);
        //column1.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column0);


        TextView column1 = new TextView(context);
        column1.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column1.setBackgroundResource(R.drawable.cell_shape);
        column1.setPadding(3,3,3,3);
        column1.setText(category);
        //column1.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column1);

        TextView column2 = new TextView(context);
        column2.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column2.setBackgroundResource(R.drawable.cell_shape);
        column2.setPadding(3,3,3,3);
        column2.setText(""+entry);
        //column2.setTextColor(context.getResources().getColor(R.color.black));
        row.addView(column2);

        //third column
        TextView column3 = new TextView(context);
        column3.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column3.setBackgroundResource(R.drawable.cell_shape);
        column3.setPadding(3,3,3,3);
        column3.setText(""+exit);
        //column3.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column3);

        //fourth column
        TextView column4 = new TextView(context);
        column3.setLayoutParams(new
                TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT));

        column4.setBackgroundResource(R.drawable.cell_shape);
        column4.setPadding(3,3,3,3);
        column4.setText(""+total);
//        column4.setTextColor(context.getResources().getColor(R.color.black));

        row.addView(column4);
      return row;
    }


    public void constructBarChart(BarChart barChart) {

        //create the dataset first
        ArrayList<BarEntry> barEntryArrayList = new ArrayList<>();
        barEntryArrayList.add(new BarEntry(getPassengerCarTotal(),0));
        barEntryArrayList.add(new BarEntry(getBusTotal(),1));
        barEntryArrayList.add(new BarEntry(getTwoAxleTrailerTotal(),2));
        barEntryArrayList.add(new BarEntry(getThreeAxleTrailerTotal(),3));

        BarDataSet barDataSet = new BarDataSet(barEntryArrayList,"Total");

        //defining the x-label
        ArrayList<String> xLabels = new ArrayList<>();
        xLabels.add("A");
        xLabels.add("B");
        xLabels.add("C");
        xLabels.add("D");


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        BarData barData = new BarData(xLabels,barDataSet);
        barChart.setData(barData);
        barChart.animateY(2000);
        barChart.animateX(2000);

        barChart.setDoubleTapToZoomEnabled(false);
    }

    public void constructDailyLineChart(Context context, String daySelected,LineChart lineChart) {
        //create the dataset first
        ArrayList<Entry> entryArrayList = new ArrayList<>();
        entryArrayList.add(new Entry(0,0));
        ArrayList<Entry> entryArrayList2 = new ArrayList<>();
        int average = getGrandTotal()/24;
        //defining the x-label
        ArrayList<String> xLabels = new ArrayList<>();
        db = new Database(context);

        for(int i =0;i<24;i++){
            String time;
            if(i<10){
                time = "0"+i;
            }else{
                time = Integer.toString(i);
            }
           Cursor cursor =  db.getHourlyTraffic(daySelected,time,new String[]{Database.TIME_IN});
            entryArrayList2.add(new Entry(average,i+1));
            if(cursor == null){
                entryArrayList.add(new Entry(0,i+1));
            }else {
                entryArrayList.add(new Entry( cursor.getCount(), i + 1));
            }
            xLabels.add(Integer.toString(i));

        }
        xLabels.add(Integer.toString(24));

        LineDataSet lineDataSet = new LineDataSet(entryArrayList,"Vehicles/hr");
        LineDataSet lineDataSet2 = new LineDataSet(entryArrayList2,"Av. Vehicles/hr");

        lineDataSet2.setColor(ColorTemplate.rgb("#FF4081"));
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setDrawValues(false);
        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet2);
        //LineData lineData = new
        LineData lineData = new LineData(xLabels,lineDataSets);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setData(lineData);
        lineChart.animateX(3000);
        lineChart.animateY(3000);
        lineChart.setDoubleTapToZoomEnabled(false);


    }
    public void constructWeeklyLineChart(Context context, ArrayList<String> datesOfTheWeek,LineChart lineChart) {
        //create the dataset first
        ArrayList<Entry> entryArrayList = new ArrayList<>();
        int average = getGrandTotal()/7;
        ArrayList<Entry> entryArrayList2 = new ArrayList<>();
        //defining the x-label
        ArrayList<String> xLabels = new ArrayList<>();
        db = new Database(context);

        for(int i =0;i<datesOfTheWeek.size();i++){
            Cursor cursor =  db.getDailyTraffic(datesOfTheWeek.get(i).trim(),new String[]{Database.TIME_IN});//the column to return ideally should be the Day e.g Monday
            if(cursor == null){ //will hardly occur
                entryArrayList.add(new Entry(0f,i));
            }else {
                entryArrayList.add(new Entry(cursor.getCount(), i));
                entryArrayList2.add(new Entry(average,i));
            }
            xLabels.add(datesOfTheWeek.get(i).split("/")[0]);//suppose to add the days of the week here mehn

        }
        LineDataSet lineDataSet = new LineDataSet(entryArrayList,"Vehicles/day");
        LineDataSet lineDataSet2 = new LineDataSet(entryArrayList2,"Av. Vehicles/day (ADT)");
        lineDataSet2.setColor(ColorTemplate.rgb("#FF4081"));
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setDrawValues(false);
        List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet2);
        //LineData lineData = new
        LineData lineData = new LineData(xLabels,lineDataSets);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setData(lineData);
        lineChart.animateX(3000);
        lineChart.animateY(3000);
        lineChart.setDoubleTapToZoomEnabled(false);


    }

    public void constructMonthlyLineChart(Context context,LineChart lineChart, int noOfDays,String month,String year) {

        //coding the madt
        int averageValue = getGrandTotal()/noOfDays;
        //create the dataset first
        ArrayList<Entry> entryArrayList = new ArrayList<>();
        ArrayList<Entry> entryArrayList2 = new ArrayList<>();
        //defining the x-label
        ArrayList<String> xLabels = new ArrayList<>();
        db = new Database(context);
        xLabels.add(Integer.toString(0));
        for(int i =1;i<=noOfDays;i++){
            String currentDay = constructDate(i,Integer.parseInt(month),Integer.parseInt(year));
            Cursor cursor =  db.getDailyTraffic(currentDay,new String[]{Database.TIME_IN});//the column to return ideally should be the Day e.g Monday
            if(cursor == null){ //will hardly occur
                entryArrayList.add(new Entry(0,i));
            }else {
                entryArrayList.add(new Entry(cursor.getCount(), i));
                entryArrayList2.add(new Entry(averageValue,i));
            }
            xLabels.add(Integer.toString(i));//suppose to add the days of the week here mehn

        }


        LineDataSet lineDataSet = new LineDataSet(entryArrayList,"Monthly");
        LineDataSet lineDataSet2 = new LineDataSet(entryArrayList2,"MADT");

        lineDataSet2.setColor(ColorTemplate.rgb("#FF4081"));
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setDrawCircleHole(false);
        lineDataSet2.setDrawValues(false);
         List<ILineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet);
        lineDataSets.add(lineDataSet2);
        Legend legend = new Legend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        
        //LineData lineData = new
        LineData lineData = new LineData(xLabels,lineDataSets);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.setData(lineData);
        lineChart.animateX(3000);
        lineChart.animateY(3000);

        lineChart.setDoubleTapToZoomEnabled(false);


    }
    private String constructDate(int day, int month, int year) {

        String actualDay = day >= 10 ? Integer.toString(day) : "0" + day;
        String actualMonth = month >= 10 ? Integer.toString(month) : "0" + month;
        String actualYear = Integer.toString(year);
        return (actualDay + "/" + actualMonth + "/" + actualYear).trim();

    }
}
