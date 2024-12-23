package com.eteration.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.eteration.app.R
import com.eteration.app.databinding.FragmentProductBinding
import com.eteration.domain.model.Product
import com.eteration.presentation.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var binding: FragmentProductBinding
    private val viewModel: ProductViewModel by activityViewModels()
    private val adapter by lazy { ProductAdapter(::onProductClick, ::onAddToCartClick) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductBinding.bind(view)

        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProduct.adapter = adapter
        binding.rvProduct.setHasFixedSize(true)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredProducts.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
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

        binding.clearFiltersButton.setOnClickListener {
            viewModel.clearFilters()
        }

        lifecycleScope.launch {
            viewModel.filterParams.collect { filterParams ->
                if (filterParams.brandFilter.isNotEmpty()) {
                    binding.clearFiltersButton.visibility = View.VISIBLE
                    binding.appliedFiltersText.visibility = View.VISIBLE
                } else {
                    binding.clearFiltersButton.visibility = View.GONE
                    binding.appliedFiltersText.visibility = View.GONE
                }
            }
        }


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

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
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




