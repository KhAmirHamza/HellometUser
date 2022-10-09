package com.hellomet.user.RoomDbMVVM;

public class Note {
    private String description;

    /* renamed from: id */
    private int f155id;
    private int priority;
    private String title;

    public Note(String title2, String description2, int priority2) {
        this.title = title2;
        this.description = description2;
        this.priority = priority2;
    }

    public void setId(int id) {
        this.f155id = id;
    }

    public int getId() {
        return this.f155id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPriority() {
        return this.priority;
    }
}
