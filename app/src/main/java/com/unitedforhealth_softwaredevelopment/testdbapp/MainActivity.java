package com.unitedforhealth_softwaredevelopment.testdbapp;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    public static final int REQUEST_CODE = 1;
    DBTools dbTools = new DBTools(this);
    private EditText dataDisplayEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataDisplayEditText = (EditText) this.findViewById(R.id.dataDisplayEditText);
        dataDisplayEditText.setText("No data published yet!");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(MainActivity.this, "Data is null", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(MainActivity.this, "onActivityResult(requestCode=" + requestCode + ", resultCode=" + resultCode + ", Intent data", Toast.LENGTH_LONG).show();

        switch (requestCode) {
            case REQUEST_CODE:
                String filepath = data.getData().getPath();
                Toast.makeText(MainActivity.this, "filepath is >" + filepath + "<", Toast.LENGTH_LONG).show();
//                String fileName = data.getData().
                dbTools = new DBTools(getApplicationContext());
                SQLiteDatabase db = dbTools.getWritableDatabase();
                String tableName = DBTools.ACTIVITY_LOG_TABLE_NAME;
//                db.execSQL("delete from " + tableName);
                try {
                    if (resultCode == RESULT_OK) {
                        try {

                            FileReader file = new FileReader(filepath);

                            BufferedReader buffer = new BufferedReader(file);
                            ContentValues contentValues = new ContentValues();
                            String line = "";
//                            db.beginTransaction();
                            int lineCount = 0;
                            while ((line = buffer.readLine()) != null) {
                                lineCount++;
//                                String[] str = line.split(",", 3);  // defining 3 columns with null or blank field //values acceptance
//                                //Id, Company,Name,Price
//                                String company = str[0].toString();
//                                String Name = str[1].toString();
//                                String Price = str[2].toString();
//
//
//                                contentValues.put("Company", company);
//                                contentValues.put("Name", Name);
//                                contentValues.put("Price", Price);
//                                db.insert(tableName, null, contentValues);
//                                lbl.setText("Successfully Updated Database.");
                            }
                            Toast.makeText(MainActivity.this, lineCount + " lines were read from the file", Toast.LENGTH_LONG).show();
//                            db.setTransactionSuccessful();
//                            db.endTransaction();
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Caught IOException " + e.getMessage().toString(), Toast.LENGTH_LONG).show();

//                            if (db.inTransaction())
//                                db.endTransaction();
//                            Dialog d = new Dialog(this);
//                            d.setTitle(e.getMessage().toString() + "first");
//                            d.show();
                            // db.endTransaction();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Result from reading in file NOT OK!", Toast.LENGTH_LONG).show();

//                        if (db.inTransaction())
//                            db.endTransaction();
//                        Dialog d = new Dialog(this);
//                        d.setTitle("Only CSV files allowed");
//                        d.show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Caught Exception " + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
//                    if (db.inTransaction())
//                        db.endTransaction();
//
//                    Dialog d = new Dialog(this);
//                    d.setTitle(ex.getMessage().toString() + "second");
//                    d.show();
                    // db.endTransaction();
                }
        }
//        myList= dbTools.getAllProducts();

//        if (myList.size() != 0) {
//            ListView lv = getListView();
//            ListAdapter adapter = new SimpleAdapter(MainActivity.this, myList,
//                    R.layout.v, new String[]{"Company", "Name", "Price"}, new int[]{
//                    R.id.txtproductcompany, R.id.txtproductname, R.id.txtproductprice});
//            setListAdapter(adapter);
//            lbl.setText("Data Imported");
//        }
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


    /**
     * This will export the Activity Journal Database Tables Data:
     * ActivityLog
     * Activity
     * ActivityGroup
     * ActivityCombo
     * <p/>
     * To CSV files
     *
     * @param item
     */
    public void menuDBExportClick(MenuItem item) {
        //For a proof of concept, export Activity table:
        ArrayList<HashMap<String, String>> activities = dbTools.getActivities();
        StringBuilder sb = new StringBuilder();
        for (HashMap<String, String> activity : activities) {
            sb.append(activity.get("activityId")).append(activity.get("activityName")).append("\n");
        }
        Toast.makeText(MainActivity.this, "Acitivities:\n" + sb.toString(), Toast.LENGTH_LONG).show();

    }

    public void menuDBMakeTestDataClick(MenuItem item) {
        dbTools.makeTestData();
        Toast.makeText(MainActivity.this, "Test data has been created.", Toast.LENGTH_LONG).show();

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
                            dbTools = new DBTools(MainActivity.this);

                        } else {
                            Toast.makeText(MainActivity.this, "Unable to delete " + db, Toast.LENGTH_LONG).show();

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
        ArrayList<HashMap<String, String>> activities = dbTools.getActivities();
        StringBuilder sb = new StringBuilder();
        if (activities.size() > 0) {
            for (HashMap<String, String> activity : activities) {
                sb.append(activity.get("activityId")).append(": ").append(activity.get("activityName")).append("\n");
            }

        } else {
            sb.append("No activities to list!");
        }

        dataDisplayEditText.setText(sb.toString());

    }

    public void menuDBImportClick(MenuItem item) {
        //Originally tried to get the csv file from user picking it
        // from downloads folder. Got the dialog, got some path, but never with
        // a file!
        //readCSVFilePickedFromActivityResult();
        //Sample of how to read lines from a file:
//        readLinesInFile();
        CSVReader csvReader = new CSVReader(getResources().openRawResource(R.raw.adayoftestdata));
        dbTools.importCSVDataIntoActivityLog(csvReader);
//        List<String[]> csvRows = csvReader.read();
//        StringBuilder sb = new StringBuilder();
//        /*
//        Date,Start,End,Activity,Group,Description
//         */
//        sb.append("Date\t").append("Start\t").append("End\t").append("Activity\t").append("Description\n");
//        for (String[] csvRow : csvRows){
//            sb.append(csvRow[0]+"\t\t").append(csvRow[1] + "\t\t").append(csvRow[2]+"\t\t").append(csvRow[3]+"\t").append(csvRow[4]+"\n");
//        }
        Toast.makeText(MainActivity.this, "Import CSV Done!", Toast.LENGTH_LONG).show();


    }

    private void readCSVFilePickedFromActivityResult() {
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
//---fileIntent.setType("gagt/sdf");
        fileIntent.setType("text/csv");
        try {
            startActivityForResult(fileIntent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, "No activity can handle picking a file. Showing alternatives.", Toast.LENGTH_LONG).show();
        }
    }

    private void readLinesInFile() {
        boolean isBypass = false;

        String line = "";
        StringBuilder sb = new StringBuilder();
        InputStream is = getResources().openRawResource(R.raw.adayoftestdata);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (sb.length() > 0) {
            isBypass = true;
            Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(MainActivity.this, "File not read!", Toast.LENGTH_LONG).show();
        }
    }
}
