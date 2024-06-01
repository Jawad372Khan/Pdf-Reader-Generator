package com.example.pdf.imagestopdf

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf.ImagesData
import com.example.pdf.databinding.ImagesItemBinding

class ImagesListAdapter(
    private var images: List<ImagesData>, private val onImageSelected: (ImagesData)-> Unit) :
    RecyclerView.Adapter<ImagesListAdapter.ImageViewHolder>() {


    private val selectedImages = mutableSetOf<ImagesData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImagesItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        holder.bind(images[position], selectedImages, onImageSelected)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun updateImages(newImages: List<ImagesData>) {
        images = newImages
        notifyDataSetChanged()
    }

    class ImageViewHolder(val binding: ImagesItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            imagesData: ImagesData,
            selectedImages: MutableSet<ImagesData>,
            onImageSelected: (ImagesData) -> Unit,

        ) {
            val image = imagesData
            binding.imageView.setImageURI(image.uri)
            binding.radioBtn.apply {
                isChecked = selectedImages.contains(image)
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedImages.add(image)
                        onImageSelected(image)
                    }
                }
            }

        }
    }
}





