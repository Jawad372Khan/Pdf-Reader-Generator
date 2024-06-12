package com.example.pdf.texttopdf

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf.R
import com.example.pdf.databinding.FragmentTextToPdfBinding
import com.example.pdf.pdfiles.PdfViewerFragment
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class TextToPdfFragment : Fragment() {
    private lateinit var binding : FragmentTextToPdfBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextToPdfBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.generatePdf.setOnClickListener {
           generatePdf()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun generatePdf() {
        val fileName = generateFileName()
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME,"$fileName.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE,"application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH,"Documents/")
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Files.getContentUri("external"),contentValues)
        uri.let {
            if(it != null)
            requireContext().contentResolver.openOutputStream(it).use { outputStream ->
                if(outputStream != null)
                {
                    writePdf(outputStream)
                    val fragment = PdfViewerFragment.newInstance(it)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer,fragment)
                        .commit()
                }
            }
        }
    }

    private fun writePdf(outputStream: OutputStream) {
        val writer = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        val text : String
        text = binding.textField.text.toString()

        document.add(Paragraph(text))
        document.close()
    }


    private fun generateFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return "PDF_" + sdf.format(Date())
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TextToPdfFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}