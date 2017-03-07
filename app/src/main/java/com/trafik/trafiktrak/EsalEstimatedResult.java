package com.trafik.trafiktrak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class EsalEstimatedResult extends AppCompatActivity {

    private TextView estimatedResult;
    private ArrayList<String> esalValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esal_estimated_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_estimated_result);
        setSupportActionBar(toolbar);

        esalValues = new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent != null){
            esalValues = intent.getStringArrayListExtra(Intent.EXTRA_TEXT);
        }
        estimatedResult = (TextView) findViewById(R.id.estimated_result);
        double totalEsal=0.0;
        StringBuilder builder = new StringBuilder();
        builder.append("ESAL Breakdown by Category\n\n\n");
        for(int i=0; i<esalValues.size();i++) {
            builder.append("Category "+(i+1));
            builder.append("\n\n");
            double esal = Double.parseDouble(esalValues.get(i));
            totalEsal += esal;
            builder.append("ESAL = " +String.format("%.3f",esal));
            builder.append("\n\n");
        }
        builder.append("\n\n");
        builder.append("Total ESAL:");
        builder.append(String.format("%.3f",totalEsal));

        estimatedResult.setText(builder.toString());

    }
    private String convertToStandardForm(double value) {

//        NumberFormat formatter = new DecimalFormat("###.#####");
//        String f = formatter.format(value);
        String valueHere = Double.toString(value) + ".00";
        String[] partOfNum = Double.toString(value).split(".");
        char[] decimalPart = partOfNum[0].toCharArray();
        int decimalSize = decimalPart.length;
        String newValue = "";      
        if (decimalSize > 6) {
            int position = decimalSize - 6;
            for (int i = 0; i < position; i++) {
                newValue += decimalPart[i];
            }
            newValue += "."; //placing dot at the appropriate place
            //write additional three digits and discard the rest
            for (int i = position; i < position + 3; i++) {
                newValue += decimalPart[i];
            }
            return newValue;
        } else if(decimalSize == 6){
            newValue += "0.";
            //write additional three digits and discard the rest
            for (int i =0; i < 3; i++) {
                newValue += decimalPart[i];
            }
            return newValue;
        }else if(decimalSize < 6){
            int numOfZeros = 6 - decimalSize;
            newValue += "0.";
            for (int i = 1; i <= numOfZeros; i++) {
                newValue += "0";
            }
            //write additional three digits and discard the rest
            for (int i =0; i < 3; i++) {
                newValue += decimalPart[i];
            }
            return newValue;
        }else{

            return "Not a number";

        }
    }

}
