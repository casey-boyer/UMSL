package edu.umsl.site.entity;

import javax.servlet.http.Part;
import java.io.InputStream;
import java.util.Map;

public class User
{
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String addressStreetOne;
    private String addressStreetTwo;
    private String city;
    private String state;
    private String timezone;
    private StateMap stateMap = new StateMap();
    private TimeZoneMap timeZoneMap = new TimeZoneMap();
    private String zipcode;
    private String birthday;
    private String phoneNumber;
    private String cellPhoneNumber;
    private String areasOfInterest;

    //The image file of the user's profile picture, and the byte array corresponding to this image file.
    private Part profilePicture;
    private byte[] profilePictureContents;

    public User() {}

    //Only two fields, for the sign-in form
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //All fields up to zipcode, for the register form
    public User(String username, String password, String firstName, String lastName,
                String addressStreetOne, String addressStreetTwo, String city,
                String state, String zipcode) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressStreetOne = addressStreetOne;
        this.addressStreetTwo = addressStreetTwo;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    //toString method for debugging purposes
    @Override
    public String toString() {
        String userString =  "\n\tUsername: " + username + "\n\tPassword: " + password +
                "\n\tAddress street one: " + addressStreetOne +
                "\n\tAddress street two: " + addressStreetTwo +
                "\n\tCity: " + city + "\n\tState: " + state + "\n\tZipcode: " + zipcode;

        return userString;
    }

    public Part getProfilePicture() {
        return profilePicture;
    }

    public void Part(Part profilePicture) {
        this.profilePicture = profilePicture;
    }

    public StateMap getStateMap() {
        return stateMap;
    }

    public void setStateMap() {
        this.stateMap = new StateMap();
    }

    public TimeZoneMap getTimeZoneMap() {
        return timeZoneMap;
    }

    public void setTimeZoneMap(TimeZoneMap timeZoneMap) {
        this.timeZoneMap = timeZoneMap;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
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

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getAreasOfInterest() {
        return areasOfInterest;
    }

    public void setAreasOfInterest(String areasOfInterest) {
        this.areasOfInterest = areasOfInterest;
    }

    public byte[] getProfilePictureContents() {
        return profilePictureContents;
    }

    public void setProfilePictureContents(byte[] profilePictureContents) {
        this.profilePictureContents = profilePictureContents;
    }
}
