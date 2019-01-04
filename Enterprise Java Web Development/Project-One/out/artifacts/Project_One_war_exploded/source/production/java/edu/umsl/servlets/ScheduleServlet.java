package edu.umsl.servlets;

import edu.umsl.model.Course;
import edu.umsl.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@WebServlet(
        name = "scheduleServlet",
        urlPatterns = "/schedule"
)
public class ScheduleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("in schedule servlet");

        HttpSession session = request.getSession();

        String action = request.getParameter("action");

        if (action == null) {
            System.out.println("action is null");
            //Redirect to course schedule page
            request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp")
                    .forward(request, response);
        }

        switch(action) {
            case "add":
                this.addCourse(request, response, session);
                break;
            case "default":
                System.out.println("default case");
                break;
        }
    }

    /*Need to check if courses conflict with each other...I guess this means there
    * needs to be a time with the courses?
    * When parsing the course ID and course number, some values i.e. '001' ARE NOT
    * VALID INTEGERS, how to handle this? Could just represent it as a string; check if
    * this should be done for student id too*/
    private void addCourse(HttpServletRequest request,
                                HttpServletResponse response,
                           HttpSession session)
            throws ServletException, IOException
    {
        if (session.getAttribute("student") == null) {
            addStudent(request, response, session);
        }

        System.out.println("adding course info");
        ArrayList<Course> courses = (ArrayList<Course>) session.getAttribute("courses");


        if (courses == null) {
            courses = new ArrayList<Course>();
        }

        String courseId = request.getParameter("cId");
        String courseName = request.getParameter("cName");
        String courseNumber = request.getParameter("cNum");
        String courseStartDate = request.getParameter("cStart");
        String courseEndDate = request.getParameter("cEnd");
        ArrayList<String> courseDays = new ArrayList<String>();

        String[] selectedCourseDays = request.getParameterValues("courseDays");

        for (String selectedCourseDay : selectedCourseDays) {
            switch (selectedCourseDay) {
                case "SUNDAY":
                    courseDays.add("Sunday");
                    break;
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
                case "SATURDAY":
                    courseDays.add("Saturday");
                    break;
            }
        }

        courses.add(new Course(Integer.parseInt(courseId), courseName, Integer.parseInt(courseNumber),
                courseStartDate, courseEndDate, courseDays));

        session.setAttribute("courses", courses);

        //Redirect to course schedule page
        request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp")
                .forward(request, response);
    }

    private void addStudent(HttpServletRequest request,
                            HttpServletResponse response,
                            HttpSession session)
            throws ServletException, IOException
    {
        System.out.println("adding student info");
        String studentId = request.getParameter("sId");
        String firstName = request.getParameter("fName");
        String lastName = request.getParameter("lName");

        Student student = new Student();
        student.setStudentId(Integer.parseInt(studentId));
        student.setFirstName(firstName);
        student.setLastName(lastName);

        session.setAttribute("student", student);
    }
}
