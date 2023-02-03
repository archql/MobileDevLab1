package com.archql.notebad;

import android.content.ContentValues;
import android.database.Cursor;

public interface SQLitePlaceable {
    ContentValues generateContentValue();
    void parseFromCursor(Cursor c);
}

