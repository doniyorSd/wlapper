package com.example.wallpaperunsplash.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.wallpaperunsplash.models.api.UnsplashImage

@Entity(tableName = "my_image")
class MyImage {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "unsplash_id")
    var unsplashId: String? = null

    @ColumnInfo(name = "url_small")
    var urlSmall: String? = null

    @ColumnInfo(name = "url_regular")
    var urlRegular: String? = null

    @ColumnInfo(name = "download_url")
    var downloadUrl: String? = null

    @ColumnInfo(name = "author")
    var author: String? = null
}