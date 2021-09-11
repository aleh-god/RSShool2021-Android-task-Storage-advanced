package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main

import androidx.lifecycle.*
import by.godevelopment.rsshool2021_android_task_storage_advanced.database.CatRepository
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import kotlinx.coroutines.launch

class CatViewModel (private val repository: CatRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits
    // added a public LiveData member variable to cache the list of words.
    val allCats: LiveData<List<Cat>> = repository.getLiveDataAllCats

//    val user: LiveData<User> = liveData {
//        val data = database.loadUser() // loadUser is a suspend function.
//        emit(data)
//    }

    // Launching a new coroutine to insert the data in a non-blocking way
    // This way, the implementation of insert() is encapsulated from the UI.
    // We're launching a new coroutine and calling the repository's insert, which is a suspend function.
    // As mentioned, ViewModels have a coroutine scope based on their lifecycle called viewModelScope, which you'll use here.
    fun insertCat(cat: Cat) = viewModelScope.launch {
        repository.insertCat(cat)
    }

    fun deleteCat(cat: Cat) = viewModelScope.launch {
        repository.deleteCat(cat)
    }

    fun updateCat(cat: Cat) = viewModelScope.launch {
        repository.updateCat(cat)
    }
}

// By using viewModels and ViewModelProvider.Factory,the framework will take care of the lifecycle of the ViewModel.
// It will survive configuration changes and even if the Activity is recreated, you'll always get the right instance of the WordViewModel class.
class CatViewModelFactory(private val repository: CatRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}