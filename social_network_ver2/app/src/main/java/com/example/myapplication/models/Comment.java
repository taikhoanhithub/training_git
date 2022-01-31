package com.example.myapplication.models;

public class Comment {
    private String commentBody;
    private long commentAt;
    private String commentBy;

    public Comment(String commentBody, long commentAt, String commentBy) {
        this.commentBody = commentBody;
        this.commentAt = commentAt;
        this.commentBy = commentBy;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public long getCommentAt() {
        return commentAt;
    }

    public Comment() {
    }

    public void setCommentAt(long commentAt) {
        this.commentAt = commentAt;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(String commentBy) {
        this.commentBy = commentBy;
    }
}
