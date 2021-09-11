package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.godevelopment.rsshool2021_android_task_storage_advanced.CatApp
import by.godevelopment.rsshool2021_android_task_storage_advanced.R
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.FragmentAddItemBinding
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModel
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModelFactory
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.CustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomTitle
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.navigator
import com.google.android.material.snackbar.Snackbar

class AddItemFragment : Fragment(), HasCustomAction, HasCustomTitle {

    companion object {
        @JvmStatic
        fun newInstance() = AddItemFragment()
    }

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView and onDestroyView.

    private val catViewModel: CatViewModel by viewModels {
        CatViewModelFactory((activity?.application as CatApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            val name = binding.addName.text
            val age = binding.addAge.text.toString().toIntOrNull()
            val breed = binding.addBreed.text
            var chek = true

            if (name.isNullOrBlank()) {
                binding.addName.error = "Even a homeless cat has a name!"
                chek = false
            }
            if (breed.isNullOrBlank()) {
                binding.addBreed.error = "We have only purebred cats!"
                chek = false
            }
            val wtfChek = age != null && age in 1..33
            if (!wtfChek) {
                binding.addAge.error = "Cats are not live so long!"
                chek = false
            }
            if (!chek) Snackbar.make(binding.root, "Please, fill in the fields correctly.", Snackbar.LENGTH_LONG).show()
            else {
                // onConfirmPressed()
                Snackbar.make(binding.root, "Cat added successfully.", Snackbar.LENGTH_LONG).show()
                catViewModel.insertCat(Cat(0, name.toString(), age!!, breed.toString()))
            }
        }
    }

    override fun getTitleRes(): Int = R.string.titleAddCatFragment

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_baseline_check_24,
            textRes = R.string.check,
            onCustomAction = Runnable {
                onConfirmPressed()
            }
        )
    }

    private fun onConfirmPressed() {
        navigator().goBack()
    }
}