package com.trafik.trafiktrak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EsalEstimatorCategory extends AppCompatActivity {

    private EditText loadEquivalencyFactor;
    private EditText numberOfAxles;
    private Button esalCategory;
    private View focusView;

    private String esalDetails[];
    private double aadtValue;
    private double laneFactor;
    private double grn;
    private int numberOfCategory;
    private double aCategoryEsal;
    private int NUM_OF_DAYS_IN_A_YEAR = 365;
    private int numberOfCategoryForTitle;
    ArrayList<String> esalValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esal_estimator_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_esal_category);
        setSupportActionBar(toolbar);
        numberOfCategoryForTitle = 1;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Category " + numberOfCategoryForTitle);

        //setting up the ui component
        numberOfAxles = (EditText) findViewById(R.id.ni);
        loadEquivalencyFactor = (EditText) findViewById(R.id.fei);
        esalCategory = (Button) findViewById(R.id.category_switcher);

        //getting details from the caller
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {

            esalDetails = intent.getStringArrayExtra(Intent.EXTRA_TEXT);
        }
        aadtValue = Double.parseDouble(esalDetails[0]);
        laneFactor = Double.parseDouble(esalDetails[1]);
        grn = Double.parseDouble(esalDetails[2]);
        numberOfCategory = Integer.parseInt(esalDetails[3]);

        if (numberOfCategory == 1) {
            esalCategory.setText("Get ESAL");
        }

        //create an array for the number of category

        esalValues = new ArrayList<>();


        esalCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyContent();
            }
        });


    }

    private void verifyContent() {

        numberOfAxles.setError(null);
        loadEquivalencyFactor.setError(null);

        String numberOfAxles = this.numberOfAxles.getText().toString();
        String loadEquivalencyFactor = this.loadEquivalencyFactor.getText().toString();


        boolean cancel = false;
        focusView = null;


        if (TextUtils.isEmpty(numberOfAxles)) {

            this.numberOfAxles.setError(getString(R.string.error_field_required));
            focusView = this.numberOfAxles;
            cancel = true;
        }

        if (TextUtils.isEmpty(loadEquivalencyFactor)) {

            this.loadEquivalencyFactor.setError(getString(R.string.error_field_required));
            focusView = this.loadEquivalencyFactor;
            cancel = true;
        }
        if (!TextUtils.isEmpty(numberOfAxles) && Integer.parseInt(numberOfAxles) <= 0) {

            this.numberOfAxles.setError(getString(R.string.invalid_entry));
            focusView = this.numberOfAxles;
            cancel = true;
        }

        if (!TextUtils.isEmpty(loadEquivalencyFactor) && Double.parseDouble(loadEquivalencyFactor) <= 0) {

            this.loadEquivalencyFactor.setError(getString(R.string.invalid_entry));
            focusView = this.loadEquivalencyFactor;
            cancel = true;
        }

        if (cancel) {
            // form field with an error.
            focusView.requestFocus();
        } else {
            int numberOfAxlesValue = Integer.parseInt(numberOfAxles);
            double loadEquivalencyFactorValue = Double.parseDouble(loadEquivalencyFactor);

            if (numberOfCategory == 1) {
                aCategoryEsal = laneFactor * grn * aadtValue * NUM_OF_DAYS_IN_A_YEAR * numberOfAxlesValue * loadEquivalencyFactorValue;
                esalValues.add(Double.toString(aCategoryEsal));

                //start the EsalEstimatedResult
                Intent intent = new Intent(EsalEstimatorCategory.this, EsalEstimatedResult.class);
                //intent.putExtra("All Esal",Double.toString(aCategoryEsal));
                intent.putStringArrayListExtra(Intent.EXTRA_TEXT, esalValues);
                startActivity(intent);

            } else if (numberOfCategory > 1) {
                aCategoryEsal = laneFactor * grn * aadtValue * NUM_OF_DAYS_IN_A_YEAR * numberOfAxlesValue * loadEquivalencyFactorValue;
                esalValues.add(Double.toString(aCategoryEsal));
                numberOfCategory -= 1;
                numberOfCategoryForTitle += 1;
                getSupportActionBar().setTitle("Category " + numberOfCategoryForTitle);
                this.numberOfAxles.setText("");
                this.loadEquivalencyFactor.setText("");
                this.numberOfAxles.requestFocus();
            }
//            //decrement the numberOfCategory
//            Toast.makeText(getApplicationContext(), "Number of Category " + numberOfCategory, Toast.LENGTH_SHORT);
//
//            if (numberOfCategoryForTitle == 1) {
//                aCategoryEsal = laneFactor * grn * aadtValue * NUM_OF_DAYS_IN_A_YEAR * numberOfAxlesValue * loadEquivalencyFactorValue;
//                esalValues.add(Double.toString(aCategoryEsal));
//                numberOfCategoryForTitle += 1;
//            } else if (numberOfCategory > 1) {
//                aCategoryEsal = laneFactor * grn * aadtValue * NUM_OF_DAYS_IN_A_YEAR * numberOfAxlesValue * loadEquivalencyFactorValue;
//                esalValues.add(Double.toString(aCategoryEsal));
//                numberOfCategory -= 1;
//
//                getSupportActionBar().setTitle("Category " + numberOfCategoryForTitle);
//                numberOfCategoryForTitle += 1;
//                this.numberOfAxles.setText("");
//                this.loadEquivalencyFactor.setText("");
//                this.numberOfAxles.requestFocus();
//            } else {
//                //start an activity giving the total esal
//                Intent intent = new Intent(EsalEstimatorCategory.this, EsalEstimatedResult.class);
//                //intent.putExtra("All Esal",Double.toString(aCategoryEsal));
//                intent.putStringArrayListExtra("All ESAL", esalValues);
//                startActivity(intent);
//            }
        }
    }
}
