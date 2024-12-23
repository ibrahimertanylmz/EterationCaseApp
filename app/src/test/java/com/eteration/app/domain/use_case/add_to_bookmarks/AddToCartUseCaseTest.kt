package com.eteration.app.domain.use_case.add_to_bookmarks

import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import com.eteration.domain.use_case.AddToBookmarksUseCase
import com.eteration.domain.use_case.AddToCartUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddToCartUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var addToCartUseCase: AddToCartUseCase

    @Before
    fun setUp() {
        repository = mockk()
        addToCartUseCase = AddToCartUseCase(repository)
    }

    @Test
    fun `invoke should call repository updateCartProduct with product`() = runBlocking {
        val product = Product(
            id = "1",
            name = "Product 1",
            image = "imageUrl",
            price = 10.0,
            description = "desc",
            model = "model",
            brand = "brand",
            createdAt = "23122024",
            isInCart = true,
            cartQuantity = 1
        )

        coEvery { repository.updateCartProduct(product) } returns Unit

        addToCartUseCase(product)

        coVerify {
            repository.updateCartProduct(product)
        }
    }
}