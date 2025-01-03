package com.eteration.presentation.favorite_products

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.eteration.app.R
import com.eteration.app.databinding.FragmentFavoritesBinding
import com.eteration.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter by lazy { FavoriteProductAdapter(::onProductClick, ::onAddToCardClick) }


    private var recyclerViewState: Parcelable? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)

        onInitUi()
        onInitObservers()
    }

    private fun onInitUi() {
        binding.rvFavorites.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFavorites.adapter = adapter
        binding.rvFavorites.setHasFixedSize(true)
    }

    private fun onInitObservers() {
        viewModel.launchOnMain {
            viewModel.bookmarkItems.collect { bookmarkItems ->
                if (bookmarkItems != null) {
                    adapter.setFavorites(bookmarkItems)
                }
            }
        }
        viewModel.loadBookmarkItems()
    }

    private fun onProductClick(product: Product) {
        val action =
            com.eteration.presentation.favorite_products.FavoritesFragmentDirections.actionFavoritesFragmentToProductDetailsFragment(
                product
            )
        findNavController().navigate(action)
    }

    private fun onAddToCardClick(product: Product) {
        product.isInCart = true
        product.cartQuantity += 1
        viewModel.addCartItem(product)
    }

    override fun onPause() {
        super.onPause()
        recyclerViewState = binding.rvFavorites.layoutManager?.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            recyclerViewState?.let {
                binding.rvFavorites.layoutManager?.onRestoreInstanceState(it)
                recyclerViewState = null
            }
        }
    }

}