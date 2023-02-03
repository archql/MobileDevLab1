package com.archql.notebad;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;

public class StoredNote implements IStorable<Note>, SQLitePlaceable {

    protected Note stored; // guaranteed, that it is unchangeable from last storage read
    protected long id;

    protected STORAGE_TYPE storageType;
    protected STORAGE_TYPE lastStorageType;

    public static String[] TABLE_NAMES = {
        "header",
        "text",
        "dateCreated",
        "dateEdited",
        "encrypted"
    };

    public StoredNote() {
        stored = new Note(); storageType = STORAGE_TYPE.NO_STORAGE; lastStorageType = storageType;
    }
    public StoredNote(@NonNull Note o, long id, STORAGE_TYPE storageFrom) {
        from(o, id, storageFrom);
    }

    public boolean store(ICRUDStorage<Note, StoredNote> storage) {
        // TODO
        return false;
    }

    @Override
    public ContentValues generateContentValue() {
        ContentValues contentValue = new ContentValues();
        contentValue.put("header", stored.header);
        contentValue.put("text", stored.text);
        contentValue.put("dateCreated", stored.dateCreated.toString());
        contentValue.put("dateEdited", stored.dateEdited.toString());
        contentValue.put("encrypted", stored.encrypted);

        return contentValue;
    }

    @Override
    public void parseFromCursor(Cursor c) {
        id = c.getLong(0);

        // this is must be declared
        lastStorageType = STORAGE_TYPE.SQLite;
        storageType = STORAGE_TYPE.SQLite;

        stored.header = c.getString(1);
        stored.text = c.getString(2);
        stored.dateCreated = LocalDateTime.parse(c.getString(3));
        stored.dateEdited = LocalDateTime.parse(c.getString(4));
        stored.encrypted = Boolean.parseBoolean(c.getString(5));
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Note getStored() {
        return stored;
    }

    @Override
    public void from(Note o, long id, STORAGE_TYPE storageFrom) {
        this.stored = o;
        this.id = id;
        this.lastStorageType = storageFrom;
        this.storageType = storageFrom;
    }

    public STORAGE_TYPE getStorageType() {
        return storageType;
    }

    public void setStorageType(STORAGE_TYPE newType) {
        this.storageType = newType;
    }
}
