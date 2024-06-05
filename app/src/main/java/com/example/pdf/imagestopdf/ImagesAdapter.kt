package com.example.pdf.imagestopdf

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf.ImagesData
import com.example.pdf.databinding.SelectedImagesListBinding

class ImagesAdapter(private var images : List<ImagesData>,private val onImageRemoved : (ImagesData) -> Unit) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {


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

        fun bind(imagesData: ImagesData, onImageRemoved: (ImagesData) -> Unit) {
            binding.selectedImage.setImageURI(imagesData.uri)
            binding.remove.setOnClickListener {
                onImageRemoved(imagesData)
            }

        }

    }
}