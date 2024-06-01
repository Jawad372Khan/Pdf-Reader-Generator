package com.example.pdf.pdfiles

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf.databinding.FragmentPdfViewerBinding


class PdfViewerFragment : Fragment() {

    private lateinit var binding: FragmentPdfViewerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPdfViewerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val uri = arguments?.getParcelable<Uri>("pdf_uri")
        if (uri != null)
        {
            displayFromUri(uri)
        }


    }

    private fun displayFromUri(uri: Uri) {
        binding.pdfView.fromUri(uri)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .defaultPage(0)
            .load()
    }

    companion object {
        @JvmStatic
        fun newInstance(uri: Uri) = PdfViewerFragment().apply {
            arguments = Bundle().apply {
                putParcelable("pdf_uri", uri)
            }
        }
    }







}