package com.eteration.data.mapping

import com.eteration.data.local.ProductEntity
import com.eteration.data.remote.model.ProductResponse
import com.eteration.domain.model.Product

fun ProductResponse.toDomain(): Product {
    return Product(
        id = id ?: "0",
        name = name ?: "Unknown",
        image = image ?: "",
        price = price ?: 0.0,
        description = description ?: "No Description",
        model = model ?: "Unknown",
        brand = brand ?: "Unknown",
        createdAt = createdAt,
        isBookmarked = false,
        isInCart = false
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