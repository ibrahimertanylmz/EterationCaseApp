package com.eteration.di

import android.content.Context
import androidx.room.Room
import com.eteration.common.Constant
import com.eteration.core.dispatchers.AppDispatcher
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.data.local.AppDatabase
import com.eteration.data.local.ProductDao
import com.eteration.data.remote.service.ProductService
import com.eteration.data.repository.ProductRepositoryImpl
import com.eteration.domain.repository.ProductRepository
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

}