package edu.umsl.site.form;

import javax.xml.bind.annotation.XmlRootElement;

/*This form contains the values for creating or updating an existing user. Since the user ID and time
* created are handled by the UserRepository and UserManager, these fields are not present in
* the user form, as the user will not be providing these.*/
@XmlRootElement(name = "user")
public class UserForm {
    private String username;
    private String password;
    private String firstName;
    private String lastName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
