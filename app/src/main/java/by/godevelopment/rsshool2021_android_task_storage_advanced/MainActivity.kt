package by.godevelopment.rsshool2021_android_task_storage_advanced

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}