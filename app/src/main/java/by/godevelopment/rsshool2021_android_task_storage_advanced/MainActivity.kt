package by.godevelopment.rsshool2021_android_task_storage_advanced

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.ActivityMainBinding
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.CustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomTitle
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.Navigator
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments.AddItemFragment
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments.ListFragment
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments.SettingsFragment

class MainActivity : AppCompatActivity(), Navigator {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.main_container)!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_container, ListFragment.newInstance())
                .commit()
        }
        // setupComponents()
    }

    private fun setupComponents() {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        updateUi()
        return true
    }

    fun settingsClick(item: android.view.MenuItem) {
        showSettingFragment()
    }

    fun fabOnClick(view: android.view.View) {
        showAddItemFragment()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_container, fragment)
            .commit()
    }

    override fun showAddItemFragment() {
        binding.fab.hide()
        launchFragment(AddItemFragment.newInstance())
    }

    override fun showSettingFragment() {
        binding.fab.hide()
        launchFragment(SettingsFragment())

    }

    override fun showListFragment() {
        binding.fab.show()
        launchFragment(ListFragment.newInstance())
    }

    override fun goBack() {
        onBackPressed()
    }

    private fun updateUi() {
        val fragment = currentFragment

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val dao = sharedPreferences.getString("dao", "")
        binding.toolbar.subtitle = "$dao DBMS implementation"

        if (fragment is HasCustomTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
        } else {
            binding.toolbar.title = "App"
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            // Get a support ActionBar corresponding to this toolbar and enable the Up button
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }
    }

    private fun createCustomToolbarAction(action: CustomAction) {
        binding.toolbar.menu.clear() // clearing old action if it exists before assigning a new one

        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
        iconDrawable.setTint(Color.WHITE)

        val menuItem = binding.toolbar.menu.add(action.textRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onCustomAction.run()
            return@setOnMenuItemClickListener true
        }

    }
}