package com.eteration.data.mapping

import com.eteration.data.local.BookmarkEntity
import com.eteration.data.local.CartEntity
import com.eteration.data.local.ProductEntity
import com.eteration.data.remote.model.ProductResponse
import com.eteration.domain.model.Product

fun ProductResponse.toDomain(): Product {
    return Product(
        id = id ?: "0",
        name = name ?: "Unknown",
        image = image ?: "",
        price = price ?: 0.00,
        description = description ?: "No Description",
        model = model ?: "Unknown",
        brand = brand ?: "Unknown",
        createdAt = createdAt,
        isBookmarked = false,
        isInCart = false,
        cartQuantity = 0
    )
}


fun ProductEntity.toDomain() = Product(
    id = id,
    name = name,
    image = image,
    price = price,
    description = description,
    model = model,
    brand = brand,
    createdAt = createdAt,
    isBookmarked = isBookmarked,
    isInCart = isInCart
)


fun Product.toCartEntity() = CartEntity(
    id = id,
    name = name,
    image = image,
    price = price,
    description = description,
    model = model,
    brand = brand,
    createdAt = createdAt,
    isBookmarked = isBookmarked,
    isInCart = isInCart,
    chartQuantity = cartQuantity
)

fun CartEntity.toProduct() = Product(
    id = id,
    name = name,
    image = image,
    price = price,
    description = description,
    model = model,
    brand = brand,
    createdAt = createdAt,
    isBookmarked = isBookmarked,
    isInCart = isInCart,
    cartQuantity = chartQuantity
)

fun Product.toBookmarkEntity() = BookmarkEntity(
    id = id,
    name = name,
    image = image,
    price = price,
    description = description,
    model = model,
    brand = brand,
    createdAt = createdAt,
    isBookmarked = isBookmarked,
    isInCart = isInCart,
    chartQuantity = cartQuantity
)

fun BookmarkEntity.toProduct() = Product(
    id = id,
    name = name,
    image = image,
    price = price,
    description = description,
    model = model,
    brand = brand,
    createdAt = createdAt,
    isBookmarked = isBookmarked,
    isInCart = isInCart,
    cartQuantity = chartQuantity
)

