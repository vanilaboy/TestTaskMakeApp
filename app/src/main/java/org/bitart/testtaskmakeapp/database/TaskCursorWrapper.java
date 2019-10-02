package org.bitart.testtaskmakeapp.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.bitart.testtaskmakeapp.Task;

import java.util.UUID;

public class TaskCursorWrapper extends CursorWrapper {

    public TaskCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Task getTask() {
        String id = getString(getColumnIndex(TaskDBSchema.TaskTable.Columns.UUID));

        Task result = new Task(UUID.fromString(id));
        result.setHeader(getString(getColumnIndex(TaskDBSchema.TaskTable.Columns.HEADER)));
        result.setBody(getString(getColumnIndex(TaskDBSchema.TaskTable.Columns.BODY)));
        result.setPriority(getInt(getColumnIndex(TaskDBSchema.TaskTable.Columns.PRIORITY)));

        return result;
    }
}
