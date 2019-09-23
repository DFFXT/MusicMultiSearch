package com.simple.module.player

import com.simple.module.player.playerInterface.LinkedListI
import com.simple.tools.IOUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Serializable

/**
 * 增强型LinkedList 具有其他方法，同时不破坏本身方法
 * 使用list 做代理
 */
class LinkedListImp<T>(private val list: MutableList<T>) : LinkedListI<T>, List<T> by list,
    Serializable {
    private var index = -1
    override fun next(): T {
        index += 1
        if (index >= size) index = 0
        save()
        return get(index)
    }

    override fun pre(): T {
        index -= 1
        if (index < 0) index = 0
        save()
        return get(index)
    }

    override fun getCurrent(): T {
        if (index == -1) index = 0
        return get(index)
    }

    override fun random(): T {
        save()
        return get(0)
    }

    fun add(element: T, index: Int = size) {
        list.add(index, element)
        save()
    }

    fun getLast(moveIndex: Boolean = false): T {
        if (moveIndex) index = size - 1
        return get(size - 1)
    }

    override fun getIndex() = index

    override fun reset() {
        index = -1
        save()
    }

    fun setIndex(index: Int) {
        this.index = index
        save()
    }

    override fun addAll(elements: Iterable<T>) {
        list.addAll(elements)
        save()
    }

    override fun clear() {
        list.clear()
        reset()
    }

    override fun remove(index: Int) {
        if (index < 0 || index >= size) return
        if (index <= this.index) {
            this.index -= 1
        }
        list.removeAt(index)
        save()
    }

    override fun remove(item: T): Boolean {
        val p = list.indexOf(item)
        if (p == -1) return false
        remove(p)
        return true
    }

    fun save() = save(this)

    companion object {
        const val name = "tmpList"

        @JvmStatic
        fun <T> load(callback: ((LinkedListI<T>?) -> Unit)) {
            GlobalScope.launch(Dispatchers.IO) {
                val obj = IOUtil.readObject<LinkedListI<T>>(name)
                callback(obj)
            }
        }

        @JvmStatic
        fun <T> load(): LinkedListImp<T>? {
            return IOUtil.readObject<LinkedListImp<T>>(name)
        }

        @JvmStatic
        private fun save(obj: Any) {
            GlobalScope.launch(Dispatchers.IO) {
                IOUtil.saveObject(obj, name)
            }
        }
    }
}

