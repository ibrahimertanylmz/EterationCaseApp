package com.eteration.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eteration.data.local.dao.ProductDao
import com.eteration.data.mapping.toDomain
import com.eteration.data.remote.service.ProductService
import com.eteration.domain.model.Product
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val productService: ProductService,
    private val searchQuery: String?,
    private val brandList: List<String?>,
    productDao: ProductDao
) : PagingSource<Int, Product>() {

    private val bookmarkedIdsFlow =
        productDao.getBookmarks().map { it.map { product -> product.id } }
    private val inChartIdsFlow = productDao.getCartItems().map { it.map { product -> product.id } }
    private var productResponse =  emptyList<Product>()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: 1

        return try {
            if (brandList.isEmpty()){
                productResponse = productService.getProducts(
                    page = position,
                    limit = params.loadSize,
                    query = searchQuery,
                    brand = null
                ).map { it.toDomain() }
            }else {
                productResponse = brandList.flatMap { brand ->
                    productService.getProducts(
                        page = position,
                        limit = params.loadSize,
                        query = searchQuery,
                        brand = brand
                    ).map { it.toDomain() }
                }
            }

            val bookmarkedIds = bookmarkedIdsFlow.first()
            val inChartIds = inChartIdsFlow.first()

            val products = productResponse.map { product ->
                product.copy(
                    isBookmarked = bookmarkedIds.contains(product.id),
                    isInCart = inChartIds.contains(product.id)
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
        val anchorPosition = state.anchorPosition ?: return null
        return state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
            ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
    }

}