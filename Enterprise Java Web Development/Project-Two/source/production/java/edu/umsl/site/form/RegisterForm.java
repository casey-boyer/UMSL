package edu.umsl.site.form;

import edu.umsl.site.entity.StateMap;
import edu.umsl.site.entity.User;

import java.util.LinkedHashMap;
import java.util.Map;

/*All of the fields necessary for the registration form*/
public class RegisterForm {
    private User user;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String addressStreetOne;
    private String addressStreetTwo;
    private String city;
    private String state;
    private StateMap stateMap;
    private String zipcode;


    public RegisterForm() {
        stateMap = new StateMap();
    }

    public StateMap getStateMap() {
        return stateMap;
    }

    public User getUser() {
        return user;
    }

    public void setUser() {
        User user = new User(username, password, firstName, lastName, addressStreetOne,
                addressStreetTwo, city, state, zipcode);
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

    public String getAddressStreetOne() {
        return addressStreetOne;
    }

    public void setAddressStreetOne(String addressStreetOne) {
        this.addressStreetOne = addressStreetOne;
    }

    public String getAddressStreetTwo() {
        return addressStreetTwo;
    }

    public void setAddressStreetTwo(String addressStreetTwo) {
        this.addressStreetTwo = addressStreetTwo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
