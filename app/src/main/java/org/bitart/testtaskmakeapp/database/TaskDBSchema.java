package org.bitart.testtaskmakeapp.database;

public class TaskDBSchema {
    public static final class TaskTable {
        public static final String NAME = "tasks";

        public static final class Columns {
            public static final String HEADER = "header";
            public static final String BODY = "body";
            public static final String PRIORITY = "priority";
            public static final String UUID = "uuid";
            public static final String DATE = "date";
        }
    }
}