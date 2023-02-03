package com.archql.notebad;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SQLiteStorage<T extends SQLitePlaceable> {
    private SQLiteStorageHelper helper;
    private Context context;
    private SQLiteDatabase database;
    private String dbName;
    private String tableName;
    private String tableId;
    private String[] columns;

    private Supplier<T> supplier;

    public SQLiteStorage(Context context, String storedObjectName, String[] columns, Supplier<T> supplier) {
        this.context = context;

        storedObjectName = storedObjectName.toLowerCase();
        dbName = storedObjectName + ".db";
        tableName = storedObjectName + 's';
        tableId = storedObjectName + "_id";

        this.columns = new String[columns.length + 1];
        this.columns[0] = tableName;
        System.arraycopy(columns, 0, this.columns, 1, columns.length);
    }

    public SQLiteStorage<T> open() /*throws SQLException*/ {
        helper = new SQLiteStorageHelper(context, dbName, tableName, columns);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public void insert(T o) {
        database.insert(tableName, null, ((SQLitePlaceable)o).generateContentValue());
    }
    public List<T> fetch() {
        Cursor cursor = database.query(tableName, columns, null, null, null, null, null);
        if (cursor != null) {
            // unpack cursor
            List<T> objects = new ArrayList<>();
            while (cursor.moveToNext()) {
                try {
                    T read = supplier.get();
                    read.parseFromCursor(cursor);
                    objects.add(read);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // close
            cursor.close();

            return objects;
        }
        return null;
    }

    public int update(long _id, T o) {
        //if (!o.getClass().equals(aClass))
        return database.update(tableName, o.generateContentValue(), tableId + " = " + _id, null);
    }

    public void delete(long _id) {
        database.delete(tableName, tableId + "=" + _id, null);
    }
}

/*
// generate content value through reflection
        for (Field f : getDeclaredFields()) {
            String value = "";
            try {
                value = Objects.requireNonNull(f.get(o)).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            contentValue.put(f.getName(), value);
        }
// read it back (not working)
try {
    Object read = aClass.newInstance();
    Field[] fields = aClass.getDeclaredFields();
    for (int i = 1; i < cursor.getColumnCount(); i++) {
        fields[i-1].set(read, cursor.getString(i));
    }
    objects.add(read);

} catch (IllegalAccessException e) {
    e.printStackTrace();
} catch (InstantiationException e) {
    e.printStackTrace();
}
 */
