package com.eteration.presentation

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eteration.app.R
import com.eteration.app.databinding.FragmentProductBinding
import com.eteration.domain.model.Product
import com.eteration.presentation.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var binding: FragmentProductBinding
    private val viewModel: ProductViewModel by activityViewModels()
    private val adapter by lazy { ProductAdapter(::onProductClick, ::onAddToCartClick)}


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductBinding.bind(view)

        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProduct.adapter = adapter
        binding.rvProduct.setHasFixedSize(true)

        //viewModel.loadProducts()


        /*lifecycleScope.launch {
            viewModel.productFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)

            }
        }

         */

        /*viewModel.filteredProducts.observe(viewLifecycleOwner, Observer { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        })

         */

        viewModel.selectedBrand.observe(viewLifecycleOwner) { brand ->
            // Update UI or ViewModel with the selected brand
            //viewModel.updateFilter(brand = brand)
        }

        // 🔥 ViewModel'deki Flow'u toplar
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredProducts.collectLatest { pagingData ->
                    //if (adapter.snapshot().items != pagingData) {
                        adapter.submitData(pagingData)
                    //}
                    //adapter.submitData(pagingData)

                    /*adapter.loadStateFlow.collectLatest { loadStates ->
                        if (loadStates.refresh is LoadState.NotLoading && recyclerViewState != null) {
                            binding.rvProduct.layoutManager?.onRestoreInstanceState(recyclerViewState)
                            recyclerViewState = null
                        }
                    }

                     */

                    /*adapter.loadStateFlow.collectLatest { loadStates ->
                        if (loadStates.refresh is LoadState.NotLoading && recyclerViewState != null) {
                            binding.rvProduct.layoutManager?.onRestoreInstanceState(recyclerViewState)
                            recyclerViewState = null // Clear state to avoid reusing it
                        }
                    }

                     */

                }
            }
        }

        viewModel.pagingSourceInvalidation.observe(viewLifecycleOwner, Observer {
            adapter.refresh()
        })

        viewModel.observeChangesAndInvalidate()

        binding.selectFilterButton.setOnClickListener {
            onFilterClick()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //val nameFilter = query
                //viewModel.applyFilters(nameFilter)
                viewModel.updateFilter(query ?: "")

                //viewModel.setSearchQuery(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //viewModel.setSearchQuery(newText ?: "")
                //val nameFilter = newText
                viewModel.updateFilter(newText ?: "")
                return true
            }
        })
    }


    private fun onProductClick(product: Product) {
        val action = ProductFragmentDirections.actionProductFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }

    private fun onAddToCartClick(product: Product) {
        viewModel.addToCart(product)
    }

    private fun onFilterClick() {
        /*val filterFragment = ProductFilterFragment()

        filterFragment.setOnFilterAppliedListener(this)
        filterFragment.show(childFragmentManager, "ProductFilterFragment")

         */
        /*val action = ProductFragmentDirections.actionProductFragmentToProductFilterFragment()
        findNavController().navigate(action)
        
         */

        val filterFragment = ProductFilterFragment()
        filterFragment.show(childFragmentManager, filterFragment.tag)
    }

}

