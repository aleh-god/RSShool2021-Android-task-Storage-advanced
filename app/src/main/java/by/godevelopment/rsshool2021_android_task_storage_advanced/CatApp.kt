package by.godevelopment.rsshool2021_android_task_storage_advanced

import android.app.Application
import by.godevelopment.rsshool2021_android_task_storage_advanced.database.CatRepository
import by.godevelopment.rsshool2021_android_task_storage_advanced.database.room.CatDataBase
import by.godevelopment.rsshool2021_android_task_storage_advanced.database.sql.CatReaderDbHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CatApp: Application() {

    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val dataBaseRoom by lazy { CatDataBase.getDatabase(this, applicationScope) }
    private val dataBaseSql by lazy { CatReaderDbHelper(this) }

    // TODO "Здесь написать делегат с переключением СУБД"
    val repository by lazy { CatRepository(dataBaseRoom.catDao()) }

}