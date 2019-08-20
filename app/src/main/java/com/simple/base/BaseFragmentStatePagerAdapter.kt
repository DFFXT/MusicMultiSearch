package com.simple.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseFragmentStatePagerAdapter(private val fragments: List<Fragment>, fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}