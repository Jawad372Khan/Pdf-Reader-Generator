package com.example.pdf.excelfiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdf.pdfiles.PdfAdapter
import com.example.pdf.databinding.FragmentExcelFilesBinding


class ExcelFilesFragment : Fragment() {

    private lateinit var binding : FragmentExcelFilesBinding
    private lateinit var excelFilesViewModel : ExcelFilesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExcelFilesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        excelFilesViewModel = ViewModelProvider(this).get(ExcelFilesViewModel::class.java)
        excelFilesViewModel.excelList.observe(viewLifecycleOwner, Observer { excelList->

            binding.apply {
                excelRecyclerView.layoutManager = LinearLayoutManager(context)
                excelRecyclerView.adapter = ExcelAdapter(excelList)
            }

        })
    }
}