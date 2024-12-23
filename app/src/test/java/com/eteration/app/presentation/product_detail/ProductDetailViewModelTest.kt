package com.eteration.app.presentation.product_detail

import com.eteration.app.presentation.utils.TestingDispatcher
import com.eteration.core.dispatchers.Dispatcher
import com.eteration.domain.model.Product
import com.eteration.domain.use_case.AddToBookmarksUseCase
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.RemoveFromBookmarksUseCase
import com.eteration.presentation.product_detail.ProductDetailViewModel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    private val addToBookmarksUseCase: AddToBookmarksUseCase = mockk(relaxed = true)
    private val removeFromBookmarksUseCase: RemoveFromBookmarksUseCase = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val appDispatcher: Dispatcher = mockk(relaxed = true)

    private lateinit var viewModel: ProductDetailViewModel

    private val testDispatcher = TestingDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ProductDetailViewModel(
            addToBookmarksUseCase,
            removeFromBookmarksUseCase,
            addToCartUseCase,
            testDispatcher
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val testProduct = Product(
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

    @Test
    fun `addToBookmarks should call addToBookmarksUseCase with the correct product`(): Unit = runTest {
        viewModel.addToBookmarks(testProduct)
        coVerify { addToBookmarksUseCase(testProduct) }
    }

    @Test
    fun `removeFromBookmarks should call removeFromBookmarksUseCase with the correct product ID`() = runTest {
        val testProductId = "1"
        viewModel.removeFromBookmarks(testProductId)
        coVerify { removeFromBookmarksUseCase(testProductId) }
    }

    @Test
    fun `addToCart should increment product quantity and call addToCartUseCase`() = runTest {
        viewModel.addToCart(testProduct)
        assertEquals(1, testProduct.cartQuantity)
        coVerify { addToCartUseCase(testProduct) }
    }
}