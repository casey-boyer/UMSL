package edu.umsl.simonsays;

import android.support.annotation.StringRes;

/*This class will store the String Resource IDs associated with each button, and will be
* used exclusively by the ColorModel class.*/
public class ColorOptions {

    /*The textResId variable will hold the String Resource ID of the color of
    * the corresponding button.*/
    private int textResId;

    /*A one-argument constructor which accepts a String Resource ID as an argument,
    * and initializes the textResId variable to this value.*/
    public ColorOptions(@StringRes int id) {
        this.textResId = id;
    }

    /*A getter method which returns the value of the textResId variable.*/
    public int getTextResId() {
        return textResId;
    }

    /*A setter method which sets the value of the textResId variable to the argument.*/
    public void setTextResId(int textResId) {
        this.textResId = textResId;
    }
}
