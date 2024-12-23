package com.eteration.app.domain.use_case.add_to_bookmarks

import com.eteration.domain.model.Product
import com.eteration.domain.repository.ProductRepository
import com.eteration.domain.use_case.AddToBookmarksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class AddToBookmarksUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var addToBookmarksUseCase: AddToBookmarksUseCase

    @Before
    fun setUp() {
        repository = mockk()
        addToBookmarksUseCase = AddToBookmarksUseCase(repository)
    }

    @Test
    fun `should call addToBookmarks method with correct product`() = runTest {
        val product = Product(
            id = "1",
            name = "Product 1",
            image = "imageUrl",
            price = 10.0,
            description = "desc",
            model = "model",
            brand = "brand",
            createdAt = "23122024",
            isBookmarked = false,
            isInCart = false,
            cartQuantity = 0
        )

        coEvery { repository.addToBookmarks(product) } returns Unit

        addToBookmarksUseCase(product)

        coVerify { repository.addToBookmarks(product) }
    }

    @Test
    fun `invoke should call repository addToBookmarks with correct product even if already bookmarked`() = runTest {
        val product = Product(
            id = "2",
            name = "Product 2",
            image = "imageUrl",
            price = 20.0,
            description = "desc",
            model = "model",
            brand = "brand",
            createdAt = "23122024",
            isInCart = false,
            cartQuantity = 0,
            isBookmarked = true
        )

        coEvery { repository.addToBookmarks(product) } returns Unit

        addToBookmarksUseCase(product)

        coVerify {
            repository.addToBookmarks(product)
        }
    }
}