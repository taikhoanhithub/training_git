package com.example.myapplication.models;

public class Follow {
    private String followedBy;  // id cua nguoi follow , id cua minh
    private long followedAt; // thoi gian bat dau follow

    public Follow() {
    }

    public String getFollowedBy() {
        return followedBy;
    }

    public void setFollowedBy(String followedBy) {
        this.followedBy = followedBy;
    }

    public long getFollowedAt() {
        return followedAt;
    }

    public void setFollowedAt(long followedAt) {
        this.followedAt = followedAt;
    }
}
