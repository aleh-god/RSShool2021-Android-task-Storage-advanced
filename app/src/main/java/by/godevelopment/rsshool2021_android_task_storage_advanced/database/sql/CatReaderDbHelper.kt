package by.godevelopment.rsshool2021_android_task_storage_advanced.database.sql

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class CatReaderDbHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
){

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(SQL_INSERT_ENTRIES)

        Log.i("DATABASE", SQL_CREATE_ENTRIES)
        Log.i("DATABASE", SQL_INSERT_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "CatReader.db"

        // By implementing the BaseColumns interface, your inner class can inherit a primary key field called _ID that some Android classes such as CursorAdapter expect it to have.
        // It's not required, but this can help your database work harmoniously with the Android framework.
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${ContractDB.FeedEntry.TABLE_NAME} (" +
                    "${ContractDB.FeedEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
                    "${ContractDB.FeedEntry.COLUMN_NAME_NAME} TEXT," +
                    "${ContractDB.FeedEntry.COLUMN_NAME_AGE} INTEGER," +
                    "${ContractDB.FeedEntry.COLUMN_NAME_BREED} TEXT)"

        private const val SQL_INSERT_ENTRIES =
//            "INSERT INTO cat_table (name, age, breed) VALUES ('Jesus', 33, 'Heaven');"
            "INSERT INTO cat_table (name, age, breed) VALUES (\"Missy\", 4, \"Snowshoe\"),(\"Lucy\", 11, \"Chausie\"),(\"Oscar\", 14, \"Aegean\"),(\"Simba\", 2, \"Savannah\"),(\"Oliver\", 2, \"Maine Coon\"),(\"Simba\", 6, \"Oriental Longhair\"),(\"Alfie\", 18, \"Toyger\"),(\"Bella\", 7, \"Manx\");"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ContractDB.FeedEntry.TABLE_NAME}"
    }
}