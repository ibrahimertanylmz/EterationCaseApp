package com.eteration.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.viewmodel.BaseViewModel
import com.eteration.data.remote.model.ProductResponse
import com.eteration.data.repository.ProductPagingSource
import com.eteration.domain.model.FilterParams
import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetBookmarksUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import com.eteration.domain.use_case.RemoveFromCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val addToCartUseCase: AddToCartUseCase,
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {

    val selectedBrand = MutableLiveData<String>()


    //val productList: Flow<PagingData<Product>> = repository.getProducts().cachedIn(viewModelScope)

    val bookmarkedIdsFlow = getBookmarksUseCase().map { it.map { product -> product.id } }
    val inChartIdsFlow = getCartItemsUseCase().map { it.map { product -> product.id } }

    private val _pagingSourceInvalidation = MutableLiveData<Unit>()
    val pagingSourceInvalidation: LiveData<Unit> get() = _pagingSourceInvalidation

    private fun invalidatePagingSource() {
        _pagingSourceInvalidation.postValue(Unit)
    }

    // Observe changes and trigger invalidation when updated
    fun observeChangesAndInvalidate() {
        viewModelScope.launch {
            // Use combine to listen for changes in both flows
            combine(bookmarkedIdsFlow, inChartIdsFlow) { bookmarkedIds, inChartIds ->
                invalidatePagingSource()
            }.collect {  }
        }
    }

    private val searchQuery = MutableStateFlow<String>("")
    private val _filterParams = MutableStateFlow(FilterParams()) // Your filter params class
    val filterParams: StateFlow<FilterParams> = _filterParams


    private val _filteredProducts = MutableLiveData<PagingData<Product>>()
    //val filteredProducts: LiveData<PagingData<Product>> = _filteredProducts

    private var nameFilter: String? = null
    var brandFilter: String? = null



    /*fun applyFilters(name: String? = null, brand: String? = null, sort: String? = null, model: String? = null) {
        nameFilter = name
        brandFilter = brand
        loadProducts() // Reload the products with the applied filters
    }

     */

    val filteredProducts: Flow<PagingData<Product>> = _filterParams
        .debounce(300) // Filtreler hızlı değişirse, yalnızca sonuncusunu işler
        .flatMapLatest { params ->
            repository.getProducts(params.nameFilter, params.brandFilter)
        }
        .cachedIn(viewModelScope) // Verileri ViewModel düzeyinde önbelleğe al

    // Filtre güncelleme fonksiyonu, filtrede bir değişiklik olduğunda çağrılır
    fun updateFilter(name: String? = null, brand: String? = null, category: String? = null) {
        val currentParams = _filterParams.value
        _filterParams.value = currentParams.copy(
            nameFilter = name ?: currentParams.nameFilter,
            brandFilter = brand ?: currentParams.brandFilter,
            categoryFilter = category ?: currentParams.categoryFilter,
        )
    }



    // 🔥 Kullanıcının arama sorgusunu güncelle
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            product.cartQuantity += 1
            addToCartUseCase(product)
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            removeFromCartUseCase(productId)
        }
    }

}