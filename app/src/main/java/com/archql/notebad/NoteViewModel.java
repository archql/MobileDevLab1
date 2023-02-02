package com.archql.notebad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoteViewModel extends ViewModel {
    // MutableLiveData allows its value to be changed
    private final MutableLiveData<Note> selectedNote = new MutableLiveData<>();

    public void setSelectedNote(Note n) {
        selectedNote.setValue(n);
    }
    public LiveData<Note> getSelectedNote() {
        return selectedNote; // LiveData is a lifecycle-aware observable data holder class
    }
}
