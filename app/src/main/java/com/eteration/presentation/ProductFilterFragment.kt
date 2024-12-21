package com.eteration.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.eteration.app.R
import com.eteration.app.databinding.FragmentProductFilterBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFilterFragment : DialogFragment() {

    private var listener: OnFilterAppliedListener? = null
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var binding: FragmentProductFilterBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductFilterBinding.bind(view)
        //setupFilterOptions()
    }


    private fun setupFilterOptions() {
        binding.brandChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedBrands = group.checkedChipIds.mapNotNull { id ->
                binding.brandChipGroup.findViewById<Chip>(id).text.toString()
            }
            /*viewModel.applyFilters(
                brand = selectedBrand,
                models = getSelectedModels()
            )

             */
        }

        // Handling sorting options
        binding.sortByRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val sort = when (checkedId) {
                R.id.sortOldToNew -> "old_to_new"
                R.id.sortNewToOld -> "new_to_old"
                R.id.sortPriceLowToHigh -> "price_low_to_high"
                else -> null
            }
            //viewModel.applyFilters(sort = sort)
        }

        // Apply Filter Button
        binding.applyFilterButton.setOnClickListener {
            listener?.onFilterApplied()
            dismiss()  // Close the filter dialog
        }
    }



    // Interface to communicate back to the parent fragment or activity
    interface OnFilterAppliedListener {
        fun onFilterApplied()
    }

    fun setOnFilterAppliedListener(listener: OnFilterAppliedListener) {
        this.listener = listener
    }
}