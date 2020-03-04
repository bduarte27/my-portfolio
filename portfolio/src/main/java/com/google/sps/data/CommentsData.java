package com.google.sps.data;

import java.util.List;

public final class CommentsData {
  private List<String> commentsList;

  public CommentsData(List<String> commentsList) {
    this.commentsList = commentsList;
  }

  public List<String> getComments() {
    return commentsList;
  }
}