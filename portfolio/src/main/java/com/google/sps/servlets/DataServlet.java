// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.CommentsData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/data")
public final class DataServlet extends HttpServlet {
  private static final String COMMENT_ENTITY = "Comment";
  private static final String COMMENT_PROPERTY = "comment";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    sendJsonResponse(
        response, convertListToJson(fetchAllCommentsAsList(createQueryToFetchAllComments())));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    addCommentToDatastore(getUserComment(request));
    response.sendRedirect("/index.html#blog-container");
  }

  private Query createQueryToFetchAllComments() {
    return new Query(COMMENT_ENTITY);
  }

  private List<String> fetchAllCommentsAsList(Query query) {
    PreparedQuery commentEntities = fetchQuery(query);
    List<String> commentsList = new ArrayList<String>();

    for (Entity commentEntity : commentEntities.asIterable()) {
      String comment = (String) commentEntity.getProperty(COMMENT_PROPERTY);
      commentsList.add(comment);
    }
    return commentsList;
  }

  private PreparedQuery fetchQuery(Query query) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    return datastore.prepare(query);
  }

  private void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private String getUserComment(HttpServletRequest request) {
    return request.getParameter("comment-input");
  }

  private void addCommentToDatastore(String comment) {
    addEntityToDatastore(createCommentEntity(comment));
  }

  private Entity createCommentEntity(String comment) {
    Entity commentEntity = new Entity(COMMENT_ENTITY);
    commentEntity.setProperty(COMMENT_PROPERTY, comment);
    return commentEntity;
  }

  private void addEntityToDatastore(Entity entity) {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(entity);
  }

  private String convertListToJson(List<String> commentsList) {
    CommentsData commentsData = new CommentsData(commentsList);
    return new Gson().toJson(commentsData);
  }
}