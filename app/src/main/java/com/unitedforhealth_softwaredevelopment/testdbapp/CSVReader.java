package com.unitedforhealth_softwaredevelopment.testdbapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serge on 29/05/2015.
 */
public class CSVReader {
    InputStream inputStream;

    public CSVReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<String[]> read() {
        List<String[]> list = new ArrayList<String[]>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String csvLine;
        try {
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                list.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
