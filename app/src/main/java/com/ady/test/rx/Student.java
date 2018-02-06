package com.ady.test.rx;

/**
 * Created by zhouxinyuan on 2018/1/17.
 */

public class Student {
    String name;
    Course[] courses;

    Student(String name, Course... courses) {
        this.name = name;
        this.courses = courses;
    }

}
