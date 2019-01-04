package edu.umsl.servlets;

import edu.umsl.model.Course;
import edu.umsl.model.Schedule;
import edu.umsl.model.Student;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

@WebServlet (
    name = "DBConnServlet",
    urlPatterns = "/DBConn"
)
public class DBConnServlet extends HttpServlet {
    private Connection connection;

    //doPost() instead
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

        //Get the value of the hidden input field with the name action
        String action = request.getParameter("action");

        if (action == null) {
            //If action is null, redirect to course schedule page
            request.getRequestDispatcher("/WEB-INF/jsp/index.jsp")
                    .forward(request, response);
        }

        //If action is not null
        switch(action) {
            case "student":
                //If the value of action is "student", then a Student is being added to the database.

                //If the session currently has a Student object associated with it, then forward
                //the request to the schedule.jsp page and do not add the Student to the database.
                if (session.getAttribute("student") != null) {
                    request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp")
                            .forward(request, response);
                    return;
                }

                //Otherwise, get a Student object from the form elements of the studentForm on
                //the studentInfo.jsp page
                Student newStudent = addStudent(request, response);

                //Attempt to add the Student to the database
                int result = insertStudent(newStudent);

                if (result == 0) {
                    //If an entry in the database already contains this Student, specifically this
                    //student id which MUST be unique, then initialize an error message stating so
                    //and set the errorMessage in the request object so it may be displayed on
                    //the studentInfo.jsp page
                    String errorMessage = "This student already exists in the database." +
                            " Re-enter your information and try again.";
                    request.setAttribute("errorMessage", errorMessage);

                    //Forward the request to the studentInfo.jsp page
                    request.getRequestDispatcher("/WEB-INF/jsp/studentInfo.jsp")
                            .forward(request, response);
                }
                else {
                    //Otherwise, set the student attribute of the session as the newly created
                    //Student object
                    session.setAttribute("student", newStudent);

                    //Forward the request to the schedule.jsp page
                    request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp")
                            .forward(request, response);
                }
                break;
            case "confirm":
                //If the value of action is "confirm", then the user is submitting the
                //schedule created on the schedule.jsp page to the database.

                //Get the Schedule object associated with the session
                Schedule schedule = (Schedule) session.getAttribute("schedule");

                if (schedule == null) {
                    //If the Schedule object is null, then the user submitted the schedule without
                    //adding any courses. Initialize an error message stating so, and set the
                    //errorMessage attribute of the request object to this String.
                    String errorMessage = "You must create a schedule with at least one course, " +
                            " and provide your student information.";
                    request.setAttribute("errorMessage", errorMessage);

                    //Forward the request back to the schedule.jsp page so the error may be
                    //displayed
                    request.getRequestDispatcher("/WEB-INF/jsp/schedule.jsp")
                            .forward(request, response);
                    return;
                }

                //Otherwise, set the name of the Schedule object given by the
                //sName input field of the submitForm form on the schedule.jsp page
                if (request.getParameter("sName") != null)
                    schedule.setName(request.getParameter("sName"));

                //Get the student associated with the session
                Student student = (Student) session.getAttribute("student");

                //Insert the courses provided by the user to the Course table in the database,
                //and insert the schedule provided by the user to the Schedule table in
                //the database
                insertCourses(schedule.getCourses(), student);
                insertSchedule(schedule, student);

                //Forward the request to the confirm.jsp page to display the user's submitted
                //information
                request.getRequestDispatcher("/WEB-INF/jsp/confirm.jsp")
                        .forward(request, response);
                break;
        }
    }

    //Initialize method of the DBConn servlet
    @Override
    public void init(ServletConfig config) throws ServletException {
        //Define connection to database
        try {
            //Get the driver for the database
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //Connect to the 'enterprise' database
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/enterprise",
                    "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.init(config);
    }

    //Return the Student object with the fields provided in the studentForm on
    //the studentInfo.jsp page
    private Student addStudent(HttpServletRequest request,
                            HttpServletResponse response)
            throws ServletException, IOException
    {
        //Get the studentId, firstName, and lastName of the student by
        //getting the values of the sId, fName, and lName input fields in the
        //studentForm form
        String studentId = request.getParameter("sId");
        String firstName = request.getParameter("fName");
        String lastName = request.getParameter("lName");

        //Create the student object with these values
        Student student = new Student();
        student.setStudentId(Integer.parseInt(studentId));
        student.setFirstName(firstName);
        student.setLastName(lastName);

        //Return the Student object
        return student;
    }

    /*This method will attempt to insert a row into the student table, based on the Student
    * object passed as an argument. It will return 1 if the row was successfully inserted, or
    * 0 otherwise.*/
    private int insertStudent(Student student) {
        //Initialize a null PreparedStatement for the insert MySQL query.
        PreparedStatement ps = null;

        //Insert into the student_id, firstname, and lastname columns of the student table
        String insertStudent = "INSERT INTO student (student_id, firstname, lastname) " +
                "VALUES (?, ?, ?)";

        //Try to execute the query
        try {
            //Create the PreparedStatement object
            ps = connection.prepareStatement(insertStudent);

            //Set the value of each parameter in the VALUES(?, ?, ?) portion of the prepared
            //statement, which is the studentId, firstName, and lastName of the Student object,
            //respectively
            ps.setString(1, String.valueOf(student.getStudentId()));
            ps.setString(2, student.getFirstName());
            ps.setString(3, student.getLastName());

            //This will execute the query and return the number of rows inserted into the student table,
            //which if the query executed successfully, is 1
            return ps.executeUpdate();
        } catch (SQLException e) {
            //If the query did not execute successfully and threw a SQLException, return 0
            //to indicate that 0 rows were inserted into the student table
            e.printStackTrace();
            return 0;
        }
    }

    /*This method will insert all of the Course objects provided by the courses ArrayList
    * into the course table, and will associate each Course object in the course table
    * with the studentId attribute of the Student object.*/
    private int insertCourses(ArrayList<Course> courses, Student student) {
        //Initialize a null PreparedStatement for the insert MySQL query.
        PreparedStatement ps = null;

        //Insert into the coursename, coursenumber, coursedays, coursetimes, and s_id columns
        //of the course table
        String insertCourses = "INSERT INTO course (coursename, coursenumber, coursedays, coursetimes, s_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            //Create the PreparedStatement object
            ps = connection.prepareStatement(insertCourses);
            int rowsInserted = 0;

            for (Course course : courses) {

                //Invoke getCourse() to test if the Course object already exists in the
                //course table. If getCourse() returns 0, then the course is present in the table;
                //otherwise, it may be added. This is to prevent duplicate courses being added to the
                //course table in case the user presses back after submitting,
                //and attempts to add more courses.
                if (getCourse(course, student) != 0) {
                    System.out.println("adding course: " + course.toString());

                    //For each Course object in the ArrayList of Course object(s), attempt
                    //to insert the values of the member variables courseName, courseNumber,
                    //courseDays, courseStart and courseEnd into the first 4
                    //parameters of the PreparedStatement ps. For the last parameter of the
                    //PreparedStatement ps, insert the studentId member variable of the Student
                    //object.
                    ps.setString(1, course.getCourseName());
                    ps.setString(2, String.valueOf(course.getCourseNumber()));
                    ps.setString(3, course.getCourseDaysString());
                    ps.setString(4, course.getCourseStart() + " " + course.getCourseEnd());
                    ps.setString(5, String.valueOf(student.getStudentId()));

                    //This will execute the query and update the rows inserted into the
                    //course table during this transaction, which should be incrementing rowsInserted
                    //by 1 each time if successful
                    rowsInserted += ps.executeUpdate();
                }
            }

            //Return the number of rowsInserted
            return rowsInserted;
        } catch (SQLException e) {
            //If a SQLException was thrown, return 0 to indicate that no rows were inserted
            //into the course table
            e.printStackTrace();
            return 0;
        }
    }

    /*This method will select the row from the course table where the s_id, studentId
    attribute of the Student object, and the coursenumber, courseNumber attribute of the
    Course object, are as provided by the method arguments. If the Select query returns a row,
    then this course is already in the course table; return 0 to indicate that it should
    not be inserted.*/
    private int getCourse(Course course, Student student) {
        //Initialize a null PreparedStatement object and a null ResultSet object, for
        //the execution of the SELECT query and the returned rows.
        PreparedStatement ps = null;
        ResultSet rs = null;

        //Select the entire row from the course table where
        //the student id, s_id, and the course number of the Course
        //object, coursenumber, are as specified from the Student and Course objects
        String getCourse = "SELECT * FROM course WHERE s_id = ? AND coursenumber = ?";

        try {
            //Create the PreparedStatement object
            ps = connection.prepareStatement(getCourse);

            //Set the first parameter of the PreparedStatement object, which is the studentId
            //attribute from the Student object. Set the second parameter of the PreparedStatement
            //object, which is the member variable courseNumber of the Course object
            ps.setString(1, String.valueOf(student.getStudentId()));
            ps.setString(2, String.valueOf(course.getCourseNumber()));

            //Execute the query, which should return a ResultSet of the row retrieved if
            //successful
            rs = ps.executeQuery();

            //If the ResultSet object has a row, then rs.next() will evaluate to true
            if (rs.next()) {
                //Return 0 to indicate that this course is already in the database, and does
                //not need to be added again.
                return 0;
            }
        } catch (SQLException e) {
            //If a SQLException was thrown, return -1 to indicate that the query could
            //not be executed.
            e.printStackTrace();
            return -1;
        }

        //If the query was executed and rs.next() did not retrieve any rows, then this course
        //is not in the course table. Return 1 to indicate that it may be added to the course table.
        return 1;
    }

    /*This method will insert the Schedule object into the schedule table, where the studentId
    * attribute of the Student object corresponds to the s_id column in the schedule table.*/
    private int insertSchedule(Schedule schedule, Student student) {
        //Initialize a null PreparedStatement object.
        PreparedStatement ps = null;

        //Insert the schedulename, the name attribute of the Schedule object, and the s_id,
        //the studentId attribute of the Student object, into the schedule table.
        String insertSchedule = "INSERT INTO schedule (schedulename, s_id) " +
                "VALUES (?, ?)";

        //Test if the schedule already exists in the schedule table; this is to ensure that the
        //schedule is not inserted into the table multiple times in the event that the user presses
        //back after confirming their schedule, adds more courses, and resubmits their schedule.
        int scheduleExists = getSchedule(student);

        //If getSchedule() returns 0, then the schedule exists in the table. Otherwise, insert
        //the schedule into the schedule table.
        if (scheduleExists != 0) {
            try {
                //Create the PreparedStatement object
                ps = connection.prepareStatement(insertSchedule);

                //Initialize the VALUES (?, ?) parameters to the name attribute of the Schedule
                //object and the studentId attribute of the Student object, respectively
                ps.setString(1, schedule.getName());
                ps.setString(2, String.valueOf(student.getStudentId()));

                //Return the amount of rows inserted into the table; if successful, this will
                //return 1.
                return ps.executeUpdate();
            } catch (SQLException e) {
                //If a SQLException was thrown, return 0 to indicate that 0 rows were inserted.
                e.printStackTrace();
                return 0;
            }
        }
        else {
            //If the schedule already exists, return 0 to indicate that no rows were inserted
            //into the table.
            return 0;
        }
    }

    //This method will retrieve a row from the schedule table, where the s_id column (studentId)
    //corresponds to the studentId attribute of the Student object passed as an argument. If the
    //Select query returns a ResultSet, then 0 is returned to indicate that this schedule should
    //not be reinserted into the table since it already exists.
    private int getSchedule(Student student) {
        //Initialize a null PreparedStatement object and a null ResultSet object, where the PreparedStament
        //object will execute the MySQL Select query and the ResultSet object will return the
        //number of rows from the Select query.
        PreparedStatement ps = null;
        ResultSet rs = null;

        //Select the schedule_id entry in the schedule table where the student ID, s_id,
        //in the row is the studentId attribute of the Student object
        String getSchedule = "SELECT schedule_id FROM schedule WHERE s_id = ?";

        try {
            //Create the PreparedStaement object
            ps = connection.prepareStatement(getSchedule);

            //Set the s_id, studentId, parameter of the PreparedStatement query, which corresponds
            //to the studentId attribute of the Student object.
            ps.setString(1, String.valueOf(student.getStudentId()));

            //Execute the query and assign the returned ResultSet object to the rs object.
            rs = ps.executeQuery();

            /*If the schedule exists in the table, return 0 to indicate this
            * row does not need to be inserted*/
            if (rs.next()) {
                return 0;
            }

            //Otherwise, return 1 to indicate that the schedule may be inserted into the table
            return 1;
        } catch (SQLException e) {
            //If a SQLException was thrown and the query could not be executed, return -1
            e.printStackTrace();
            return -1;
        }
    }
}
