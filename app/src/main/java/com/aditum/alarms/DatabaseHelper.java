package com.aditum.alarms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import lombok.val;

//**************************************************************
public final class DatabaseHelper
        extends SQLiteOpenHelper
//**************************************************************
{

    private static final String DATABASE_NAME = "alarms.db";
    private static final int SCHEMA = 1;

    private static final String TABLE_NAME = "alarms";

    public static final String _ID = "_id";
    public static final String COL_TIME = "time";
    public static final String COL_LABEL = "label";
    public static final String COL_MON = "mon";
    public static final String COL_TUES = "tues";
    public static final String COL_WED = "wed";
    public static final String COL_THURS = "thurs";
    public static final String COL_FRI = "fri";
    public static final String COL_SAT = "sat";
    public static final String COL_SUN = "sun";
    public static final String COL_INTAKE = "intake";
    public static final String COL_FIRED = "fired";

    public static final String COL_IS_ENABLED = "is_enabled";

    private static DatabaseHelper sInstance = null;


    //**************************************************************
    public static synchronized DatabaseHelper getInstance(Context context)
    //**************************************************************
    {
        if (sInstance == null)
        {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    //**************************************************************
    private DatabaseHelper(Context context)
    //**************************************************************
    {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    //**************************************************************
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    //**************************************************************
    {

        Log.i(getClass().getSimpleName(), "Creating database...");

        final String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIME + " INTEGER NOT NULL, " +
                COL_LABEL + " TEXT, " +
                COL_MON + " INTEGER NOT NULL, " +
                COL_TUES + " INTEGER NOT NULL, " +
                COL_WED + " INTEGER NOT NULL, " +
                COL_THURS + " INTEGER NOT NULL, " +
                COL_FRI + " INTEGER NOT NULL, " +
                COL_SAT + " INTEGER NOT NULL, " +
                COL_SUN + " INTEGER NOT NULL, " +
                COL_IS_ENABLED + " INTEGER NOT NULL," +
                COL_INTAKE + " INTEGER NOT NULL, " +
                COL_FIRED + " INTEGER NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(CREATE_ALARMS_TABLE);

    }

    //**************************************************************
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    //**************************************************************
    {
        throw new UnsupportedOperationException("This shouldn't happen yet!");
    }

    //**************************************************************
    public long addAlarm()
    //**************************************************************
    {
        return addAlarm(new Alarm());
    }

    //**************************************************************
    long addAlarm(Alarm alarm)
    //**************************************************************
    {
        return getWritableDatabase().insert(TABLE_NAME, null, AlarmUtils.toContentValues(alarm));
    }

    //**************************************************************
    public int updateAlarm(Alarm alarm)
    //**************************************************************
    {
        val time =  alarm.getTime();
        if (time <= System.currentTimeMillis())
            return 0;

        final String where = _ID + "=?";
        final String[] whereArgs = new String[] { Long.toString(alarm.getId()) };
        return getWritableDatabase()
                .update(TABLE_NAME, AlarmUtils.toContentValues(alarm), where, whereArgs);
    }

    //**************************************************************
    public int deleteAlarm(Alarm alarm)
    //**************************************************************
    {
        return deleteAlarm(alarm.getId());
    }

    //**************************************************************
    int deleteAlarm(long id)
    //**************************************************************
    {
        final String where = _ID + "=?";
        final String[] whereArgs = new String[] { Long.toString(id) };
        return getWritableDatabase().delete(TABLE_NAME, where, whereArgs);
    }

    //**************************************************************
    public List<Alarm> getAlarms()
    //**************************************************************
    {

        Cursor c = null;
        try
        {
            c = getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, "time asc");
            return AlarmUtils.buildAlarmList(c);
        }
        finally
        {
            if (c != null && !c.isClosed()) c.close();
        }

    }

    //**************************************************************
    public List<Alarm> getMedicineByDate(long startDate, long endDate)
    //**************************************************************
    {

        Cursor c = null;
        try
        {
            c = getReadableDatabase().query(TABLE_NAME, null,
                                            "time>=" + startDate + " and " + "time<=" + endDate,
                                            null, null, null, "time asc");
            return AlarmUtils.buildAlarmList(c);
        }
        finally
        {
            if (c != null && !c.isClosed()) c.close();
        }

    }

}
