package com.is416.tasks.model;

import java.util.Date;

/**
 * Created by Gods on 2/3/2018.
 */

public class Task {
    private Date created;
    private String content;
    private boolean isComplete;
    private Date completed;

    public Task(Date created, String content, boolean isComplete) {
        this.created = created;
        this.content = content;
        this.isComplete = isComplete;
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
        return isComplete;

    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
