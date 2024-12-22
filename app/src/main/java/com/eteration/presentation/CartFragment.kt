package com.eteration.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.eteration.app.R
import com.eteration.app.databinding.FragmentCartBinding
import com.eteration.domain.model.Product
import com.eteration.presentation.adapter.CartAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment: Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by viewModels()
    private val adapter by lazy { CartAdapter(::onIncreaseClick, ::onDecreaseClick) }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCartBinding.bind(view)

        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter
        binding.rvCart.setHasFixedSize(true)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.cartProducts.collect { cartProducts ->
                    if (cartProducts != null) {
                        cartProducts.forEach {
                            Log.d("cartProduct", it.name)
                        }
                        adapter.setCartItems(cartProducts)
                    }

                    /*viewModel.totalPrice.collect { total ->
                        binding.totalPrice.text =  "Total:\n$total"
                    }

                     */

                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.totalPrice.collect { total ->
                    binding.totalPrice.text = "Total:\n${String.format("%.2f ₺", total)}"
                }
            }
        }

        viewModel.loadCartItems()

    }

    private fun onDecreaseClick(product: Product) {
        if (product.cartQuantity == 0){
            Toast.makeText(requireContext(), "Product has been removed from your chart!",Toast.LENGTH_SHORT).show()
        }
        viewModel.updateCartItem(product)
    }

    private fun onIncreaseClick(product: Product) {
        viewModel.updateCartItem(product)
    }

}

