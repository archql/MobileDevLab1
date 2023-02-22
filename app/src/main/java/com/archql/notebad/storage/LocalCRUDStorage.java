package com.archql.notebad.storage;

import android.content.Context;

import androidx.annotation.NonNull;

import com.archql.notebad.entities.Note;
import com.archql.notebad.entities.StoredNote;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class LocalCRUDStorage implements ICRUDStorage<Note, StoredNote> {

    protected LocalStorage<Note> storage;
    protected static final String extension = ".txt";
    protected static final STORAGE_TYPE storageType = STORAGE_TYPE.LOCAL;

    private long lastId = 0;

    private String convertIdToFilename(long id) {
        return Base64.getEncoder().encodeToString(Long.toString(id).getBytes()) + extension;
    }
    private long convertFilenameToId(String filename) {
        String name = filename.substring(0, filename.length() - extension.length());
        long id = Long.parseLong(new String(Base64.getDecoder().decode(name)));
        // cunning injection - every decoded file gives us new max id
        lastId = Math.max(lastId, id);
        return id;
    }

    public LocalCRUDStorage(Context ctx) {
        storage = new LocalStorage<>(ctx, extension);
    }

    @Override
    public boolean Create(@NonNull StoredNote obj) {
        // assign new id to object
        lastId++;
        obj.setId(lastId);
        return storage.writeToFile(convertIdToFilename(lastId), obj.getStored());
    }

    @Override
    public StoredNote Read(long _id) {
        Note n = (Note)storage.readFromFile(convertIdToFilename(_id));
        StoredNote sn = new StoredNote();
        sn.from(n, _id, storageType);
        return sn;
    }

    @Override
    public List<StoredNote> ReadAll() {
        List<StoredNote> result = new ArrayList<>();
        Map<String, Note> loaded = storage.loadFromAllFiles();
        for (Map.Entry<String, Note> entry : loaded.entrySet()) {
            StoredNote sn = new StoredNote(entry.getValue(), convertFilenameToId(entry.getKey()), storageType);
            result.add(sn);
        }

        return result;
    }

    @Override
    public boolean Update(@NonNull StoredNote newObj) {
        String filename = convertIdToFilename(newObj.getId());
        if (!storage.checkIfFileExists(filename)) {
            return false;
        }
        return storage.writeToFile(filename, newObj.getStored());
    }

    @Override
    public boolean Delete(@NonNull StoredNote obj) {
        return storage.deleteFile(convertIdToFilename(obj.getId()));
    }

    @Override
    public boolean Delete(long _id) {
        return storage.deleteFile(convertIdToFilename(_id));
    }
}
