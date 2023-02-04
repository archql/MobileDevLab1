package com.archql.notebad;

public interface IStorable<T> {
    long getId();
    T getStored();
    void from(T o, long id, STORAGE_TYPE storageFrom);
    // TODO ???
    boolean isChanged();
    void edited();
}
