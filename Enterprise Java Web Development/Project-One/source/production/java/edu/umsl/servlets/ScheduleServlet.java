package edu.umsl.servlets;

import edu.umsl.model.Course;
import edu.umsl.model.Schedule;
import edu.umsl.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(
        name = "scheduleServlet",
        urlPatterns = "/schedule"
)
public class ScheduleServlet extends HttpServlet {

    //Invoke doPost()
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Get the session
        HttpSession session = request.getSession();

        //Get the value of the hidden input field, with the name action
        String action = request.getParameter("action");

        if (action == null) {
            //If action is null, redirect to course schedule page
            request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp")
                    .forward(request, response);
        }

        //If action is not null
        switch(action) {
            case "add": //If action is "add", add the course entered by the user
                this.addCourse(request, response, session);
                break;
            case "default":
                break;
        }
    }

    private void addCourse(HttpServletRequest request,
                                HttpServletResponse response,
                           HttpSession session)
            throws ServletException, IOException
    {
        //Get the Schedule object and Student object associated with this session
        Schedule schedule = (Schedule) session.getAttribute("schedule");
        Student student = (Student) session.getAttribute("student");

        //If the Schedule object is null, create the schedule object. The Student object
        //will not be null, since the student's information must be entered in the form on the
        //studentInfo.jsp page, and upon successful form completion, that page will redirect to
        //the schedule.jsp page where a student may add a course and submit the schedule.
        if (schedule == null) {
            schedule = new Schedule();
            schedule.setStudent(student);
        }

        /*Get the value of the form elements for adding a course in the courseForm on the schedule.jsp
        * page. This includes the course name, course number, course start time and end time,
        * and the days selected for the course.*/
        String courseName = request.getParameter("cName");
        String courseNumber = request.getParameter("cNum");
        String courseStartTime = request.getParameter("cStart");
        String courseEndTime = request.getParameter("cEnd");
        String courseStartTimeOfDay = request.getParameter("courseStartTimeOfDay");
        String courseEndTimeOfDay = request.getParameter("courseEndTimeOfDay");

        //Initialize an empty ArrayList<> for the days the course occurs on.
        ArrayList<String> courseDays = new ArrayList<String>();

        //Get the days that were 'checked' for the course.
        String[] selectedCourseDays = request.getParameterValues("courseDays");

        //Loop through each day that was selected for the course, and add it to the
        //courseDays ArrayList.
        for (String selectedCourseDay : selectedCourseDays) {
            switch (selectedCourseDay) {
                case "MONDAY":
                    courseDays.add("Monday");
                    break;
                case "TUESDAY":
                    courseDays.add("Tuesday");
                    break;
                case "WEDNESDAY":
                    courseDays.add("Wednesday");
                    break;
                case "THURSDAY":
                    courseDays.add("Thursday");
                    break;
                case "FRIDAY":
                    courseDays.add("Friday");
                    break;
            }
        }

        //Create a new course object with the information retrieved from the courseForm.
        Course course = new Course(courseName, Integer.parseInt(courseNumber),
                courseStartTime, courseEndTime, courseDays);

        //Set the LocalTime courseStartTime and courseEndTime member variables of the Course
        //object, and include the 'PM' or 'AM' values for these times.
        course.setCourseStartTime(courseStartTime, courseStartTimeOfDay);
        course.setCourseEndTime(courseEndTime, courseEndTimeOfDay);
        course.setCourseStart(courseStartTime, courseStartTimeOfDay);
        course.setCourseEnd(courseEndTime, courseEndTimeOfDay);

        //Set the length of the course, in minutes, and the course start time and end time,
        //in minutes.
        course.setCourseLength();
        course.setCourseStartMinutes();
        course.setCourseEndMinutes();

        //Attempt to add this Course object to the Schedule object.
        Course conflict = schedule.addCourse(course);

        //If the addCourse() method returned a non-null Course object, then this Course
        //object could not be added because it conflicted with another Course. Set the "conflict"
        //attribute of the session object, which is the course that this Course conflicted with,
        //as well as the "addedCourse" attribute, which was the Course that was unable to be added.
        //These will be used to display an error message on the schedule.jsp page, indicating that
        //these two courses conflicted with each other and that the Course object could not be added.
        if (conflict != null) {
            session.setAttribute("conflict", conflict);
            session.setAttribute("addedCourse", course);
        }
        else //Remove the conflict attribute if it was previously set
            session.removeAttribute("conflict");

        //Helper method, printCourses(), that will print the current courseMap of Course objects
        //to the console
        System.out.println("-----\nAFTER ADDING COURSES:");
        schedule.printCourses();

        //Make sure the ArrayList<> corresponding to each key in courseMap is ordered, based on
        //a 24-hour clock of the Course object times
        schedule.getOrderedCourses();

        //Set the schedule attribute
        session.setAttribute("schedule", schedule);

        //Redirect to course schedule page
        request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp")
                .forward(request, response);
    }
}
