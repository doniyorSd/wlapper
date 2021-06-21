package com.example.wallpaperunsplash.retrofit

import android.content.Context

object Common {

    const val BASE_URL = "https://api.unsplash.com/"
    const val ACCESS_KEY = "GvkXC5a27OTubqeucaBgaKFbooecpbC9CGgDl9MWsKY"
    const val SECRET_KEY = "qIt6i86zxoAVtK_Z6dp67tWt2FzpSpymmNW2EAx0gvY"

    fun retrofitService(context: Context): RetrofitService =
        RetrofitClient.getRetrofit(BASE_URL).create(RetrofitService::class.java)

}