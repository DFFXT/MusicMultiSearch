package com.simple.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

open class BaseViewModle:ViewModel(),CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main+job
    private val job= SupervisorJob()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}