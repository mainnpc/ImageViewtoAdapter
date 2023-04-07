package com.base.view
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel: ViewModel(),CoroutineScope {

    open fun detachView(){
        job.cancel()
    }

    private var job= Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO +job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}