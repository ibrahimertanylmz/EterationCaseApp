package com.eteration.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.eteration.app.R
import com.eteration.app.databinding.FragmentProductFilterBinding
import com.eteration.domain.model.BrandFilterOption
import com.eteration.presentation.adapter.BrandFilterAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFilterFragment : BottomSheetDialogFragment(R.layout.fragment_product_filter) {

    private val viewModel: ProductViewModel by activityViewModels()
    private lateinit var binding: FragmentProductFilterBinding

    private var brandFilterAdapter= BrandFilterAdapter()
    private val brandFilterOptions = mutableListOf(
        BrandFilterOption("Lamborghini"),
        BrandFilterOption("Ferrari"),
        BrandFilterOption("Ford"),
        BrandFilterOption("Mercedes")
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductFilterBinding.bind(view)

        binding.recyclerViewBrandFilter.visibility = View.VISIBLE
        binding.recyclerViewBrandFilter.layoutManager = LinearLayoutManager(requireContext())


        binding.updateFiltersButton.setOnClickListener {
            viewModel.updateFilter(brand = brandFilterOptions.filter { it.isSelected }.map { it.name })
            dismiss()
        }

        viewModel.filterParams.value.brandFilter.forEach { filterParam->
            brandFilterOptions.forEach { brandFilterOption->
                if (brandFilterOption.name == filterParam)
                    brandFilterOption.isSelected = true
            }
        }

        //brandFilterAdapter.submitList()


        binding.recyclerViewBrandFilter.adapter = brandFilterAdapter
        brandFilterAdapter.submitList(brandFilterOptions)

        binding.searchBrandEditText.addTextChangedListener { text ->
            filterBrands(text.toString())
        }
    }

    private fun filterBrands(query: String) {
        val filteredBrands = brandFilterOptions.filter { it.name.contains(query, ignoreCase = true) }
        if (filteredBrands.isNotEmpty()) brandFilterAdapter.submitList(filteredBrands.toList())
    }
}