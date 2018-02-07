package com.is416.tasks.model;

import java.util.Date;

/**
 * Created by Gods on 2/3/2018.
 */

public class Task {
    private String id;
    private Date created;
    private String content;
    private Date completed = null;

    public Task(String id, Date created, String content) {
        this.id = id;
        this.created = created;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCompleted() {
        return completed;
    }

    public void setCompleted(Date completed) {
        this.completed = completed;
    }

    public boolean isComplete() {
        return this.completed != null;
    }
}
