package com.example.mviapplication.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mviapplication.R
import com.example.mviapplication.core.common.ImageLoader
import com.example.mviapplication.core.models.Photo
import kotlinx.android.synthetic.main.list_item_image.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author s.buvaka
 */
class ImageAdapter(private val imageLoader: ImageLoader) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val items: MutableList<Photo> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], imageLoader)
    }

    fun update(items: Collection<Photo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal fun bind(photo: Photo, imageLoader: ImageLoader) {
            with(itemView) {
                imageLoader.loadImage(photo.src.medium, imageContainer)
            }
        }
    }
}
