package com.example.wallpaperunsplash.retrofit

import com.example.wallpaperunsplash.models.api.UnsplashImage
import com.example.wallpaperunsplash.models.search_api.SearchResponse
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("photos")
    fun getImage(
        @Query("client_id") client_id: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("w") width: Int,
        @Query("h") height: Int,
        @Query("q") quality: Int
    ): Observable<Response<List<UnsplashImage>>>

    @GET("search/photos")
    fun getImageByCategory(
        @Query("query") query: String,
        @Query("client_id") client_id: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("w") width: Int,
        @Query("h") height: Int,
        @Query("q") quality: Int
    ): Observable<Response<SearchResponse>>

    @GET("photos")
    fun getImageById(
        @Query("id") id: String,
        @Query("client_id") client_id: String,
        @Query("w") width: Int,
        @Query("h") height: Int,
        @Query("q") quality: Int
    ): Observable<Response<UnsplashImage>>

    @GET("photos")
    fun getPopular(
        @Query("client_id") client_id: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("w") width: Int,
        @Query("h") height: Int,
        @Query("q") quality: Int,
        @Query("order_by") order_by: String
    ): Observable<Response<List<UnsplashImage>>>

    @GET("photos")
    fun getRandom(
        @Query("query") query: String,
        @Query("client_id") client_id: String,
        @Query("w") width: Int,
        @Query("h") height: Int,
        @Query("q") quality: Int,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): Observable<Response<List<UnsplashImage>>>

    @GET("photos")
    fun download(
        @Query(":id") id: String,
        @Query("query") query: String,
        @Query("client_id") client_id: String,
    ): Observable<Response<UnsplashImage>>

}