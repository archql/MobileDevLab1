package com.archql.notebad;

import java.util.List;

public interface ICRUDStorage<T, Storable extends IStorable<T>> {
    boolean Create(Storable obj);
    Storable Read(long _id);
    List<Storable> ReadAll();
    boolean Update(Storable newObj);
    boolean Delete(Storable obj);
}
