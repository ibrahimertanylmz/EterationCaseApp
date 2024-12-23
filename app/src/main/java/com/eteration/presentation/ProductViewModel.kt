package com.eteration.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.viewmodel.BaseViewModel
import com.eteration.domain.model.FilterParams
import com.eteration.domain.model.Product
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetBookmarksUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import com.eteration.domain.use_case.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    getBookmarksUseCase: GetBookmarksUseCase,
    getCartItemsUseCase: GetCartItemsUseCase,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {

    private val bookmarkedIdsFlow = getBookmarksUseCase().map { it.map { product -> product.id } }
    private val inChartIdsFlow = getCartItemsUseCase().map { it.map { product -> product.id } }

    private val _filterParams = MutableStateFlow(FilterParams())
    val filterParams: StateFlow<FilterParams> = _filterParams

    private val _pagingSourceInvalidation = MutableLiveData<Unit>()
    val pagingSourceInvalidation: LiveData<Unit> get() = _pagingSourceInvalidation

    private fun invalidatePagingSource() {
        _pagingSourceInvalidation.postValue(Unit)
    }

    fun observeChangesAndInvalidate() {
        launchOnDb {
            combine(bookmarkedIdsFlow, inChartIdsFlow) { _, _ ->
                invalidatePagingSource()
            }.collect {  }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val filteredProducts: Flow<PagingData<Product>> = _filterParams
        .debounce(300)
        .flatMapLatest { params ->
            getProductsUseCase.execute(params.nameFilter, params.brandFilter)
        }
        .cachedIn(viewModelScope)

    fun updateFilter(name: String? = null, brand: List<String?> = emptyList(), category: String? = null) {
        val currentParams = _filterParams.value
        _filterParams.value = currentParams.copy(
            nameFilter = name ?: currentParams.nameFilter,
            brandFilter = brand,
            categoryFilter = category ?: currentParams.categoryFilter,
        )
    }

    fun clearFilters() {
        _filterParams.value = FilterParams(
            nameFilter = null,
            brandFilter = emptyList(),
            categoryFilter = null
        )
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            product.cartQuantity += 1
            addToCartUseCase(product)
        }
    }

}