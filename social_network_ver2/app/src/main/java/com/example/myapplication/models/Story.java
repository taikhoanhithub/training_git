package com.example.myapplication.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Story implements Serializable {
  private String storyBy;
  private long storyAt;

  public Story() {
  }

  public ArrayList<UserStories> getStories() {
    return stories;
  }

  public void setStories(ArrayList<UserStories> stories) {
    this.stories = stories;
  }

  ArrayList<UserStories> stories;

  public String getStoryBy() {
    return storyBy;
  }

  public void setStoryBy(String storyBy) {
    this.storyBy = storyBy;
  }

  public long getStoryAt() {
    return storyAt;
  }

  public void setStoryAt(long storyAt) {
    this.storyAt = storyAt;
  }
}
