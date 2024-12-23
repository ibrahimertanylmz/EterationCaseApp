package com.eteration.presentation.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eteration.domain.use_case.GetCartItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartBadgeViewModel @Inject constructor(private val getCartItemsUseCase: GetCartItemsUseCase) :
    ViewModel() {

    private val _cartItemCount = MutableLiveData(0)
    val cartItemCount: LiveData<Int> = _cartItemCount

    private val inChartIds = getCartItemsUseCase().map { it.map { product -> product.id } }

    init {
        setChartItemQuantity()
    }

    private fun setChartItemQuantity() {
        viewModelScope.launch {
            inChartIds.collect { idList ->
                val size = idList.size
                _cartItemCount.value = size
            }
        }
    }
}