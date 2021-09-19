package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation

import androidx.fragment.app.Fragment
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showAddItemFragment()

    fun showEditItemFragment(cat: Cat)

    fun showSettingFragment()

    fun showListFragment()

    fun goBack()

}