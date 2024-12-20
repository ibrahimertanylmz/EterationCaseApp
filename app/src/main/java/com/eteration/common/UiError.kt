package com.eteration.common

import android.content.Context
import androidx.annotation.StringRes
import com.eteration.app.R

sealed class UiError {
    data class DynamicString(val value: String) : UiError()
    data class StringResource(@StringRes val id: Int) : UiError()

    companion object {
        fun unknownError(): UiError {
            return UiError.StringResource(R.string.error_message)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is UiError.DynamicString -> this.value
            is UiError.StringResource -> context.getString(this.id)
        }
    }
}