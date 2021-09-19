package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main

import android.util.Log
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import by.godevelopment.rsshool2021_android_task_storage_advanced.database.CatRepository
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatViewModel (private val repository: CatRepository) : ViewModel() {

    val preferenceCatApp by lazy { repository.getPreference() }

    private val listCats = repository.getAllCats()

    private val liveDataAllCats: LiveData<List<Cat>> by lazy { repository.getLiveDataAllCats() }

    private val flowAllCats by lazy { repository.getFlowAllCats() }

    private fun getLoadingState() = emptyList<Cat>()

    //#1: Показ результата однократной операции с модифицированным держателем данных

    private val _myLiveData: MutableLiveData<List<Cat>> = MutableLiveData(getLoadingState())
    private val catsLiveData: LiveData<List<Cat>> = _myLiveData

    // Load data from a suspend fun and mutate state
    init {
        viewModelScope.launch {
            val result: List<Cat> = repository.getAllCats()
            _myLiveData.value = result
        }
    }

    private val _myStateFlow: MutableStateFlow<List<Cat>> = MutableStateFlow(getLoadingState())
    private val catsStateFlow: StateFlow<List<Cat>> = _myStateFlow
    // Load data from a suspend fun and mutate state
    init {
        viewModelScope.launch {
                val result: List<Cat> = repository.getAllCats()
                _myStateFlow.value = result
        }
    }

    //#2: Показ результата однократной операции
    // Это эквивалент предыдущего сниппета, демонстрирующий результат вызова корутины без модифицированного теневого свойства.

    private val catsEmitLiveData: LiveData<List<Cat>> = liveData {
        // Поскольку держатели состояния всегда имеют значение, хорошей идеей будет обернуть наше UI-состояние
        // в какой-нибудь класс Result, который поддерживает такие состояния, как Loading, Success и Error.
        emit(getLoadingState())
        emit(repository.getAllCats())
    }

    private val catsEmitStateFlow: StateFlow<List<Cat>> = flow {
        emit(repository.getAllCats())
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000), // Or Lazily because it's a one-shot
        initialValue = getLoadingState()
    )   // stateIn — это оператор Flow, который преобразует его в StateFlow.

    //#3: Однократная загрузка данных с параметрами

    private val catsMappedLiveData: LiveData<List<Cat>> = liveDataAllCats.map { cats ->
            Log.i("CatApp", "CatViewModel map.Sorted LiveData ${preferenceCatApp.getSortPreference()}")
            when (preferenceCatApp.getSortPreference()) {
                "ID" -> cats.sortedBy { Cat -> Cat.id }
                "Name" -> cats.sortedBy { Cat -> Cat.name.lowercase() }
                "Age" -> cats.sortedBy { Cat -> Cat.age }
                "Breed" -> cats.sortedBy { Cat -> Cat.breed.lowercase() }
                else -> cats.sortedBy { Cat -> Cat.id }
            }
    } // .asLiveData()

    // switchMap — это преобразование, тело которого выполняется, а результат подписывается при изменении liveData.
    private val preferenceLiveData = repository.getPreference().getPreferenceToLiveData()
    val catsSwitchedLiveData: LiveData<List<Cat>> = preferenceLiveData.switchMap { pref ->
        liveData {
            emit(
                sortedNewListCatsFromRepBy(pref)
            )
        }
    }

    private fun sortedNewListCatsFromRepBy(pref: String): List<Cat> {
        Log.i("CatApp", "CatViewModel switchMap.Sorted LiveData ${preferenceCatApp.getSortPreference()}")
        val cats = repository.getAllCats()
        when (pref) {
            "ID" -> cats.sortedBy { Cat -> Cat.id }
            "Name" -> cats.sortedBy { Cat -> Cat.name.lowercase() }
            "Age" -> cats.sortedBy { Cat -> Cat.age }
            "Breed" -> cats.sortedBy { Cat -> Cat.breed.lowercase() }
            else -> {}
        }
        return cats
    }

    private val catsMappedFlow: Flow<List<Cat>> = flowAllCats.map { cats ->
        Log.i("CatApp", "CatViewModel map.Sorted Flow ${preferenceCatApp.getSortPreference()}")
        when (preferenceCatApp.getSortPreference()) {
            "ID" -> cats.sortedBy { Cat -> Cat.id }
            "Name" -> cats.sortedBy { Cat -> Cat.name.lowercase() }
            "Age" -> cats.sortedBy { Cat -> Cat.age }
            "Breed" -> cats.sortedBy { Cat -> Cat.breed.lowercase() }
            else -> cats.sortedBy { Cat -> Cat.id }
        }
    }

    private val catsMappedLastFlow: StateFlow<List<Cat>> = flowAllCats.mapLatest { cats ->
        Log.i("CatApp", "CatViewModel map.Sorted Flow ${preferenceCatApp.getSortPreference()}")
        when (preferenceCatApp.getSortPreference()) {
            "ID" -> cats.sortedBy { Cat -> Cat.id }
            "Name" -> cats.sortedBy { Cat -> Cat.name.lowercase() }
            "Age" -> cats.sortedBy { Cat -> Cat.age }
            "Breed" -> cats.sortedBy { Cat -> Cat.breed.lowercase() }
            else -> cats.sortedBy { Cat -> Cat.id }
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = getLoadingState()
    )

    private val catsTransformLatestFlow: StateFlow<List<Cat>> = flowAllCats.transformLatest { cats ->
        emit(getLoadingState())
        emit(repository.getAllCats())
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = getLoadingState() // Note the different Loading states
    )

    //#4: Наблюдение за потоком данных с параметрами
    // Данные не извлекаются, а наблюдаются, поэтому мы автоматически распространяем изменения в источнике данных на пользовательский интерфейс.

//    private val userId: LiveData<String?> =
//        preferenceLiveData.map { user -> user.id }.asLiveData()
//
//    val result = preferenceLiveData.switchMap { pref ->
//        sortedNewListCatsFromRepBy(pref).asLiveData()
//    }
//
//    private val user: Flow<String?> =
//        authManager.observeUser().map { user -> user?.id }
//
//    val res: LiveData<Result<Item>> = userId.flatMapLatest { newUserId ->
//        repository.observeItem(newUserId)
//    }.asLiveData()


    //catsLiveData
    //catsEmitLiveData
    //catsMappedLiveData
    //catsSwitchedLiveData

    val catsOutLiveData = catsLiveData

    //catsStateFlow
    //catsEmitStateFlow
    //catsMappedFlow
    //catsMappedLastFlow
    //catsTransformLatestFlow

    val catsOutStateFlow = catsStateFlow

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

    private fun <T> Flow<T>.asLiveDataFlow() =
        shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)
    // Eagerly: начать немедленно и остановить, когда scope будет отменен.
    // @param scope the coroutine scope in which sharing is started.
    // @param started the strategy that controls when sharing is started and stopped.
    // @param initialValue the initial value of the state flow.
    // This value is also used when the state flow is reset using the [SharingStarted.WhileSubscribed] strategy with the `replayExpirationMillis` parameter.

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