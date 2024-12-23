package com.eteration.app.presentation.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.eteration.domain.model.Product
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.getOrAwaitValue(): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = Observer<T> {
        data = it
        latch.countDown()
    }
    this.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)
    this.removeObserver(observer)
    return data ?: throw NullPointerException("No value was set on LiveData")
}

private val testProduct = Product(
    id = "1",
    name = "Ford Fiesta",
    image = "imageUrl",
    price = 799.99,
    description = "Best car ever.",
    model = "2024",
    brand = "Ford",
    createdAt = "2024-12-23",
    isBookmarked = false,
    isInCart = false,
    cartQuantity = 0
)