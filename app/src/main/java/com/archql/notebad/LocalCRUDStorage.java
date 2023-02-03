package com.archql.notebad;

import android.content.Context;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class LocalCRUDStorage implements ICRUDStorage<Note, StoredNote> {

    protected LocalStorage<Note> storage;
    protected static final String extension = ".txt";
    protected static final STORAGE_TYPE storageType = STORAGE_TYPE.LOCAL;

    private String convertIdToFilename(long id) {
        return Base64.getEncoder().encodeToString(Long.toString(id).getBytes()) + extension;
    }
    private long convertIdToFilename(String filename) {
        String name = filename.substring(0, filename.length() - extension.length() - 1);
        return Long.parseLong(new String(Base64.getDecoder().decode(name)));
    }

    LocalCRUDStorage(Context ctx) {
        storage = new LocalStorage<>(ctx, extension);
    }

    @Override
    public boolean Create(StoredNote obj) {
        return storage.writeToFile(convertIdToFilename(obj.getId()), obj.getStored());
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
            StoredNote sn = new StoredNote(entry.getValue(), convertIdToFilename(entry.getKey()), storageType);
            result.add(sn);
        }

        return result;
    }

    @Override
    public boolean Update(StoredNote newObj) {
        return storage.writeToFile(convertIdToFilename(newObj.getId()), newObj.getStored());
    }

    @Override
    public boolean Delete(StoredNote obj) {
        return storage.deleteFile(convertIdToFilename(obj.getId()));
    }
}
