package com.eteration.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatcher {

    val main: CoroutineDispatcher

    val io: CoroutineDispatcher

    val db: CoroutineDispatcher

    val computation: CoroutineDispatcher

}