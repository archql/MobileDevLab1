package com.archql.notebad.storage;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.archql.notebad.entities.Note;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Random;

public class LocalStorageTest {

    LocalStorage<Note> noteLocalStorage;
    Note a;
    Note b;
    Note e;
    Note n;

    String a_name = "a.txt";
    String b_name = "b.txt";
    String e_name = "e.txt";
    String n_name = "n.txt";

    Random rnd = new Random();

    private void makeUnique(String name) {
        while (noteLocalStorage.checkIfFileExists(name)) {
            final char c = (char)(rnd.nextInt(26) + 'a');
            name = c + name;
        }
    }

    @Before
    public void setUp() throws Exception {
        a = new Note("Note a", "description of note a");
        b = new Note("Note b", "description of note b");
        e = new Note();
        n = null;

        Context context = ApplicationProvider.getApplicationContext();
        noteLocalStorage = new LocalStorage<>(context, ".txt");

        // setup
        makeUnique(a_name);
        makeUnique(b_name);
        makeUnique(e_name);
        makeUnique(n_name);
    }

    @Test
    /*
    positive test
    - this test covers addition, deletion, check on existence, loading from all files or sep file
    whats founded:
    - 1) writing null return false, writing to nonexistent return false
    - 2) reading nonexistent/wrong return null
    - 3) readAll nonexistent do not added to result
    - 5) deleting wrong/nonexistent return false
    - 6) number of files written == number of files loaded
    - 7) !!! writeFile works as override
    reading performs correctly (object.equals == true)
     */
    public void complex1() {
        // check if text files not created before
        Assert.assertFalse(noteLocalStorage.checkIfFileExists(a_name));
        Assert.assertFalse(noteLocalStorage.checkIfFileExists(b_name));
        Assert.assertFalse(noteLocalStorage.checkIfFileExists(e_name));
        Assert.assertFalse(noteLocalStorage.checkIfFileExists(n_name));

        // get amount of files before the test (6)
        int before = noteLocalStorage.loadFromAllFiles().size();

        // check if I can write files
        Assert.assertTrue(noteLocalStorage.writeToFile(a_name, a));
        Assert.assertTrue(noteLocalStorage.writeToFile(b_name, b));
        Assert.assertTrue(noteLocalStorage.writeToFile(e_name, e));
        // negative test (null) (1)
        Assert.assertFalse(noteLocalStorage.writeToFile(n_name, n));
        // fail if wrong name (negative test for writeToFile) (1)
        Assert.assertFalse(noteLocalStorage.writeToFile("test/a.txt", e));

        // check if corresponding files exist
        Assert.assertTrue(noteLocalStorage.checkIfFileExists(a_name));
        Assert.assertTrue(noteLocalStorage.checkIfFileExists(b_name));
        Assert.assertTrue(noteLocalStorage.checkIfFileExists(e_name));
        // negative test (null) (1)
        Assert.assertFalse(noteLocalStorage.checkIfFileExists(n_name));
        // check on wrong file (negative test for checkIfFileExists)
        Assert.assertFalse(noteLocalStorage.checkIfFileExists(".txt"));

        // check if objects loaded (5)
        Map<String, Note> res = noteLocalStorage.loadFromAllFiles();
        Assert.assertEquals(before + 3, res.size());
        Assert.assertTrue(res.containsKey(a_name));
        Assert.assertTrue(res.containsKey(b_name));
        Assert.assertTrue(res.containsKey(e_name));
        // negative test (null) (3)
        Assert.assertFalse(res.containsKey(n_name));

        // check if objects loaded correctly
        Assert.assertEquals(a, res.get(a_name));
        Assert.assertEquals(b, res.get(b_name));
        Assert.assertEquals(e, res.get(e_name));
        // negative test (null) (3)
        Assert.assertNull(res.get(n_name));

        // check if readFromFile works correctly (2)
        Assert.assertEquals(a, noteLocalStorage.readFromFile(a_name));
        Assert.assertEquals(b, noteLocalStorage.readFromFile(b_name));
        Assert.assertEquals(e, noteLocalStorage.readFromFile(e_name));
        Assert.assertEquals(n, noteLocalStorage.readFromFile(n_name));
        Assert.assertNull(noteLocalStorage.readFromFile("somewromg name / he"));

        // clear after me
        Assert.assertTrue(noteLocalStorage.deleteFile(a_name));
        Assert.assertTrue(noteLocalStorage.deleteFile(b_name));
        Assert.assertTrue(noteLocalStorage.deleteFile(e_name));
        Assert.assertFalse(noteLocalStorage.deleteFile(n_name)); // (5)
        // check on wrong file (negative test for deleteFile) (5)
        Assert.assertFalse(noteLocalStorage.deleteFile(".txt"));
        //Assert.assertFalse(noteLocalStorage.deleteFile(""));

        // check if cleared (5)
        Assert.assertEquals(before, noteLocalStorage.loadFromAllFiles().size());
        Assert.assertFalse(noteLocalStorage.deleteFile(a_name));
        Assert.assertFalse(noteLocalStorage.deleteFile(b_name));
        Assert.assertFalse(noteLocalStorage.deleteFile(e_name));
        Assert.assertFalse(noteLocalStorage.deleteFile(n_name));
    }

    @After
    public void tearDown() throws Exception {
        // force delete all
        noteLocalStorage.deleteFile(a_name);
        noteLocalStorage.deleteFile(b_name);
        noteLocalStorage.deleteFile(e_name);
        noteLocalStorage.deleteFile(n_name);
    }

    //@Test
    //public void writeToFile() {
//
    //}
//
    //@Test
    //public void loadFromAllFiles() {
    //}
//
    //@Test
    //public void readFromFile() {
    //}
//
    //@Test
    //public void deleteFile() {
    //}
//
    //@Test
    //public void checkIfFileExists() {
    //}
}