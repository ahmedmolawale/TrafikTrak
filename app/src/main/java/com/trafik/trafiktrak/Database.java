package com.trafik.trafiktrak;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.StringTokenizer;


public class Database {

    private static final String TAG = "DictionaryDatabase";
    public static String timetaken=null;



    //The columns we'll include in the trafik table
    public static final String VEHICLE_TAG ="vehicle_tag";
    public static final String DATE = "date";
    public static final String TIME_IN = "time_in";
    public static final String DIRECTION ="direction";
    public static final String CATEGORY = "category";
    public static final String LOCATION = "location";


    private static final String DATABASE_NAME = "TRAFFIC_INFORMATION";
    private static final String FTS_VIRTUAL_TABLE = "VEHICLE_TRAFFIC";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;
    private static SQLiteDatabase mDatabase;

    public Database(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }




    //inner class to create the database
    private static class DatabaseOpenHelper extends SQLiteOpenHelper {

        private final Context mHelperContext;


        //A string to create the virtual table
        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                        " USING fts3 (" +
                        VEHICLE_TAG + " TEXT, " +
                        DATE + " TEXT, " +
                        TIME_IN + " TEXT, " +
                        DIRECTION + " TEXT, " +
                        CATEGORY + " TEXT, " +
                        LOCATION + " TEXT)";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            mHelperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadRecords(); // loading the records into the virtual table
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }

        private void loadRecords() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadRecordsFromFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadRecordsFromFile() throws IOException {
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.traffic_volume_updated);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            StringTokenizer recordFields;
            String line;
            reader.readLine();  //reading the first redundant header

            try {
                while ((line = reader.readLine()) != null) {
                    recordFields = new StringTokenizer(line, ";");
                    //String[] recordFields = TextUtils.split(line, ";");
                    //if (recordFields.length < 13) continue;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(VEHICLE_TAG, recordFields.nextToken().trim());
                    contentValues.put(DATE, recordFields.nextToken().trim());
                    contentValues.put(TIME_IN, recordFields.nextToken().trim());
                    contentValues.put(DIRECTION, recordFields.nextToken().trim());
                    contentValues.put(CATEGORY, recordFields.nextToken().trim());
                    contentValues.put(LOCATION, recordFields.nextToken().trim());
                    Log.e(TAG, "record added with details: " + contentValues);
                    mDatabase.insert(FTS_VIRTUAL_TABLE, null, contentValues);
                }

            }
            catch (Exception en){
                Log.v(TAG, "Error Occured " + en.toString());
            }
            finally {
                reader.close();
            }

        }
    }

    public long insertVehicleMovement(ContentValues contentValues){

            return mDatabase.insert(FTS_VIRTUAL_TABLE,null,contentValues);
    }

    public Cursor getWordMatches(String query, String[] columns) {
        String selection = VEHICLE_TAG + " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);
    }
    public Cursor getTheHourCount(String query, String columns[]) {
        String selection = TIME_IN+ " MATCH ?";
        String[] selectionArgs = new String[] {query+"*"};

        return query(selection, selectionArgs, columns);

    }
    public Cursor getHourlyTraffic(String date,String time, String columns[]){
        String selection = DATE + " = ? AND "+TIME_IN + " LIKE ?";
        String[] selectionArgs = new String[] {date,time+"%"};
        return query(selection, selectionArgs, columns);
    }
    public Cursor getDailyTraffic(String dateSelected, String[] columns) {
        String selection = DATE + " = ?";
        String[] selectionArgs = new String[] {dateSelected};
        return query(selection, selectionArgs, columns);
    }
    public Cursor getMonthDays(String month, String year, String[] columns) {
        String selection = DATE + " LIKE ?";
        String[] selectionArgs = new String[] {"%"+month+"/"+year};
        return query(selection, selectionArgs, columns);

    }

    public Cursor getVehicleMovements(String columns[]){

        return query(null, null, columns);
    }
    public Cursor isDateAvailable(String item, String columns[]){
        String selection = DATE + " = ?";
        String[] selectionArgs = new String[] {item};
        return query(selection, selectionArgs, columns);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);
        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }

        return cursor;
    }

}