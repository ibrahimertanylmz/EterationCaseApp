package com.eteration.domain.repository

import androidx.paging.PagingData
import com.eteration.data.remote.model.ProductResponse
import com.eteration.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProducts( nameFilter: String? = null,
                     brandFilter: List<String?> = emptyList()
    ):  Flow<PagingData<Product>>
    fun getCartItems(): Flow<List<Product>>
    suspend fun updateCartProduct(product: Product)
    suspend fun removeFromCart(productId: String)
    fun getBookmarks(): Flow<List<Product>>
    suspend fun addToBookmarks(product: Product)
    suspend fun removeFromBookmarks(productId: String)
}