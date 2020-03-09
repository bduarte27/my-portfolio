package com.google.sps.data.authentication;

public class Authentication {
  public boolean isLoggedIn;
  public String emailAddress;

  public Authentication(boolean isLoggedIn, String emailAddress) {
    this.isLoggedIn = isLoggedIn;
    this.emailAddress = emailAddress;
  }
}