package com.example.wallpaperunsplash.dao

import androidx.room.*
import com.example.wallpaperunsplash.entity.MyImage
import io.reactivex.*

@Dao
interface ImageDao {
    @Insert
    fun addImage(myImage: MyImage): Completable

    @Update
    fun updateImage(myImage: MyImage): Single<Int>

    @Delete
    fun deleteImage(myImage: MyImage): Single<Int>

    @Query("select * from my_image where id=:id")
    fun getImageById(id: Int): Maybe<MyImage>

    @Query("select * from my_image where unsplash_id=:id")
    fun getImageByUnsplashId(id: String): Maybe<MyImage>

    @Query("select * from my_image")
    fun getAllImages(): Flowable<List<MyImage>>

}