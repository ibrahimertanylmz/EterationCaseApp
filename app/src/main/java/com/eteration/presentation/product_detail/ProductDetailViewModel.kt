package com.eteration.presentation.product_detail

import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.viewmodel.BaseViewModel
import com.eteration.domain.model.Product
import com.eteration.domain.use_case.AddToBookmarksUseCase
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.RemoveFromBookmarksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val addToBookmarksUseCase: AddToBookmarksUseCase,
    private val removeFromBookmarksUseCase: RemoveFromBookmarksUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {

    fun addToBookmarks(product: Product) {
        launchOnDb {
            addToBookmarksUseCase(product)
        }
    }

    fun removeFromBookmarks(productId: String) {
        launchOnDb {
            removeFromBookmarksUseCase(productId)
        }
    }

    fun addToCart(product: Product) {
        launchOnDb {
            product.cartQuantity += 1
            addToCartUseCase(product)
        }
    }

}