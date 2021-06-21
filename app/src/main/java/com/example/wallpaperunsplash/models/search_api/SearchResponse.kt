package com.example.wallpaperunsplash.models.search_api

import com.example.wallpaperunsplash.models.api.UnsplashImage

data class SearchResponse(
    val total: Int,
    val total_pages: Int,
    val results: List<UnsplashImage>)