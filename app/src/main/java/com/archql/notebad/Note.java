package com.archql.notebad;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Note implements Serializable {
    protected String header;
    protected String text;

    protected LocalDateTime dateCreated;
    protected LocalDateTime dateEdited;

    protected boolean encrypted;

    public Note(String header, String text) {
        this();
        this.header = header;
        this.text = text;
    }

    public Note() {
        encrypted = false;
        dateCreated = LocalDateTime.now();
        dateEdited = LocalDateTime.now();

        this.header = "";
        this.text = "";
    }

    private static final DateTimeFormatter fmt1 = DateTimeFormatter.ofPattern("dd MMM");
    private static final DateTimeFormatter fmt2 = DateTimeFormatter.ofPattern("dd MMM yyyy");
    public String getDateString() {
        if (dateEdited.getYear() == LocalDateTime.now().getYear()) {
            return dateEdited.format(fmt1);
        } else {
            return dateEdited.format(fmt2);
        }
    }


    // Getters and setters
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(LocalDateTime dateEdited) {
        this.dateEdited = dateEdited;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
/*
    TextView textView = (TextView) findViewById(R.id.mytextview01);
    Spannable word = new SpannableString("Your message");

    word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

    textView.setText(word);
    Spannable wordTwo = new SpannableString("Your new message");

    wordTwo.setSpan(new ForegroundColorSpan(Color.RED), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    textView.append(wordTwo);
     */
}
