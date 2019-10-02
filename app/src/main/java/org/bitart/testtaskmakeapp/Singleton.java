package org.bitart.testtaskmakeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.bitart.testtaskmakeapp.database.TaskBaseHelper;
import org.bitart.testtaskmakeapp.database.TaskCursorWrapper;
import org.bitart.testtaskmakeapp.database.TaskDBSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Singleton {

    private static Singleton sSingleton;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private Singleton(Context context) {
        mDatabase = new TaskBaseHelper(context)
                .getWritableDatabase();
    }

    public static Singleton getInstance(Context context) {
        if(sSingleton == null) {
            sSingleton = new Singleton(context);
        }
        return sSingleton;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> crimes = new ArrayList<>();
        TaskCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getTask());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public void addTask(Task task) {
        ContentValues values = getContentValues(task);
        mDatabase.insert(TaskDBSchema.TaskTable.NAME, null, values);
    }

    public Task getTask(UUID id) {
        TaskCursorWrapper cursor = queryCrimes(
                TaskDBSchema.TaskTable.Columns.UUID + " = ?",
                new String[]{id.toString()}
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getTask();
        } finally {
            cursor.close();
        }
    }

    public void removeTask(Task task){
        mDatabase.delete(TaskDBSchema.TaskTable.NAME,
                TaskDBSchema.TaskTable.Columns.UUID + " = ?",
                new String[]{task.getId().toString()});
    }

    public void updateTask(Task task) {
        String uuidString = task.getId().toString();
        ContentValues values = getContentValues(task);
        mDatabase.update(TaskDBSchema.TaskTable.NAME, values,
                TaskDBSchema.TaskTable.Columns.UUID + " = ?",
                new String[]{uuidString});
    }

    private TaskCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TaskDBSchema.TaskTable.NAME,
                null,
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new TaskCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskDBSchema.TaskTable.Columns.HEADER, task.getHeader());
        values.put(TaskDBSchema.TaskTable.Columns.BODY, task.getBody());
        values.put(TaskDBSchema.TaskTable.Columns.PRIORITY, task.getPriority());
        values.put(TaskDBSchema.TaskTable.Columns.UUID, task.getId().toString());

        return values;
    }
}
