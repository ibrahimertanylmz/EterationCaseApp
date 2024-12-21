package com.eteration.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eteration.common.Resource
import com.eteration.data.local.BookmarkEntity
import com.eteration.data.local.CartEntity
import com.eteration.data.local.ProductDao
import com.eteration.data.remote.model.ProductResponse
import com.eteration.data.remote.service.ProductService
import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(private val service: ProductService, private val productDao: ProductDao) : ProductRepository {
    override fun getProducts(nameFilter: String?, brandFilter: String?):  Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 4, enablePlaceholders = false),
            pagingSourceFactory = { ProductPagingSource(service, nameFilter, brandFilter, productDao) }
        ).flow
    }

    override suspend fun getProductsFiltered(category: String): Flow<PagingData<ProductResponse>> {
        TODO("Not yet implemented")
    }

    // 2️⃣ Toggle bookmark (add or remove)
    override suspend fun toggleBookmark(productId: String) {
        val currentBookmarks = productDao.getAllBookmarkedIds().firstOrNull().orEmpty()
        if (currentBookmarks.contains(productId)) {
            productDao.deleteBookmark(productId)
        } else {
            productDao.insertBookmark(BookmarkEntity(productId))
        }
    }

    // 3️⃣ Add item to cart
    override suspend fun addToCart(productId: String, quantity: Int) {
        productDao.addToCart(CartEntity(productId, quantity))
    }

    // 4️⃣ Remove item from cart
    override suspend fun removeFromCart(productId: String) {
        productDao.removeFromCart(productId)
    }

    // 5️⃣ Get all cart items
    override fun getCartItems(): Flow<List<CartEntity>> {
        return productDao.getAllCartItems()
    }

}