package com.eteration.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eteration.common.Constant

@Entity(tableName = Constant.TABLE_BOOKMARKS)
data class BookmarkEntity(
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

