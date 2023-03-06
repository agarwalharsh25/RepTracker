package com.reptracker.android.utilities;

import android.content.Context;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.reptracker.android.models.PendingVisit;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class Utility {

    public enum VisitStatus {
        PENDING,
        COMPLETED
    }

    public void setTextView(TextView textView, String text) {
        if (text == null || text.equals(""))
            textView.setVisibility(View.GONE);
        else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(Html.fromHtml(text));
        }
    }

    public void saveVisitTimeStamp(PendingVisit pendingVisit, String column, Context context) {
        Date date = new Date();
        long timeMilli = date.getTime();
        DBHandler db = new DBHandler(context);
        for (PendingVisit.Visits visit: pendingVisit.getVisits()) {
            db.saveVisitTimestamp(visit.getVisitId(), column, String.valueOf(timeMilli));
        }
    }

    public void saveVisitTimeStamp(String visitId, String column, Context context) {
        Date date = new Date();
        long timeMilli = date.getTime();
        DBHandler db = new DBHandler(context);
        db.saveVisitTimestamp(visitId, column, String.valueOf(timeMilli));
    }

    public String createDirectory(String folderName) {
        String folderPath = Environment.getExternalStorageDirectory() + "/RepTracker/" + folderName + "/";
        File folder = new File(folderPath);

        if (!folder.exists()) {
            File directory = new File(folderPath);
            directory.mkdirs();
        }

        return folderPath;
    }

    public String createDirectoryAndSaveFile(byte[] data, String folderName, String fileName) {
        String absolutePath = "";

        String folderPath = createDirectory(folderName);

        File file = new File(folderPath, fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(data);
            outStream.close();
            absolutePath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return absolutePath;
    }

}
