package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
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

class ListFragment : Fragment(), HasCustomTitle, HasCustomAction {

    companion object {
        @JvmStatic
        fun newInstance() = ListFragment()
    }

    private var _binding: FragmentListItemsBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView and onDestroyView.

    private lateinit var catAdapter: CatAdapter

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
    }

    private fun renderUI() {
        catViewModel.allCats.observe(viewLifecycleOwner, Observer { cats ->
             cats?.let {
                 if (!cats.isNullOrEmpty()) {
                    catAdapter.setCatList(cats as List<Cat>)
                     val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                     val order = sharedPreferences.getString("sortBy", "")
                     binding.header.text = "Sorted list by $order"
                 } else {
                     binding.header.text = "List is empty!"
                     catAdapter.setCatList(emptyList())
                 }
            }
        })
    }

    private fun setup() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        catAdapter = CatAdapter { position -> myActionClick(position) } // Адаптер передает сюда позицию холдера в списке и вызывает исполнение.
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = catAdapter
            }
    }

    private fun myActionClick(position: Int) {
        catViewModel.allCats.observe(viewLifecycleOwner, Observer { cats ->
            cats?.let {
                if (!cats.isNullOrEmpty() && position < cats.size) {
                    val cat = it[position]
                    Snackbar.make(
                        binding.root,
                        "Selected cat: ${cat.name}",
                        Snackbar.LENGTH_LONG)
                        .setAction("Remove cat") {
                            catViewModel.deleteCat(cat)
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