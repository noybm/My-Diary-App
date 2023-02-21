package com.app.mydiary;

import com.google.firebase.Timestamp;

public class ModelClass {
    String title;
    String content;
    Timestamp timestamp;
    String date;


    public ModelClass() {
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getContent() {

        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public Timestamp getTimestamp() {

        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {

        this.timestamp = timestamp;
    }
}
