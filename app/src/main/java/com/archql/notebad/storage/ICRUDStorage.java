package com.archql.notebad.storage;

import androidx.annotation.NonNull;

import java.util.List;

public interface ICRUDStorage<T, Storable extends IStorable<T>> {
    boolean Create(@NonNull Storable obj);
    Storable Read(long _id);
    List<Storable> ReadAll();
    /* Update method requires id param to be valid id in storage! */
    boolean Update(@NonNull Storable newObj);
    boolean Delete(@NonNull Storable obj);
    boolean Delete(long _id);
}
