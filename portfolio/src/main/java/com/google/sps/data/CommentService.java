package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.sps.data.Comment;
import java.util.ArrayList;
import java.util.List;

public class CommentService {
  private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  private CommentService() {}

  private static Query createQueryForAllSorted() {
    return new Query(Comment.ENTITY).addSort("timestamp", SortDirection.DESCENDING);
  }

  public static List<Comment> fetchAllAsList() {
    Query query = createQueryForAllSorted();
    List<Comment> comments = new ArrayList<Comment>();
    for (Entity entity : CommentService.datastore.prepare(query).asIterable()) {
      comments.add(Comment.fromEntity(entity));
    }
    return comments;
  }

  public static void add(Comment comment) {
    CommentService.datastore.put(comment.toEntity());
  }

  public static String toJson(List<Comment> comments) {
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    return gson.toJson(comments);
  }
}