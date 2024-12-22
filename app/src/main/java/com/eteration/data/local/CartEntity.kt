package com.eteration.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eteration.data.remote.model.ProductResponse

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey val id: String,
    val name: String,
    val image: String,
    val price: Double,
    val description: String,
    val model: String,
    val brand: String,
    val createdAt: String,
    val isBookmarked: Boolean,
    val isInCart: Boolean,
    val chartQuantity: Int
)