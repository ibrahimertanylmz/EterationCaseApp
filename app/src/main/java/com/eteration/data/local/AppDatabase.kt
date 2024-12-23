package com.eteration.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eteration.data.local.dao.ProductDao
import com.eteration.data.local.entity.BookmarkEntity
import com.eteration.data.local.entity.CartEntity

@Database(entities = [CartEntity::class, BookmarkEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
}