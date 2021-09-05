package by.godevelopment.rsshool2021_android_task_storage_advanced

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.ActivityMainBinding
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModel
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModelFactory
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.ListFragment
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // To create the ViewModel you used the viewModels delegate, passing in an instance of our ViewModelFactory.
    // This is constructed based on the repository retrieved from the Application.
    // This property can be accessed only after the Activity is attached to the Application, and access prior to that will result in IllegalArgumentException.
//    private val catViewModel: CatViewModel by viewModels {
//         CatViewModelFactory((application as CatApp).repository)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_content)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, ListFragment.newInstance())
                .commitNow()
        }
        setupComponents()
    }

    private fun setupComponents() {
        // setSupportActionBar(binding.toolbar)
        // setupFabAndToolbar()
    }
}