package edu.umsl.stopwatch.database

object LapsSchema {
    const val NAME = "lap" /*Name of the table*/
    object Cols {
        const val ID = "id" /*ID of each row*/
        const val LAP_NUMBER = "lap_number" /*Lap number of each lap*/
        const val LAP_INTERVAL = "lap_interval" /*Lap interval of each lap*/
        const val TIMESTAMP = "timestamp" /*Timestamp of each lap*/
    }
}