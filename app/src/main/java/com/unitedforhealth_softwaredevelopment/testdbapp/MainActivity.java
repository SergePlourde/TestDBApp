package com.unitedforhealth_softwaredevelopment.testdbapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    private DBTools dbTools = new DBTools(this);

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
        Toast.makeText(MainActivity.this, "The Activity Journal Database will be created, if it is not already created.", Toast.LENGTH_LONG).show();
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

    public void menuDBCreateTestDataClick(MenuItem item) {
        dbTools.makeTestData();
    }
}
