package com.eteration.domain.use_case

import com.eteration.domain.repository.ProductRepository


class RemoveFromCartUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(productId: String) {
        repository.removeFromCart(productId)
    }
}