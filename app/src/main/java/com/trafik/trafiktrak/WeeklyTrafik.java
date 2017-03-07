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

import java.util.ArrayList;

public class WeeklyTrafik extends AppCompatActivity {

    private TextView availableDates, selectedDates;
    private Button pickDate, viewInfo;
    private int year, month, day;
    private static final int DIALOG_ID = 2;
    private Database db;
    private String dateSelected;
    private boolean weekAvailable;
    private int[] MONTH_DAYS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int DAYS_IN_A_WEEK = 7;
    private String endWeekDate;
    private ArrayList<String> datesOfTheWeek;
    public static final String WEEKLY_INTENT_ID = "week.intent.id";
    public static final String DATES_OF_THE_WEEK = "dates.week.intent.id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekly_trafik);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_weekly);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        availableDates = (TextView) findViewById(R.id.available_date_on_weekly);
        selectedDates = (TextView) findViewById(R.id.selected_week);
        pickDate = (Button) findViewById(R.id.pick_date_on_weekly);
        viewInfo = (Button) findViewById(R.id.view_weekly_table);

        //getting to set the available dates
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
                if (weekAvailable) {
                    //correct
                     int passengerCarEntryCount =0, passengerCarExitCount=0;
                     int busEntryCount=0, busExitCount=0 ;
                     int twoAxleTrailerEntryCount=0,twoAxleTrailerExitCount=0;
                     int threeAxleTrailerEntryCount=0, threeAxleTrailerExitCount=0;

                     int passengerCarTotal = 0;
                     int busTotal = 0;
                     int twoAxleTrailerTotal = 0;
                     int threeAxleTrailerTotal = 0;

                     int entryTotal = 0, exitTotal = 0;

                     int grandTotal = 0;


                    for(String date:datesOfTheWeek){
                        Cursor cursor = db.getDailyTraffic(date, null);
                        TableData tableData = new TableData(cursor);
                        passengerCarEntryCount += tableData.getPassengerCarEntryCount();
                        passengerCarExitCount += tableData.getPassengerCarExitCount();
                        busEntryCount += tableData.getBusEntryCount();
                        busExitCount += tableData.getBusExitCount();
                        twoAxleTrailerEntryCount += tableData.getTwoAxleTrailerEntryCount();
                        twoAxleTrailerExitCount += tableData.getTwoAxleTrailerExitCount();
                        threeAxleTrailerEntryCount += tableData.getThreeAxleTrailerEntryCount();
                        threeAxleTrailerExitCount += tableData.getThreeAxleTrailerExitCount();

                        passengerCarTotal += tableData.getPassengerCarTotal();
                        busTotal += tableData.getBusTotal();
                        twoAxleTrailerTotal += tableData.getTwoAxleTrailerTotal();
                        threeAxleTrailerTotal += tableData.getThreeAxleTrailerTotal();

                        entryTotal += tableData.getEntryTotal();
                        exitTotal += tableData.getExitTotal();
                        grandTotal += tableData.getGrandTotal();

                    }
                    TableData tableData = new TableData();   //creating a new tabledata which correspond to a week
                    tableData.setPassengerCarEntryCount(passengerCarEntryCount);
                    tableData.setPassengerCarExitCount(passengerCarExitCount);
                    tableData.setBusEntryCount(busEntryCount);
                    tableData.setBusExitCount(busExitCount);
                    tableData.setTwoAxleTrailerEntryCount(twoAxleTrailerEntryCount);
                    tableData.setTwoAxleTrailerExitCount(twoAxleTrailerExitCount);
                    tableData.setThreeAxleTrailerEntryCount(threeAxleTrailerEntryCount);
                    tableData.setThreeAxleTrailerExitCount(threeAxleTrailerExitCount);
                    tableData.setPassengerCarTotal(passengerCarTotal);
                    tableData.setBusTotal(busTotal);
                    tableData.setTwoAxleTrailerTotal(twoAxleTrailerTotal);
                    tableData.setThreeAxleTrailerTotal(threeAxleTrailerTotal);
                    tableData.setEntryTotal(entryTotal);
                    tableData.setExitTotal(exitTotal);
                    tableData.setGrandTotal(grandTotal);

                    //wrap and send to the analysis activity
                    Intent intent = new Intent(WeeklyTrafik.this, WeeklyTrafikAnalysis.class);
                    intent.putExtra(WEEKLY_INTENT_ID, tableData);
                    intent.putStringArrayListExtra(DATES_OF_THE_WEEK,datesOfTheWeek);
                    startActivity(intent);

                } else {
                    Toast toast = Toast.makeText(v.getContext(), "Week not available. Select a different day", Toast.LENGTH_LONG);
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
                weekAvailable = false;
                selectedDates.setText("Selected Week is: Week is not available");

            } else {
                //check if the next seven days is available
                if (isTheNextSevenDaysAvailable(dayOfMonth, monthOfYear + 1, year)) {
                    weekAvailable = true;
                    selectedDates.setText("Selected Week is: " + dateSelected + " - " + endWeekDate);
                }else{
                    selectedDates.setText("Selected Week is: Week is not available");
                }
            }
        }
    };

    private boolean isTheNextSevenDaysAvailable(int startDayOfWeek, int month, int year) {
        //assuming startDayOfMonth is 25...30

        int endDayOfWeek = startDayOfWeek + DAYS_IN_A_WEEK -1;  //32
        //

        datesOfTheWeek = new ArrayList<>();
        if (year % 4 == 0)
            MONTH_DAYS[1] = 29;//for a leap year

        if (endDayOfWeek <= MONTH_DAYS[month - 1]) {//check whether we hit the last day of the month
            //within the same month
            //check whether the date exist
            endWeekDate = this.constructDate(endDayOfWeek, month, year);
            Cursor cursor = db.isDateAvailable(endWeekDate, null);
            if (cursor != null) {
                //we use this to track the dates of the week
                for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
                    datesOfTheWeek.add(this.constructDate(startDayOfWeek + i, month, year));
                }
                return true;
            } else
                return false;


        } else {
            //get the number of days that the previous month can provide
            int maxDaysFromPrevious = MONTH_DAYS[month - 1] - startDayOfWeek;
            int daysNeededFromNextMonth = DAYS_IN_A_WEEK - maxDaysFromPrevious;
            endWeekDate = this.constructDate(daysNeededFromNextMonth, month >= 12 ? 1 : month + 1, month >= 12 ? year + 1 : year);
            /**
             * Above, The ternary operator is for the case when month is december,we got to move to next year
             * the added 1 to the month if for moving to the next month, and the
             * one added to year is for moving to a new year
             * explaining below usage too.
             */
            //check whether the date exist
            Cursor cursor = db.isDateAvailable(endWeekDate, null);
            if (cursor != null) {
                //we use this to track the dates of the week
                for (int i = 0; i < maxDaysFromPrevious; i++) {
                    datesOfTheWeek.add(this.constructDate(startDayOfWeek + i, month, year));
                }
                for (int i = 0; i < daysNeededFromNextMonth; i++) {   //working with the case when we have week extending to next month
                    datesOfTheWeek.add(this.constructDate( i+1, month >= 12 ? 1 : month + 1, month >= 12 ? year + 1 : year));
                }
                return true;
            } else
                return false;
        }
    }
    private String constructDate(int day, int month, int year) {

        String actualDay = day >= 10 ? Integer.toString(day) : "0" + day;
        String actualMonth = month >= 10 ? Integer.toString(month) : "0" + month;
        String actualYear = Integer.toString(year);
        return actualDay + "/" + actualMonth + "/" + actualYear;

    }
}
