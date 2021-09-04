package by.godevelopment.rsshool2021_android_task_storage_advanced.database.sql

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import by.godevelopment.rsshool2021_android_task_storage_advanced.database.room.CatDao
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CatDaoSqlImplementation(context: Context) : CatDao {

    private val catReaderDbHelper = CatReaderDbHelper(context.applicationContext)
    private val dbRead = catReaderDbHelper.readableDatabase
    private val dbWrite = catReaderDbHelper.writableDatabase

    override fun getAll(): Flow<List<Cat>> {

        val cursor : Cursor = dbRead.rawQuery("SELECT * FROM ${ContractDB.FeedEntry.TABLE_NAME}", null)
        val listOfResult = mutableListOf<Cat>()

        cursor.use { cursor ->
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(ContractDB.FeedEntry.COLUMN_NAME_ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContractDB.FeedEntry.COLUMN_NAME_NAME))
                    val age = cursor.getInt(cursor.getColumnIndex(ContractDB.FeedEntry.COLUMN_NAME_AGE))
                    val breed =
                        cursor.getString(cursor.getColumnIndex(ContractDB.FeedEntry.COLUMN_NAME_BREED))
                    listOfResult.add(Cat(id, name, age, breed))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return flowOf(listOfResult)
    }

    override suspend fun deleteAll() {
        dbRead.rawQuery("DELETE FROM ${ContractDB.FeedEntry.TABLE_NAME}", null)
    }

    override suspend fun insertCat(cat: Cat) {

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(ContractDB.FeedEntry.COLUMN_NAME_NAME, cat.name)
            put(ContractDB.FeedEntry.COLUMN_NAME_AGE, cat.age)
            put(ContractDB.FeedEntry.COLUMN_NAME_BREED, cat.breed)
        }
        // Insert the new row, returning the primary key value of the new row
        val newRowId = dbWrite?.insert(ContractDB.FeedEntry.TABLE_NAME, null, values)

        if (newRowId != 0L) Log.i("DAO", "Insert cat complete.")
        else Log.i("DAO", "ERROR insert cat!!!")
    }

    override suspend fun updateCat(cat: Cat) {
        // New value for one column
        val values = ContentValues().apply {
            put(ContractDB.FeedEntry.COLUMN_NAME_NAME, cat.name)
            put(ContractDB.FeedEntry.COLUMN_NAME_AGE, cat.age)
            put(ContractDB.FeedEntry.COLUMN_NAME_BREED, cat.breed)
        }

        // Which row to update, based on the title
        val selection = "${ContractDB.FeedEntry.COLUMN_NAME_ID} LIKE ?"

        // You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings.
        val selectionArgs = arrayOf("${cat.id}")
        val updateRows = dbWrite.update(
            ContractDB.FeedEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs)

        if (updateRows != 0) Log.i("DAO", "Update cat complete.")
        else Log.i("DAO", "ERROR update cat!!!")
    }

    override suspend fun deleteCat(cat: Cat) {
        // Define 'where' part of query.
        val selection = "${ContractDB.FeedEntry.COLUMN_NAME_ID} LIKE ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf("${cat.id}")
        // Issue SQL statement.
        val deletedRows = dbWrite.delete(ContractDB.FeedEntry.TABLE_NAME, selection, selectionArgs)

        if (deletedRows != 0) Log.i("DAO", "Delete cat complete.")
        else Log.i("DAO", "ERROR delete cat!!!")
    }

    fun getCatFromDataBase(idCat: Int) : Cat {

        // The array of columns to return (pass null to get all)
        val projection = null
        // arrayOf(BaseColumns._ID, ContractDB.FeedEntry.COLUMN_NAME_TITLE, ContractDB.FeedEntry.COLUMN_NAME_SUBTITLE)

        // Третий и четвертый аргументы (selection и selectionArgs) объединяются для создания предложения WHERE.
        // Поскольку аргументы предоставляются отдельно от запроса выбора, они экранируются перед объединением.
        // Это делает ваши операторы выбора невосприимчивыми к SQL-инъекции.

        // The columns for the WHERE clause
        val selection = "${ContractDB.FeedEntry.COLUMN_NAME_ID} = ?"
        // The values for the WHERE clause
        val selectionArgs = arrayOf("$idCat")

        val cursor = dbRead.query(
            ContractDB.FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(ContractDB.FeedEntry.COLUMN_NAME_ID))
            val name = cursor.getString(cursor.getColumnIndex(ContractDB.FeedEntry.COLUMN_NAME_NAME))
            val age = cursor.getInt(cursor.getColumnIndex(ContractDB.FeedEntry.COLUMN_NAME_AGE))
            val breed = cursor.getString(cursor.getColumnIndex(ContractDB.FeedEntry.COLUMN_NAME_BREED))
            cursor.close()
            return Cat(id, name, age, breed)
        }
        return Cat(0, "Jesus", 33, "Heaven")
    }

    fun executeSqlHelperClose() {
        catReaderDbHelper.close()
    }

}