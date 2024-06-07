package com.example.pdf.pdfiles

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.PointF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.pdf.databinding.FragmentSignatureBinding
import com.itextpdf.io.image.ImageData
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Suppress("DEPRECATION")
class SignatureFragment : Fragment() {


    private lateinit var binding : FragmentSignatureBinding
    private var signatureBitmap: Bitmap? = null
    private var dropX: Float = 0f
    private var dropY: Float = 0f
    private var dropPage : Int = 1



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignatureBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


         val uri = arguments?.getParcelable<Uri>("pdf_uri")
        if(uri != null)
        {
            binding.pdfView.fromUri(uri)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load()



        }
        binding.saveSignature.setOnClickListener {
            saveSignature()
        }
        binding.clearSignature.setOnClickListener {
            binding.signatureView.clear()
        }
        binding.savePdf.setOnClickListener {
            if(uri!=null){
                embed(uri)
            }

        }
        binding.imageView.setOnTouchListener(View.OnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val shadowBuilder = View.DragShadowBuilder(view)
                view.startDragAndDrop(null, shadowBuilder, view, 0)

                true
            } else {
                false
            }
        })
        binding.pdfView.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> true
                DragEvent.ACTION_DRAG_ENTERED -> true
                DragEvent.ACTION_DRAG_LOCATION -> true
                DragEvent.ACTION_DRAG_EXITED -> true
                DragEvent.ACTION_DROP -> {
                    val view = event.localState as View
                    val x = event.x.toInt() - view.width / 2
                    val y = event.y.toInt() - view.height / 2
                    dropX = event.x
                    dropY = event.y
                    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.leftMargin = x
                    layoutParams.topMargin = y
                    view.layoutParams = layoutParams
                    view.visibility = View.VISIBLE

                    dropPage = binding.pdfView.getCurrentPage() + 1
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    if (!event.result) {
                        (event.localState as View).visibility = View.VISIBLE
                    }
                    true
                }
                else -> false
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun embed(uri : Uri)
    {
        try {
            val outputPdf = File(Environment.getExternalStorageDirectory(), "signed_out.pdf")
            requireContext().contentResolver.openInputStream(uri).use { inputStream ->
                val pdfReader = PdfReader(inputStream)
                val pdfDocSource = com.itextpdf.kernel.pdf.PdfDocument(pdfReader)

                // Create a writer for the output PDF
                val pdfWriter = PdfWriter(outputPdf)
                val pdfDocTarget = com.itextpdf.kernel.pdf.PdfDocument(pdfWriter)

                val numPgs = pdfDocSource.numberOfPages
                for (i in 1..numPgs) {
                    pdfDocSource.copyPagesTo(i, i, pdfDocTarget)
                }


                pdfDocSource.close()

                val page = pdfDocTarget.getPage(dropPage)
                val pdfCanvas = PdfCanvas(page)

                val byteArrayOutputStream = ByteArrayOutputStream()
                signatureBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()
                val imageData: ImageData = ImageDataFactory.create(imageBytes)

                if (signatureBitmap != null) {
                    val imageX = dropX - signatureBitmap!!.width / 2
                    val imageY = page.pageSize.height - dropY - signatureBitmap!!.height / 2
                    pdfCanvas.addImage(
                        imageData,
                        imageX,
                        imageY,
                        signatureBitmap!!.width.toFloat(),
                        true
                    )
                }

                pdfDocTarget.close()
                savePdfToStorage(outputPdf)
            }

        }
        catch (e : IOException)
        {
            e.printStackTrace()        }

    }



    private fun saveSignature() {
        signatureBitmap = binding.signatureView.getSignatureBitmap()
        val signatureFile = File(Environment.getExternalStorageDirectory(),"signature.png")
        try {
            FileOutputStream(signatureFile).use {
                signatureBitmap?.compress(Bitmap.CompressFormat.PNG,100,it)
                binding.imageView.apply {
                    setImageBitmap(signatureBitmap)
                    visibility = View.VISIBLE
                }

            }
        }catch(e : IOException)
        {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun savePdfToStorage(pdfFile: File) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Files.FileColumns.DISPLAY_NAME, "signed_out.pdf")
            put(MediaStore.Files.FileColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.Files.FileColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }

        val contentResolver = requireContext().contentResolver
        val pdfUri = contentResolver.insert(
            MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
            contentValues
        )

        pdfUri?.let { pdfUri ->
            contentResolver.openOutputStream(pdfUri)?.use { outputStream ->
                pdfFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        pdfUri?.let { pdfUri ->
            binding.pdfView.fromUri(pdfUri).load()
            Toast.makeText(requireContext(), "PDF saved to Documents", Toast.LENGTH_LONG).show()
        } ?: run {
            Toast.makeText(requireContext(), "Failed to save PDF", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(uri : Uri) =
            SignatureFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("pdf_uri",uri)
                }
            }
    }
}