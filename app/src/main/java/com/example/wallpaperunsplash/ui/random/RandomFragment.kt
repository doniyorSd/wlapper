package com.example.wallpaperunsplash.ui.random

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
import com.example.wallpaperunsplash.databinding.FragmentRandomBinding
import com.example.wallpaperunsplash.models.api.UnsplashImage
import com.example.wallpaperunsplash.retrofit.Common
import com.example.wallpaperunsplash.retrofit.RetrofitService
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import kotlin.random.Random

class RandomFragment : Fragment() {

    lateinit var binding: FragmentRandomBinding
    lateinit var list: ArrayList<UnsplashImage>
    lateinit var rvAdapter: RvAdapter
    lateinit var retrofitService: RetrofitService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRandomBinding.inflate(layoutInflater)
        val homeIndicator = requireActivity().findViewById<ImageView>(R.id.home_indicator)
        val popularIndicator = requireActivity().findViewById<ImageView>(R.id.popular_indicator)
        val randomIndicator = requireActivity().findViewById<ImageView>(R.id.random_indicator)
        val likedIndicator = requireActivity().findViewById<ImageView>(R.id.like_indicator)

        homeIndicator.visibility = View.INVISIBLE
        popularIndicator.visibility = View.INVISIBLE
        randomIndicator.visibility = View.VISIBLE
        likedIndicator.visibility = View.INVISIBLE

        val home = requireActivity().findViewById<LinearLayout>(R.id.home_button)
        home.setOnClickListener {
            findNavController().navigate(R.id.nav_popular)
        }
        val popular = requireActivity().findViewById<LinearLayout>(R.id.popular_button)
        popular.setOnClickListener {
            findNavController().navigate(R.id.nav_popular)
        }
        val like = requireActivity().findViewById<LinearLayout>(R.id.like_button)
        like.setOnClickListener {
            findNavController().navigate(R.id.nav_liked)
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
        retrofitService.getRandom(
            "random",
            Common.ACCESS_KEY,
            800,
            600,
            80,
            20,
            Random.nextInt(1, 100)
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