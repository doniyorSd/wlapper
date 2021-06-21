package com.example.wallpaperunsplash.models.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@kotlinx.parcelize.Parcelize
data class User(
    val name: String,
): Parcelable