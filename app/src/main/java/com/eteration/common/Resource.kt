package com.eteration.common

sealed class Resource<T>(val data: T? = null, val uiError: UiError? = null) {

    class Success<T>(data: T) : Resource<T>(data)

    class Error<T>(uiError: UiError, data: T? = null) : Resource<T>(data, uiError)

    class Loading<T>() : Resource<T>()
}