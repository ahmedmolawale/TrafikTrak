package com.trafik.trafiktrak;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ESALEstimator extends AppCompatActivity {

    private EditText laneFactor;
    private EditText growthRate;
    private EditText designLife;
    private EditText numberOfCategory;

    private Button estimateESAL;
    private View focusView;
    private static final int NUM_OF_DAYS_IN_A_YEAR = 365;
    private double grn;
    private double aadtValue;
    private double esalValue;
    private EditText aadt;

    private SharedPreferences sharedPreferences;
    private String savedAADT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_esal_estimator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_esal_estimator);
        setSupportActionBar(toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        savedAADT = sharedPreferences.getString("AADTVALUE", "");
        //loading the UI Components
        aadt = (EditText) findViewById(R.id.aadt);

        laneFactor = (EditText) findViewById(R.id.fd);
        numberOfCategory = (EditText) findViewById(R.id.num_of_category);
        growthRate = (EditText) findViewById(R.id.growth_rate);
        designLife = (EditText) findViewById(R.id.design_life);
        estimateESAL = (Button) findViewById(R.id.pre_esal_estimator);


        if (!savedAADT.equals("")) {

            aadt.setText(savedAADT);
        }


//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
//
//            aadtValue = intent.getDoubleExtra(Intent.EXTRA_TEXT, 0.0);
//
//        }


        estimateESAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptESALEstimation();

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void attemptESALEstimation() {

        // Reset errors.
        aadt.setError(null);
        laneFactor.setError(null);
        growthRate.setError(null);
        designLife.setError(null);
        numberOfCategory.setError(null);


        // Store values at the time of the calculation attempt.
        String aadt = this.aadt.getText().toString();
        String laneFactor = this.laneFactor.getText().toString();
        String growthRate = this.growthRate.getText().toString();
        String designLife = this.designLife.getText().toString();
        String numberOfCategory = this.numberOfCategory.getText().toString();


        boolean cancel = false;
        focusView = null;


        if (TextUtils.isEmpty(aadt)) {

            this.aadt.setError(getString(R.string.error_field_required));
            focusView = this.aadt;
            cancel = true;
        }

        if (TextUtils.isEmpty(laneFactor)) {

            this.laneFactor.setError(getString(R.string.error_field_required));
            focusView = this.laneFactor;
            cancel = true;
        }

        if (TextUtils.isEmpty(growthRate)) {

            this.growthRate.setError(getString(R.string.error_field_required));
            focusView = this.growthRate;
            cancel = true;
        }

        if (TextUtils.isEmpty(designLife)) {

            this.designLife.setError(getString(R.string.error_field_required));
            focusView = this.designLife;
            cancel = true;
        }

        if (TextUtils.isEmpty(numberOfCategory)) {

            this.numberOfCategory.setError(getString(R.string.error_field_required));
            focusView = this.numberOfCategory;
            cancel = true;
        }


        if (!TextUtils.isEmpty(aadt) && Double.parseDouble(aadt) <= 0) {

            this.aadt.setError(getString(R.string.invalid_entry));
            focusView = this.aadt;
            cancel = true;
        }

        if (!TextUtils.isEmpty(laneFactor) && Double.parseDouble(laneFactor) <= 0) {

            this.laneFactor.setError(getString(R.string.invalid_entry));
            focusView = this.laneFactor;
            cancel = true;
        }

        if (!TextUtils.isEmpty(growthRate) && (Double.parseDouble(growthRate) <= 0 || Double.parseDouble(growthRate) > 100)) {

            this.growthRate.setError(getString(R.string.invalid_entry));
            focusView = this.growthRate;
            cancel = true;
        }

        if (!TextUtils.isEmpty(designLife) && Double.parseDouble(designLife) <= 0) {

            this.designLife.setError(getString(R.string.invalid_entry));
            focusView = this.designLife;
            cancel = true;
        }


        if (!TextUtils.isEmpty(numberOfCategory) && Integer.parseInt(numberOfCategory) <= 0) {

            this.numberOfCategory.setError(getString(R.string.invalid_entry));
            focusView = this.numberOfCategory;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //open esal estimator category
            double growthRateValue = Double.parseDouble(growthRate);
            double designLifeValue = Double.parseDouble(designLife);


            double actualGrowth = growthRateValue / 100;
            grn = (Math.pow((1 + actualGrowth), designLifeValue) - 1) / actualGrowth;
            Intent intent = new Intent(ESALEstimator.this, EsalEstimatorCategory.class);
            String[] esalDetails = {aadt, laneFactor, Double.toString(grn), numberOfCategory};
            intent.putExtra(Intent.EXTRA_TEXT, esalDetails);
            startActivity(intent);

//            double actualGrowth = growthRateValue / 100;
//            grn = (Math.pow((1 + actualGrowth), designLifeValue) - 1) / actualGrowth;
//            esalValue = laneFactorValue * grn * aadtValue * NUM_OF_DAYS_IN_A_YEAR * numberOfCategoryValue * loadEquivalencyFactorValue;
//            //to convert value to standard form
//            String esalInScientific = parseToScientificNotation(esalValue);
//            displayESALValue(esalInScientific);

        }
    }

    private void displayESALValue(String esalValue) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Esal value for the data given is:\n " + esalValue);
        builder.setTitle("ESAL Value");
        builder.setCancelable(true);
        builder.setPositiveButton("Okay, Good!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }

    public String parseToScientificNotation(double value) {
        int cont = 0;
        DecimalFormat decimalFormat = new DecimalFormat("0.######");
        while (((int) value) != 0) {
            value /= 10;
            cont++;
        }
        return decimalFormat.format(value).replace(",", ".") + " x10^ -" + cont;
    }




}


