package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface HasCustomAction {

    fun getCustomAction(): CustomAction
}

class CustomAction(
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val textRes: Int,
    val onCustomAction: Runnable
)