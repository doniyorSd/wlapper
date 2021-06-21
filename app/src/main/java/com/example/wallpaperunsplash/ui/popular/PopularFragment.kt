package com.example.wallpaperunsplash.ui.popular

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
import com.example.wallpaperunsplash.databinding.FragmentPopularBinding
import com.example.wallpaperunsplash.models.api.UnsplashImage
import com.example.wallpaperunsplash.retrofit.Common
import com.example.wallpaperunsplash.retrofit.RetrofitService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class PopularFragment : Fragment() {

    lateinit var binding: FragmentPopularBinding
    lateinit var rvAdapter: RvAdapter
    lateinit var retrofitService: RetrofitService
    lateinit var list: ArrayList<UnsplashImage>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPopularBinding.inflate(layoutInflater)

        val homeIndicator = requireActivity().findViewById<ImageView>(R.id.home_indicator)
        val popularIndicator = requireActivity().findViewById<ImageView>(R.id.popular_indicator)
        val randomIndicator = requireActivity().findViewById<ImageView>(R.id.random_indicator)
        val likedIndicator = requireActivity().findViewById<ImageView>(R.id.like_indicator)

        homeIndicator.visibility = View.INVISIBLE
        popularIndicator.visibility = View.VISIBLE
        randomIndicator.visibility = View.INVISIBLE
        likedIndicator.visibility = View.INVISIBLE

        val home = requireActivity().findViewById<LinearLayout>(R.id.home_button)
        home.setOnClickListener {
            findNavController().navigate(R.id.nav_home)
        }
        val like = requireActivity().findViewById<LinearLayout>(R.id.like_button)
        like.setOnClickListener {
            findNavController().navigate(R.id.nav_liked)
        }
        val random = requireActivity().findViewById<LinearLayout>(R.id.random_button)
        random.setOnClickListener {
            findNavController().navigate(R.id.nav_random)
        }

        setImages()

        rvAdapter = RvAdapter(list, object : RvAdapter.OnMyItemClickListener {
            override fun onMyItemClick(unsplashImage: UnsplashImage) {
                val bundle = Bundle()
                bundle.putParcelable("photo", unsplashImage)
                findNavController().navigate(R.id.photoFragment, bundle)
            }
        })
        binding.rv.adapter = rvAdapter

        return binding.root
    }


    @SuppressLint("CheckResult")
    fun setImages() {
        list = ArrayList()
        list.clear()
        retrofitService = Common.retrofitService(requireContext())
        retrofitService.getPopular(
            Common.ACCESS_KEY,
            Random.nextInt(1, 100),
            20,
            800,
            600,
            80,
            "popular"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                object : Observer<Response<List<UnsplashImage>>> {
                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: Response<List<UnsplashImage>>) {
                        if (t != null && t.isSuccessful) {
                            list.addAll(t.body()!!)
                            rvAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onComplete() {
                        binding.progressBar.visibility = View.GONE
                    }
                })
    }
}