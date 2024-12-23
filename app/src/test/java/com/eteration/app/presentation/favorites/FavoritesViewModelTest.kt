package com.eteration.app.presentation.favorites

import app.cash.turbine.test
import com.eteration.app.presentation.utils.TestingDispatcher
import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetBookmarksUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import com.eteration.presentation.favorite_products.FavoritesViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FavoritesViewModelTest {

    private lateinit var viewModel: FavoritesViewModel
    private val repository: ProductRepository = mockk()
    private val getBookmarksUseCase: GetBookmarksUseCase = mockk()
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val getCartItemsUseCase: GetCartItemsUseCase = mockk()
    private val dispatcher = TestingDispatcher()
    private val testDispatcher = StandardTestDispatcher()



    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FavoritesViewModel(
            repository,
            getBookmarksUseCase,
            addToCartUseCase,
            getCartItemsUseCase,
            dispatcher
        )
    }

    @Test
    fun `loadBookmarkItems should update bookmarkItems with isInCart flag`() = runTest {
        val bookmarkedProduct = Product(id = "1", name = "Product Bookmarked 1",image = "imageUrl", price = 10.0, description = "desc", model = "model", brand = "brand","23122024", isInCart = false , isBookmarked = true, cartQuantity = 0)
        val bookmarkedProduct2 = Product(id = "2", name = "Product Bookmarked 2",image = "imageUrl", price = 10.0, description = "desc", model = "model", brand = "brand","23122024", isInCart = false, isBookmarked = true ,cartQuantity = 0)
        val cartProduct = Product(id = "1", name = "Product Bookmarked 1",image = "imageUrl", price = 10.0, description = "desc", model = "model", brand = "brand","23122024", isInCart = true ,cartQuantity = 2)

        every { getBookmarksUseCase() } returns flowOf(listOf(bookmarkedProduct, bookmarkedProduct2))
        every { getCartItemsUseCase() } returns flowOf(listOf(cartProduct))

        viewModel.loadBookmarkItems()

        viewModel.bookmarkItems.test {
            val updatedBookmarks = awaitItem()
            assertEquals(2, updatedBookmarks?.size)
            assertEquals(true, updatedBookmarks?.first { it.id == "1" }?.isInCart)
            assertEquals(false, updatedBookmarks?.first { it.id == "2" }?.isInCart)
            cancelAndIgnoreRemainingEvents()
        }
    }
}