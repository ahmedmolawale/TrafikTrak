package com.trafik.trafiktrak;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button getLiveData;
    private Button estimateESAL;
    private Button estimateAADT;
    private Button estimatePHF;


    private String aadtChoosen = "Estimate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home_screen);
        setSupportActionBar(toolbar);

        //loading the UI components

        getLiveData = (Button) findViewById(R.id.get_live_data);
        estimateESAL = (Button) findViewById(R.id.estimate_esal);
        estimateAADT = (Button)findViewById(R.id.estimate_aadt);
        estimatePHF = (Button) findViewById(R.id.estimate_phf);

        getLiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, LiveDataScreen.class));
            }
        });

        estimateAADT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, AADTEstimator.class));
            }
        });

        estimateESAL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //displayChoiceOfAADT();
                startActivity(new Intent(HomeScreen.this,ESALEstimator.class));
            }
        });




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void displayChoiceOfAADT() {


        final CharSequence[] aadtOptions = {"Estimate AADT", "AADT from Live Data"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AADT Options");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(aadtChoosen.equals("Estimate")){
                    startActivity(new Intent(HomeScreen.this, AADTEstimator.class));
                }else if(aadtChoosen.equals("Live Data")){



                }else{

                    Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_SHORT).show();

                }
//                Intent intent = new Intent(StandardDashboard.this, GradesTaker.class);
//                String[] extraDetails = {password, level, semesterChoosen};  //Matric no, level and semester, will be needed when saving grades
//                intent.putExtra(Intent.EXTRA_TEXT, extraDetails);
//                startActivity(intent);
            }

        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setSingleChoiceItems(aadtOptions, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {

                    case 0:
                        aadtChoosen = "Estimate";
                        break;
                    case 1:
                        aadtChoosen = "Live Data";
                        break;
                }

            }
        }).show();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
