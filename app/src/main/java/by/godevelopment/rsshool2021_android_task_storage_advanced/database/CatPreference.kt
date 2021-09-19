package by.godevelopment.rsshool2021_android_task_storage_advanced.database

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager

class CatPreference(private val context: Context) {

    private val sharedPreferences by lazy {PreferenceManager.getDefaultSharedPreferences(context)}
    init {
        val checkPref = sharedPreferences.getString(DAO_KEY, "")
        Log.i("CatApp", "init Preferences = $checkPref")
        if (checkPref == "") {
           sharedPreferences.edit {
               putString(DAO_KEY, DEFAULT_DAO)
               putString(SORTED_KEY, DEFAULT_SORTED)
           }
        }
    }

    private val _preferenceToLiveData = MutableLiveData<String>()

    fun getPreferenceToLiveData(): LiveData<String> {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        return _preferenceToLiveData
    }

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
        Log.i("CatApp", "CatPreference Listener - Key LiveData.value = $key")
        preferences.getString(key, "")?.let {
            _preferenceToLiveData.value = it
            Log.i("CatApp", "CatPreference Listener - Value LiveData.value = $it")
        }
    }

    fun getDaoPreference(): String {
        val checkDao = sharedPreferences.getString(DAO_KEY, "")
        Log.i("CatApp", "CatPreference getDao - Value Preferences = $checkDao")
        return checkDao ?: DEFAULT_DAO
    }

    fun getSortPreference(): String {
        val checkSort = sharedPreferences.getString(SORTED_KEY, "")
        Log.i("CatApp", "CatPreference getSort - Value Preferences = $checkSort")
        return checkSort ?: DEFAULT_SORTED
    }

    companion object {
        private const val PREFERENCE_NAME = "cats_pref"
        private const val DAO_KEY = "dao"
        private const val DEFAULT_DAO = "ROOM"
        private const val SORTED_KEY = "sortBy"
        private const val DEFAULT_SORTED = "Name"
    }
}