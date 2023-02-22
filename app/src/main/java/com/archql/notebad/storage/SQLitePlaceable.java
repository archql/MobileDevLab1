package com.archql.notebad.storage;

import android.content.ContentValues;
import android.database.Cursor;

public interface SQLitePlaceable {
    ContentValues generateContentValue();
    void parseFromCursor(Cursor c);
}

