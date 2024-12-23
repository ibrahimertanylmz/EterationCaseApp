package com.eteration.presentation.product_detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.eteration.app.R
import com.eteration.app.databinding.FragmentProductDetailBinding
import com.eteration.core.util.PriceFormatter
import com.eteration.domain.model.Product
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment: Fragment(R.layout.fragment_product_detail) {

    private lateinit var binding: FragmentProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductDetailBinding.bind(view)
        val product = com.eteration.presentation.product_detail.ProductDetailFragmentArgs.fromBundle(
            requireArguments()
        ).product

        onInitUi(product)
    }

    private fun onInitUi(product: Product) {
        binding.product = product
        Glide.with(binding.productImage).load(product.image).into(binding.productImage)
        binding.productPrice.text = "Price :\n${PriceFormatter.formatPrice(product.price)}"
        if (product.isInCart){
            binding.buttonAddToCart.text = getString(R.string.in_your_chart)
            binding.buttonAddToCart.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.app_gray))

        }
        onBookmarkState(product)

        binding.buttonAddToCart.setOnClickListener {
            onAddToCard(product)
        }
    }

    private fun onBookmarkState(product: Product) {
        binding.addToBookmarkImage.setOnClickListener {
            product.isBookmarked = true
            binding.product = product
            viewModel.addToBookmarks(product)
        }

        binding.removeBookmarkImage.setOnClickListener {
            product.isBookmarked = false
            binding.product = product
            viewModel.removeFromBookmarks(product.id)
        }
    }

    private fun onAddToCard(product: Product) {
        binding.buttonAddToCart.text = getString(R.string.in_your_chart)
        binding.buttonAddToCart.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.app_gray))
        binding.buttonAddToCart.isClickable = false
        viewModel.addToCart(product)
    }
}

