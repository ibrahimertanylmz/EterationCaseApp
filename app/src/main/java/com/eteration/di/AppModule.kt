package com.eteration.di

import android.content.Context
import androidx.room.Room
import com.eteration.common.Constant
import com.eteration.core.dispatchers.AppDispatcher
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.util.NetworkHelper
import com.eteration.core.util.NetworkHelperImpl
import com.eteration.data.local.AppDatabase
import com.eteration.data.local.dao.ProductDao
import com.eteration.data.remote.service.ProductService
import com.eteration.data.repository.ProductRepositoryImpl
import com.eteration.domain.repository.ProductRepository
import com.eteration.domain.use_case.AddToBookmarksUseCase
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetBookmarksUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import com.eteration.domain.use_case.GetProductsUseCase
import com.eteration.domain.use_case.RemoveFromBookmarksUseCase
import com.eteration.domain.use_case.RemoveFromCartUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesNetworkService(): ProductService {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ProductService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppDispatcher(): Dispatcher {
        return AppDispatcher()
    }

    @Provides
    fun provideNetworkHelper(@ApplicationContext context: Context): NetworkHelper {
        return NetworkHelperImpl(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideProductRepository(
        productService: ProductService,
        productDao: ProductDao
    ): ProductRepository {
        return ProductRepositoryImpl(productService, productDao)
    }

    @Provides
    fun provideGetProductsUseCase(repository: ProductRepository): GetProductsUseCase {
        return GetProductsUseCase(repository)
    }

    @Provides
    fun provideGetCartItemsUseCase(repository: ProductRepository): GetCartItemsUseCase {
        return GetCartItemsUseCase(repository)
    }

    @Provides
    fun provideAddToCartUseCase(repository: ProductRepository): AddToCartUseCase {
        return AddToCartUseCase(repository)
    }

    @Provides
    fun provideGetBookmarksUseCase(repository: ProductRepository): GetBookmarksUseCase {
        return GetBookmarksUseCase(repository)
    }

    @Provides
    fun provideAddToBookmarksUseCase(repository: ProductRepository): AddToBookmarksUseCase {
        return AddToBookmarksUseCase(repository)
    }

    @Provides
    fun removeFromBookmarksUseCase(repository: ProductRepository): RemoveFromBookmarksUseCase {
        return RemoveFromBookmarksUseCase(repository)
    }

    @Provides
    fun removeFromCartUseCase(repository: ProductRepository): RemoveFromCartUseCase {
        return RemoveFromCartUseCase(repository)
    }

}