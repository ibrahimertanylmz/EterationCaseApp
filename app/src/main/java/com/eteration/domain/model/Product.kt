package com.eteration.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val image: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String,
    var isBookmarked: Boolean = false,
    var isInCart: Boolean = false,
    var cartQuantity: Int = 0
) : Parcelable