package com.eteration.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.eteration.app.databinding.BrandFilterItemBinding
import com.eteration.domain.model.BrandFilterOption


class BrandFilterAdapter : RecyclerView.Adapter<BrandFilterAdapter.BrandViewHolder>() {

    private var brandList = mutableListOf<BrandFilterOption>()

    fun submitList(newBrands: List<BrandFilterOption>) {
        val diffCallback = BrandDiffCallback(brandList, newBrands)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        brandList.clear()
        brandList.addAll(newBrands)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding = BrandFilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brand = brandList[position]
        holder.bind(brand)
    }

    override fun getItemCount(): Int = brandList.size

    inner class BrandViewHolder(private val binding: BrandFilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: BrandFilterOption) {
            binding.checkboxBrand.text = brand.name
            binding.checkboxBrand.isChecked = brand.isSelected
            binding.checkboxBrand.setOnCheckedChangeListener { _, isChecked ->
                brand.isSelected = isChecked
                //onBrandSelected(brand)
            }
        }
    }

    class BrandDiffCallback(
        private val oldList: List<BrandFilterOption>,
        private val newList: List<BrandFilterOption>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}