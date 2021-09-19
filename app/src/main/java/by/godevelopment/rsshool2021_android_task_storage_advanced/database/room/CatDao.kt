package by.godevelopment.rsshool2021_android_task_storage_advanced.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {

    @Query("SELECT * FROM cat_table")
    fun getAllFlow(): Flow<List<Cat>>

    @Query("SELECT * FROM cat_table")
    fun getAllLiveData(): LiveData<List<Cat>>

    @Query("DELETE FROM cat_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCat(cat: Cat)

    // Room uses the primary key to match passed entity instances to rows in the database.
    // If there is no row with the same primary key, Room makes no changes.
    @Update
    suspend fun updateCat(cat: Cat)

    // Room uses the primary key to match passed entity instances to rows in the database.
    @Delete
    suspend fun deleteCat(cat: Cat)


    @Query("SELECT * FROM cat_table")
    fun getAllCats(): List<Cat>

}