package com.example.pdf.scandocuments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pdf.dataclasses.ImagesBitmap
import com.example.pdf.databinding.SelectedImagesListBinding

class ScanImagesAdapter(private var images : List<ImagesBitmap>, private val onImageRemoved : (ImagesBitmap) -> Unit) : RecyclerView.Adapter<ScanImagesAdapter.ImagesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val binding = SelectedImagesListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image, onImageRemoved)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class ImagesViewHolder(val binding: SelectedImagesListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imagesBitmap: ImagesBitmap, onImageRemoved: (ImagesBitmap) -> Unit) {
            Glide.with(itemView.context)
                .load(imagesBitmap.bitmap)
                .into(binding.selectedImage)

            binding.remove.setOnClickListener {
                onImageRemoved(imagesBitmap)
            }

        }

    }
}