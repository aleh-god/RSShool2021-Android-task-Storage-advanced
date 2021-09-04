package by.godevelopment.rsshool2021_android_task_storage_advanced.database

import androidx.annotation.WorkerThread
import by.godevelopment.rsshool2021_android_task_storage_advanced.database.room.CatDao
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import kotlinx.coroutines.flow.Flow

class CatRepository (private val catDao: CatDao) {

    val getFlowAllCats: Flow<List<Cat>> = catDao.getAll()

    // Обозначает, что аннотированный метод должен вызываться только в рабочем потоке.
    @WorkerThread
    suspend fun insertCat(cat: Cat) {
        catDao.insertCat(cat)
    }

    @WorkerThread
    suspend fun updateCat(cat: Cat) {
        catDao.updateCat(cat)
    }

    @WorkerThread
    suspend fun deleteCat(cat: Cat) {
        catDao.deleteCat(cat)
    }

    @WorkerThread
    suspend fun deleteAllCats() {
        catDao.deleteAll()
    }

    //    fun getSortedListOfCats(order: OrderType): List<Cat> {
//        return when (order) {
//            OrderType.ID -> getListOfCats()
//            OrderType.NAME -> getListOfCats().sortedBy { Cat -> Cat.name.lowercase() }
//            OrderType.AGE -> getListOfCats().sortedBy { Cat -> Cat.age }
//            OrderType.BREED -> getListOfCats().sortedBy { Cat -> Cat.breed.lowercase() }
//        }
//    }

}