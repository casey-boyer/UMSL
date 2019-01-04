package edu.umsl.site.form;

import edu.umsl.site.entity.User;

/*All of the fields necessary for the sign in form*/
public class SignInForm {
    private User user;
    private String username;
    private String password;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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
}
