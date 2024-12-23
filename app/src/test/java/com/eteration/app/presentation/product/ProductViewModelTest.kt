package com.eteration.app.presentation.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.eteration.app.presentation.utils.TestCoroutineRule
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.core.util.NetworkHelper
import com.eteration.domain.model.Product
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetBookmarksUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import com.eteration.domain.use_case.GetProductsUseCase
import com.eteration.presentation.product.ProductViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import androidx.lifecycle.Observer
import androidx.paging.PagingData
import androidx.paging.map
import com.eteration.domain.model.FilterParams
import io.mockk.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var productViewModel: ProductViewModel

    private val getProductsUseCase: GetProductsUseCase = mockk()
    private val addToCartUseCase: AddToCartUseCase = mockk()
    private val getBookmarksUseCase: GetBookmarksUseCase = mockk()
    private val getCartItemsUseCase: GetCartItemsUseCase = mockk()
    private val networkHelper: NetworkHelper = mockk()
    private val dispatcher: Dispatcher = mockk()

    private val networkObserver: Observer<Boolean> = mockk(relaxed = true)

    @Before
    fun setUp() {
        coEvery { getBookmarksUseCase() } returns flow { emit(listOf()) }
        coEvery { getCartItemsUseCase() } returns flow { emit(listOf()) }

        every { networkHelper.isNetworkConnected() } returns true

        productViewModel = ProductViewModel(
            getProductsUseCase,
            addToCartUseCase,
            getBookmarksUseCase,
            getCartItemsUseCase,
            networkHelper,
            dispatcher
        )
    }

    @Test
    fun `when network is connected, should update isConnected LiveData`() = runTest {
        every { networkHelper.isNetworkConnected() } returns true

        productViewModel.isConnected.observeForever(networkObserver)
        productViewModel.checkNetworkState()

        verify { networkObserver.onChanged(true) }
    }

    @Test
    fun `when network is disconnected, should update isConnected LiveData`() = runTest {
        every { networkHelper.isNetworkConnected() } returns false

        productViewModel.isConnected.observeForever(networkObserver)
        productViewModel.checkNetworkState()

        verify { networkObserver.onChanged(false) }
    }

    @Test
    fun `when filter is updated, should change filterParams state`() = runTest {
        val name = "Ford"
        val brand = listOf("Ford")

        productViewModel.updateFilter(name = name, brand = brand)

        assertEquals(name, productViewModel.filterParams.value.nameFilter)
        assertEquals(brand, productViewModel.filterParams.value.brandFilter)
    }

    @Test
    fun `when clear filters is called, should reset filterParams state`() = runTest {

        productViewModel.updateFilter(name = "Ford", brand = listOf("Ford"))
        productViewModel.clearFilters()

        assertNull(productViewModel.filterParams.value.nameFilter)
        assertTrue(productViewModel.filterParams.value.brandFilter.isEmpty())
        assertNull(productViewModel.filterParams.value.categoryFilter)
    }

    @Test
    fun `when filtered products are requested, should return filtered products flow`() = runTest {
         val testProduct = Product(
            id = "1",
            name = "Ford Fiesta",
            image = "imageUrl",
            price = 799.99,
            description = "Best car ever.",
            model = "2024",
            brand = "Ford",
            createdAt = "2024-12-23",
            isBookmarked = false,
            isInCart = false,
            cartQuantity = 0
        )
        val pagingData = PagingData.from(listOf(testProduct))
        val filterParams = FilterParams(nameFilter = "Fiesta", brandFilter = listOf("Ford"))
        val flow = flowOf(pagingData)

        every { getProductsUseCase.execute(any(), any()) } returns flow
        productViewModel.updateFilter(filterParams.nameFilter, filterParams.brandFilter)

        val list = mutableListOf<Product>()

        val job = launch {
            productViewModel.filteredProducts.collectLatest { result ->
                result.map { product -> list.add(product) }
            }
        }
        advanceUntilIdle()
        job.cancel()
        assertNotNull(list)
        assertEquals(listOf(testProduct), list)
    }
}
