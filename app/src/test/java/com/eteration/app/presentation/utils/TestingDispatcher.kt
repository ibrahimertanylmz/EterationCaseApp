package com.eteration.app.presentation.utils

import com.eteration.core.dispatchers.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.Unconfined

class TestingDispatcher : Dispatcher {

    override val main: CoroutineDispatcher = Unconfined

    override val io: CoroutineDispatcher = Unconfined

    override val db: CoroutineDispatcher = Unconfined

    override val computation: CoroutineDispatcher = Unconfined

}