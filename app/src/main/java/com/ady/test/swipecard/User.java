package com.ady.test.swipecard;

import java.util.Random;

public class User {

  public String name;
  public int age;

  public User(String userId) {
    this.name = "testUser" + userId;
    this.age = new Random().nextInt(50);
  }
}
