package com.eteration.data.remote.service

import com.eteration.data.remote.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {
    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("search") query: String? = null,
        @Query("brand") brand: String? = null,
    ): List<ProductResponse>

    @GET("products")
    suspend fun getProductsFiltered(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("category") category: String,
    ): List<ProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: String): ProductResponse
}