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

public class SQLiteStorageTest {

    SQLiteStorage<StoredNote> noteSQLiteStorage;
    Context context;

    @Before
    public void setUp() throws Exception {
        context = ApplicationProvider.getApplicationContext();
        noteSQLiteStorage = new SQLiteStorage<>(context,
                "test", StoredNote.TABLE_NAMES, StoredNote::new);
        // db is named test.db
        // table is named tests
        // index column is named test_id
    }

    @Test
    public void defaultOperations() {
        StoredNote a = new StoredNote(new Note(), 0, STORAGE_TYPE.SQLite);
        StoredNote b = new StoredNote(new Note("hello", "so text"), 0, STORAGE_TYPE.SQLite);
        StoredNote c = new StoredNote();
        // @NotNull ??
        noteSQLiteStorage.open();

        // try to insert every object into db
        // 1) insert them positively
        Assert.assertNotEquals(-1, noteSQLiteStorage.insert(a));
        Assert.assertNotEquals(-1, noteSQLiteStorage.insert(b));
        Assert.assertNotEquals(-1, noteSQLiteStorage.insert(c));
        // 2) try to insert null (@NotNull)
        //Assert.assertEquals(-1, noteSQLiteStorage.insert(null));
        // 3) try to insert duplicate (should give ok result, bc id will be different)
        Assert.assertNotEquals(-1, noteSQLiteStorage.insert(b));

        // try to fetch data from db (should be 4 records)
        List<StoredNote> fetched = noteSQLiteStorage.fetch();
        Assert.assertEquals(4, fetched.size());
        // check them for correct save & loading
        Assert.assertEquals(fetched.get(0).getStored(), a.getStored());
        Assert.assertEquals(fetched.get(1).getStored(), b.getStored());
        Assert.assertEquals(fetched.get(2).getStored(), c.getStored());
        Assert.assertEquals(fetched.get(3).getStored(), b.getStored());

        Assert.assertEquals(fetched.get(0).getStorageType(), a.getStorageType());
        Assert.assertEquals(fetched.get(1).getStorageType(), b.getStorageType());
        Assert.assertNotEquals(fetched.get(2).getStorageType(), c.getStorageType()); // because it had default, now must have sqlite
        Assert.assertEquals(fetched.get(3).getStorageType(), b.getStorageType());

        Assert.assertEquals(fetched.get(0).getStorageType(), STORAGE_TYPE.SQLite);
        Assert.assertEquals(fetched.get(1).getStorageType(), STORAGE_TYPE.SQLite);
        Assert.assertEquals(fetched.get(2).getStorageType(), STORAGE_TYPE.SQLite);
        Assert.assertEquals(fetched.get(3).getStorageType(), STORAGE_TYPE.SQLite);

        a = fetched.get(0); b = fetched.get(1); c = fetched.get(2);
        StoredNote d = fetched.get(3);
        // update one of them
        d.getStored().setHeader("Just changed the header");
        Assert.assertTrue(noteSQLiteStorage.update(d.getId(), d) > 0);
        // get it all back
        fetched = noteSQLiteStorage.fetch();
        Assert.assertEquals(4, fetched.size());
        boolean ok = false;
        for (StoredNote sn : fetched) {
            if (sn.getId() == d.getId()) {
                Assert.assertEquals("Just changed the header", sn.getStored().getHeader());
                Assert.assertEquals(d.hashCode(), sn.hashCode());
                Assert.assertEquals(d.getStored(), sn.getStored());
                ok = true;
                break;
            }
        }
        Assert.assertTrue(ok); // founded

        // delete all records
        noteSQLiteStorage.delete(a.getId());
        noteSQLiteStorage.delete(b.getId());
        noteSQLiteStorage.delete(c.getId());
        noteSQLiteStorage.delete(d.getId());

        // check if table empty
        fetched = noteSQLiteStorage.fetch();
        Assert.assertEquals(0, fetched.size());
    }

    @After
    public void tearDown() throws Exception {
        noteSQLiteStorage.deleteAll();
        noteSQLiteStorage.close();
    }

    @Test
    public void wrongName() {
        // negative test
        Context context = ApplicationProvider.getApplicationContext();
        SQLiteStorage<StoredNote> storedNoteSQLiteStorage = new SQLiteStorage<>(context,
                "/___///////", StoredNote.TABLE_NAMES, StoredNote::new);
        storedNoteSQLiteStorage.open();

        StoredNote sn = new StoredNote(new Note(), 0, STORAGE_TYPE.SQLite);
        // try insert
        Assert.assertEquals(-1, storedNoteSQLiteStorage.insert(sn));
        Assert.assertEquals(-1, storedNoteSQLiteStorage.insert(sn));

        // try update
        Assert.assertEquals(-1, storedNoteSQLiteStorage.update(sn.getId(), sn));

        // try read
        List<StoredNote> fetched = storedNoteSQLiteStorage.fetch();
        Assert.assertEquals(0, fetched.size());

        storedNoteSQLiteStorage.close();
    }
}