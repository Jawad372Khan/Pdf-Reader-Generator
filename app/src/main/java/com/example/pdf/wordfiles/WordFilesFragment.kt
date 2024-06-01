package com.example.pdf.wordfiles

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdf.pdfiles.PdfAdapter
import com.example.pdf.databinding.FragmentWordFilesBinding


class WordFilesFragment : Fragment() {


    private lateinit var binding : FragmentWordFilesBinding
    private lateinit var wordFilesViewMode : WordFilesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWordFilesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        wordFilesViewMode = ViewModelProvider(this).get(WordFilesViewModel::class.java)

        wordFilesViewMode.wordFiles.observe(viewLifecycleOwner, Observer { wordFiles->

            binding.apply {
                wordRecyclerView.layoutManager = LinearLayoutManager(context)
                wordRecyclerView.adapter = WordAdapter(wordFiles)
            }

        })


    }
}