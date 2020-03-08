package com.google.sps.data.authentication;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationService {
  private static final String LOOP_BACK_PATH = "/";
  private static final String LOGIN_STATUS_PARAMETER = "login-status";

  private AuthenticationService() {}

  public static boolean loginStatus() {
    return UserServiceFactory.getUserService().isUserLoggedIn();
  }

  public static void updateLoginStatus(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String buttonClicked = request.getParameter(AuthenticationService.LOGIN_STATUS_PARAMETER);
    if (buttonClicked.equals("Login")) {
      AuthenticationService.userLogin(response);
    } else {
      AuthenticationService.userLogout(response);
    }
  }

  public static String createJsonLoginStatusString() {
    String json = "{";
    json += "\"isLoggedIn\": " + AuthenticationService.loginStatus();
    json += "}";
    return json;
  }

  private static void userLogin(HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    if (!userService.isUserLoggedIn()) {
      response.sendRedirect(userService.createLoginURL(AuthenticationService.LOOP_BACK_PATH));
    } else {
      response.sendRedirect(AuthenticationService.LOOP_BACK_PATH);
    }
  }

  private static void userLogout(HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    response.sendRedirect(userService.createLogoutURL(AuthenticationService.LOOP_BACK_PATH));
  }
}