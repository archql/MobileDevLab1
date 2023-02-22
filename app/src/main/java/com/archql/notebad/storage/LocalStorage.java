package com.archql.notebad.storage;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LocalStorage<T extends Serializable> {

    private Context ctx;
    private String extension;

    LocalStorage(Context ctx, String extension) {
        this.ctx = ctx;
        this.extension = extension;
    }

    public boolean writeToFile(String filename, T s)
    {
        if (s == null) {
            return false;
        }
        //File file = new File(ctx.getFilesDir(), filename + );
        try (FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_PRIVATE)) {
            ObjectOutputStream objectOut = new ObjectOutputStream(fos);
            objectOut.writeObject(s);
            objectOut.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public Map<String, T> loadFromAllFiles() {
        Map<String, T> result = new HashMap<>();
        String[] files = ctx.getFilesDir().list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(extension)) {
                    T obj = readFromFile(name);
                    if (obj != null) {
                        result.put(name, obj);
                    }
                }
                return false;
            }
        });
        return result;
    }

    /**
     *
     * @param filename - name of file where object is located
     * @return - null on failure
     */
    public T readFromFile(String filename) {
        FileInputStream fis;
        T s = null;
        try {
            fis = ctx.openFileInput(filename);
            ObjectInputStream objectInput = new ObjectInputStream(fis);
            s = (T)(objectInput.readObject());
            objectInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public boolean deleteFile(String filename) {
        try {
            File f = new File(ctx.getFilesDir(), filename);           //file to be deleted
            return f.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkIfFileExists(String filename) {
        File f = new File(ctx.getFilesDir(), filename);
        return f.exists();
    }
}
