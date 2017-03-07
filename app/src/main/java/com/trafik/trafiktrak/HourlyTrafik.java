package com.trafik.trafiktrak;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;

import java.io.Serializable;
import java.util.ArrayList;

public class HourlyTrafik extends AppCompatActivity{

    private LinearLayout hourlyScreen;
    private TextView availableDates, selectedDates;
    private Button pickDate;
    private Button viewInfo;
    private Spinner availableTime;
    private BarChart barChart;
    private Database db;
    private static final int DIALOG_ID = 0;
    private int year, month, day;
    private String hourSelected, dateSelected;
    private boolean dateSelect, hourSelect;

    public static final String HOUR_INTENT_ID = "hour.intent.id";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hourly_trafik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hourly);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowTitleEnabled(true);

        //setting up the UI components


        hourlyScreen = (LinearLayout) findViewById(R.id.hourly_screen);
        availableDates = (TextView) findViewById(R.id.available_date_id);
        selectedDates  = (TextView) findViewById(R.id.selected_date_id);
        pickDate  = (Button) findViewById(R.id.pick_date);
        viewInfo = (Button) findViewById(R.id.view_hourly_table);
        availableTime  = (Spinner) findViewById(R.id.spinner);
        invokeTimeNotAvailable();
        //getting to set the available dates
        db = new Database(this);
        Cursor cursor = db.getVehicleMovements(null);
        cursor.moveToFirst();
        String start_date = cursor.getString(cursor.getColumnIndex(Database.DATE));
        //let us initialise our date picker with the earliest date in the database
        String[] dates = start_date.split("/");
        day = Integer.parseInt(dates[0]);
        month = (Integer.parseInt(dates[1]) -1);
        year = Integer.parseInt(dates[2]);

        cursor.moveToLast();
        String last_date = cursor.getString(cursor.getColumnIndex(Database.DATE));

        availableDates.setText(start_date + " - "+ last_date);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showDialog(DIALOG_ID);
            }
        });
        viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateSelect && hourSelect){

                    //correct
                    //lets search for where date is dateselected and hour of hourselected matches
                    String hour = hourSelected.split("-")[0].trim();
                    Cursor cursor = db.getHourlyTraffic(dateSelected,hour,null);
                    if(cursor == null){

                        Toast.makeText(v.getContext(),"Data could not be fetched!",Toast.LENGTH_LONG).show();

                    }else {
                        //I eventually created a model for the table data....cool, thanks to serializable concept
                        TableData tableData = new TableData(cursor);
                        Intent intent = new Intent(HourlyTrafik.this,HourlyTrafikAnalysis.class);
                        intent.putExtra(HOUR_INTENT_ID,tableData);
                        startActivity(intent);
                    }
                }else{
                    Snackbar.make(hourlyScreen,"Select date and time",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    protected Dialog onCreateDialog(int dailog_id){
        if(dailog_id == DIALOG_ID) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
            return datePickerDialog;
        }else{
            return null;
        }
    }
    private void invokeTimeNotAvailable(){
        String[] available = {"Not Available"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, available);
        availableTime.setAdapter(adapter);

    }

        private DatePickerDialog.OnDateSetListener dateSetListener
                = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //check the database for the selected date ehn...
                dateSelected = constructDate(dayOfMonth,monthOfYear+1,year);
                Cursor cursor = db.isDateAvailable(dateSelected,null);
                if(cursor == null){
                    invokeTimeNotAvailable();
                    dateSelect = false;
                    selectedDates.setText("Selected Date is: Date not available");

                }else{
                    dateSelect=true;
                    selectedDates.setText("Selected Date is: "+ dateSelected);
                    updateAvailableTimeInSpinner(cursor);
                }
            }
        };



    private void updateAvailableTimeInSpinner(Cursor cursor) {
        final ArrayList<String> availableTime = new ArrayList<>();
        //lets formulate the available hours
        availableTime.add("Available Time...");

        while(cursor.moveToNext()){

             String time = cursor.getString(cursor.getColumnIndex(Database.TIME_IN));
              String start_hour = time.split(":")[0]; //returning the hour
            String end_hour = start_hour+":59";
            String timeToSpinner = start_hour + " - " + end_hour;
            if(availableTime.contains(timeToSpinner)){
                continue;

            }else
            {
                availableTime.add(timeToSpinner);
            }

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.availableTime.getContext(), android.R.layout.simple_list_item_1, availableTime);

        this.availableTime.setAdapter(adapter);

        this.availableTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub
                int index = parent.getSelectedItemPosition();
                if(index ==0){
                    hourSelect = false;

                }else {
                    hourSelect = true;
                    hourSelected = availableTime.get(index);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private String constructDate(int day, int month, int year) {

        String actualDay = day >= 10 ? Integer.toString(day) : "0" + day;
        String actualMonth = month >= 10 ? Integer.toString(month) : "0" + month;
        String actualYear = Integer.toString(year);
        return actualDay + "/" + actualMonth + "/" + actualYear;

    }

}

