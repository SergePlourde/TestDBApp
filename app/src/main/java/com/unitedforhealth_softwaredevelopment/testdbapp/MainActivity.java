package com.unitedforhealth_softwaredevelopment.testdbapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    private DBTools dbTools = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dbTools.rebuildDB();

        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void menuDBCreateClick(MenuItem item) {
        dbTools = getDBTools();
        Cursor cursor = dbTools.getAllDBTables();
        String dbName = dbTools.createDB();
        Toast.makeText(MainActivity.this, "Database has " + cursor.getCount() + " tables", Toast.LENGTH_LONG).show();

        if (dbName.length() == 0){
            Toast.makeText(MainActivity.this, "Database does not exists. If you want to re-create it, please delete it first", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Database " + dbName + " has been created successfully.", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * This will export the Activity Journal Database Tables Data:
     * ActivityLog
     * Activity
     * ActivityGroup
     * ActivityCombo
     *
     * To CSV files
     *
     * @param item
     */
    public void menuDBExportClick(MenuItem item) {
        //For a proof of concept, export Activity table:
        ArrayList<HashMap<String, String>> activities = dbTools.getActivities();
        StringBuilder sb = new StringBuilder();
        for (HashMap<String, String> activity : activities){
            sb.append(activity.get("activityId")).append(activity.get("activityName")).append("\n");
        }
        Toast.makeText(MainActivity.this, "Acitivities:\n"+sb.toString(), Toast.LENGTH_LONG).show();

    }

    public void menuDBMakeTestDataClick(MenuItem item) {
        dbTools.makeTestData();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void menuDBDeleteClick(MenuItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Do you really want to delete the database?")
                .setMessage(dbTools.getDatabaseName() + " will be deleted permanently")
                        //.setIcon???
                .setNegativeButton("NO!!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "The database was NOT deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("Yes, delete it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do the deletion!
                        File db = MainActivity.this.getApplicationContext().getDatabasePath(dbTools.getDatabaseName());
                        SQLiteDatabase sdb = dbTools.getWritableDatabase();
                        sdb.close();
                        dbTools.close();
                        dbTools = null;
                        boolean isDeleted = db.delete();
                        if (isDeleted) {
                            Toast.makeText(MainActivity.this, db + " is deleted.", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Unable to delete "+db, Toast.LENGTH_LONG).show();

                        }
                    }
                })
                        //@todo: add a delete icon (garbage can?!) .setIcon(whatever)
                .show();

    }

    public DBTools getDBTools() {
        return dbTools == null ? new DBTools(this) : dbTools;
    }

    public void showDataButtonClick(View view) {
        Toast.makeText(MainActivity.this,
                "Please be patient, this functionality is still under construction.",
                Toast.LENGTH_LONG).show();

    }
}
