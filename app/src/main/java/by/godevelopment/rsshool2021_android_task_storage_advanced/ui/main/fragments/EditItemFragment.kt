package by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.fragments

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.godevelopment.rsshool2021_android_task_storage_advanced.CatApp
import by.godevelopment.rsshool2021_android_task_storage_advanced.R
import by.godevelopment.rsshool2021_android_task_storage_advanced.databinding.FragmentEditItemBinding
import by.godevelopment.rsshool2021_android_task_storage_advanced.entity.Cat
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModel
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.CatViewModelFactory
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.CustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomAction
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.HasCustomTitle
import by.godevelopment.rsshool2021_android_task_storage_advanced.ui.main.contractNavigation.navigator
import com.google.android.material.snackbar.Snackbar

fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

class EditItemFragment: Fragment(), HasCustomTitle, HasCustomAction {

    private lateinit var cat: Cat

    companion object {
        @JvmStatic
        fun newInstance(selectedCat: Cat): EditItemFragment  {
            return EditItemFragment().apply {
                this.cat = selectedCat
            }
        }
    }

    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!  // This property is only valid between onCreateView and onDestroyView.

    private val catViewModel: CatViewModel by viewModels {
        CatViewModelFactory((activity?.application as CatApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderUI()
        setupListener()
    }

    private fun renderUI() {
        binding.headerEdit.text = "Cat id = ${cat.id}"
        binding.editName.text = cat.name.toEditable()
        binding.editAge.text = cat.age.toString().toEditable()
        binding.editBreed.text = cat.breed.toEditable()
    }

    private fun setupListener() {
        binding.saveButton.setOnClickListener {
            val name = binding.editName.text
            val age = binding.editAge.text.toString().toIntOrNull()
            val breed = binding.editBreed.text

            var chek = true
            if (name.isNullOrBlank()) {
                binding.editName.error = "Even a homeless cat has a name!"
                chek = false
            }
            if (breed.isNullOrBlank()) {
                binding.editBreed.error = "We have only purebred cats!"
                chek = false
            }
            val wtfChek = age != null && age in 1..33
            if (!wtfChek) {
                binding.editAge.error = "Cats are not live so long!"
                chek = false
            }
            if (!chek) Snackbar.make(binding.root, "Please, fill in the fields correctly.", Snackbar.LENGTH_LONG).show()
            else {
                catViewModel.updateCat(Cat(cat.id, name.toString(), age!!, breed.toString()))
                onConfirmPressed()
            }
        }

        binding.deleteButton.setOnClickListener {
            catViewModel.deleteCat(cat)
            onConfirmPressed()
        }

    }

    override fun getTitleRes(): Int = R.string.titleEditItemFragment

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}