package com.eteration.core.util

import android.annotation.SuppressLint

object PriceFormatter {
    @SuppressLint("DefaultLocale")
    fun formatPriceWithQuantity(price: Double, quantity: Int): String {
        return String.format("%.2f ₺", price * quantity)
    }
    @SuppressLint("DefaultLocale")
    fun formatPrice(price: Double): String {
        return String.format("%.2f ₺", price)
    }
}