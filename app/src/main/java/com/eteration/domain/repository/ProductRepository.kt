package com.eteration.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.eteration.common.Resource
import com.eteration.data.local.CartEntity
import com.eteration.data.remote.model.ProductResponse
import com.eteration.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    fun getProducts( nameFilter: String? = null,
                     brandFilter: String? = null):  Flow<PagingData<Product>>

    suspend fun getProductsFiltered(category: String): Flow<PagingData<ProductResponse>>

    suspend fun toggleBookmark(productId: String)
    suspend fun addToCart(productId: String, quantity: Int)
    suspend fun removeFromCart(productId: String)
    fun getCartItems(): Flow<List<CartEntity>>
}