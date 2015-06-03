package com.unitedforhealth_softwaredevelopment.testdbapp;

/**
 * Created by Serge on 02/04/2015.
 * Based on Derek Banas' Android Development Tutorial 12
 */

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// SQLiteOpenHelper helps you open or create a database
//@todo: to view all bookmarks in a project, press Shift+F11
public class DBTools extends SQLiteOpenHelper {
    //2015-05-25,6:10,7:00,Warmup and walk-jog,Exercises,"Regular description test, with one comma"

    public final static int CSV_ACTIVITY_LOG_DATE_COL_NUM = 0;
    public final static int CSV_ACTIVITY_LOG_START_TIME_COL_NUM = 1;
    public final static int CSV_ACTIVITY_LOG_END_TIME_COL_NUM = 2;
    public final static int CSV_ACTIVITY_LOG_ACTIVITY_COL_NUM = 3;
    public final static int CSV_ACTIVITY_LOG_ACTIVITY_GROUP_COL_NUM = 4;
    public final static int CSV_ACTIVITY_LOG_DESCRIPTION_COL_NUM = 5;
    public final static String DATABASE_NAME = "activitytimesheet.db";
    public final static String ACTIVITY_TABLE_NAME = "activity";
    public final static String ACTIVITY_TABLE_FIELD_NAMED_ACTIVITY_ID = "activityId";
    public final static String ACTIVITY_TABLE_FIELD_LOG_DATE_TIME_START = "logDateTimeStart";
    public final static String ACTIVITY_TABLE_CALC_FIELD_START_TIME = "startTime";
    public final static String ACTIVITY_TABLE_CALC_FIELD_START_DATE = "startDate";

    public final static String ACTIVITY_TABLE_FIELD_NAMED_ACTIVITY_NAME = "activityName";
    public final static String ACTIVITY_GROUP_TABLE_NAME = "activitygroup";
    public final static String ACTIVITY_GROUP_NAME_FIELD = "activityGroupName";
    public final static String ACTIVITY_GROUP_ID_FIELD = "activityGroupId";
    public final static String ACTIVITY_COMBO_TABLE_NAME = "activitycombo";
                    /*
                        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (1, 1)");

                 */
    public final static  String ACTIVITY_COMBO_ACTIVITY_ID_FIELD = "activityId";
    public final static  String ACTIVITY_COMBO_ACTIVITY_GROUP_ID_FIELD = "activityGroupId";
    public final static String ACTIVITY_LOG_TABLE_NAME = "activitylog";
    public final static String ACTIVITY_LOG_LOG_ID = "logId";


    public static final int ACTIVITY_COMBO_ID_COLUMN = 0;
    public static final int ACTIVITY_COMBO_ACTIVITY_ID_COLUMN = 1;
    public static final int ACTIVITY_COMBO_ACTIVITY_GROUP_ID_COLUMN = 2;
    public static final int ACTIVITY_COMBO_ACTIVITY_NAME_COLUMN = 3;
    public static final int ACTIVITY_COMBO_ACTIVITY_GROUP_NAME_COLUMN = 4;

    public static final String[] ACTIVITY_LOG_KEY_MAP = {
            "logId",
            "startDate",
            "startTime",
            "endTime",
            "activityId",
            "activityGroupId",
            "activityDescription",
            "activityName",
            "activityGroupName"
    };
    ArrayList<ActivityCombo> activityComboArrayList;
    private ArrayList<HashMap<String, String>> allActivityLog;
    private HashMap<String, ActivityCombo> activityComboHashMap;

    public DBTools(Context applicationcontext) {

        // Call to use the database or to create it
        super(applicationcontext, "activitytimesheet.db", null, 1);
//        makeActivityComboArrayList();
//        makeActivityComboHashMap();


    }

    private void makeActivityComboHashMap() {
        activityComboHashMap = new HashMap<>(activityComboArrayList.size());
        for (ActivityCombo combo : activityComboArrayList) {
            activityComboHashMap.put(combo.toString(), combo);
        }
    }

    // onCreate is called the first time the database is created

    public void onCreate(SQLiteDatabase database) {
        ArrayList<String> createCommands = new ArrayList();
/*
        SELECT l.logId, l.logDateTimeStart, l.logDateTimeEnd, l.activityId, l.activityGroupId, l.activityDescription, a.activityName, g.activityGroupName FROM activitylog l INNER JOIN activity a ON l.activityId = a.activityId INNER JOIN activitygroup g ON l.activityGroupId = g.activityGroupId ORDER BY l.logStartDateTime;
*/

        createCommands.add("CREATE TABLE " + ACTIVITY_LOG_TABLE_NAME + " (" +
                "logId INTEGER PRIMARY KEY, " +
                "logDateTimeStart DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "logDateTimeEnd DATETIME, " +
                "activityId INTEGER, " +
                "activityGroupId INTEGER, " +
                "activityDescription TEXT)");

        //@todo: make sure activityName is unique. One activityName empty is allowed.
        createCommands.add("CREATE TABLE " + ACTIVITY_TABLE_NAME + " (" +
                "activityId INTEGER PRIMARY KEY, " +
                "activityName TEXT)");

        //@todo: make sure activityGroupName is unique. One activityGroupName empty is allowed.
        createCommands.add("CREATE TABLE " + ACTIVITY_GROUP_TABLE_NAME + " ( " +
                "activityGroupId INTEGER PRIMARY KEY, " +
                "activityGroupName TEXT)");

        //@todo: make sure we don't have duplicate occurrences of activityId and activityGroupId
        createCommands.add("CREATE TABLE " + ACTIVITY_COMBO_TABLE_NAME + " (" +
                "activityComboId INTEGER PRIMARY KEY, " +
                "activityId INTEGER, " +
                "activityGroupId INTEGER)");

        /*
        createCommands.add("CREATE TABLE activitylog (" +
                "logId INTEGER PRIMARY KEY, " +
                "logDateTimeStart DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "logDateTimeEnd DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "activityId INTEGER, " +
                "activityGroupId INTEGER, " +
                "activityDescription TEXT)");

        */
        Log.d("MySimpleActivityTracker", "In onCreate, there are "+createCommands.size()+" CREATE TABLE commands");

        for (String command : createCommands){
            Log.d("MySimpleActivityTracker", "going to execute "+command);

            database.execSQL(command);
            Log.d("MySimpleActivityTracker", "executed");

        }
        Log.d("MySimpleActivityTracker", "going to execute makeTestData()");

    }

    public void rebuildDB(){
        Log.d("MySimpleActivityTracker", "Start of rebuildDB");

        SQLiteDatabase db =  this.getWritableDatabase();
        String [] tableNames = {"activitylog", "activity", "activitygroup", "activitycombo"};
        for (String tableName : tableNames){
            String query = "DROP TABLE IF EXISTS "+tableName;
            //@todo: on a real world upgrade you don't want to erase the user's data!!! Do something about this.
            db.execSQL(query);
        }

        Log.d("MySimpleActivityTracker", "All tables dropped, going to onCreate");
        this.onCreate(db);


    }

    // onUpgrade is used to drop tables, add tables, or do anything
    // else it needs to upgrade
    // This is dropping the table to delete the data and then calling
    // onCreate to make an empty table

    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
//        String [] tableNames = {"activitylog", "activity", "activitygroup", "activitycombo"};
//        for (String tableName : tableNames){
//            String query = "DROP TABLE IF EXISTS "+tableName;
//            //@todo: on a real world upgrade you don't want to erase the user's data!!! Do something about this.
//            database.execSQL(query);
//        }
//        onCreate(database);
    }


    /*
    @todo: eventually we should present the user with only a single day activity log, defaulted to today
     */
    public ArrayList<HashMap<String, String>> getAllActivityLog() {

//        makeTestData();

        // ArrayList that contains every row in the query result, and each row key / value stored in a HashMap

        ArrayList<HashMap<String, String>> activityLogArrayList;

        activityLogArrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT l.logId, l.logDateTimeStart, SUBSTR(l.logDateTimeEnd, 12, 8) 'endTime', l.activityId, " +
                "l.activityGroupId, l.activityDescription, a.activityName, g.activityGroupName " +
                "FROM activitylog l " +
                "INNER JOIN activity a ON l.activityId = a.activityId " +
                "INNER JOIN activitygroup g ON l.activityGroupId = g.activityGroupId " +
                "ORDER BY l.logDateTimeStart";

        // Open a database for reading and writing

//        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteDatabase database = this.getReadableDatabase();

        // Cursor provides read and write access for the
        // data returned from a database query

        // rawQuery executes the query and returns the result as a Cursor

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> logEntry = new HashMap<String, String>();

                // Store the key / value pairs in a HashMap
                // Access the Cursor data by index that is in the same order
                // as used when creating the table
                /*
                l.logId, l.logDateTimeStart, l.logDateTimeEnd, l.activityId, " +
                "l.activityGroupId, l.activityDescription, a.activityName, g.activityGroupName
                 */
                logEntry.put("logId", cursor.getString(0));
                logEntry.put("logDateTimeStart", cursor.getString(1));
                logEntry.put("endTime", cursor.getString(2));
                logEntry.put("activityId", cursor.getString(3));
                logEntry.put("activityGroupId", cursor.getString(4));
                logEntry.put("activityDescription", cursor.getString(5));
                logEntry.put("activityName", cursor.getString(6));
                logEntry.put("activityGroupName", cursor.getString(7));

                activityLogArrayList.add(logEntry);
            } while (cursor.moveToNext());
        }

        return activityLogArrayList;
    }
    public ArrayList<HashMap<String, String>> getActivities() {

        ArrayList<HashMap<String, String>> recordsArrayList;

        recordsArrayList = new ArrayList<HashMap<String, String>>();

        String selectQuery = "SELECT * FROM activity";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> record = new HashMap<String, String>();

                record.put("activityId", cursor.getString(0));
                record.put("activityName", cursor.getString(1));

                recordsArrayList.add(record);
            } while (cursor.moveToNext());
        }

        return recordsArrayList;
    }

    public void makeTestData() {
        SQLiteDatabase database = this.getWritableDatabase();
        ArrayList<String> insertCommands = new ArrayList();

        insertCommands.add("INSERT INTO activity (activityName) values('Entretien')");
        insertCommands.add("INSERT INTO activity (activityName) values('Repas')");
        insertCommands.add("INSERT INTO activity (activityName) values('Lessive')");
        insertCommands.add("INSERT INTO activity (activityName) values('Hygiène personnelle')");
        insertCommands.add("INSERT INTO activity (activityName) values('Planification')");
        insertCommands.add("INSERT INTO activity (activityName) values('Préparation')");
        insertCommands.add("INSERT INTO activity (activityName) values('Prière')");

        insertCommands.add("INSERT INTO activitygroup (activityGroupName) values('Entretien')");
        insertCommands.add("INSERT INTO activitygroup (activityGroupName) values('Administration')");
        insertCommands.add("INSERT INTO activitygroup (activityGroupName) values('Employabilité')");
        insertCommands.add("INSERT INTO activitygroup (activityGroupName) values('Alimentation')");
        insertCommands.add("INSERT INTO activitygroup (activityGroupName) values('Activités spirituelles')");

        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (1, 1)");
        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (2, 4)");
        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (3, 1)");
        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (4, 1)");
        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (5, 2)");
        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (6, 4)");
        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (7, 5)");
        //2015-04-04 15:40:49
        insertCommands.add("INSERT INTO activitylog (logDateTimeStart, logDateTimeEnd, activityId, activityGroupId, activityDescription) " +
                "VALUES (\"2015-04-04 15:40:49\", \"2015-04-04 15:50:00\", 1, 1, \"Entretien, Entretien\")");
        Log.d("MySimpleActivityTracker", "in makeTestData(), there are " + insertCommands.size() + " insert commands");

        for (String command : insertCommands){
            Log.d("MySimpleActivityTracker", "Command: " + command);
            database.execSQL(command);
            Log.d("MySimpleActivityTracker", "... executed");


        }


    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    /*
    If database already exist, return an empty string name.
    Else, create the database.
     */
    public String createDB() {
        StringBuilder dbName = new StringBuilder(getDatabaseName());
        if (dbName != null && dbName.length() > 0) {
            dbName.delete(0, dbName.length());
            return dbName.toString();
        }

        return dbName.toString();

    }

    public Cursor getAllDBTables(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' ", null);
        db.close();
        db = null;
        return cursor;
    }

    public void importCSVDataIntoActivityLog(CSVReader csvReader) {
        List<String[]> csvRows = csvReader.read();
        /*
        Expecting:
            Date,Start,End,Activity,Group,Description
            2015-05-25,6:00,6:10,,Personal Hygiene,Regular description test
            2015-05-25,6:10,7:00,Warmup and walk-jog,Exercises,"Regular description test, with one comma"
            2015-05-25,7:00,7:25,,Personal Hygiene,"Regular description test, with, at two, commas"
            2015-05-25,7:25,7:30,Prepare meal,Meals,"Regular description test, with, commas, and double “quotes” multiple “again between quotes”, and “orphan quote"
            2015-05-25,7:30,7:50,Take meal,Meals,
            2015-05-25,7:50,8:30,Commuting,Transportation,
            2015-05-25,8:30,8:45,Emails reading,Communication,
            2015-05-25,8:45,9:00,Planning,Administration,
            2015-05-25,9:00,9:55,Work,Job,
            2015-05-25,9:55,10:00,"Work-stand-up, move, relax",Job,
            ...
            ...
            2015-05-25,19:00,20:00,Continuing education,Professional advancement,
            2015-05-25,20:00,20:30,,Socialize,
            2015-05-25,20:30,21:00,Prepare meal,Meals,
            2015-05-25,21:00,21:15,Dishes,Maintenance,
            2015-05-25,21:15,21:30,,Cleaning,
            2015-05-25,21:30,21:45,Walk,Exercises,
            2015-05-25,21:45,22:00,,Personal Hygiene,
         */
        String csvDate = null;
        String csvStartTime = null;
        String csvEndTime = null;
        String csvActivity = null;
        String csvActivityGroup = null;
        String csvActivityDescription = null;

        String logDateTimeStart = null;
        String logDateTimeEnd = null;
        int activityId = 0;
        int activityGroupId = 0;
        ActivityCombo activityCombo = null; // getActivityCombo(activityComboDescription);
        int lineCount = 0;
        int lineCountInvalid = 0;
        int lineSkips = 0;
        for (String[] csvRow : csvRows) {
            ++lineCount;
            if (csvRow.length <= 4) {
                lineSkips++;
                continue;
            }


            logDateTimeStart = DateAndTime.makeSqLiteDateStampString(
                    csvRow[CSV_ACTIVITY_LOG_DATE_COL_NUM],
                    csvRow[CSV_ACTIVITY_LOG_START_TIME_COL_NUM]);
            if (logDateTimeStart == null) {
                lineCountInvalid++;
                continue;
            }

            logDateTimeEnd = DateAndTime.makeSqLiteDateStampString(
                    csvRow[CSV_ACTIVITY_LOG_DATE_COL_NUM],
                    csvRow[CSV_ACTIVITY_LOG_END_TIME_COL_NUM]);
            if (logDateTimeEnd == null) {
                lineCountInvalid++;
                continue;
            }

            csvActivity = csvRow[CSV_ACTIVITY_LOG_ACTIVITY_COL_NUM];

            csvActivityGroup = "";
            if (csvRow.length > 4)
                csvActivityGroup = csvRow[CSV_ACTIVITY_LOG_ACTIVITY_GROUP_COL_NUM];

            csvActivityDescription = "";
            if (csvRow.length > 5)
                csvActivityDescription = csvRow[CSV_ACTIVITY_LOG_DESCRIPTION_COL_NUM];

            //@todo next: build up activity and activitygroup table records by using the getActivityCombo method. This must also update the new lists members

//            activityCombo = getActivityCombo(activityComboDescription);
        }
        Log.d("DBTools.importCSV", lineCount + " lines processed, " + lineCountInvalid + " lines invalid.");
    }

//        private ActivityCombo getActivityCombo(String activityComboDescription) {
//            ActivityCombo activityCombo = activityCombosHashMap.get(activityComboDescription);
//            int activityId = 0;
//            int activityGroupId = 0;
//
//            if (activityCombo == null) {
//                String[] activityAndActivityGroup = parseActivityGroupDescription(activityComboDescription);
//                activityId = dbTools.getIdFromName(DBTools.ACTIVITY_TABLE_NAME, DBTools.ACTIVITY_TABLE_FIELD_NAMED_ACTIVITY_NAME, activityAndActivityGroup[0]);
//                if (activityId == 0) {
//                    activityId = dbTools.insertListItemAndReturnId(DBTools.ACTIVITY_TABLE_NAME, DBTools.ACTIVITY_TABLE_FIELD_NAMED_ACTIVITY_NAME, activityAndActivityGroup[0]);
//                }
//
//                activityGroupId = dbTools.getIdFromName(DBTools.ACTIVITY_GROUP_TABLE_NAME, DBTools.ACTIVITY_GROUP_NAME_FIELD, activityAndActivityGroup[1]);
//                if (activityGroupId == 0) {
//                    activityGroupId = dbTools.insertListItemAndReturnId(DBTools.ACTIVITY_GROUP_TABLE_NAME, DBTools.ACTIVITY_GROUP_NAME_FIELD, activityAndActivityGroup[1]);
//                }
//            /*
//                    insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (1, 1)");
//
//             */
//                dbTools.insertActivityCombo(activityId, activityGroupId);
//                setupLists();
//                activityCombo = activityCombosHashMap.get(activityAndActivityGroup[0] + " / " + activityAndActivityGroup[1]);
//            }
//
//            return activityCombo;
//        }


    public long insertActivityLog(HashMap<String, String> queryValuesMap) {
        SQLiteDatabase database = this.getWritableDatabase();

        // Stores key value pairs being the column name and the data
        // ContentValues data type is needed because the database
        // requires its data type to be passed

        ContentValues values = new ContentValues();
        /*
        queryValuesMap.put("logDateTimeStart", startDateEditText.getText().toString() + " " + startTimeEditText.getText().toString());
        queryValuesMap.put("logDateTimeEnd", endDateEditText.getText().toString() + " " + startTimeEditText.getText().toString());
        queryValuesMap.put("activityId", Integer.toString(activityId));
        queryValuesMap.put("activityGroupId", Integer.toString(activityGroupId));
        queryValuesMap.put("activityDescription", activityAndGroupAutoCompleteTextView.getText().toString());

         */

        values.put("logDateTimeStart", queryValuesMap.get("logDateTimeStart"));
        values.put("logDateTimeEnd", queryValuesMap.get("logDateTimeEnd"));
        values.put("activityId", queryValuesMap.get("activityId"));
        values.put("activityGroupId", queryValuesMap.get("activityGroupId"));
        values.put("activityDescription", queryValuesMap.get("activityDescription"));

        // Inserts the data in the form of ContentValues into the
        // table name provided

        long lastIdInserted = database.insert("activitylog", null, values);
        Log.d("MySimpleActivityTracker", "New activity logged: " + values.toString());

        // Release the reference to the SQLiteDatabase object

        database.close();
        return lastIdInserted;

    }

//    public ArrayList<HashMap<String, String>> getAllActivityCombos() {
//        ArrayList<HashMap<String, String>> list = new ArrayList<>();
//
//        // ArrayList that contains every row in the database
//        // and each row key / value stored in a HashMap
//
//
//        /*
//        createCommands.add("CREATE TABLE activitycombo (" +
//                "activityComboId INTEGER PRIMARY KEY, " +
//                "activityId INTEGER, " +
//                "activityGroupId INTEGER)");
//        */
//        String selectQuery =
//                "SELECT " +
//                        "ac.activityComboId, ac.activityId, ac.activityGroupId, a.activityName, " +
//                        "g.activityGroupName " +
//                        "FROM activitycombo ac " +
//                        "INNER JOIN activity a ON ac.activityId = a.activityId " +
//                        "INNER JOIN activitygroup g ON ac.activityGroupId = g.activityGroupId " +
//                        "ORDER BY a.activityName, g.activityGroupName";
//
//        // Open a database for reading and writing
//
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        // Cursor provides read and write access for the
//        // data returned from a database query
//
//        // rawQuery executes the query and returns the result as a Cursor
//
//        Cursor cursor = database.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                HashMap<String, String> item = new HashMap<String, String>();
//
//                // Store the key / value pairs in a HashMap
//                // Access the Cursor data by index that is in the same order
//                // as used when creating the table
//                /*
//                l.logId, l.logDateTimeStart, l.logDateTimeEnd, l.activityId, " +
//                "l.activityGroupId, l.activityDescription, a.activityName, g.activityGroupName
//                 */
//                item.put("activityComboId", cursor.getString(0));
//                item.put("activityId", cursor.getString(1));
//                item.put("activityGroupId", cursor.getString(2));
//                item.put("activityName", cursor.getString(3));
//                item.put("activityGroupName", cursor.getString(4));
//
//                list.add(item);
//            } while (cursor.moveToNext());
//        }
//
//        return list;
//    }


    public void makeActivityComboArrayList() {
        activityComboArrayList = new ArrayList<>();
        ActivityCombo activityCombo = null;

        String selectQuery =
                "SELECT " +
                        "ac.activityComboId, ac.activityId, ac.activityGroupId, a.activityName, " +
                        "g.activityGroupName " +
                        "FROM activitycombo ac " +
                        "INNER JOIN activity a ON ac.activityId = a.activityId " +
                        "INNER JOIN activitygroup g ON ac.activityGroupId = g.activityGroupId " +
                        "ORDER BY a.activityName, g.activityGroupName";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                activityCombo = new ActivityCombo(
                        cursor.getInt(ACTIVITY_COMBO_ID_COLUMN),
                        cursor.getInt(ACTIVITY_COMBO_ACTIVITY_ID_COLUMN),
                        cursor.getInt(ACTIVITY_COMBO_ACTIVITY_GROUP_ID_COLUMN),
                        cursor.getString(ACTIVITY_COMBO_ACTIVITY_NAME_COLUMN),
                        cursor.getString(ACTIVITY_COMBO_ACTIVITY_GROUP_NAME_COLUMN));
                activityComboArrayList.add(activityCombo);
            } while (cursor.moveToNext());
        }
        Log.d("TestDBApp", "ActivityCombos: " + activityComboArrayList.size());
        for (ActivityCombo activityCombo1 : activityComboArrayList) {

            Log.d("TestDBApp", "Combo with id " + activityCombo1.getActivityComboId() + " is " + activityCombo1.toString());
        }
    }

//    public Activity getActivityByName(String activityName) {
//        return null;
//    }

    /**
     *
     * @param tableName of the table to query
     * @param stringFieldName of the string key field of this table
     * @param entityName the string value of that field we are looking for
     * @return int id (autoinc field value)
     */
    public int getIdFromName(String tableName, String stringFieldName, String entityName) {

        SQLiteDatabase database = this.getReadableDatabase();
//        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + tableName + " where " + stringFieldName + "=?";
        Cursor cursor = database.rawQuery(selectQuery, new String[]{entityName});
        ArrayList<Integer> ids = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                ids.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        //@todo: in the cases where we expect only one, and there is more, we might do some warning or log... because we have data discrepencies
        return ids.size() > 0 ? ids.get(0) : 0;
    }

    /**
     *
     * @param tableName
     * @param stringFieldName: the name of the key field to search for
     * @param fieldValue: the value to search for
     * @return the id of the newly created list item.
     */
    public int insertListItemAndReturnId(String tableName, String stringFieldName, String fieldValue) {
        /*
        insertCommands.add("INSERT INTO activity (activityName) values('Entretien')");
        insertCommands.add("INSERT INTO activitygroup (activityGroupName) values('Activités spirituelles')");

        insertCommands.add("INSERT INTO activitycombo (activityId, activityGroupId) VALUES (1, 1)");
         */
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("INSERT INTO " + tableName + " (" + stringFieldName +
                ") values('" + fieldValue + "')");
        return getIdFromName(tableName, stringFieldName, fieldValue);
    }

    public void insertActivityCombo(int activityId, int activityGroupId) {
        SQLiteDatabase database = this.getWritableDatabase();

        String sql = "INSERT INTO " + ACTIVITY_COMBO_TABLE_NAME
                + " (" + ACTIVITY_COMBO_ACTIVITY_ID_FIELD
                + ", " + ACTIVITY_COMBO_ACTIVITY_GROUP_ID_FIELD
                + ") VALUES ( " + activityId + ", " + activityGroupId + ")";
        database.execSQL(sql);
    }

//    public void endEarlierActivity(long lastIdInserted, String latestLogDateTimeStart) {
//        //Find the activity earlier to this one that has no end time
//
//        long idBeforeLastIdInserted = lastIdInserted - 1;
//        //We want the log entry just before the last one, only if its end time is not already filled:
//        //@todo: and also if it is reasonable to end! Don't end the earlier activity if it is more than n hours...
//        String selectStatement = "select logDateTimeEnd from activitylog " +
//                "WHERE logId = " + idBeforeLastIdInserted +
//                " AND LENGTH(TRIM(logDateTimeEnd))<11 " +
//                "LIMIT 1";
//
//        SQLiteDatabase database = this.getWritableDatabase();
//        Cursor cursor = database.rawQuery(selectStatement, null);
//        HashMap<String, String> logEntry = new HashMap<String, String>();
//        String logDateTimeEnd = null;
//
//        if ( cursor != null && cursor.moveToFirst()) {
//            logDateTimeEnd = cursor.getString(0);
//            //@todo: check if we need to split this activity log entry (if it spans on 2 days)
//            String updateStatement = "update activityLog SET logDateTimeEnd = '" +
//                    latestLogDateTimeStart + "' WHERE logId = " + idBeforeLastIdInserted;
//            database.execSQL(updateStatement);
//            Log.d("TestDBApp", "Earlier activity ended at: "+latestLogDateTimeStart);
//        }
//        database.close();
//    }

    public ArrayList<HashMap<String, String>> getTodayActivityLog() {
        ArrayList<HashMap<String, String>> activityLogArrayList;

        activityLogArrayList = new ArrayList<HashMap<String, String>>();
        String[] fields = {
                "l.logId, "
        };
        /*
        ACTIVITY_LOG_KEY_MAP
                    "logId",
            "startDate",
            "startTime",
            "endTime",
            "activityId",
            "activityGroupId",
            "activityDescription",
            "activityName",
            "activityGroupName"

         */
        String selectQuery = "SELECT l.logId, " +
                "SUBSTR(l.logDateTimeStart, 1, 10) 'startDate', " +
                "SUBSTR(l.logDateTimeStart, 12, 8) 'startTime', " +
                "SUBSTR(l.logDateTimeEnd, 12, 8) 'endTime', " +
                "l.activityId, " +
                "l.activityGroupId, " +
                "l.activityDescription, " +
                "a.activityName, " +
                "g.activityGroupName " +
                "FROM activitylog l " +
                "INNER JOIN activity a ON l.activityId = a.activityId " +
                "INNER JOIN activitygroup g ON l.activityGroupId = g.activityGroupId " +
                "WHERE startDate = '" + DateAndTime.today() + "' " +
                "ORDER BY startTime";

        // Open a database for reading and writing

//        SQLiteDatabase database = this.getWritableDatabase();
        SQLiteDatabase database = this.getReadableDatabase();

        // Cursor provides read and write access for the
        // data returned from a database query

        // rawQuery executes the query and returns the result as a Cursor

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> logEntry = new HashMap<String, String>();

                // Store the key / value pairs in a HashMap
                // Access the Cursor data by index that is in the same order
                // as used when creating the table
                /*
                l.logId, l.logDateTimeStart, l.logDateTimeEnd, l.activityId, " +
                "l.activityGroupId, l.activityDescription, a.activityName, g.activityGroupName
                 */
                for (int i = 0; i < ACTIVITY_LOG_KEY_MAP.length; i++) {
                    logEntry.put(ACTIVITY_LOG_KEY_MAP[i], cursor.getString(i));
//                    logEntry.put("logId", cursor.getString(0));
//                    logEntry.put("logStartDate", cursor.getString(1));
//                    logEntry.put("logStartTime", cursor.getString(2));
//                    logEntry.put("endTime", cursor.getString(3));
//                    logEntry.put("activityId", cursor.getString(4));
//                    logEntry.put("activityGroupId", cursor.getString(5));
//                    logEntry.put("activityDescription", cursor.getString(6));
//                    logEntry.put("activityName", cursor.getString(7));
//                    logEntry.put("activityGroupName", cursor.getString(8));

                }

                activityLogArrayList.add(logEntry);
            } while (cursor.moveToNext());
        }

        return activityLogArrayList;
    }

    public ArrayList<HashMap<String, String>> getActivityLog(String dateString) {
        ArrayList<HashMap<String, String>> activityLogArrayList;

        activityLogArrayList = new ArrayList<HashMap<String, String>>();
        String[] fields = {"l.logId, "};
        String selectQuery = "SELECT l.logId, " +
                "SUBSTR(l.logDateTimeStart, 1, 10) 'startDate', " +
                "SUBSTR(l.logDateTimeStart, 12, 8) 'startTime', " +
                "SUBSTR(l.logDateTimeEnd, 12, 8) 'endTime', " +
                "l.activityId, " +
                "l.activityGroupId, " +
                "l.activityDescription, " +
                "a.activityName, " +
                "g.activityGroupName " +
                "FROM activitylog l " +
                "INNER JOIN activity a ON l.activityId = a.activityId " +
                "INNER JOIN activitygroup g ON l.activityGroupId = g.activityGroupId " +
                "WHERE startDate = '" + dateString + "' " +
                "ORDER BY startTime";

        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> logEntry = new HashMap<String, String>();

                for (int i = 0; i < ACTIVITY_LOG_KEY_MAP.length; i++) {
                    logEntry.put(ACTIVITY_LOG_KEY_MAP[i], cursor.getString(i));
                }

                activityLogArrayList.add(logEntry);
            } while (cursor.moveToNext());
        }

        return activityLogArrayList;
    }

//    public HashMap<String, String> getLogEntry(String logId) {
//        HashMap<String, String> logEntry = new HashMap<String, String>();
//        //Contrary to the getAllActivityLog and getTodayActivityLog methods which is aiming to
//        // display a list of formatted log entries in the main view, this is aimed a providing the
//        // raw data for being edited.
//        String selectQuery = "SELECT l.logId, l.logDateTimeStart, l.logDateTimeEnd, l.activityId, " +
//                "l.activityGroupId, l.activityDescription, a.activityName, g.activityGroupName " +
//                "FROM activitylog l " +
//                "INNER JOIN activity a ON l.activityId = a.activityId " +
//                "INNER JOIN activitygroup g ON l.activityGroupId = g.activityGroupId " +
//                "WHERE logId = " + logId;
//
//        SQLiteDatabase database = this.getReadableDatabase();
//        Cursor cursor = database.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                logEntry.put("logId", cursor.getString(0));
//                logEntry.put("logDateTimeStart", cursor.getString(1));
//                logEntry.put("logDateTimeEnd", cursor.getString(2));
//                logEntry.put("activityId", cursor.getString(3));
//                logEntry.put("activityGroupId", cursor.getString(4));
//                logEntry.put("activityDescription", cursor.getString(5));
//                logEntry.put("activityName", cursor.getString(6));
//                logEntry.put("activityGroupName", cursor.getString(7));
//
//            } while (cursor.moveToNext());
//        }
//
//        return logEntry;
//    }

    /**
     *
     * @param activityId
     * @return
     */
    public String getActivityName(String activityId) {
        String selectQuery = "SELECT * FROM " + ACTIVITY_TABLE_NAME + " where " + ACTIVITY_TABLE_FIELD_NAMED_ACTIVITY_ID + "=?";
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, new String[]{activityId});
        ArrayList<String> activities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                activities.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        //@todo: in the cases where we expect only one, and there is more, we might do some warning or log... because we have data discrepencies
        return activities.size() > 0 ? activities.get(0) : "";
    }

    public String getActivityGroupName(String activityGroupId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ACTIVITY_GROUP_TABLE_NAME + " where " + ACTIVITY_GROUP_ID_FIELD + "=?";
        Cursor cursor = database.rawQuery(selectQuery, new String[]{activityGroupId});
        ArrayList<String> activities = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                activities.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        //@todo: in the cases where we expect only one, and there is more, we might do some warning or log... because we have data discrepencies
        return activities.size() > 0 ? activities.get(0) : "";
    }

//    public void updateActivityLog(String logId, HashMap<String, String> logEntry) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues values = TestDBAppUtility.getActivityLogContentValues(logEntry);
//        database.update(ACTIVITY_LOG_TABLE_NAME, values, ACTIVITY_LOG_LOG_ID + " = ?", new String[] {logId});
//        database.close();
//    }

//    public void deleteLogEntry(String logId) {
//        SQLiteDatabase database = this.getWritableDatabase();
//        database.delete(ACTIVITY_LOG_TABLE_NAME, ACTIVITY_LOG_LOG_ID + " = ?", new String[] {logId});
//        database.close();
//    }
}