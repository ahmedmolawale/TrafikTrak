package com.trafik.trafiktrak;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DailyTrafik extends AppCompatActivity {

    private Database db;
    private TextView availableDates, selectedDates;
    private Button pickDate;
    private Button viewInfo;
    private static final int DIALOG_ID = 1;
    private int year, month, day;

    private String  dateSelected;
    private boolean dateSelect;
    public static final String DAILY_INTENT_ID = "daily.intent.id";
    public static final String DAY_SELECTED_INTENT_ID = "day.selected.intent.id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_trafik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_daily);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        availableDates = (TextView) findViewById(R.id.available_date_on_daily);
        selectedDates  = (TextView) findViewById(R.id.selected_date_on_daily);
        pickDate  = (Button) findViewById(R.id.pick_date_on_daily);
        viewInfo = (Button) findViewById(R.id.view_daily_table);

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
                if(dateSelect){
                    //correct
                    //lets search for where date is dateselected
                    Cursor cursor = db.getDailyTraffic(dateSelected,null);
                    if(cursor == null){ //hardly gonna happen
                        Toast.makeText(v.getContext(),"Data could not be fetched!",Toast.LENGTH_LONG).show();

                    }else {
                        //I eventually created a model for the table data....cool, thanks to serializable concept
                        TableData tableData = new TableData(cursor);
                        Intent intent = new Intent(DailyTrafik.this,DailyTrafikAnalysis.class);
                        intent.putExtra(DAILY_INTENT_ID,tableData);
                        intent.putExtra(DAY_SELECTED_INTENT_ID,dateSelected);
                        startActivity(intent);
                    }
                }else{
                    Toast toast =Toast.makeText(v.getContext(),"Please select a day", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
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

    private String constructDate(int day, int month, int year) {

        String actualDay = day >= 10 ? Integer.toString(day) : "0" + day;
        String actualMonth = month >= 10 ? Integer.toString(month) : "0" + month;
        String actualYear = Integer.toString(year);
        return actualDay + "/" + actualMonth + "/" + actualYear;

    }


    private DatePickerDialog.OnDateSetListener dateSetListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //check the database for the selected date ehn...
            dateSelected = constructDate(dayOfMonth,monthOfYear+1,year);
            Cursor cursor = db.isDateAvailable(dateSelected,null);
            if(cursor == null){
                dateSelect = false;
                selectedDates.setText("Selected Date is: Date not available");

            }else{
                dateSelect=true;
                selectedDates.setText("Selected Date is: "+ dateSelected);
            }
        }
    };
    }