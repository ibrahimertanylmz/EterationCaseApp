package com.eteration.domain.use_case

import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository

class AddToCartUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product) = repository.updateCartProduct(product)
}