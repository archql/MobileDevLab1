package com.archql.notebad.storage;

import android.content.Context;

import androidx.annotation.NonNull;

import com.archql.notebad.entities.Note;
import com.archql.notebad.entities.StoredNote;

import java.util.ArrayList;
import java.util.List;

public class SQLiteCRUDStorage implements ICRUDStorage<Note, StoredNote> {

    protected SQLiteStorage<StoredNote> sqliteStorage;
    protected static final STORAGE_TYPE storageType = STORAGE_TYPE.SQLite;

    public SQLiteCRUDStorage(Context context, String storedObjectName) {
        sqliteStorage = new SQLiteStorage<>(context, storedObjectName, StoredNote.TABLE_NAMES, StoredNote::new);
        sqliteStorage.open();
    }

    public void close() {
        sqliteStorage.close();
    }

    @Override
    public boolean Create(@NonNull StoredNote obj) {
        boolean success = true;
        try {
            long id = sqliteStorage.insert(obj);
            if (id == -1) {
                success = false;
            } else {
                obj.setId(id); // update our id
            }
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
    public boolean Update(@NonNull StoredNote newObj) {
        try {
            return sqliteStorage.update(newObj.getId(), newObj) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean Delete(@NonNull StoredNote obj) {
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
