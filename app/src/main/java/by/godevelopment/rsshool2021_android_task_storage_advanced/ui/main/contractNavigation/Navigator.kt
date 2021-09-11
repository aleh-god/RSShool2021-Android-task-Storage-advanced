package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation

import androidx.fragment.app.Fragment

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showAddItemFragment()

    fun showSettingFragment()

    fun showListFragment()

    fun goBack()

}