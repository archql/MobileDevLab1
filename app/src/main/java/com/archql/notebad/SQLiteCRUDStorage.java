package com.archql.notebad;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class SQLiteCRUDStorage implements ICRUDStorage<Note, StoredNote> {

    protected SQLiteStorage<StoredNote> sqliteStorage;
    protected static final STORAGE_TYPE storageType = STORAGE_TYPE.SQLite;

    SQLiteCRUDStorage(Context context, String storedObjectName) {
        sqliteStorage = new SQLiteStorage<>(context, storedObjectName, StoredNote.TABLE_NAMES, StoredNote::new);
        sqliteStorage.open();
    }

    public void close() {
        sqliteStorage.close();
    }

    @Override
    public boolean Create(StoredNote obj) {
        boolean success = true;
        try {
            obj.id = sqliteStorage.insert(obj); // update our id
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public StoredNote Read(long _id) {
        return null;
    }

    @Override
    public List<StoredNote> ReadAll() {
        List<StoredNote> result = new ArrayList<>();
        try {
            result = sqliteStorage.fetch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean Update(StoredNote newObj) {
        boolean success = true;
        try {
            sqliteStorage.update(newObj.getId(), newObj);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public boolean Delete(StoredNote obj) {
        boolean success = true;
        try {
            sqliteStorage.delete(obj.getId());
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public boolean Delete(long _id) {
        boolean success = true;
        try {
            sqliteStorage.delete(_id);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}
