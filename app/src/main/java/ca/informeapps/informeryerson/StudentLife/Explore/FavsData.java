/*
 * Copyright (c) 2014. Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose is not granted without permission from owner, for more information please contact informeapplications@gmail.com
 */

package ca.informeapps.informeryerson.StudentLife.Explore;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * Created by Shahar on 2014-08-27.
 */
public class FavsData {

    Context context;
    String data[][];

    FavsData(Context context,String data[][])
    {
        this.context=context;
        this.data=data;
    }



    public void writeToFile()throws IOException
    {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("favData.txt", Context.MODE_PRIVATE));
        for (int x=0; x<data.length; x++)
        {
            for(int z=0; z<data[0].length; z++)
            {
                outputStreamWriter.write(data[x][z]+"  ");
            }
            String separator = System.getProperty("line.separator");
            outputStreamWriter.write(separator);
        }

        outputStreamWriter.close();
    }

    public String[][] readFile() throws IOException
    {
        Vector<String> soup = new Vector<String>();
        InputStream inputStream = context.openFileInput("favData.txt");
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String l;
            while (( l = bufferedReader.readLine()) != null && !((l=bufferedReader.readLine()).equals("null null"))) {

                soup.add(bufferedReader.readLine());
            }
        }
        String[][] flying= new String[soup.size()][2];
        for(int x=0;x<soup.size();x++)
        {
            flying[x][0]= soup.get(x).split("  ")[0];

        }
        return flying;
    }

}
