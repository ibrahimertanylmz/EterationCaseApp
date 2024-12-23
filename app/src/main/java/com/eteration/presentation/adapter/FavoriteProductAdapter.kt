package com.eteration.presentation.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.eteration.app.R
import com.eteration.app.databinding.ProductItemBinding
import com.eteration.domain.model.Product

class FavoriteProductAdapter(
    private val onFavoriteClicked: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit
) : RecyclerView.Adapter<FavoriteProductAdapter.FavoriteViewHolder>() {

    private val favoriteItems = mutableListOf<Product>()

    fun setFavorites(favorites: List<Product>) {
        val diffCallback = ProductDiffCallback(favoriteItems, favorites)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        favoriteItems.clear()
        favoriteItems.addAll(favorites)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteItems[position])
    }

    override fun getItemCount(): Int = favoriteItems.size

    inner class FavoriteViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DefaultLocale")
        fun bind(product: Product) {
            binding.apply {
                productTitle.text = product.name
                productPrice.text = String.format("%.2f ₺", product.price)

                Glide.with(root.context)
                    .load(product.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(productImage)

                binding.bookmarkIcon.visibility = View.VISIBLE

                if (product.isInCart){
                    binding.addToCartButton.setText(R.string.in_your_chart)
                    binding.addToCartButton.setBackgroundColor(Color.parseColor("#BABECC"))
                }
                else{
                    binding.addToCartButton.setText(R.string.add_to_cart)
                    binding.addToCartButton.setBackgroundColor(Color.parseColor("#2a59fe"))
                }

                binding.addToCartButton.setOnClickListener {
                    onAddToCartClick(product)
                    binding.addToCartButton.setBackgroundColor(Color.parseColor("#BABECC"))
                    binding.addToCartButton.isClickable = false                }

                root.setOnClickListener {
                    onFavoriteClicked(product)
                }
            }
        }
    }

    private class ProductDiffCallback(
        private val oldList: List<Product>,
        private val newList: List<Product>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
