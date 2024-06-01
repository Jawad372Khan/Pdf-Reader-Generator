package com.example.pdf.wordfiles

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf.databinding.FragmentWordFilesViewerBinding
import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.InputStream


class WordFilesViewerFragment : Fragment() {


    private lateinit var binding : FragmentWordFilesViewerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWordFilesViewerBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val uri = arguments?.getParcelable<Uri>("word_uri")

        if (uri != null) {
            requireContext().contentResolver.openInputStream(uri).let {
               if(it != null)
               {
                   val text = readWordFile(it)

//                   val pages = pageinateText(text,100)
//                   binding.viewPager.adapter = WordPagerAdapter(requireContext(),pages)
                     binding.wordTex.text = text

               }

            }
        }

    }

    private fun pageinateText(text: String, charsPerPage: Int): List<String> {
        val pages = mutableListOf<String>()
        var start = 0
        while(start < text.length)
        {
            val end = Math.min(start+charsPerPage, text.length)
            pages.add(text.substring(start,end))
            start = end
        }
        return pages
    }

    private fun readWordFile(inputStream: InputStream): String {
        return try {
            val document = XWPFDocument(inputStream)
            val text = StringBuilder()
            for (paragraph in document.paragraphs)
            {
                text.append(paragraph.text).append("\n")
            }
            text.toString()
        } catch (e : Exception)
        {
            "Error Reading Word File"
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(uri : Uri) =
            WordFilesViewerFragment().apply {
                arguments = Bundle().apply {
                 putParcelable("word_uri",uri)
                }
            }
    }
}