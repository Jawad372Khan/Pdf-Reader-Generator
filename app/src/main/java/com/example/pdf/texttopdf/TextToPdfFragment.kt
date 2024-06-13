package com.example.pdf.texttopdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf.utils.PdfUtils
import com.example.pdf.R
import com.example.pdf.databinding.FragmentTextToPdfBinding
import com.example.pdf.pdfiles.PdfViewerFragment


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
           val uri = PdfUtils.generatePdf(requireContext(),binding.textField.text.toString())
            if(uri != null)
            {
                val fragment = PdfViewerFragment.newInstance(uri)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer,fragment)
                    .commit()
            }

        }
    }

}