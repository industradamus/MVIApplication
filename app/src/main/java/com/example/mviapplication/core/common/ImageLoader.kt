package com.example.mviapplication.core.common

import android.widget.ImageView
import com.bumptech.glide.Glide

/**
 * @author s.buvaka
 */
interface ImageLoader {

    fun loadImage(url: String, into: ImageView)
}

class ImageLoaderImpl : ImageLoader {

    override fun loadImage(url: String, into: ImageView) {
        Glide.with(into)
            .load(url)
            .into(into)
    }
}
