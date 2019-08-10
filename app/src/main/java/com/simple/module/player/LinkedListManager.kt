package com.simple.module.player

import com.simple.module.player.playerInterface.LinkedListI
import java.util.*

/**
 * 增强型LinkedList 具有其他方法，同时不破坏本身方法
 * 使用list 做代理
 */
class LinkedListImp<T>(private val list: LinkedList<T>) : LinkedListI<T>, List<T> by list {
    private var index = -1
    override fun next() :T{
        index+=1
        if(index>=size) index=0
        return get(index)
    }

    override fun pre() :T{
        index -=1
        if(index<0) index=0
        return get(index)
    }

    override fun random(): T {
        return get(0)
    }
}

