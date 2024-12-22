package com.eteration.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eteration.app.databinding.CartItemBinding
import com.eteration.domain.model.Product


class CartAdapter(
    private val onIncreaseClicked: (Product) -> Unit,
    private val onDecreaseClicked: (Product) -> Unit,
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val cartItems = mutableListOf<Product>()

    fun setCartItems(cartProducts: List<Product>) {
        val diffCallback = ProductDiffCallback(cartItems, cartProducts)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        cartItems.clear()
        cartItems.addAll(cartProducts)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DefaultLocale")
        fun bind(product: Product) {
            binding.apply {
                productName.text = product.name
                productPrice.text = String.format("%.2f ₺", product.price * product.cartQuantity)
                quantityText.text = product.cartQuantity.toString()
                increaseQuantityButton.setOnClickListener {
                    product.cartQuantity += 1
                    productPrice.text = String.format("%.2f ₺", product.price * product.cartQuantity)
                    quantityText.text = product.cartQuantity.toString()
                    onIncreaseClicked(product)
                }
                decreaseQuantityButton.setOnClickListener {
                    product.cartQuantity -= 1
                    productPrice.text = String.format("%.2f ₺", product.price * product.cartQuantity)
                    if (product.cartQuantity != 0) quantityText.text = product.cartQuantity.toString()
                    onDecreaseClicked(product)
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

