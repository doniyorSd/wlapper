package com.example.wallpaperunsplash.ui.liked

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.example.wallpaperunsplash.R
import com.example.wallpaperunsplash.adapters.RvAdapter
import com.example.wallpaperunsplash.databinding.FragmentLikedBinding
import com.example.wallpaperunsplash.db.AppDatabase
import com.example.wallpaperunsplash.entity.MyImage
import com.example.wallpaperunsplash.models.api.Links
import com.example.wallpaperunsplash.models.api.UnsplashImage
import com.example.wallpaperunsplash.models.api.Urls
import com.example.wallpaperunsplash.models.api.User
import com.example.wallpaperunsplash.retrofit.Common
import com.example.wallpaperunsplash.retrofit.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LikedFragment : Fragment() {

    lateinit var binding: FragmentLikedBinding
    lateinit var retrofitService: RetrofitService
    lateinit var appDatabase: AppDatabase
    lateinit var rvAdapter: RvAdapter
    lateinit var list: ArrayList<MyImage>
    lateinit var unsplashList: ArrayList<UnsplashImage>

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLikedBinding.inflate(layoutInflater)
        val homeIndicator = requireActivity().findViewById<ImageView>(R.id.home_indicator)
        val popularIndicator = requireActivity().findViewById<ImageView>(R.id.popular_indicator)
        val randomIndicator = requireActivity().findViewById<ImageView>(R.id.random_indicator)
        val likedIndicator = requireActivity().findViewById<ImageView>(R.id.like_indicator)

        homeIndicator.visibility = View.INVISIBLE
        popularIndicator.visibility = View.INVISIBLE
        randomIndicator.visibility = View.INVISIBLE
        likedIndicator.visibility = View.VISIBLE

        val home = requireActivity().findViewById<LinearLayout>(R.id.home_button)
        home.setOnClickListener {
            findNavController().navigate(R.id.nav_home)
        }
        val popular = requireActivity().findViewById<LinearLayout>(R.id.popular_button)
        popular.setOnClickListener {
            findNavController().navigate(R.id.nav_popular)
        }
        val random = requireActivity().findViewById<LinearLayout>(R.id.random_button)
        random.setOnClickListener {
            findNavController().navigate(R.id.nav_random)
        }

        list = ArrayList()
        unsplashList = ArrayList()
        appDatabase = AppDatabase.getInstance(requireContext())
        appDatabase.imageDao().getAllImages()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                list.addAll(t)
                for (myImage in list) {
                    val urls = Urls(null, myImage.urlSmall!!, null, myImage.urlRegular, null, null, null)
                    val links = Links(myImage.downloadUrl!!, "", "", "")
                    val user = User(myImage.author!!)
                    var unsplashImage = UnsplashImage(
                        myImage.unsplashId!!,
                        null,
                        null,
                        null,
                        null,
                        null,
                        urls,
                        links,
                        user
                    )
                    unsplashList.add(unsplashImage)
                }
                rvAdapter.notifyDataSetChanged()
            }, {

            })

        rvAdapter = RvAdapter(unsplashList, object : RvAdapter.OnMyItemClickListener {
            override fun onMyItemClick(unsplashImage: UnsplashImage) {
                val bundle = Bundle()
                bundle.putParcelable("photo", unsplashImage)
                findNavController().navigate(R.id.photoFragment, bundle)
            }
        })
        binding.rv.adapter = rvAdapter

        return binding.root
    }
}