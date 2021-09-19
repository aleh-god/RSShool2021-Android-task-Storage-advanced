package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.godevelopment.rsshool2021_android_task_storage_advanced.CatApp
import by.godevelopment.rsshool2021_android_task_storage_advanced.R
import by.godevelopment.rsshool2021_android_task_storage_advanced.adapter.CatAdapter
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.FragmentListItemsBinding
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModel
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModelFactory
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.CustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomTitle
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.navigator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ListFragment : Fragment(), HasCustomTitle, HasCustomAction {

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    private var _binding: FragmentListItemsBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView and onDestroyView.

    private lateinit var catAdapter: CatAdapter
    // private lateinit var cats: List<Cat>

    private val catViewModel: CatViewModel by viewModels {
        CatViewModelFactory((activity?.application as CatApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        renderUI()

        // catViewModel.catsOutStateFlow.onEach(::renderAdapter).launchIn(lifecycleScope)
    }

    private fun setup() {
        setupRecyclerView()
        setupPreferenceListener()
    }

    private fun renderUI() {
        catViewModel.viewModelScope.launch {
            catViewModel.catsOutLiveData.observe(viewLifecycleOwner, { cats ->
                cats?.let {
                    if (!cats.isNullOrEmpty()) {
                        catAdapter.setCatList(cats.toList())
                        val order = catViewModel.preferenceCatApp.getSortPreference()
                        binding.header.text = "Sorted list by $order"
                    } else {
                        binding.header.text = "List is empty!"
                        catAdapter.setCatList(emptyList())
                    }
                }
            })
        }
    }

    private fun renderAdapter(catsIn: List<Cat>) {
        val order = catViewModel.preferenceCatApp.getSortPreference()
        binding.header.text = "Sorted list by $order"
        catAdapter.setCatList(catsIn)
        // cats = catsIn
    }

    private fun setupRecyclerView() {
        catAdapter = CatAdapter { position -> myActionClick(position) }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = catAdapter
            }
    }

    private fun setupPreferenceListener() {
        catViewModel.preferenceCatApp.getPreferenceToLiveData().observe(viewLifecycleOwner, {
            renderUI()
            Log.i("CatApp", "ListFragment renderUI() - liveData = $it")
        })
    }

    private fun myActionClick(position: Int) {
        Log.i("CatApp", "myActionClick = $position")
        catViewModel.catsOutLiveData.observe(viewLifecycleOwner, { cats ->
            cats?.let {
                if (!cats.isNullOrEmpty() && position < cats.size) {
                    val cat = it[position]
                    Snackbar.make(
                        binding.root,
                        "Selected cat: ${cat.name}",
                        Snackbar.LENGTH_LONG)
                        .setAction("Edit/Delete cat") {
                            navigator().showEditItemFragment(cat)
                        }
                        .show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getTitleRes(): Int = R.string.titleListFragment

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_baseline_settings_24,
            textRes = R.string.settings,
            onCustomAction = Runnable {
                navigator().showSettingFragment()
            }
        )
    }
}