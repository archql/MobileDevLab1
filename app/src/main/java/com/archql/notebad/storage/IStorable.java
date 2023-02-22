package com.archql.notebad.storage;

public interface IStorable<T> {
    long getId();
    void setId(long id);
    T getStored();
    void from(T o, long id, STORAGE_TYPE storageFrom);
    // TODO ???
    boolean isChanged();
    void edited();
}
