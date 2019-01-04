package edu.umsl.stopwatch.database

import edu.umsl.stopwatch.Laps
import android.util.Log
import org.jetbrains.anko.db.*

class LapsPersistence(private val dbHelper: DBHelper) {

    /*Returns all the laps in the table*/
    fun getAllLaps(): List<Laps> {
        val db = dbHelper.writableDatabase

        /*The query specifies the table name and to order the laps in ascending order*/
        val cursor = db.query(LapsSchema.NAME, null, null, null,
                null, null, "${LapsSchema.Cols.LAP_NUMBER} ASC")

        /*The array of laps that will be returned*/
        val lapsArray = arrayListOf<Laps>()

        /*While there are still more rows in the table, get the ID, lapNumber, lapInterval,
        * and timeStamp for each Laps object*/
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(LapsSchema.Cols.ID))
            val lapNumber = cursor.getInt(cursor.getColumnIndex(LapsSchema.Cols.LAP_NUMBER))

            var lapInterval = "0"
            try {
                if(!cursor.isNull(cursor.getColumnIndex(LapsSchema.Cols.LAP_INTERVAL)))
                    lapInterval = cursor.getString(cursor.getColumnIndex(LapsSchema.Cols.LAP_INTERVAL))
            }
            catch(tr: Throwable) {
                Log.e("getAllLaps", "null lap interval")
            }

            val timeStamp = cursor.getString(cursor.getColumnIndex(LapsSchema.Cols.TIMESTAMP))
            lapsArray.add(Laps(id, lapNumber, lapInterval, timeStamp)) /*Add the Laps object to the array*/
        }
        cursor.close()
        return lapsArray
    }

    /*Get a particular lap, based on the ID, in the table.*/
    fun getLapAtPosition(position: Int): Laps {
        val db = dbHelper.writableDatabase

        /*The lap object to be returned.*/
        var queryLap: Laps? = null

        /*The 'WHERE' part of the statement, which specifies the ID column
        * and a '?' for the prepared statement to prevent SQL injections*/
        val idRequest = "${LapsSchema.Cols.ID} = ?"

        /*Execute the query; select from the table (NAME), where
        * the value of the prepared statement is the position argument passed
        * to the function*/
        db.select(LapsSchema.NAME)
                .whereSimple(idRequest, position.toString()).exec {
                    while(this.moveToNext()) //Necessary for the cursor to get the specific row
                    {
                        /*Retrieve the ID, lapNumber, lapInterval, and timeStamp of the lap.*/
                        val id = this.getInt(this.getColumnIndex(LapsSchema.Cols.ID))
                        val lapNumber = this.getInt(this.getColumnIndex(LapsSchema.Cols.LAP_NUMBER))
                        val lapInterval = this.getString(this.getColumnIndex(LapsSchema.Cols.LAP_INTERVAL))
                        val timeStamp = this.getString(this.getColumnIndex(LapsSchema.Cols.TIMESTAMP))

                        /*Initialize the queryLap Laps object.*/
                        queryLap = Laps(id, lapNumber, lapInterval, timeStamp)
                    }
                }

        return queryLap!! //Return the lap

    }

    /*Method to insert a Laps object into the table*/
    fun insertLaps(lapData: Laps) {
        dbHelper.use {
            beginTransaction()

            /*Insert the Laps object into the table, specifcying the ID, lapNumber,
            * lapInterval, and timeStamp member variables of the object as values for
            * each corresponding column*/
            insert(LapsSchema.NAME,
                    LapsSchema.Cols.ID to lapData.id,
                    LapsSchema.Cols.LAP_NUMBER to lapData.lapNumber,
                    LapsSchema.Cols.LAP_INTERVAL to lapData.lapInterval,
                    LapsSchema.Cols.TIMESTAMP to lapData.timestamp)

            setTransactionSuccessful()
            endTransaction()
        }
    }

    /*Method to delete all the laps in the table*/
    fun deleteLaps() {
        /*The return value of the number of rows deleted*/
        var deletedRows: Int = 0
        dbHelper.use {
            beginTransaction()

            /*The delete statement specifies the name of the table, "1" specifies
            * ALL rows in the table, and whereArgs is null to indicate all rows in the table;
            * specifying "1" returns the number of rows deleted*/
            deletedRows = delete(LapsSchema.NAME, "1", null)

            setTransactionSuccessful()
            endTransaction()
        }

    }
}