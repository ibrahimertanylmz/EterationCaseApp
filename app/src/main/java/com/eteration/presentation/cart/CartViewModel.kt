package com.eteration.presentation.cart

import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.viewmodel.BaseViewModel
import com.eteration.domain.model.Product
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {

    private val _cartProducts = MutableStateFlow<List<Product>?>(emptyList())
    val cartProducts: StateFlow<List<Product>?> get() = _cartProducts

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> get() = _totalPrice

     fun loadCartItems() {
        launchOnDb {
            getCartItemsUseCase().collect { products ->
                _cartProducts.value = products
                updateTotalPrice(products)
            }
        }
    }

    fun updateCartItem(product: Product) {
        launchOnDb {
            addToCartUseCase(product)
        }
    }

    private fun updateTotalPrice(cartProducts: List<Product>) {
        val total = cartProducts.sumOf { it.price * it.cartQuantity }
        _totalPrice.value = total
    }

}