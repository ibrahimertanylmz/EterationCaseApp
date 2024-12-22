package com.eteration.presentation

import androidx.lifecycle.viewModelScope
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.viewmodel.BaseViewModel
import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import com.eteration.domain.use_case.RemoveFromCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {

    private val _cartProducts = MutableStateFlow<List<Product>?>(emptyList())
    val cartProducts: StateFlow<List<Product>?> get() = _cartProducts

    private val _totalPrice = MutableStateFlow(0.0)  // Store the total price
    val totalPrice: StateFlow<Double> get() = _totalPrice

     fun loadCartItems() {
        viewModelScope.launch {
            getCartItemsUseCase().collect { products ->
                _cartProducts.value = products
                updateTotalPrice(products)  // Update the total price whenever the cart changes
            }
        }
    }

    fun updateCartItem(product: Product) {
        viewModelScope.launch {
            addToCartUseCase(product)
        }
    }

    private fun updateTotalPrice(cartProducts: List<Product>) {
        val total = cartProducts.sumOf { it.price * it.cartQuantity }
        _totalPrice.value = total
    }

    fun decreaseCartItemQuantity(productId: String) {
        viewModelScope.launch {
            removeFromCartUseCase(productId)
        }
    }

}