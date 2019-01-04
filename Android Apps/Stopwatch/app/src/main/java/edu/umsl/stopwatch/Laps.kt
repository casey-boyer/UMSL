package edu.umsl.stopwatch

/*Simple data class for a Laps object, which will be used when adding or retrieving items from the
* database. The member values include the ID, lap number, lap interval, and timeStamp of when
* the lap was added*/
data class Laps(val id: Int, val lapNumber: Int, val lapInterval: String, val timestamp: String)