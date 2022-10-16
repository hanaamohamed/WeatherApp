package com.volvo.image_loader

import android.widget.ImageView
import com.bumptech.glide.Glide
import javax.inject.Inject

interface ImageLoader {
    fun loadImage(url: String, imageView: ImageView)
}

internal class ImageLoaderImpl @Inject constructor() : ImageLoader {
    override fun loadImage(url: String, imageView: ImageView) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }

}