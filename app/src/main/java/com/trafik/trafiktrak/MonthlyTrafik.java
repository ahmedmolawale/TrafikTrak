package com.trafik.trafiktrak;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class MonthlyTrafik extends AppCompatActivity {


    private TextView availableDates, selectedMonth;
    private Button pickDate, viewInfo;
    private int year, month, day;
    private static final int DIALOG_ID = 3;
    private Database db;
    private String dateSelected;
    private String monthSeleceted;
    private static final String[] MONTHS = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST",
            "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    private int[] MONTH_DAYS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final String MONTH_INTENT_ID = "month.intent.id";
    public static final String DAYS_IN_MONTH = "days.month.intent.id";
    public static final String MONTH_SELECTED = "month.selected.intent.id";
    public static final String YEAR_SELECTED = "year.selected.intent.id";
    private boolean monthAvailable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monthly_trafik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_monthly);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        availableDates = (TextView) findViewById(R.id.available_date_on_monthly);
        selectedMonth = (TextView) findViewById(R.id.selected_month);
        pickDate = (Button) findViewById(R.id.pick_date_on_monthly);
        viewInfo = (Button) findViewById(R.id.view_monthly_table);
        db = new Database(this);

        Cursor cursor = db.getVehicleMovements(null);
        cursor.moveToFirst();
        String start_date = cursor.getString(cursor.getColumnIndex(Database.DATE));
        //let us initialise our date picker with the earliest date in the database
        final String[] dates = start_date.split("/");
        day = Integer.parseInt(dates[0]);
        month = (Integer.parseInt(dates[1]) - 1);
        year = Integer.parseInt(dates[2]);

        cursor.moveToLast();
        String last_date = cursor.getString(cursor.getColumnIndex(Database.DATE));

        availableDates.setText(start_date + " - " + last_date);

        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
        viewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (monthAvailable) {
                    //correct
                    String monthSelected = dateSelected.split("/")[1];
                    String yearSelected = dateSelected.split("/")[2];
                    if (Integer.parseInt(dateSelected.split("/")[2]) % 4 == 0) {  //changing MONTH_DAYS based on leap year
                        MONTH_DAYS[1] = 29;
                    }
                    Cursor cursor = db.getMonthDays(monthSelected, yearSelected, null);
                    if (cursor != null) {
                        TableData tableData = new TableData(cursor);
                        //wrap and send to the analysis activity
                        Intent intent = new Intent(MonthlyTrafik.this, MonthlyTrafikAnalysis.class);
                        intent.putExtra(MONTH_INTENT_ID, tableData);
                        intent.putExtra(DAYS_IN_MONTH, MONTH_DAYS[Integer.parseInt(monthSelected) - 1]);
                        intent.putExtra(MONTH_SELECTED,monthSelected);
                        intent.putExtra(YEAR_SELECTED,yearSelected);
                        startActivity(intent);
                    } else {
                        Toast toast = Toast.makeText(v.getContext(), "An error occurred in getting month days", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(v.getContext(), "Month not available. Select a different month", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


    }

    protected Dialog onCreateDialog(int dailog_id) {
        if (dailog_id == DIALOG_ID) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
            return datePickerDialog;
        } else {
            return null;
        }
    }



    private DatePickerDialog.OnDateSetListener dateSetListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //check the database for the selected date ehn...

            dateSelected = constructDate(dayOfMonth, monthOfYear + 1, year);//had to increment by 1 cos the month returned by onDateSet is 0-based
            Cursor cursor = db.isDateAvailable(dateSelected, null);

            if (cursor == null) {
                monthAvailable = false;
                selectedMonth.setText("Selected Month is: Month is not available");

            } else {
                //we got to check whether the full month is available

                if (isFullDaysOfMonthAvailable(dateSelected)) {
                    monthAvailable = true;
                    selectedMonth.setText("Selected Month is: " + MONTHS[monthOfYear] + " , " + year);

                }
            }
        }
    };

    private boolean isFullDaysOfMonthAvailable(String dateSelected) {

        if (Integer.parseInt(dateSelected.split("/")[2]) % 4 == 0) {
            MONTH_DAYS[1] = 29;
        }
        Cursor cursor = db.getMonthDays(dateSelected.split("/")[1], dateSelected.split("/")[2], new String[]{Database.DATE});
        if (cursor != null && cursor.moveToLast()) {
            String date = cursor.getString(cursor.getColumnIndex(Database.DATE)); //getting the last date of the month
            int monthInInt = Integer.parseInt(dateSelected.split("/")[1]) - 1;
            if (Integer.parseInt(date.split("/")[0])==MONTH_DAYS[monthInInt]) { //checking if we have the last day of the month
                return true;
            } else {
                return false;
            }
        } else {
            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    private String constructDate(int day, int month, int year) {

        String actualDay = day >= 10 ? Integer.toString(day) : "0" + day;
        String actualMonth = month >= 10 ? Integer.toString(month) : "0" + month;
        String actualYear = Integer.toString(year);
        return actualDay + "/" + actualMonth + "/" + actualYear;

    }
}

