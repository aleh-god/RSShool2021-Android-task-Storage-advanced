package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import by.godevelopment.rsshool2021_android_task_storage_advanced.R
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomTitle

class SettingsFragment : PreferenceFragmentCompat(), HasCustomTitle {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen, rootKey)
    }

    override fun getTitleRes(): Int = R.string.titleSettingsFragment
}