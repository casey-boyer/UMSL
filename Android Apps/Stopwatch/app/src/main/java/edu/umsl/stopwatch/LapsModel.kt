package edu.umsl.stopwatch

import android.util.Log
import edu.umsl.stopwatch.database.LapsPersistence
import java.util.*

class LapsModel(private val persistence: LapsPersistence) {
    private var idKey: Int = 0 /*To keep track of correct values for the primary key in the database*/

    /*The total laps thus far*/
    var laps: List<Laps> = persistence.getAllLaps()

    /*Retrieve the total amount of laps whenever laps are added*/
    fun updateLaps() {
        laps = persistence.getAllLaps()
    }

    /*Insert a lap into the database. This involves calculating the "lap interval",
    * or the difference between the previous lap's timestamp and the current laps's timestamp.
    * The arguments, lapNumber and timeStamp, will be stored in the laps database.*/
    fun insertLap(lapNumber: Int, timeStamp: String) {
        var lapInterval: String = timeStamp /*Initially set the lapInterval to the timeStamp.*/

        /*If the idKey > 0, then there is a previous lap in the database & so the lapInterval
        * must be calculated.*/
        if (idKey > 0) {
            /*Get the previous lap in the database by decrementing idKey by 1.*/
            val prevLap: Laps = persistence.getLapAtPosition((idKey - 1))

            /*The prevTime is the time the previous lap was added, and the
            * curTime is the time the current lap was added.*/
            var prevTime: Double = tokenizeString(prevLap.timestamp)
            var curTime: Double = tokenizeString(timeStamp)
            var timeDiff: Double = 0.0

            if ( (prevTime != 0.0) && (curTime != 0.0)) {

                /*The time difference between each time intervals multipled by 60 and divided
                * by 1000 to get this as milliseconds*/
                timeDiff = ((curTime - prevTime) * 60) / 1000

                timeDiff = ((timeDiff - timeDiff.toLong()) * 1000)

                /*Delimit the timeDiff value based on the decimal place, where
                * the value in front of the decimal place is seconds and the value
                * after the decimal place is milliseconds*/
                val strArr = timeDiff.toString().split(".")
                var timeStr: String = "00:"

                /*Format the timeInterval string*/
                for (i in strArr.indices) {
                    if (i == 0) {
                        if (strArr[i].toDouble() < 10)
                            timeStr += "0" + strArr[i] + ":"
                        else
                            timeStr += strArr[i] + ":"
                    }
                    else if (i == 1) {
                        if (strArr[i].toDouble() > 999) {
                            /*Get only three decimal places so the value isnt extremely long*/
                            Log.e("substring", "${strArr[i].substring(0, 3)}")

                            timeStr += (strArr[i].substring(0, 3))
                        }
                        else {
                            timeStr += strArr[i]
                        }
                    }
                }

                lapInterval = timeStr
            }
            else
                lapInterval = "0"
        }

        Log.e("LapsModel", "insertLap, lapInterval=${lapInterval}")


        /*Create the Laps object that will be inseted into the database by initialzing the
        * ID, lapNumber, lapInterval, and timeStamp fields.*/
        var lapData = Laps(idKey, lapNumber, lapInterval, timeStamp)

        /*Add the lap to the database.*/
        persistence.insertLaps(lapData)

        idKey++ /*Increment idKey for when the next lap is inserted.*/
    }

    /*Method to remove all the laps from the table; this is invoked when
    * the reset button is clicked or the app is closed.*/
    fun destroy() {
        Log.e("LapModel", "in destroy")

        /*Delete all the laps from the database.*/
        persistence.deleteLaps()

        /*Make laps reflect that there are no laps in the database.*/
        laps = persistence.getAllLaps()
        idKey = 0 /*Reassign idKey to 0 for future laps being added.*/
    }

    /*Method to tokenize the timeStamp retrieved from the database*/
    fun tokenizeString(time: String): Double {
        /*Get a string array for each element, that is minutes, seconds, and milliseconds*/
        var stringToken: StringTokenizer = StringTokenizer(time, ":")
        var strArr: MutableList<String> = mutableListOf<String>()
        var time: Double = 0.0 /*the time of the laps timeStamp to be returned*/

        /*First element will be minutes, second will be seconds, third will be milliseconds*/
        while (stringToken.hasMoreTokens()) {
            strArr.add(stringToken.nextToken())
        }

        if (strArr.size != 0) {
            var minutes: Double = strArr.get(0).toDouble()
            var seconds: Double = strArr.get(1).toDouble()
            var milliseconds: Double = strArr.get(2).toDouble()

            /*The time is represented as minutes*/
            time = (seconds / 60) + (milliseconds / 60000) + minutes
        }

        return time
    }
}