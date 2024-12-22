package com.eteration.domain.use_case

import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow

class GetBookmarksUseCase(private val repository: ProductRepository) {
    operator fun invoke(): Flow<List<Product>> = repository.getBookmarks()
}