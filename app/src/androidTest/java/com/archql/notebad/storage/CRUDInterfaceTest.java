package com.archql.notebad.storage;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.archql.notebad.entities.Note;
import com.archql.notebad.entities.StoredNote;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CRUDInterfaceTest {

    LocalCRUDStorage localCRUDStorage;
    SQLiteCRUDStorage sqliteCRUDStorage;

    @Before
    public void setUp() throws Exception {
        Context context = ApplicationProvider.getApplicationContext();
        localCRUDStorage = new LocalCRUDStorage(context);
        sqliteCRUDStorage = new SQLiteCRUDStorage(context, "test_crud");
    }
    @After
    public void tearDown() throws Exception {
        // delete all created
        List<StoredNote> localStored = localCRUDStorage.ReadAll();
        for (int i = 0; i < localStored.size(); i++) {
            localCRUDStorage.Delete(localStored.get(i).getId());
        }
        List<StoredNote> sqliteStored = sqliteCRUDStorage.ReadAll();
        for (int i = 0; i < sqliteStored.size(); i++) {
            sqliteCRUDStorage.Delete(sqliteStored.get(i).getId());
        }
        // close
        sqliteCRUDStorage.close();
        // localCRUDStorage does not need close
    }

    @Test
    /*
    known that crud of local storage Update diff from sqlite
     */
    public void checkIfCRUDWorksSame() {
        // init some objects
        StoredNote a = new StoredNote(new Note("hello", "world"), 0, STORAGE_TYPE.NO_STORAGE);
        StoredNote b = new StoredNote(new Note("hi", "world"), 0, STORAGE_TYPE.NO_STORAGE);
        StoredNote c = new StoredNote();

        Assert.assertEquals(localCRUDStorage.Create(a), sqliteCRUDStorage.Create(a));
        Assert.assertEquals(localCRUDStorage.Create(b), sqliteCRUDStorage.Create(b));
        Assert.assertEquals(localCRUDStorage.Create(c), sqliteCRUDStorage.Create(c));

        List<StoredNote> localStored = localCRUDStorage.ReadAll();
        List<StoredNote> sqliteStored = sqliteCRUDStorage.ReadAll();

        Assert.assertEquals(3, sqliteStored.size());
        Assert.assertEquals(localStored.size(), sqliteStored.size());
        int numEquals = 0;
        StoredNote c1 = null, c2 = null; // extract c notes with their new ids
        for (int i = 0; i < localStored.size(); i++) {
            // check storage type
            Assert.assertEquals(localStored.get(i).getLastStorageType(), STORAGE_TYPE.LOCAL);
            Assert.assertEquals(sqliteStored.get(i).getLastStorageType(), STORAGE_TYPE.SQLite);
            Assert.assertEquals(localStored.get(i).getStorageType(), STORAGE_TYPE.LOCAL);
            Assert.assertEquals(sqliteStored.get(i).getStorageType(), STORAGE_TYPE.SQLite);
            // check if equal Notes were loaded
            for (int j = 0; j < sqliteStored.size(); j++) {
                if (localStored.get(i).getStored().equals(sqliteStored.get(j).getStored())) {
                    if (localStored.get(i).getStored().equals(c.getStored())) { // for next test
                        c1 = localStored.get(i);
                        c2 = sqliteStored.get(j);
                    }
                    numEquals++;
                    break;
                }
            }
        }
        Assert.assertEquals(localStored.size(), numEquals);
        Assert.assertNotNull(c1);
        Assert.assertNotNull(c2);

        // updates (cringe with ids)
        c.getStored().setHeader("changed");
        c1.getStored().setHeader("changed");
        c2.getStored().setHeader("changed");

        // Update method requires id param to be valid id in storage!
        Assert.assertTrue(localCRUDStorage.Update(c1));
        Assert.assertTrue(sqliteCRUDStorage.Update(c2));

        // check if update worked
        List<StoredNote> localStored2 = localCRUDStorage.ReadAll();
        List<StoredNote> sqliteStored2 = sqliteCRUDStorage.ReadAll();

        boolean ok = false;
        for (int i = 0; i < localStored2.size(); i++) {
            if (localStored2.get(i).getStored().equals(c.getStored())) {
                ok = true;
                break;
            }
        }
        Assert.assertTrue(ok);
        ok = false;
        for (int i = 0; i < sqliteStored2.size(); i++) {
            if (sqliteStored2.get(i).getStored().equals(c.getStored())) {
                ok = true;
                break;
            }
        }
        Assert.assertTrue(ok);

        // delete (with old containers, just to check id does not changed)
        for (int i = 0; i < localStored.size(); i++) {
            localCRUDStorage.Delete(localStored.get(i).getId());
            sqliteCRUDStorage.Delete(sqliteStored.get(i).getId());
        }

        // check if nothing left
        localStored = localCRUDStorage.ReadAll();
        sqliteStored = sqliteCRUDStorage.ReadAll();

        Assert.assertEquals(localStored.size() == 0, sqliteStored.size() == 0);
    }
}
