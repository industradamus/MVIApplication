package com.example.mviapplication.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mviapplication.R
import com.example.mviapplication.core.common.ImageLoader
import com.example.mviapplication.core.models.Photo
import kotlinx.android.synthetic.main.list_item_image.view.*

/**
 * @author s.buvaka
 */
class ImageAdapter(private val imageLoader: ImageLoader) : PagedListAdapter<Photo, ImageAdapter.ViewHolder>(DiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, imageLoader) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal fun bind(photo: Photo, imageLoader: ImageLoader) {
            with(itemView) {
                imageLoader.loadImage(photo.src.medium, imageContainer)
            }
        }
    }

    class DiffUtils : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
    }
}
