package edu.umsl.stopwatch

import android.os.SystemClock
import android.util.Log

class MainModel {

    private var timeStamp: String? = null /*The timeStamp which will reflect the elapsed time*/
    private var lapNumbers: Int = 0 /*Number of laps thus far*/
    private var startTime: Long = 0 /*The time the timer started*/
    private var minutes: Long = 0 /*The time in minutes of elapsed time*/
    private var seconds: Long = 0 /*The time in seconds of elapsed time*/
    private var milliseconds: Long = 0 /*The time in milliseconds of elapsed time*/

    constructor() {
        timeStamp = "00:00:000" /*Assign an initial value to indicate no time has elapsed.*/
    }

    /*Get the timeStamp.*/
    fun getTimeStamp() : String {
        return timeStamp ?: "0"
    }

    /*Get the number of laps.*/
    fun getLapNumbers(): Int {
        return lapNumbers
    }

    /*Get the time the timer started.*/
    fun getStartTime() : Long {
        /*startTime is the time since the system was booted.*/
        startTime = SystemClock.elapsedRealtime()
        return startTime
    }

    /*updateLaps() simply increments the lapNumbers variable to indicate a lap was added.*/
    fun updateLaps() {
        lapNumbers++
    }

    /*updateTime() accepts the total elapsedTime thus far, and retrieves the amount of
    * minutes, seconds, and milliseconds to format it as a string "MM:SS:MS"*/
    fun updateTime(elapsedTime: Long) {
        /*minutes is the elapsedTime divided by 1000 (this yields seconds), and the result divided by 60*/
        minutes = ( (elapsedTime / 1000) / 60)

        /*seconds is the elapsedTime divided by 1000 and the remainder of this result divided
        * by 60 (get seconds as a whole number)*/
        seconds = ( (elapsedTime / 1000) % 60)

        /*milliseconds is the remainder of the elapsedTime divided by 1000
        * (get milliseconds as a whole number)*/
        milliseconds = (elapsedTime % 1000)


        timeStamp = ""

        /*How to format the string.
        * If minutes, seconds, or milliseconds is between 1-10, then it should be padded
        * with a zero.
        * If minutes, seconds, or milliseconds is greater than 10, no padding is necessary.
        * Otherwise, if the values are 0, then the timeStamp value for the time unit is "00"*/

        if ( (minutes > 0) && (minutes < 10)) {
            Log.e("Model", "updateTime, minutes<0&>10")
            timeStamp += "0" + minutes.toString() + ":"
        }
        else if (minutes > 10) {
            Log.e("Model", "updateTime, minutes>10")
            timeStamp += minutes.toString() + ":"
        }
        else {
            Log.e("Model", "updateTime, minutes == 0")
            timeStamp += "00:"
        }

        if ( (seconds > 0) && (seconds < 10))
            timeStamp += "0" + seconds.toString() + ":"
        else if (seconds > 10)
            timeStamp += seconds.toString() + ":"
        else
            timeStamp += "00:"


        if ( (milliseconds > 0) && (milliseconds < 10))
            timeStamp += "0" + milliseconds.toString()
        else if (milliseconds > 10)
            timeStamp += milliseconds.toString()
        else
            timeStamp += "00"
    }

    /*Method to reset the data whenever the reset button is clicked.
    * Reassign all variables to 0, and reassign the time stamp to "00:00:00"*/
    fun reset() {
        timeStamp = "00:00:00"
        lapNumbers = 0
        startTime = 0
        minutes = 0
        seconds = 0
        milliseconds = 0
    }

}