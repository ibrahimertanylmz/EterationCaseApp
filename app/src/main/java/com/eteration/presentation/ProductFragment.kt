package com.eteration.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.eteration.app.R
import com.eteration.app.databinding.FragmentProductBinding
import com.eteration.domain.model.Product
import com.eteration.presentation.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_product) {

    private lateinit var binding: FragmentProductBinding
    private val viewModel: ProductViewModel by viewModels()
    private val adapter = ProductAdapter(::onProductClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductBinding.bind(view)

        binding.rvProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProduct.adapter = adapter
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

        // 🔥 ViewModel'deki Flow'u toplar
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.filteredProducts.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
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
}

