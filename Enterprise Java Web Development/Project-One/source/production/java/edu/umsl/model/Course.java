package edu.umsl.model;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*This is the Course class, that represents an individual course added by the student.*/
public class Course {
    private String courseName; //The name of the course
    private Integer courseNumber; //The course number
    private String courseStart; //The time the course begins, a string in the 00:00AM/PM format
    private String courseEnd; //The time the course ends, a string in the 00:00AM/PM format
    private LocalTime courseStartTime; //The time the course begins as a LocalTime object
    private LocalTime courseEndTime; //The time the course ends as a LocalTime object
    private long courseStartMinutes; //The offset of the course start time from 08:00 AM, in minutes
    private long courseEndMinutes; //The offset of the course end time from 08:00 AM, in minutes
    private ArrayList<String> courseDays; //This array represents the days of the week of the course
    private String courseLength; //The length of the course, in minutes, as a string

    //An empty constructor to declare a Course object as Course()
    public Course() {

    }

    //A constructor that will create a Course object based on the form fields on the schedule.jsp
    //page. Other Course variables, such as the LocalTime members and courseLength, will be set
    //using the appropriate set() methods of the Course object.
    public Course(String courseName, Integer courseNumber, String courseStart,
                  String courseEnd, ArrayList<String> courseDays)
    {
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        this.courseStart = courseStart;
        this.courseEnd = courseEnd;
        this.courseDays = courseDays;
    }

    //Return the course name
    public String getCourseName() {
        return courseName;
    }

    //Set the course name
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    //Return the course number
    public Integer getCourseNumber() {
        return courseNumber;
    }

    //Set the course number
    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    //Return the course start time, a String
    public String getCourseStart() {
        return courseStart;
    }

    //Set the course start time. The courseStart string is in the 00:00 format, where
    //the timeOfDay string has the value 'AM' or 'PM'.
    public void setCourseStart(String courseStart, String timeOfDay) {
        this.courseStart = courseStart + timeOfDay;
    }

    //Return the course end time, a String
    public String getCourseEnd() {
        return courseEnd;
    }

    //Set the course end time. The courseEnd string is in the 00:00 format, where
    //the timeOfDay string has the value 'AM' or 'PM'.
    public void setCourseEnd(String courseEnd, String timeOfDay) {
        this.courseEnd = courseEnd + timeOfDay;
    }

    //Set the courseStartTime, a LocalTime object. This will invoke the setCourseTime()
    //method, which returns a LocalTime object representing the course start time in the 00:00-24:00
    //military time format.
    public void setCourseStartTime(String courseTime, String timeOfDay) {
        this.courseStartTime = setCourseTime(courseTime, timeOfDay);
    }

    //Set the courseEndTime, a LocalTime object. This will invoke the setCourseTime()
    //method, which returns a LocalTime object representing the course end time in the 00:00-24:00
    //military time format.
    public void setCourseEndTime(String courseTime, String timeOfDay) {
        this.courseEndTime = setCourseTime(courseTime, timeOfDay);
    }

    //This will return the courseStartTime, a LocalTime object, representing the course
    //start as an actual time unit rather than a String.
    public LocalTime getCourseStartTime() {
        return courseStartTime;
    }

    //This will return the courseEndTime, a LocalTime object, representing the course end
    //as an actual time unit rather than a String.
    public LocalTime getCourseEndTime() {
        return courseEndTime;
    }

    //This will set the course start time, in minutes, as an offset from 08:00 A.M.
    //08:00 A.M. is the earliest time courses may begin, and the course start time in minutes
    //will be used to pad the courses in the schedule display.
    public void setCourseStartMinutes() {
        this.courseStartMinutes = Duration.between(LocalTime.of(8, 0), courseStartTime)
                .toMinutes();
    }

    //This will set the course end time, in minutes, as an offset from 08:00 A.M.
    //08:00 A.M. is the earliest time courses may begin, and the course end time in minutes
    //will be used to pad the courses in the schedule display.
    public void setCourseEndMinutes() {
        this.courseEndMinutes = Duration.between(LocalTime.of(8, 0), courseEndTime)
                .toMinutes();
    }

    //Return the course start time, offset from 08:00 A.M., in minutes.
    public long getCourseStartMinutes() {
        return courseStartMinutes;
    }

    //Return the course end time, offset from 08:00 A.M., in minutes.
    public long getCourseEndMinutes() {
        return courseEndMinutes;
    }

    //This will assign the length of the course as a string, in minutes, to
    //the courseLength member variable. This value will be used for a course's 'height'
    //in the schedule display.
    public void setCourseLength() {
        this.courseLength = String.valueOf((Duration.between(courseStartTime,
                courseEndTime).toMinutes()));
    }

    //Return the course length in minutes.
    public String getCourseLength() {
        return courseLength;
    }

    //This method will retrieve the course start or end time, in military time format,
    //as a LocalTime object and returns this object to the calling statement.
    private LocalTime setCourseTime(String courseTime, String timeOfDay) {
        LocalTime courseLocalTime;

        //If the course time is at or after 01:00 P.M., then retrieve this value
        //in military time format.
        if (("PM".equalsIgnoreCase(timeOfDay)) && !(courseTime.substring(0, 2).equals("12"))) {
            //Get the hours of the course, which is the first 2 values of the course time string.
            String courseHours = courseTime.substring(1, 2);

            //Add the 12 to the course hours to get the course time in military format.
            String adjustedStart = String.valueOf(Integer.parseInt(courseHours) + 12);

            //Replace the hours in the course time string with the newly formatted military
            //time hours.
            courseTime = courseTime.replace(courseTime.substring(0, 2), adjustedStart);

            //Get the course time as a LocalTime object
            courseLocalTime = LocalTime.parse(courseTime, DateTimeFormatter.ISO_LOCAL_TIME);
        }
        else {
            //Get the course time as a LocalTime object
            courseLocalTime = LocalTime.parse(courseTime, DateTimeFormatter.ISO_LOCAL_TIME);
        }

        //Return the course time as a LocalTime object
        return courseLocalTime;
    }

    //This will return the ArrayList<> of String objects representing the days of the week
    //the course occurs on.
    public ArrayList<String> getCourseDays() {
        return courseDays;
    }

    //This will initialize the ArrayList<> of String objects that represent the days of the
    //week the course occurs on.
    public void setCourseDays(ArrayList<String> courseDays) {
        this.courseDays = courseDays;
    }

    //This returns the ArrayList<> of String objects representing the days of the week the
    //course occurs on as a String, where each String in the ArrayList<> is concatenated with another.
    public String getCourseDaysString() {
        String courseDaysStr = "";

        for (String string: courseDays) {
            courseDaysStr += string + " ";
        }

        return courseDaysStr;
    }

    /*This method overrides the default toString() method of a Course object, and returns
    * a titled and formatted String containing the name, days, and times of the Course.*/
    @Override
    public String toString() {
        return "Course Name: " + this.getCourseName() + ", days: " +
                this.getCourseDays().toString() + ", times: " + this.getCourseStartTime().toString() +
                "-" + this.getCourseEndTime().toString();
    }
}
