package com.eteration.domain.use_case

import com.eteration.domain.repository.ProductRepository

class RemoveFromBookmarksUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(productId: String) {
        repository.removeFromBookmarks(productId)
    }
}