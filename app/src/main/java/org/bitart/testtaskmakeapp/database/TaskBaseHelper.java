package org.bitart.testtaskmakeapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "taskDatabase.db";

    public TaskBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TaskDBSchema.TaskTable.NAME +
                "(" +
                " _id integer primary key autoincrement, " +
                TaskDBSchema.TaskTable.Columns.HEADER + ", " +
                TaskDBSchema.TaskTable.Columns.BODY + ", " +
                TaskDBSchema.TaskTable.Columns.UUID + ", " +
                TaskDBSchema.TaskTable.Columns.PRIORITY
                + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
