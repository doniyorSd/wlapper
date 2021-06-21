package com.example.wallpaperunsplash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.wallpaperunsplash.adapters.RvAdapter
import com.example.wallpaperunsplash.databinding.FragmentPagerBinding
import com.example.wallpaperunsplash.models.api.UnsplashImage
import com.example.wallpaperunsplash.models.search_api.SearchResponse
import com.example.wallpaperunsplash.retrofit.Common
import com.example.wallpaperunsplash.retrofit.RetrofitService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

private const val ARG_PARAM1 = "param1"

class PagerFragment : Fragment() {
    private var param1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    lateinit var binding: FragmentPagerBinding
    lateinit var retrofitService: RetrofitService
    lateinit var list: ArrayList<UnsplashImage>
    lateinit var rvAdapter: RvAdapter

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPagerBinding.inflate(layoutInflater)

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
        if (param1 == "ALL") {
            retrofitService.getImage(Common.ACCESS_KEY, Random.nextInt(1,100),20,800, 600,80)
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
        } else {
            retrofitService.getImageByCategory(
                param1!!.toLowerCase(Locale.ROOT),
                Common.ACCESS_KEY,Random.nextInt(1,100), 20,800, 600,80
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<Response<SearchResponse>>{
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Response<SearchResponse>) {
                        if (t.isSuccessful && t.body() != null) {
                            list.addAll(t.body()!!.results)
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            PagerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}