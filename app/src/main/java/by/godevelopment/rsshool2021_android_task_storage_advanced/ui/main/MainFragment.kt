package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView and onDestroyView.

    private lateinit var viewModel: CatViewModel
    // private val catViewModel: CatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[CatViewModel::class.java]

        useModel()
    }

    private fun useModel() {
        viewModel.allCats.observe(viewLifecycleOwner, Observer {
            val cat = it.first()
            binding.message.text = cat.name
        })
    }

}