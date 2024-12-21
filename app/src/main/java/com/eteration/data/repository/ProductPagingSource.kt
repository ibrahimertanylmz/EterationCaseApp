package com.eteration.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eteration.data.local.ProductDao
import com.eteration.data.mapping.toDomain
import com.eteration.data.remote.model.ProductResponse
import com.eteration.data.remote.service.ProductService
import com.eteration.domain.model.Product
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val productService: ProductService,
    private val searchQuery: String?,
    private val brand: String?,
    private val productDao: ProductDao
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: 1

        return try {
            val productsResponse = productService.getProducts(
                page = position, limit = params.loadSize, query = searchQuery,
                brand = brand
            ).map { it.toDomain() }

            val bookmarkedIds = productDao.getAllBookmarkedIds().firstOrNull().orEmpty()

            val products = productsResponse.map { product ->
                product.copy(
                    isBookmarked = bookmarkedIds.contains(product.id)
                )
            }

            LoadResult.Page(
                data = products,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (products.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}