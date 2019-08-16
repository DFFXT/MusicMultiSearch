package com.simple.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import java.util.*

/**
 * 一个具有生命周期的对象（类似于activity）
 */
abstract class BaseLifeCycle : Lifecycle(), LifecycleOwner {
    protected var state: State = State.INITIALIZED
        set(value) {
            field = value
            for (observer in observers) {
                if (observer is DefaultLifecycleObserver) {
                    if (value == State.DESTROYED) {
                        observer.onDestroy(this)
                    } else if (value == State.CREATED) {
                        observer.onCreate(this)
                    }
                }
            }
        }
    private val observers = LinkedList<LifecycleObserver>()
    override fun addObserver(observer: LifecycleObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: LifecycleObserver) {
        observers.remove(observer)
    }

    override fun getCurrentState() = state

    override fun getLifecycle(): Lifecycle {
        return this
    }
}