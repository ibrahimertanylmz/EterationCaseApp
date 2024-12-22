package com.eteration.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.viewmodel.BaseViewModel
import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import com.eteration.domain.use_case.AddToBookmarksUseCase
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetBookmarksUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {

    private val _bookmarkItems = MutableStateFlow<List<Product>?>(emptyList())
    val bookmarkItems: StateFlow<List<Product>?> get() = _bookmarkItems

    fun loadBookmarkItems() {
        viewModelScope.launch {
            combine(
                getBookmarksUseCase(),
                getCartItemsUseCase()
            ) { bookmarks, cartItems ->
                bookmarks.map { bookmark ->
                    bookmark.copy(
                        isInCart = cartItems.any { cartItem -> cartItem.id == bookmark.id }
                    )
                }
            }.collect { updatedBookmarks ->
                _bookmarkItems.value = updatedBookmarks
            }
        }
    }

    fun addCartItem(product: Product) {
        viewModelScope.launch {
            addToCartUseCase(product)
        }
    }

}