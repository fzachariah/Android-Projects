package edu.uncc.notekeeper_firebase;

import java.util.Date;

/*
Assignment InClass10
Note.java
Sai Yesaswy Mylavarapu, Harish Pendyala, Febin Zachariah, Danjie Gu
 */


public class Note {
    private String id;
    private String taskNote;
    private String status;
    private String priority;
    private Date createdTime;

    @Override
    public String toString() {
        return "Note{" +
                "createdTime=" + createdTime +
                ", id=" + id +
                ", taskNote='" + taskNote + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Note(String taskNote, String status, String priority, Date createdTime) {
        this.taskNote = taskNote;
        this.status = status;
        this.priority=priority;
        this.createdTime=createdTime;
    }

    public Note(){

    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

}
