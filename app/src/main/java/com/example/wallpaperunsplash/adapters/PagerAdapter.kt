package com.example.wallpaperunsplash.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.wallpaperunsplash.PagerFragment

class PagerAdapter(var list: List<String>, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return PagerFragment.newInstance(list[position])
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position]
    }
}