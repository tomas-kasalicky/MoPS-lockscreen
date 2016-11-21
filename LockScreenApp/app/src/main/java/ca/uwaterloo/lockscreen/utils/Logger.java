package ca.uwaterloo.lockscreen.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kasal on 21.11.2016.
 */

public class Logger {
    public static void log(Context ctx, String message){
        String folderName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File folder = new File(folderName);
        folder.mkdirs();
        String fn = folderName + "/MoPS_log.txt";
        File logFile = new File(fn);

        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
            }
        }
        try
        {
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(message);
            buf.newLine();
            buf.flush();
            buf.close();
        }
        catch (IOException e)
        {
        }
    }
}
