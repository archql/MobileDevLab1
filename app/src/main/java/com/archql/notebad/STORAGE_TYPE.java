package com.archql.notebad;

public enum STORAGE_TYPE {
    NO_STORAGE,
    LOCAL,
    SQLite {
        @Override
        public STORAGE_TYPE next() {
            return LOCAL; // see below for options for this line
        };
    };

    public STORAGE_TYPE next() {
        // No bounds checking required here, because the last instance overrides
        return values()[ordinal() + 1];
    }
}
