package com.example.wallpaperunsplash.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wallpaperunsplash.R
import com.example.wallpaperunsplash.adapters.PagerAdapter
import com.example.wallpaperunsplash.databinding.FragmentHomeBinding
import com.example.wallpaperunsplash.databinding.TabItemBinding
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var tabView: TabItemBinding
    lateinit var pagerAdapter: PagerAdapter
    lateinit var categoryList: ArrayList<String>
    lateinit var sharedPreferences: SharedPreferences
    var tabPos = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val homeIndicator = requireActivity().findViewById<ImageView>(R.id.home_indicator)
        val popularIndicator = requireActivity().findViewById<ImageView>(R.id.popular_indicator)
        val randomIndicator = requireActivity().findViewById<ImageView>(R.id.random_indicator)
        val likedIndicator = requireActivity().findViewById<ImageView>(R.id.like_indicator)

        homeIndicator.visibility = View.VISIBLE
        popularIndicator.visibility = View.INVISIBLE
        randomIndicator.visibility = View.INVISIBLE
        likedIndicator.visibility = View.INVISIBLE

        val popular = requireActivity().findViewById<LinearLayout>(R.id.popular_button)
        popular.setOnClickListener {
            findNavController().navigate(R.id.nav_popular)
        }
        val random = requireActivity().findViewById<LinearLayout>(R.id.random_button)
        random.setOnClickListener {
            findNavController().navigate(R.id.nav_random)
        }
        val like = requireActivity().findViewById<LinearLayout>(R.id.like_button)
        like.setOnClickListener {
            findNavController().navigate(R.id.nav_liked)
        }

        loadCategoris()

        pagerAdapter = PagerAdapter(categoryList, requireActivity().supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        setTabs(0)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPos = tab!!.position
                val title = tab.customView?.findViewById<TextView>(R.id.title)
                val circle = tab.customView?.findViewById<ImageView>(R.id.circle)
                circle?.visibility = View.VISIBLE
                title?.setTextColor(Color.parseColor("#FFFFFF"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val title = tab?.customView?.findViewById<TextView>(R.id.title)
                val circle = tab?.customView?.findViewById<ImageView>(R.id.circle)
                circle?.visibility = View.INVISIBLE
                title?.setTextColor(Color.parseColor("#7E8993"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        sharedPreferences = requireActivity().getSharedPreferences("TAB", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("tabPos", tabPos).apply()
    }

    override fun onStart() {
        super.onStart()

        sharedPreferences = requireActivity().getSharedPreferences("TAB", Context.MODE_PRIVATE)
        val pos = sharedPreferences.getInt("tabPos", 0)

        loadCategoris()

        pagerAdapter = PagerAdapter(categoryList, requireActivity().supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.setScrollPosition(pos, 0f, true)
        binding.viewPager.currentItem = pos
        setTabs(pos)

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tabPos = tab!!.position
                val title = tab.customView?.findViewById<TextView>(R.id.title)
                val circle = tab.customView?.findViewById<ImageView>(R.id.circle)
                circle?.visibility = View.VISIBLE
                title?.setTextColor(Color.parseColor("#FFFFFF"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val title = tab?.customView?.findViewById<TextView>(R.id.title)
                val circle = tab?.customView?.findViewById<ImageView>(R.id.circle)
                circle?.visibility = View.INVISIBLE
                title?.setTextColor(Color.parseColor("#7E8993"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

    }

    private fun loadCategoris() {
        categoryList = ArrayList()
        categoryList.clear()
        categoryList.add("ALL")
        categoryList.add("ANIMALS")
        categoryList.add("TECHNOLOGY")
        categoryList.add("NATURE")
    }

    private fun setTabs(pos: Int) {
        val tabCount = binding.tabLayout.tabCount
        for (i in 0 until tabCount) {
            tabView = TabItemBinding.inflate(LayoutInflater.from(requireContext()), null, false)
            val tabAt = binding.tabLayout.getTabAt(i)
            tabAt?.customView = tabView.root
            tabView.title.text = binding.tabLayout.getTabAt(i)!!.text

            if (i == pos) {
                tabView.circle.visibility = View.VISIBLE
                tabView.title.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                tabView.circle.visibility = View.INVISIBLE
                tabView.title.setTextColor(Color.parseColor("#7E8993"))
            }
        }
    }
}