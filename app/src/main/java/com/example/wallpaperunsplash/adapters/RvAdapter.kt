package com.example.wallpaperunsplash.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wallpaperunsplash.databinding.ItemImageBinding
import com.example.wallpaperunsplash.models.api.UnsplashImage
import com.squareup.picasso.Picasso

class RvAdapter(var list: List<UnsplashImage>, var onMyItemClickListener: OnMyItemClickListener) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(var itemImageBinding: ItemImageBinding) :
        RecyclerView.ViewHolder(itemImageBinding.root) {
        fun onBind(unsplashImage: UnsplashImage) {
            Picasso.get().load(unsplashImage.urls.small).into(itemImageBinding.image)
            itemImageBinding.root.setOnClickListener {
                onMyItemClickListener.onMyItemClick(unsplashImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnMyItemClickListener {
        fun onMyItemClick(unsplashImage: UnsplashImage)
    }
}