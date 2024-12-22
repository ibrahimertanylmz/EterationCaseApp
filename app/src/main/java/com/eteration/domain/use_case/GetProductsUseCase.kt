package com.eteration.domain.use_case

import androidx.paging.PagingData
import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val productRepository: ProductRepository) {

    fun execute(nameFilter: String? = null, brandFilter: String? = null): Flow<PagingData<Product>> {
        return productRepository.getProducts(nameFilter, brandFilter)
    }
}