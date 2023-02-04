package com.archql.notebad;

import android.content.ContentValues;
import android.database.Cursor;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class StoredNote implements IStorable<Note>, SQLitePlaceable {

    protected Note stored; // guaranteed, that it is unchangeable from last storage read
    protected long id;
    protected int lastHash; // prevent from double save, TODO 2^32 combinations only

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
        from(new Note(), 0, STORAGE_TYPE.NO_STORAGE);
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

        // in the end
        lastHash = stored.hashCode();
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

        // in the end
        lastHash = stored.hashCode();
    }

    @Override
    public boolean isChanged() {
        return lastHash != stored.hashCode() || lastStorageType != storageType;
    }

    @Override
    public void edited() {
        stored.dateEdited = LocalDateTime.now();
        lastHash = stored.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoredNote that = (StoredNote) o;
        return id == that.id &&
                stored.equals(that.stored) &&
                storageType == that.storageType &&
                lastStorageType == that.lastStorageType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stored, id, storageType, lastStorageType);
    }

    public STORAGE_TYPE getStorageType() {
        return storageType;
    }
    public void setStorageType(STORAGE_TYPE newType) {
        this.storageType = newType;
    }


}
