package edu.umsl.model;

//This is the Class definition for a Student object, representing the Student of a particular
//schedule.
public class Student {
    private Integer studentId; //The student's ID.
    private String firstName; //The student's first name.
    private String lastName; //The student's last name.

    //Return the studentId.
    public Integer getStudentId() {
        return studentId;
    }

    //Set the studentId.
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    //Return the firstName.
    public String getFirstName() {
        return firstName;
    }

    //Set the firstName.
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //Return the lastName.
    public String getLastName() {
        return lastName;
    }

    //Set the lastName.
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
