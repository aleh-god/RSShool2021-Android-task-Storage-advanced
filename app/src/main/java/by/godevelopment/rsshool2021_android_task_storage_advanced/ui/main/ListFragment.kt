package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import by.godevelopment.rsshool2021_android_task_storage_advanced.CatApp
import by.godevelopment.rsshool2021_android_task_storage_advanced.adapter.CatAdapter
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.ListFragmentBinding
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import com.google.android.material.snackbar.Snackbar

class ListFragment : Fragment() {

    companion object {
        fun newInstance() = ListFragment()
    }

    private var _binding: ListFragmentBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView and onDestroyView.

    private lateinit var catAdapter: CatAdapter

    private val catViewModel: CatViewModel by viewModels {
        CatViewModelFactory((activity?.application as CatApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListFragmentBinding.inflate(inflater, container, false)
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
                catAdapter.setCatList(cats)
                binding.header.text = it.first().name
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
            }       //.apply { setCatList(listOf(Cat(0,"null", 0 , "null"))) }
    }

    private fun myActionClick(position: Int) {
        catViewModel.allCats.observe(viewLifecycleOwner, Observer { cats ->
            cats?.let {
                val cat = it[position]
                Snackbar.make(binding.root, "Selected cat: ${cat.name}", Snackbar.LENGTH_LONG)
                    .setAction("Remove cat") {
                        catViewModel.deleteCat(cat)
                    }
                    .show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}