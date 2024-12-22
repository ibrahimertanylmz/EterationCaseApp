package com.eteration.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.eteration.app.databinding.ProductItemBinding
import com.eteration.data.remote.model.ProductResponse
import com.eteration.domain.model.Product

class ProductAdapter(private val onclick: (Product) -> Unit, private val onAddToCartClick: (Product) -> Unit ) : PagingDataAdapter<Product, ProductAdapter.ProductViewHolder>(PRODUCT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        product?.let { holder.bind(it, onclick, onAddToCartClick) }
    }

    class ProductViewHolder(private val binding: ProductItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(product: Product, onclick: (Product) -> Unit, onAddToCartClick: (Product) -> Unit ) {
            binding.productTitle.text = product.name
            binding.productPrice.text = String.format("%.2f ₺", product.price)
            Glide.with(binding.root)
                .load(product.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.productImage)

            if (product.isBookmarked) binding.bookmarkIcon.visibility = View.VISIBLE else binding.bookmarkIcon.visibility = View.GONE

            binding.root.setOnClickListener {
                onclick(product)
            }

            if (product.isInCart) binding.addToCartButton.text = "In Your Cart"
            else binding.addToCartButton.text = "Add to Cart"
            binding.addToCartButton.setOnClickListener {
                if (!product.isInCart){
                    binding.addToCartButton.text = "In Your Cart"
                    binding.addToCartButton.isClickable = false
                    product.isInCart = true
                    onAddToCartClick(product)
                }
            }
        }
    }

    companion object {
        private val PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
        }
    }
}
