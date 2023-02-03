package com.archql.notebad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteStorageHelper extends SQLiteOpenHelper {

    // database version
    static final int DB_VERSION = 3;

    private final String createTable;
    private final String dropTable;

    public SQLiteStorageHelper(Context context, String name, String tableName, String[] columns) {
        super(context, name, null, DB_VERSION);

        String createTable1 = "create table " + tableName + "(" + columns[0] + " INTEGER PRIMARY KEY AUTOINCREMENT";
        dropTable = "DROP TABLE IF EXISTS " + tableName;

        for (int i = 1; i < columns.length; i++) {
            createTable1 += ", " + columns[i] + " TEXT";
        }
        createTable1 += ")";
        createTable = createTable1;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropTable);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
