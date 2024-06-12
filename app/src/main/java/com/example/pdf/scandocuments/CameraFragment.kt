package com.example.pdf.scandocuments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.pdf.ImagesBitmap
import com.example.pdf.PdfUtils
import com.example.pdf.R
import com.example.pdf.databinding.FragmentCameraBinding
import com.example.pdf.pdfiles.PdfViewerFragment
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class CameraFragment : Fragment() {

    private lateinit var binding : FragmentCameraBinding
    private var selectedImage = mutableListOf<ImagesBitmap>()
    private lateinit var adapter : ScanImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        openDocumentScanner()

        adapter = ScanImagesAdapter(selectedImage,this::onImageRemoved)
        binding.cameraPics.adapter = adapter

        binding.pdfConvert.setOnClickListener {
            convertToPdf()
        }
    }

    private fun openDocumentScanner() {

        val options = GmsDocumentScannerOptions.Builder().apply {
            setGalleryImportAllowed(true)
            setPageLimit(10)
            setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG)
            setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)

        }
            .build()

        val scanner = GmsDocumentScanning.getClient(options)
        scanner.getStartScanIntent(requireActivity())
            .addOnSuccessListener {
                scannerLauncher.launch(IntentSenderRequest.Builder(it).build())
            }
            .addOnFailureListener{
                it.printStackTrace()
                Toast.makeText(requireContext(), "Failed to start document scanner: ${it.message}", Toast.LENGTH_LONG).show()
            }

    }
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("NotifyDataSetChanged")
    private val scannerLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){
        if(it.resultCode == Activity.RESULT_OK)
        {
            it.data.let {
                val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(it)
                scanResult?.getPages()?.let { pages ->
                    GlobalScope.launch(Dispatchers.IO){
                        for (page in pages) {
                            val imageUri = page.imageUri
                            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
                            val enhanceBitmap = enhanceImage(bitmap)

                            selectedImage.add(ImagesBitmap(enhanceBitmap))
                            launch(Dispatchers.Main){
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }


                }
            }
        }
    }

    private  fun enhanceImage(bitmap: Bitmap) : Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val grayscaleBitmap = Bitmap.createBitmap(width, height, bitmap.config)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    val pixel = bitmap.getPixel(x, y)
                    val r = Color.red(pixel)
                    val g = Color.green(pixel)
                    val b = Color.blue(pixel)
                    val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
                    grayscaleBitmap.setPixel(x, y, Color.rgb(gray, gray, gray))
                }
            }


     return grayscaleBitmap
        
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun convertToPdf() {
        if(selectedImage.isEmpty())
        {
            Toast.makeText(context,"No Images Selected", Toast.LENGTH_LONG).show()
            return
        }
        GlobalScope.launch(Dispatchers.IO){
            val pdfUri = createPdfFile(requireContext())
            launch(Dispatchers.Main) {
                if(pdfUri != null)
                {
                    Toast.makeText(requireContext(),"Pdf Created at : $pdfUri", Toast.LENGTH_LONG).show()
                    val fragment = PdfViewerFragment.newInstance(pdfUri)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer,fragment)
                        .commit()

                }

            }
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
                        PdfUtils.createPdfFromBitmaps(context,selectedImage.map {
                            it.bitmap
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

    @SuppressLint("NotifyDataSetChanged")
    private fun onImageRemoved(image: ImagesBitmap)
    {
        selectedImage.remove(image)
        adapter.notifyDataSetChanged()

    }



}