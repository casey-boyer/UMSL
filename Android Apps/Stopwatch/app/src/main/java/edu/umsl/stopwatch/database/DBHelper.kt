package edu.umsl.stopwatch.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DBHelper(context: Context): ManagedSQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    /*Create the database if it does not exist.
    * The ID column is an integer value and serves as the primary key.
    * The lap number column is a text value and reflects the lap number out of all the laps
    * The lap interval is a text value and reflects the time interval the lap was added vs the
    * previous lap
    * The timestamp is a text value which reflects the time the lap was added*/
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(LapsSchema.NAME, true,
                LapsSchema.Cols.ID to INTEGER + PRIMARY_KEY,
                LapsSchema.Cols.LAP_NUMBER to TEXT,
                LapsSchema.Cols.LAP_INTERVAL to TEXT,
                LapsSchema.Cols.TIMESTAMP to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    /*The database name, LapsDB, and the database version, 1.*/
    companion object {
        const val DB_NAME = "LapsDB.db"
        const val DB_VERSION = 1
    }
}