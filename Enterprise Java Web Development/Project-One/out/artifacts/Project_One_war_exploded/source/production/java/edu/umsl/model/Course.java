package edu.umsl.model;

import java.util.ArrayList;
import java.util.Date;

public class Course {
    private Integer courseId;
    private String courseName;
    private Integer courseNumber;
    private String courseStart;
    private String courseEnd;
    private ArrayList<String> courseDays;

    public Course() {

    }

    public Course(Integer courseId, String courseName, Integer courseNumber, String courseStart,
                  String courseEnd, ArrayList<String> courseDays)
    {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseDays = courseDays;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseStart() {
        return courseStart;
    }

    public void setCourseStart(String courseStart) {
        this.courseStart = courseStart;
    }

    public String getCourseEnd() {
        return courseEnd;
    }

    public void setCourseEnd(String courseEnd) {
        this.courseEnd = courseEnd;
    }

    public ArrayList<String> getCourseDays() {
        return courseDays;
    }

    public void setCourseDays(ArrayList<String> courseDays) {
        this.courseDays = courseDays;
    }
}
