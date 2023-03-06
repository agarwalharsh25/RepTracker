package com.reptracker.android.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reptracker.android.models.Visit;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reptracker";

    private static final String TABLE_VISITS = "visits";
    private static final String COLUMN_VISIT_ID = "visitId";
    public static final String COLUMN_VISIT_STARTED = "visitStarted";
    public static final String COLUMN_REACHED_HOSPITAL = "reachedHospital";
    public static final String COLUMN_PRESENTATION_STARTED = "presentationStarted";
    public static final String COLUMN_PRESENTATION_FINISHED = "presentationFinished";
    public static final String COLUMN_VISIT_FINISHED = "visitFinished";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VISITS_TABLE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_VISITS + "("
                + COLUMN_VISIT_ID + " TEXT PRIMARY KEY, "
                + COLUMN_VISIT_STARTED + " TEXT, "
                + COLUMN_REACHED_HOSPITAL + " TEXT, "
                + COLUMN_PRESENTATION_STARTED + " TEXT, "
                + COLUMN_PRESENTATION_FINISHED + " TEXT, "
                + COLUMN_VISIT_FINISHED + " TEXT" + ")";

        db.execSQL(CREATE_VISITS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VISITS);

        onCreate(db);
    }

    private boolean checkIfVisitPresent(String visitId) {
        boolean found = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        String sql = "SELECT " + COLUMN_VISIT_ID + " FROM " + TABLE_VISITS + " WHERE " + COLUMN_VISIT_ID + " = '" + visitId + "'";
        cursor = db.rawQuery(sql,null);

        if(cursor.getCount() > 0){
            found = true;
        }

        cursor.close();
        db.close();

        return found;
    }

    // Adding
    private void addVisit(String visitId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_VISIT_ID, visitId);
        values.put(COLUMN_VISIT_STARTED, "");
        values.put(COLUMN_REACHED_HOSPITAL, "");
        values.put(COLUMN_PRESENTATION_STARTED, "");
        values.put(COLUMN_PRESENTATION_FINISHED, "");
        values.put(COLUMN_VISIT_FINISHED, "");

        db.insert(TABLE_VISITS, null, values);
        db.close();
    }

    // Saving
    void saveVisitTimestamp(String visitId, String column, String value) {
        if (!checkIfVisitPresent(visitId)) {
            addVisit(visitId);
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, value);

        // updating visit row
        db.update(TABLE_VISITS,
                values,
                COLUMN_VISIT_ID + " = ?",
                new String[] { visitId });
        db.close();
    }

    // Fetching
    public Visit fetchVisitData(String visitId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Visit visit = null;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_VISITS + " WHERE " + COLUMN_VISIT_ID + " = '" + visitId + "'"  , null);
        if (cursor != null)
        {
            if (cursor.moveToFirst() && cursor.getCount() == 1)
            {
                String visitStarted = cursor.getString(cursor.getColumnIndex(COLUMN_VISIT_STARTED));
                String reachedHospital = cursor.getString(cursor.getColumnIndex(COLUMN_REACHED_HOSPITAL));
                String presentationStarted = cursor.getString(cursor.getColumnIndex(COLUMN_PRESENTATION_STARTED));
                String presentationFinished = cursor.getString(cursor.getColumnIndex(COLUMN_PRESENTATION_FINISHED));
                String visitFinished = cursor.getString(cursor.getColumnIndex(COLUMN_VISIT_FINISHED));

                visit = new Visit(visitId, visitStarted, reachedHospital, presentationStarted, presentationFinished, visitFinished);
            }
            cursor.close();
        }
        db.close();

        return visit;
    }

    // Deleting
    public void deleteVisit(String visitId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VISITS, COLUMN_VISIT_ID + " = ?",
                new String[] { visitId });
        db.close();
    }
}
