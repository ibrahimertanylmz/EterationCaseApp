package com.eteration.presentation.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.eteration.app.R
import com.eteration.app.databinding.FragmentProductBinding
import com.eteration.domain.model.Product
import com.eteration.presentation.product.filter.ProductFilterFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var binding: FragmentProductBinding
    private val viewModel: ProductViewModel by activityViewModels()
    private val adapter by lazy { ProductAdapter(::onProductClick, ::onAddToCartClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProductBinding.bind(view)

        onInitUi()
        onInitObservers()

    }

    private fun onInitUi() {
        onInitRv()
        binding.selectFilterButton.setOnClickListener {
            onFilterClick()
        }
        binding.clearFiltersButton.setOnClickListener {
            viewModel.clearFilters()
        }
        onPagerAdapterLoadState()
        initSearch()

    }

    private fun onInitObservers() {
        viewModel.launchOnMain {
            viewModel.filteredProducts.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
        viewModel.pagingSourceInvalidation.observe(viewLifecycleOwner, Observer {
            adapter.refresh()
        })
        viewModel.observeChangesAndInvalidate()
        viewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                Toast.makeText(
                    requireContext(),
                    getText(R.string.no_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.launchOnMain {
            viewModel.filterParams.collect { filterParams ->
                if (filterParams.brandFilter.isNotEmpty()) {
                    emptyFiltersUi()
                } else {
                    onFilterUi()
                }
            }
        }
    }

    private fun onPagerAdapterLoadState() {
        adapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    showLoading()
                }

                is LoadState.NotLoading -> {
                    hideLoading()
                }
                is LoadState.Error -> {
                    hideLoading()
                    val error = (loadState.refresh as LoadState.Error).error
                    Toast.makeText(
                        requireContext(),
                        error.message ?: getString(R.string.error_common),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun onFilterUi() {
        binding.clearFiltersButton.visibility = View.GONE
        binding.appliedFiltersText.visibility = View.GONE
    }

    private fun emptyFiltersUi() {
        binding.clearFiltersButton.visibility = View.VISIBLE
        binding.appliedFiltersText.visibility = View.VISIBLE
    }

    private fun onInitRv() {
        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProduct.adapter = adapter
        binding.rvProduct.setHasFixedSize(true)
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun initSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.updateFilter(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateFilter(newText ?: "")
                return true
            }
        })
    }

    private fun onProductClick(product: Product) {
        val action =
            ProductFragmentDirections.actionProductFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }

    private fun onAddToCartClick(product: Product) {
        viewModel.addToCart(product)
    }

    private fun onFilterClick() {
        val filterFragment = ProductFilterFragment()
        filterFragment.show(childFragmentManager, filterFragment.tag)
    }

}




