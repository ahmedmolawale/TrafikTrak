package com.trafik.trafiktrak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class LiveDataScreen extends AppCompatActivity {

    Button hourlyTrafik, dailyTrafik, weeklyTrafik, monthlyTrafik, annualTrafik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_data_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setting the UI components up

        hourlyTrafik = (Button) findViewById(R.id.view_hourly_info);
        dailyTrafik = (Button)findViewById(R.id.view_daily_info);
        weeklyTrafik = (Button)findViewById(R.id.view_weekly_info);
        monthlyTrafik = (Button)findViewById(R.id.view_monthly_info);
        annualTrafik = (Button)findViewById(R.id.view_annual_info);

        hourlyTrafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveDataScreen.this, HourlyTrafik.class));
            }
        });
        dailyTrafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveDataScreen.this, DailyTrafik.class));
            }
        });

        weeklyTrafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveDataScreen.this, WeeklyTrafik.class));
            }
        });
        monthlyTrafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveDataScreen.this, MonthlyTrafik.class));
            }
        });
        annualTrafik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LiveDataScreen.this, AnnualTrafik.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
