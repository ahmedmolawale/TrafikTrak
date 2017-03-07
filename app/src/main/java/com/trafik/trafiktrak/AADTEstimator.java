package com.trafik.trafiktrak;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AADTEstimator extends AppCompatActivity {

    private EditText dailyVolume;
    private EditText monthlyFactor;
    private EditText weeklyFactor;

    private Button estimateAADT;
    private View focusView;

    private double aadtValue;

    //not useful for now
    public static AADTEstimator aadtEstimatorController;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aadt_estimator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_aadt_estimator);
        setSupportActionBar(toolbar);

        aadtEstimatorController = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //loading the UI components
        dailyVolume = (EditText) findViewById(R.id.daily_volume);
        monthlyFactor = (EditText) findViewById(R.id.mfi);
        weeklyFactor = (EditText) findViewById(R.id.wfi);
        estimateAADT = (Button) findViewById(R.id.calculate_aadt);

        estimateAADT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptAADTEstimation();

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void attemptAADTEstimation() {

        // Reset errors.
        dailyVolume.setError(null);
        monthlyFactor.setError(null);
        weeklyFactor.setError(null);

        // Store values at the time of the calculation attempt.
        String dailyVolume = this.dailyVolume.getText().toString();
        String monthlyFactor = this.monthlyFactor.getText().toString();
        String weeklyFactor = this.weeklyFactor.getText().toString();


        boolean cancel = false;
        focusView = null;

        if (TextUtils.isEmpty(dailyVolume)) {

            this.dailyVolume.setError(getString(R.string.error_field_required));
            focusView = this.dailyVolume;
            cancel = true;
        }

        if (TextUtils.isEmpty(monthlyFactor)) {

            this.monthlyFactor.setError(getString(R.string.error_field_required));
            focusView = this.monthlyFactor;
            cancel = true;
        }

        if (TextUtils.isEmpty(weeklyFactor)) {

            this.weeklyFactor.setError(getString(R.string.error_field_required));
            focusView = this.weeklyFactor;
            cancel = true;
        }

        if (!TextUtils.isEmpty(dailyVolume) && Double.parseDouble(dailyVolume) <= 0) {

            this.dailyVolume.setError(getString(R.string.invalid_entry));
            focusView = this.dailyVolume;
            cancel = true;
        }

        if (!TextUtils.isEmpty(monthlyFactor) && Double.parseDouble(monthlyFactor) <= 0) {

            this.monthlyFactor.setError(getString(R.string.invalid_entry));
            focusView = this.monthlyFactor;
            cancel = true;
        }

        if (!TextUtils.isEmpty(weeklyFactor) && Double.parseDouble(weeklyFactor) <= 0) {

            this.weeklyFactor.setError(getString(R.string.invalid_entry));
            focusView = this.weeklyFactor;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            aadtValue = Double.parseDouble(dailyVolume) * Double.parseDouble(monthlyFactor) * Double.parseDouble(weeklyFactor);
            sharedPreferences.edit().putString("AADTVALUE",Double.toString(aadtValue)).apply();
            displayAADTValue(aadtValue);

        }
    }

    private void displayAADTValue(final double aadtValue) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("AADT value for the data given is:\n " + aadtValue);
        builder.setTitle("AADT");
        builder.setCancelable(true);
        builder.setPositiveButton("Proceed to ESAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //start the ESAL activity
                //finish the current activity
                finish();
                Intent intent = new Intent(AADTEstimator.this, ESALEstimator.class);
                intent.putExtra(Intent.EXTRA_TEXT,aadtValue+ "Veh./day" );
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builder.show();

    }

}
