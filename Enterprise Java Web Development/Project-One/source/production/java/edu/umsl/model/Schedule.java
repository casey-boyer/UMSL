package edu.umsl.model;

import java.util.*;

//This is the Schedule class definition, which holds the Course object(s) and Student object
//associated with a Student's schedule.
public class Schedule {
    private Student student; //The Student associated with this schedule
    private ArrayList<Course> courses; //The Course(s) associated with this schedule
    private Map<String, ArrayList<Course>> courseMap; //The day associated with each Course
    private String name; //The name of the schedule

    //An empty constructor for the schedule, that initializes the courses ArrayList,
    //Student object, and name to empty values. Invokes the initCourseMap() method to
    //initialize each day of the week for a course.
    public Schedule() {
        this.courses = new ArrayList<Course>();
        this.student = new Student();
        this.name = "";
        this.courseMap = new HashMap<String, ArrayList<Course>>();
        initCourseMap();
    }

    //An argument constructor for the schedule, which initializes the student, courses ArrayList,
    //name of the schedule, and courseMap.
    public Schedule(Student student, ArrayList<Course> courses, String name) {
        this.student = student;
        this.courses = courses;
        this.name = name;
        this.courseMap = new HashMap<String, ArrayList<Course>>();
        initCourseMap();
    }

    //This initializes each key entry of the courseMap HashMap, by providing the String
    //for each day of the week a Course object may occur on, and an empty ArrayList<>
    //representing the Course(s) present on that day.
    private void initCourseMap() {
        courseMap.put("Monday", new ArrayList<>());
        courseMap.put("Tuesday", new ArrayList<>());
        courseMap.put("Wednesday", new ArrayList<>());
        courseMap.put("Thursday", new ArrayList<>());
        courseMap.put("Friday", new ArrayList<>());
    }

    /*This method attempts to add a Course object, course, to the courseMap HashMap and the
    * courses ArrayList. It will iterate through each Course ArrayList<> associated with each
    * key in the courseMap to determine if the course conflicts with any other courses
    * on that key, or day. If so, the conflicting Course object is returned and the Course
    * object is not added to the courseMap or courses ArrayList<>. If no conflicting Course
    * object is found, then null is returned to indicate that the Course object was
    * successfully added to the courseMap and courses ArrayList<>.*/
    public Course addCourse(Course course) {
        /*If adding a course, and a conflict is not found until several iterations through
        * the below loop, then we need to make sure this course is removed entirely from courseMap.
        * To be sure it is removed, initialize an empty Course object 'conflict'; IF a conflicting
        * Course is found, conflict will be assigned this Course object, and since conflict
        * will not be null, removeAllOccurrences() will be invoked to remove any occurrences of
        * this Course in the HashMap (since it cannot be added, but may have been added on a day
        * with no conflicting course).
        * The Course conflict will be returned to indicate that a conflicting Course was found
        * if it is not null.*/
        Course conflict = new Course();

        //See if the Map contains a course that also occurs on this course's day
        if (courseMap == null) {
            System.out.println("course is null");
        }
        else {
            //If the courseMap contains a course/courses on these days, iterate
            //through the course(s) on these days to see if there are conflicting times
            for(int i = 0; i < course.getCourseDays().size(); i++) {
                switch(course.getCourseDays().get(i)) {
                    case "Monday":
                        //Test if there are any other Course objects corresponding to the key Monday
                        if (courseMap.get("Monday").size() > 0) {
                            //Test if there is a conflicting Course object on Monday with this
                            //Course, by invoking compareCourses() with this Course object and the
                            //ArrayList<> of Course objects on Monday
                            conflict = compareCourses(course, courseMap.get("Monday"));

                            //If compareCourses() returned a non-null value, then a conflicting
                            //Course object is present on Monday
                            if (conflict != null) {
                                //Remove all occurrences of this Course object, in case it was
                                //added to other keys in the courseMap without a conflicting Course
                                removeAllOccurrences(course);

                                //Return the conflicting Course object to indicate this Course
                                //could not be added
                                return conflict;
                            }
                        }

                        //Otherwise, add this Course object to the value associated with the
                        //key Monday
                        courseMap.get("Monday").add(course);
                        break;
                    case "Tuesday":
                        //Test if there are any other Course objects corresponding to the key
                        //Tuesday
                        if (courseMap.get("Tuesday").size() > 0) {
                            //Test if there is a conflicting Course object on Tuesday with this
                            //Course, by invoking compareCourses() with this Course object and the
                            //ArrayList<> of Course objects on Tuesday
                            conflict = compareCourses(course, courseMap.get("Tuesday"));

                            //If compareCourses() returned a non-null value, then a conflicting
                            //Course object is present on Tuesday
                            if (conflict != null) {
                                //Remove all occurrences of this Course object, in case it was
                                //added to other keys in the courseMap without a conflicting Course,
                                //and return the conflicting Course object to indicate this Course
                                //could not be added
                                removeAllOccurrences(course);
                                return conflict;
                            }

                        }

                        //Otherwise, add this Course object to the value associated with the
                        //key Tuesday
                        courseMap.get("Tuesday").add(course);
                        break;
                    case "Wednesday":
                        //Test if there are any other Course objects corresponding to the key Wednesday
                        if (courseMap.get("Wednesday").size() > 0) {
                            //Test if there is a conflicting Course object on Wednesday with this
                            //Course, by invoking compareCourses() with this Course object and the
                            //ArrayList<> of Course objects on Wednesday
                            conflict = compareCourses(course, courseMap.get("Wednesday"));

                            //If compareCourses() returned a non-null value, then a conflicting
                            //Course object is present on Wednesday
                            if (conflict != null) {
                                //Remove all occurrences of this Course object, in case it was
                                //added to other keys in the courseMap without a conflicting Course,
                                //and return the conflicting Course object to indicate this Course
                                //could not be added
                                removeAllOccurrences(course);
                                return conflict;
                            }

                        }
                        //Otherwise, add this Course object to the value associated with the
                        //key Wednesday
                        courseMap.get("Wednesday").add(course);
                        break;
                    case "Thursday":
                        //Test if there are any other Course objects corresponding to the key Thursday
                        if (courseMap.get("Thursday").size() > 0) {
                            //Test if there is a conflicting Course object on Thursday with this
                            //Course, by invoking compareCourses() with this Course object and the
                            //ArrayList<> of Course objects on Thursday
                            conflict = compareCourses(course, courseMap.get("Thursday"));

                            //If compareCourses() returned a non-null value, then a conflicting
                            //Course object is present on Thursday
                            if (conflict != null) {
                                //Remove all occurrences of this Course object, in case it was
                                //added to other keys in the courseMap without a conflicting Course,
                                //and return the conflicting Course object to indicate this Course
                                //could not be added
                                removeAllOccurrences(course);
                                return conflict;
                            }

                        }
                        //Otherwise, add this Course object to the value associated with the
                        //key Thursday
                        courseMap.get("Thursday").add(course);
                        break;
                    case "Friday":
                        //Test if there are any other Course objects corresponding to the key Friday
                        if (courseMap.get("Friday").size() > 0) {
                            //Test if there is a conflicting Course object on Friday with this
                            //Course, by invoking compareCourses() with this Course object and the
                            //ArrayList<> of Course objects on Friday
                            conflict = compareCourses(course, courseMap.get("Friday"));

                            //If compareCourses() returned a non-null value, then a conflicting
                            //Course object is present on Friday
                            if (conflict != null) {
                                //Remove all occurrences of this Course object, in case it was
                                //added to other keys in the courseMap without a conflicting Course,
                                //and return the conflicting Course object to indicate this Course
                                //could not be added
                                removeAllOccurrences(course);
                                return conflict;
                            }

                        }
                        //Otherwise, add this Course object to the value associated with the
                        //key Friday
                        courseMap.get("Friday").add(course);
                        break;
                }
            }
        }

        //If this point is reached, then no compareCourses() did not return a Course object
        //to indicated a conflict. Thus, this course may be added to the courses ArrayList<>.
        courses.add(course);

        //Return null to indicate that this Course was successfully added with no conflict.
        return null;
    }

    /*In case a Course that was added to the schedule was found to conflict
    * with the times of another course on a specific day, make sure that all occurrences of this course
    * are removed from the map.*/
    private void removeAllOccurrences(Course course) {
        for (int i = 0; i < course.getCourseDays().size(); i++) {
            //If the key (a day of the week, which returns an ArrayList) contains this course, remove it
            if (courseMap.get(course.getCourseDays().get(i)).contains(course)) {
                //Remove the course from the ArrayList<Course> that occurs at this day
                courseMap.get(course.getCourseDays().get(i)).remove(course);
            }
        }
    }

    /*This is a simple method that prints the ArrayList<Course> value associated with each
    * day of the week (String) in the courseMap HashMap to the console.*/
    public void printCourses() {
        System.out.println("-------\nPrinting courses:\n");
        for (Map.Entry<String, ArrayList<Course>> entry : courseMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + "\tValue = " + entry.getValue());
        }
    }

    /*This method will compare the LocalTime start time and end time member variables of
    * the course trying to be added to the Courses ArrayList<> at a day (key). It will
    * return a Course object, specifically a Course that conflicts with the course being added,
    * if there is an illegal time overlap; otherwise, it will return null to indicate that
    * the course being added did not conflict with any Course(s) associated with this day.*/
    private Course compareCourses(Course courseOne, ArrayList<Course> otherCourses) {
        /*Check if the course start times are the same
        * Check if the course end times are the same
        * Check if the course starts at the time another ends, vice versa..
        * Check if the new course occurs at any point during any other course
        *
        * If a conflict is found, return the Course object that this new course conflicts with;
        * otherwise, return null to indicate no conflict was found*/

        /*Iterate through the ArrayList of Course objects, comparing the LocalTime member
        * variables of each Course in this array with the courseOne Course object that is
        * trying to be added*/
        for (Course otherCourse : otherCourses) {
            /*If the start time of the Course object being added is before the start time
            * of the current Course object in the ArrayList*/
            if (courseOne.getCourseStartTime().isBefore(otherCourse.getCourseStartTime())) {
                /*Then, if the end time of the Course object being added is before the start
                * time of the current Course object in the ArrayList, return this course in the
                * array to indicate that the Course object being added conflicts with this course.
                * This means that the course being added does not end before this course starts,
                * so there is an 'illegal' time overlap.*/
                if (!(courseOne.getCourseEndTime().isBefore(otherCourse.getCourseStartTime()))) {
                    return otherCourse;
                }
            }
            else if (courseOne.getCourseStartTime().isAfter(otherCourse.getCourseStartTime())) {
                /*^^If the start time of the Course object being added is after the start time
                * of the current Course object in the ArrayList*/

                /*Then, if the start time of the Course object being added is after the end time
                * of the current Course object in the ArrayList, return this course in the
                * array to indicate that the Course object being added conflicts with this course.
                * This means that the course being added starts after the start time of this course,
                * but does not end after the end time of this course, so there is an 'illegal' time
                * overlap.*/
                if (!(courseOne.getCourseStartTime().isAfter(otherCourse.getCourseEndTime()))) {
                    return otherCourse;
                }
            }
            else if (courseOne.getCourseStartTime().equals(otherCourse.getCourseStartTime())) {
                /*If the start time of the Course object being added equals the start time
                * of the current Course object in the ArrayList, then the courses begin at the
                * same time and there is an 'illegal' time overlap. Return this Course in the
                * array to indicate that it conflicts with the Course being added.*/
                return otherCourse;
            }
            else if (courseOne.getCourseEndTime().equals(otherCourse.getCourseEndTime())) {
                /*If the end time of the Course object being added equals the end time of the
                * current Course object in the ArrayList, then the courses end at the
                * same time and there is an 'illegal' time overlap. Return this Course in the
                * array to indicate that it conflicts with the Course being added.*/
                return otherCourse;
            }
        }

        /*If none of the above conditions evaluated to true, then this course does not conflict
        * with any other Course object on this day (corresponding to this key). Return null
        * to indicate that the course may be added.*/
        return null;
    }

    /*This method will sort each ArrayList<> in the HashMap according to the Course start time,
    * so that each ArrayList<> value will always begin with the course at the 'beginning' of the
     * day, and always end with the course at the 'end' of the day.*/
    public void getOrderedCourses() {
        Collections.sort(courseMap.get("Monday"), (courseOne, courseTwo) ->
                courseOne.getCourseStartTime().compareTo(courseTwo.getCourseStartTime()));
        Collections.sort(courseMap.get("Tuesday"), (courseOne, courseTwo) ->
                courseOne.getCourseStartTime().compareTo(courseTwo.getCourseStartTime()));
        Collections.sort(courseMap.get("Wednesday"), (courseOne, courseTwo) ->
                courseOne.getCourseStartTime().compareTo(courseTwo.getCourseStartTime()));
        Collections.sort(courseMap.get("Thursday"), (courseOne, courseTwo) ->
                courseOne.getCourseStartTime().compareTo(courseTwo.getCourseStartTime()));
        Collections.sort(courseMap.get("Friday"), (courseOne, courseTwo) ->
                courseOne.getCourseStartTime().compareTo(courseTwo.getCourseStartTime()));
    }

    //This method will return the Student object associated with this schedule.
    public Student getStudent() {
        return student;
    }

    //This method will set the Student object associated with this schedule.
    public void setStudent(Student student) {
        this.student = student;
    }

    //This method will return the ArrayList of Course objects associated with this schedule.
    public ArrayList<Course> getCourses() {
        return courses;
    }

    //This method will set the ArrayList of Course objects associated with this schedule.
    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    //This method will return the courseMap HashMap associated with this schedule.
    public Map<String, ArrayList<Course>> getCourseMap() {
        return this.courseMap;
    }

    //This method will return the name associated with this schedule.
    public String getName() {
        return this.name;
    }

    //This method will set the name associated with this schedule.
    public void setName(String name) {
        this.name = name;
    }
}
