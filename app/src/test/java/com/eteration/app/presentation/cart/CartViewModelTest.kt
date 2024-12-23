package com.eteration.app.presentation.cart

import app.cash.turbine.test
import com.eteration.app.presentation.utils.TestingDispatcher
import com.eteration.domain.model.Product
import com.eteration.domain.use_case.AddToCartUseCase
import com.eteration.domain.use_case.GetCartItemsUseCase
import com.eteration.presentation.cart.CartViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private val getCartItemsUseCase: GetCartItemsUseCase = mockk()
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val dispatcher = TestingDispatcher()

    @Before
    fun setup() {
        viewModel = CartViewModel(getCartItemsUseCase, addToCartUseCase, dispatcher)
    }

    @Test
    fun `loadCartItems should update cartProducts and totalPrice`() = runTest {
        val product1 = Product(id = "1", name = "Product 1",image = "imageUrl", price = 10.0, description = "desc", model = "model", brand = "brand","23122024", isInCart = true ,cartQuantity = 2)
        val product2 = Product(id = "2", name = "Product 2",image = "imageUrl", price = 5.0, description = "desc", model = "model", brand = "brand","23122024", isInCart = true ,cartQuantity = 3)
        val cartProducts = listOf(product1, product2)
        every { getCartItemsUseCase() } returns flowOf(cartProducts)

        viewModel.loadCartItems()
        viewModel.cartProducts.test {
            val products = awaitItem()
            assertEquals(cartProducts, products)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.totalPrice.test {
            val total = awaitItem()
            assertEquals(35.0, total, 0.0)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateCartItem should call addToCartUseCase`() = runTest {
        val product = Product(id = "1", name = "Product 1",image = "imageUrl", price = 10.0, description = "desc", model = "model", brand = "brand","23122024", isInCart = true ,cartQuantity = 2)
        viewModel.updateCartItem(product)
        coVerify { addToCartUseCase(product) }
    }
}