package by.godevelopment.rsshool2021_android_task_storage_advanced

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.ActivityMainBinding
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModel
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModelFactory
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.CustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomTitle
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.Navigator
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments.AddItemFragment
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments.EditItemFragment
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments.ListFragment
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments.SettingsFragment

class MainActivity : AppCompatActivity(), Navigator {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.main_container)!!


    private val catViewModel: CatViewModel by viewModels {
        CatViewModelFactory((application as CatApp).repository)
    }

    private val fragmentListener =
        object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUi()
        }
    }

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

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)

        val dao = catViewModel.preferenceCatApp.getDaoPreference()
        binding.toolbar.subtitle = "$dao DBMS implementation"

        setupListener()
    }

    private fun setupListener() {
        catViewModel.preferenceCatApp.getPreferenceToLiveData().observe(this, {
            Log.i("CatApp", "Listener Main - String LiveDataPreferences = $it")
            if (it == "ROOM" || it == "SQL") binding.toolbar.subtitle = "$it DBMS implementation"
        })
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
        launchFragment(AddItemFragment.newInstance())
    }

    override fun showEditItemFragment(cat: Cat) {
        launchFragment(EditItemFragment.newInstance(cat))
    }

    override fun showSettingFragment() {
        launchFragment(SettingsFragment())
    }

    override fun showListFragment() {
        launchFragment(ListFragment.newInstance())
    }

    override fun goBack() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            onBackPressed()
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateUi() {
        val fragment = currentFragment

        if (fragment is HasCustomTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
        } else {
            binding.toolbar.title = "App"
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            // Get a support ActionBar corresponding to this toolbar and enable the Up button
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
            binding.fab.hide()
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
            binding.fab.show()
        }

        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }
    }

    private fun createCustomToolbarAction(action: CustomAction) {
        binding.toolbar.menu.clear() // clearing old action if it exists before assigning a new one

        // If the given drawable is wrapped, we will copy over certain state over to the wrapped drawable, such as its bounds, level, visibility and state.
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

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }
}