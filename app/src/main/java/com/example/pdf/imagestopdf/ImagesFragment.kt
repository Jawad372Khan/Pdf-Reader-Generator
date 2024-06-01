package com.example.pdf.imagestopdf

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.pdf.ImagesData
import com.example.pdf.PdfUtils
import com.example.pdf.R
import com.example.pdf.databinding.FragmentImagesBinding
import com.example.pdf.pdfiles.PdfViewerFragment

class ImagesFragment : Fragment() {


    private lateinit var binding: FragmentImagesBinding
    private lateinit var imagesViewModel: ImagesViewModel
    private val selectedImages = mutableListOf<ImagesData>()
    private lateinit var imageAdapter : ImagesListAdapter
    private lateinit var selectedImageAdapter : ImagesAdapter




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        imagesViewModel = ViewModelProvider(this).get(ImagesViewModel::class.java)


        binding.selectImages.setOnClickListener {
            openGallery()
        }


        var imagesList = mutableListOf<ImagesData>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val query: Cursor? = context?.contentResolver?.query(
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
        imageAdapter = ImagesListAdapter(imagesList,this::onImageSelected)
        binding.imagesRecyclerView.adapter = imageAdapter

        selectedImageAdapter = ImagesAdapter(selectedImages,this::onImageRemoved)
        binding.selectedRecyclerView.adapter = selectedImageAdapter

        binding.pdfBtn.setOnClickListener {
           convertToPdf()

        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        startActivity(intent)

    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun convertToPdf() {
        if(selectedImages.isEmpty())
        {
            Toast.makeText(context,"No Images Selected",Toast.LENGTH_LONG).show()
            return
        }


        val pdfUri = createPdfFile(requireContext())
        if(pdfUri != null)
        {
            Toast.makeText(requireContext(),"Pdf Created at : $pdfUri",Toast.LENGTH_LONG).show()
            val fragment = PdfViewerFragment.newInstance(pdfUri)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer,fragment)
                .commit()
        }



    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun createPdfFile(context : Context): Uri? {

     val contentResolver = context.contentResolver
        val fileName = "selected_images ${System.currentTimeMillis()}.pdf"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME,fileName)
            put(MediaStore.MediaColumns.MIME_TYPE,"application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH,"Documents/")
        }
        val pdfUri : Uri? = contentResolver.insert(MediaStore.Files.getContentUri("external"),
            contentValues)
        pdfUri?.let { uri->
            try {
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (outputStream != null) {
                        PdfUtils.createPdfFromImages(context,selectedImages.map {
                            it.uri
                        },outputStream)
                    }
                }
            }
            catch (e : Exception)
            {
                e.printStackTrace()
                return null
            }
        }
        return pdfUri
    }


    private fun onImageSelected(image : ImagesData)
    {

        selectedImages.add(image)
        binding.selectedRecyclerView.adapter = selectedImageAdapter

    }

    private fun onImageRemoved(image: ImagesData)
    {

        selectedImages.remove(image)
        binding.selectedRecyclerView.adapter = selectedImageAdapter
        binding.imagesRecyclerView.adapter = imageAdapter

    }

}
