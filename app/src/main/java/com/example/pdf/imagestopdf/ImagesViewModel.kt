package com.example.pdf.imagestopdf

import android.app.Application
import android.content.ContentUris
import android.database.Cursor
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pdf.dataclasses.ImagesData

class ImagesViewModel(application: Application): AndroidViewModel(application) {

    private val _imagesList = MutableLiveData<List<ImagesData>>()
    val imagesList: LiveData<List<ImagesData>> get() = _imagesList

    private val _selectedImages = MutableLiveData<MutableList<ImagesData>>()
    val selectedImages: LiveData<MutableList<ImagesData>> get() = _selectedImages

    init {
        loadImages()
        _selectedImages.value = mutableListOf()
    }

    private fun loadImages() {
        val imagesList = mutableListOf<ImagesData>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query: Cursor? =   getApplication<Application>().contentResolver?.query(
            collection,
            projection,
            null,
            null,
            sortOrder
        )

        query?.use {
            val columnId = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (it.moveToNext()) {
                val id = it.getLong(columnId)
                val uri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                imagesList.add(ImagesData(uri))
            }
        }

        _imagesList.postValue(imagesList)
    }

    fun selectImage(image: ImagesData) {
        _selectedImages.value?.add(image)
        _selectedImages.postValue(_selectedImages.value)
    }

    fun deselectImage(image: ImagesData) {
        _selectedImages.value?.remove(image)
        _selectedImages.postValue(_selectedImages.value)
    }

    fun removeImage(image: ImagesData) {
        _selectedImages.value?.remove(image)
        _selectedImages.postValue(_selectedImages.value)
    }
}