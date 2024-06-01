package com.example.pdf.pdfiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.pdf.databinding.FragmentPdfBinding


class PdfFragment : Fragment() {




    private lateinit var binding : FragmentPdfBinding
    private lateinit var pdfViewModel: PdfViewModel

    val REQUEST_CODE = 1



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentPdfBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        pdfViewModel = ViewModelProvider(this).get(PdfViewModel::class.java)
        pdfViewModel.pdfFiles.observe(viewLifecycleOwner, Observer { pdfFiles->
            binding.pdfRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.pdfRecyclerView.adapter = PdfAdapter(pdfFiles)

        })

    }


}