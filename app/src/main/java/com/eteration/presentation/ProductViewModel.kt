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
    appDispatcher: Dispatcher
) : BaseViewModel(appDispatcher) {

    //val productList: Flow<PagingData<Product>> = repository.getProducts().cachedIn(viewModelScope)

    private val searchQuery = MutableStateFlow<String>("")
    private val _filterParams = MutableStateFlow(FilterParams())


    private val _filteredProducts = MutableLiveData<PagingData<Product>>()
    //val filteredProducts: LiveData<PagingData<Product>> = _filteredProducts

    private var nameFilter: String? = null
    private var brandFilter: String? = null

    /*fun loadProducts() {
        viewModelScope.launch {
            repository.getProducts(nameFilter, brandFilter)
                .flow
                .cachedIn(viewModelScope)
                .collectLatest { pagingData ->
                    _filteredProducts.postValue(pagingData)
                }
        }
    }

     */

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


    fun addToCart(productId: String, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(productId, quantity)
        }
    }


    // 🔥 Kullanıcının arama sorgusunu güncelle
    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }
}