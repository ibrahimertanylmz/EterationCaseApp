package com.eteration.presentation

import androidx.lifecycle.viewModelScope
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.viewmodel.BaseViewModel
import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import com.eteration.domain.use_case.AddToBookmarksUseCase
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.RemoveFromBookmarksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val addToBookmarksUseCase: AddToBookmarksUseCase,
    private val removeFromBookmarksUseCase: RemoveFromBookmarksUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {


    fun addToBookmarks(product: Product) {
        viewModelScope.launch {
            addToBookmarksUseCase(product)
        }
    }

    fun removeFromBookmarks(productId: String) {
        viewModelScope.launch {
            removeFromBookmarksUseCase(productId)
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            product.cartQuantity += 1
            addToCartUseCase(product)
        }
    }

}