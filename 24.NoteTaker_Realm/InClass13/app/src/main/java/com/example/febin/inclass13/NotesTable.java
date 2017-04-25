package com.example.febin.inclass13;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by febin on 24/04/2017.
 */

public class NotesTable extends RealmObject {

    private String id;
    private String taskNote;
    private String status;
    private int priority;
    private Date createdTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "NotesTable{" +
                "id='" + id + '\'' +
                ", taskNote='" + taskNote + '\'' +
                ", status=" + status +
                ", priority='" + priority + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
}
