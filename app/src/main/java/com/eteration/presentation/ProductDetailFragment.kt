package com.eteration.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.eteration.app.R
import com.eteration.app.databinding.FragmentProductBinding
import com.eteration.app.databinding.FragmentProductDetailBinding
import com.eteration.presentation.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailFragment: Fragment(R.layout.fragment_product_detail) {

    private lateinit var binding: FragmentProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()

    @SuppressLint("DefaultLocale", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProductDetailBinding.bind(view)

        val product = ProductDetailFragmentArgs.fromBundle(requireArguments()).product

        binding.product = product
        Glide.with(binding.productImage).load(product.image).into(binding.productImage)
        binding.productPrice.text = "Price :\n${String.format("%.2f ₺", product.price)}"
        if (product.isInCart){
            binding.buttonAddToCart.text = getString(R.string.in_your_chart)
            binding.buttonAddToCart.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.app_gray))

        }
        binding.addToBookmarkImage.setOnClickListener {
            product.isBookmarked = true
            binding.product = product
            viewModel.addToBookmarks(product)
        }

        binding.removeBookmarkImage.setOnClickListener {
            product.isBookmarked = false
            binding.product = product
            viewModel.removeFromBookmarks(product.id)
            //viewModel.toggleBookmark(product.id)
        }

        binding.buttonAddToCart.setOnClickListener {
            binding.buttonAddToCart.text = getString(R.string.in_your_chart)
            binding.buttonAddToCart.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.app_gray))
            viewModel.addToCart(product)
        }
    }
}

