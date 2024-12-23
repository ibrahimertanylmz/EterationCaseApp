package com.eteration.presentation.cart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.eteration.app.R
import com.eteration.app.databinding.FragmentCartBinding
import com.eteration.core.util.PriceFormatter
import com.eteration.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    private val adapter by lazy { CartAdapter(::onIncreaseClick, ::onDecreaseClick) }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCartBinding.bind(view)
        onInitUi()
        onInitObservers()
    }

    private fun onInitUi() {
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter
        binding.rvCart.setHasFixedSize(true)
    }

    @SuppressLint("SetTextI18n")
    private fun onInitObservers() {
        viewModel.launchOnMain {
            viewModel.cartProducts.collect { cartProducts ->
                if (cartProducts != null) {
                    adapter.setCartItems(cartProducts)
                }
            }
        }
        viewModel.launchOnMain {
            viewModel.totalPrice.collect { total ->
                binding.totalPrice.text = "Total:\n${PriceFormatter.formatPrice(total)}"
            }
        }
        viewModel.loadCartItems()
    }

    private fun onDecreaseClick(product: Product) {
        if (product.cartQuantity == 0) {
            Toast.makeText(requireContext(), getText(R.string.removed_product), Toast.LENGTH_SHORT)
                .show()
        }
        viewModel.updateCartItem(product)
    }

    private fun onIncreaseClick(product: Product) {
        viewModel.updateCartItem(product)
    }

}

