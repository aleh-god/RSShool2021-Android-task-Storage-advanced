package by.godevelopment.rsshool2021_android_task_storage_advanced.database

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.*
import by.godevelopment.rsshool2021_android_task_storage_advanced.database.room.CatDao
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class CatRepository (
    private val catDaoRoomImplementation: CatDao,
    private val catDaoSqlImplementation: CatDao,
    private val preferences: CatPreference
    ) {

    private val catDao: CatDao
    get() {
        return when (preferences.getDaoPreference()) {
            "SQL" -> {
                Log.i("CatApp", "CatRepository get SQL")
                catDaoSqlImplementation
            }
            else -> {
                Log.i("CatApp", "CatRepository get ROOM")
                catDaoRoomImplementation
            }
        }
    }

    // Output Flow
    @WorkerThread
    fun getFlowAllCats(): Flow<List<Cat>> = catDao.getAllFlow()

    // Output LiveData
    @WorkerThread
    fun getLiveDataAllCats(): LiveData<List<Cat>> = catDao.getAllLiveData() // listCatsSortedData

    fun getPreference(): CatPreference = preferences

    fun getAllCats() = catDao.getAllCats()

    @WorkerThread
    suspend fun insertCat(cat: Cat) {
        catDao.insertCat(cat)
        Log.i("CatApp", "CatRepository catDao.insertCat = ${cat.id}")
    }

    @WorkerThread
    suspend fun updateCat(cat: Cat) {
        catDao.updateCat(cat)
        Log.i("CatApp", "CatRepository catDao.updateCat = ${cat.id}")
    }

    @WorkerThread
    suspend fun deleteCat(cat: Cat) {
        catDao.deleteCat(cat)
        Log.i("CatApp", "CatRepository catDao.deleteCat = ${cat.id}")
    }

    @WorkerThread
    suspend fun deleteAllCats() {
        catDao.deleteAll()
    }

}