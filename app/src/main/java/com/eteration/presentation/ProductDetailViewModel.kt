package com.eteration.presentation

import androidx.lifecycle.viewModelScope
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.viewmodel.BaseViewModel
import com.eteration.domain.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {


    fun toggleBookmark(productId: String) {
        viewModelScope.launch {
            repository.toggleBookmark(productId)
        }
    }

}