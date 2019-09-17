package com.simple.module.internet

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@Deprecated("使用协程代替")
class ConcurrentRequest {

    class Builder {
        private val request = LinkedList<Runnable>()
        private var completeCount = 0
        private var callback: ((hasError:Boolean) -> Unit)? = null
        private var error=false
        fun addRequest(vararg runnable: Runnable): Builder {
            request.addAll(runnable)
            return this
        }
        fun addRequest(runnable: List<Runnable>): Builder {
            request.addAll(runnable)
            return this
        }

        fun complete(callback: (Boolean) -> Unit): Builder {
            this.callback = callback
            return this
        }

        private fun callCallback() {
            if (completeCount == request.size) {
                GlobalScope.launch(Dispatchers.Main) {
                    callback?.invoke(error)
                }
            }
        }

        fun request() {
            for (req in request) {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        req.run()
                    } catch (e: Exception) {
                        error=true
                        e.printStackTrace()
                    } finally {
                        completeCount++
                        callCallback()
                    }
                }
            }
        }
    }

}