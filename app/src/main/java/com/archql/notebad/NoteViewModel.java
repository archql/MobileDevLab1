package com.archql.notebad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {
    // MutableLiveData allows its value to be changed
    private final MutableLiveData<StoredNote> selectedNote = new MutableLiveData<>();
    private final MutableLiveData<LocalCRUDStorage> localCRUDStorage = new MutableLiveData<>();
    private final MutableLiveData<SQLiteCRUDStorage> sqliteCRUDStorage = new MutableLiveData<>();

    public void setSelectedNote(StoredNote n) {
        selectedNote.setValue(n);
    }
    public LiveData<StoredNote> getSelectedNote() {
        return selectedNote; // LiveData is a lifecycle-aware observable data holder class
    }

    public void setLocalStorage(LocalCRUDStorage localCRUDStorage) {
        this.localCRUDStorage.setValue(localCRUDStorage);
    }
    public LiveData<LocalCRUDStorage> getLocalStorage() {
        return localCRUDStorage;
    }

    public void setSQLiteStorage(SQLiteCRUDStorage sqliteCRUDStorage) {
        this.sqliteCRUDStorage.setValue(sqliteCRUDStorage);
    }
    public LiveData<SQLiteCRUDStorage> getSQLiteStorage() {
        return sqliteCRUDStorage;
    }
}
