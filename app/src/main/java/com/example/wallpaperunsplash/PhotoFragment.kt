package com.example.wallpaperunsplash

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wallpaperunsplash.databinding.BottomSheetBinding
import com.example.wallpaperunsplash.databinding.FragmentPhotoBinding
import com.example.wallpaperunsplash.db.AppDatabase
import com.example.wallpaperunsplash.entity.MyImage
import com.example.wallpaperunsplash.models.api.UnsplashImage
import com.example.wallpaperunsplash.retrofit.RetrofitService
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import io.alterac.blurkit.BlurLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class PhotoFragment : Fragment() {

    lateinit var binding: FragmentPhotoBinding
    lateinit var appDatabase: AppDatabase
    lateinit var retrofitService: RetrofitService
    private val TAG = "PhotoFragment"
    lateinit var mHandler: Handler

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoBinding.inflate(layoutInflater)

        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.GONE
        val bottomNavigation = requireActivity().findViewById<BlurLayout>(R.id.blur_layout)
        bottomNavigation.visibility = View.GONE

        val unsplashImage = arguments?.getParcelable<UnsplashImage>("photo")
        Picasso.get().load(unsplashImage?.urls?.regular).into(binding.image)

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.info.setOnClickListener {
            val inflate = BottomSheetBinding.inflate(layoutInflater)
            val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)
            inflate.author.text = "Author: " + unsplashImage?.user?.name
            bottomSheetDialog.setContentView(inflate.root)
            bottomSheetDialog.show()
        }


        binding.download.setOnClickListener {
            askPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                val url = URL(unsplashImage?.urls?.regular)
                val thread: Thread = object : Thread() {
                    override fun run() {
                        try {
                            while (true) {
                                val bmp = BitmapFactory.decodeStream(
                                    url.openConnection().getInputStream()
                                )
                                saveImageToExternal(unsplashImage?.id!!, bmp)
                            }
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }
                }
                thread.start()
            }.onDeclined { e ->
                println(3)
                if (e.hasDenied()) {
                    AlertDialog.Builder(requireContext())
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }

                if (e.hasForeverDenied()) {
                    e.goToSettings();
                }
            }
        }

        appDatabase = AppDatabase.getInstance(requireContext())
        appDatabase.imageDao().getImageByUnsplashId(unsplashImage?.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                if (t != null) {
                    binding.likes.setImageResource(R.drawable.fill_like)
                }
            }, {

            }, {
                Log.d(TAG, "onCreateView: 12")
            })

        binding.like.setOnClickListener {
            appDatabase.imageDao().getImageByUnsplashId(unsplashImage.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    Log.d(TAG, "accept: 1")
                    binding.likes.setImageResource(R.drawable.like)
                    appDatabase.imageDao().deleteImage(t!!)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                }, {

                }, {
                    Log.d(TAG, "accept: 2")
                    binding.likes.setImageResource(R.drawable.fill_like)
                    val myImage = MyImage()
                    myImage.unsplashId = unsplashImage.id
                    myImage.urlSmall = unsplashImage.urls.small
                    myImage.urlRegular = unsplashImage.urls.regular
                    myImage.downloadUrl = unsplashImage.links.download
                    myImage.author = unsplashImage.user.name
                    appDatabase.imageDao().addImage(myImage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                })
        }
        return binding.root
    }

    @Throws(IOException::class)
    fun saveImageToExternal(imgName: String, bm: Bitmap) {
        val path: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Wallpaper Unsplash") //Creates app specific folder
        path.mkdirs()
        val imageFile = File(path, "$imgName.png") // Imagename.png
        val out: FileOutputStream = FileOutputStream(imageFile)
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, out) // Compress Image
            out.flush()
            out.close()
            MediaScannerConnection.scanFile(
                context, arrayOf(imageFile.getAbsolutePath()), null
            ) { path, uri ->
            }
        } catch (e: Exception) {
            throw IOException()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.visibility = View.VISIBLE
        val bottomNavigation =
            requireActivity().findViewById<BlurLayout>(R.id.blur_layout)
        bottomNavigation.visibility = View.VISIBLE
    }
}