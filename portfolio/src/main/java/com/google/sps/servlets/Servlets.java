package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

class Servlets {
  private Servlets() {}

  public static void sendJsonResponse(HttpServletResponse response, String json)
      throws IOException {
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
}