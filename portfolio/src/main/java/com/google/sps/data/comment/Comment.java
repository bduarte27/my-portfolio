package com.google.sps.data.comment;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.annotations.Expose;
import javax.servlet.http.HttpServletRequest;

public class Comment {
  public static final String ENTITY = "Comment";
  private static final String AUTHOR_PROPERTY = "author";
  private static final String COMMENT_PROPERTY = "comment";
  private static final String TIMESTAMP_PROPERTY = "timestamp";

  @Expose private String author;
  @Expose private String comment;
  @Expose private long timestamp;

  private Comment(String author, String comment, long timestamp) {
    this.author = author;
    this.comment = comment;
    this.timestamp = timestamp;
  }

  public static Comment fromEntity(Entity entity) {
    return new Comment((String) entity.getProperty(AUTHOR_PROPERTY),
        (String) entity.getProperty(COMMENT_PROPERTY),
        (long) entity.getProperty(TIMESTAMP_PROPERTY));
  }

  public static Comment fromServletRequest(HttpServletRequest request) {
    UserService userService = UserServiceFactory.getUserService();
    return new Comment(userService.getCurrentUser().getEmail(),
        request.getParameter(COMMENT_PROPERTY), System.currentTimeMillis());
  }

  public Entity toEntity() {
    Entity entity = new Entity(ENTITY);
    entity.setProperty(AUTHOR_PROPERTY, author);
    entity.setProperty(COMMENT_PROPERTY, comment);
    entity.setProperty(TIMESTAMP_PROPERTY, timestamp);
    return entity;
  }
}