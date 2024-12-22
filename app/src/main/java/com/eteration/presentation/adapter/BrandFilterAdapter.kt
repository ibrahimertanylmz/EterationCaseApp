package com.eteration.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eteration.app.databinding.BrandFilterItemBinding
import com.eteration.domain.model.BrandFilterOption

class BrandFilterAdapter(
    private val brands: List<BrandFilterOption>,
    private val onBrandSelected: (BrandFilterOption) -> Unit
) : RecyclerView.Adapter<BrandFilterAdapter.BrandViewHolder>() {

    private var brandList = brands

    fun submitList(newBrands: List<BrandFilterOption>) {
        brandList = newBrands
        notifyDataSetChanged()
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
                onBrandSelected(brand)
            }
        }
    }
}