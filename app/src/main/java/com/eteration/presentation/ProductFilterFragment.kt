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

    private var listener: OnFilterAppliedListener? = null
    private val viewModel: ProductViewModel by activityViewModels()
    private lateinit var binding: FragmentProductFilterBinding

    private lateinit var brandFilterAdapter: BrandFilterAdapter
    private val brandFilterOptions = mutableListOf(
        BrandFilterOption("Lamborghini"),
        BrandFilterOption("Ferrari"),
        BrandFilterOption("Ford")
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductFilterBinding.bind(view)
        //setupFilterOptions()

        val brands = listOf("Lamborghini", "Ferrari", "Ford")

        // Initialize the RecyclerView Adapter
        brandFilterAdapter = BrandFilterAdapter(brandFilterOptions) { selectedBrand ->
            // Update ViewModel with selected brands
            //viewModel.updateFilter(brand = )
        }

        binding.recyclerViewBrandFilter.visibility = View.VISIBLE
        binding.recyclerViewBrandFilter.layoutManager = LinearLayoutManager(requireContext())


        binding.updateFiltersButton.setOnClickListener {
            /*val brandFilter =  brandFilterOptions.filter { it.isSelected }.map { it.name }.first()
            listener?.onFilterApplied(brandFilter)
            dismiss() // Dialog'u kapat

             */

            //viewModel.selectedBrand.value = brandFilterOptions.filter { it.isSelected }.map { it.name }.first()
            viewModel.updateFilter(brand = "Mercedes Benz")
            //requireActivity().supportFragmentManager.popBackStack()
            dismiss()

        }

        binding.recyclerViewBrandFilter.adapter = brandFilterAdapter
        brandFilterAdapter.submitList(brandFilterOptions)
        Log.d("ProductFilterFragment", "Brand Options: $brandFilterOptions")


        // Setup search functionality
        binding.searchBrandEditText.addTextChangedListener { text ->
            filterBrands(text.toString())
        }


    }

    private fun filterBrands(query: String) {
        val filteredBrands = brandFilterOptions.filter { it.name.contains(query, ignoreCase = true) }
        if (filteredBrands.isNotEmpty()) brandFilterAdapter.submitList(filteredBrands.toList())
    }

    private fun getSelectedBrands(): List<String> {
        return brandFilterOptions.filter { it.isSelected }.map { it.name }
    }




    // Interface to communicate back to the parent fragment or activity
    interface OnFilterAppliedListener {
        fun onFilterApplied(brandFilter: String)
    }

    fun setOnFilterAppliedListener(listener: OnFilterAppliedListener) {
        this.listener = listener
    }
}