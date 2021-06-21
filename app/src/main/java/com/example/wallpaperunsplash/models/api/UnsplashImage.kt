package com.example.wallpaperunsplash.models.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashImage(
    val id: String?,
    val width: Int?,
    val height: Int?,
    val likes: Int?,
    val description: String?,
    val alt_description: String?,
    val urls: Urls,
    val links: Links,
    val user: User
) : Parcelable