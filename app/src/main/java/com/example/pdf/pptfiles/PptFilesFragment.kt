package com.example.pdf.pptfiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdf.pdfiles.PdfAdapter
import com.example.pdf.databinding.FragmentPptFilesBinding


class PptFilesFragment : Fragment() {

    private lateinit var binding: FragmentPptFilesBinding
   private lateinit var pptViewModel : PptFilesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPptFilesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        pptViewModel = ViewModelProvider(this).get(PptFilesViewModel::class.java)
        pptViewModel.pptList.observe(viewLifecycleOwner, Observer { pptList->
            binding.apply {
                pptRecyclerView.layoutManager = LinearLayoutManager(context)
                pptRecyclerView.adapter = PptAdapter(pptList)
            }

        })
    }
}