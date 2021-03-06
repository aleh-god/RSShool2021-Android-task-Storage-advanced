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
        // You'll update the CatsApplication to contain an applicationScope, then pass that to the CatRoomDatabase.getDatabase.
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
                    .addCallback(CatDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class CatDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        // In the CatRoomDatabase, you'll create a custom implementation of the RoomDatabase.Callback(),
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
                    val cats = listOf<Cat>(Cat(0, "Missy", 4, "Snowshoe"), Cat(0, "Lucy", 11, "Chausie"), Cat(0, "Oscar", 14, "Aegean"), Cat(0, "Simba", 2, "Savannah"), Cat(0, "Oliver", 2, "Maine Coon"), Cat(0, "Simba", 6, "Oriental Longhair"), Cat(0, "Alfie", 18, "Toyger"), Cat(0, "Bella", 7, "Manx"))
                    for (cat in cats) catDao.insertCat(cat)
                }
            }
        }
    }
}