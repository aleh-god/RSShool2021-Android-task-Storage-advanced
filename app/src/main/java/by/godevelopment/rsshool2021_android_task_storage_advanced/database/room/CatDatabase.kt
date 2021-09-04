package by.godevelopment.rsshool2021_android_task_storage_advanced.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Cat::class], version = 1)
abstract class CatDataBase : RoomDatabase() {
    // All of your DAOs need to have abstract methods that return the corresponding DAO.
    abstract fun catDao(): CatDao

    companion object {
        @Volatile
        private var INSTANCE: CatDataBase? = null

        // Populating the database isn't related to a UI lifecycle, therefore you shouldn't use a CoroutineScope like viewModelScope.
        // It's related to the app's lifecycle.
        // You'll update the WordsApplication to contain an applicationScope, then pass that to the WordRoomDatabase.getDatabase.
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CatDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CatDataBase::class.java,
                    "app_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        // In the WordRoomDatabase, you'll create a custom implementation of the RoomDatabase.Callback(),
        // that also gets a CoroutineScope as constructor parameter.
        // Then, you override the onCreate method to populate the database.
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var catDao = database.catDao()

                    // Delete all content here.
                    catDao.deleteAll()

                    // Add sample
                    val id = 1
                    val name = "Jesus"
                    val age = 10
                    val breed = "Godcat"
                    var cat = Cat(id, name, age, breed)
                    catDao.insertCat(cat)
                }
            }
        }
    }
}