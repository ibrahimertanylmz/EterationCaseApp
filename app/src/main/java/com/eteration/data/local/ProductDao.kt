package com.eteration.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    // 2️⃣ Bookmark Queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE productId = :productId")
    suspend fun deleteBookmark(productId: String)

    @Query("SELECT productId FROM bookmarks")
    fun getAllBookmarkedIds(): Flow<List<String>>

    // 3️⃣ Cart Queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartEntity)

    @Query("DELETE FROM cart WHERE productId = :productId")
    suspend fun removeFromCart(productId: String)

    @Query("SELECT * FROM cart")
    fun getAllCartItems(): Flow<List<CartEntity>>
}