package com.eteration.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eteration.data.local.ProductDao
import com.eteration.data.mapping.toBookmarkEntity
import com.eteration.data.mapping.toCartEntity
import com.eteration.data.mapping.toProduct
import com.eteration.data.remote.model.ProductResponse
import com.eteration.data.remote.service.ProductService
import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(
    private val service: ProductService,
    private val productDao: ProductDao
) : ProductRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getProducts(nameFilter: String?, brandFilter: List<String?>): Flow<PagingData<Product>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                ProductPagingSource(
                    service,
                    nameFilter,
                    brandFilter,
                    productDao
                )
            }
        )

        return productDao.getBookmarks().distinctUntilChanged()
            .flatMapLatest {
                pager.flow
            }
    }

    override suspend fun getProductsFiltered(category: String): Flow<PagingData<ProductResponse>> {
        TODO("Not yet implemented")
    }

    override fun getCartItems(): Flow<List<Product>> {
        return productDao.getCartItems().map { cartEntities ->
            cartEntities.map { it.toProduct() }
        }
    }

    override suspend fun updateCartProduct(product: Product) {
        if (product.cartQuantity > 0) productDao.insertCartItem(product.toCartEntity())
        else productDao.deleteCartItem(product.id)
    }

    override suspend fun removeFromCart(productId: String) {
        productDao.deleteCartItem(productId)
    }

    override fun getBookmarks(): Flow<List<Product>> {
        return productDao.getBookmarks().map { bookmarkEntities ->
            bookmarkEntities.map { it.toProduct() }
        }
    }

    override suspend fun addToBookmarks(product: Product) {
        productDao.insertBookmark(product.toBookmarkEntity())
    }

    override suspend fun removeFromBookmarks(productId: String) {
        productDao.deleteBookmark(productId)
    }
}