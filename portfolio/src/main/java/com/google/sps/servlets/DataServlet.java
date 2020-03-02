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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public final class DataServlet extends HttpServlet {
  private List<String> commentsList = new ArrayList<String>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String blogData = convertListToJson();

    response.setContentType("application/json;");
    response.getWriter().println(blogData);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      // retrieve comment from the request and add to List
      String comment = getUserComment(request);
      saveComment(comment);

      commentsList.add(comment);

      // send user back to original page
      response.sendRedirect("/index.html#blog-container");
  }

  private void saveComment(String comment) {
      // Add comment to Datastore database
      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("comment", comment);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
  }

  private String getUserComment(HttpServletRequest request) {
      // Retrieve comment and wrap in quotes so it can be read by JSON as String
      String comment = "\"";
      comment += request.getParameter("comment-input");
      comment += "\"";
      return comment;
  }

  private String convertListToJson() {
    String jsonList = "{";
    jsonList += "\"commentsList\": ";
    jsonList += commentsList;
    jsonList += "}";
    return jsonList;
  }
}