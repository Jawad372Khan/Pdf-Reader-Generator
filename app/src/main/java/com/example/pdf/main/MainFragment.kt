package com.example.pdf.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf.R
import com.example.pdf.textextractor.TextExtractorFragment
import com.example.pdf.databinding.BottomSheetBinding
import com.example.pdf.databinding.FragmentMainBinding
import com.example.pdf.scandocuments.CameraFragment
import com.example.pdf.imagestopdf.ImagesFragment
import com.example.pdf.barcodescanning.BarCodeScanningFragment
import com.example.pdf.pdfiles.PdfFragment
import com.example.pdf.texttopdf.TextToPdfFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewPager()
        binding.addPdf.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())

            val binding = BottomSheetBinding.inflate(LayoutInflater.from(requireContext()),null)
            binding.apply {
                imgToPdf.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, ImagesFragment())
                        .commit()
                    dialog.dismiss()
                }
                scanDocument.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, CameraFragment())
                        .commit()
                    dialog.dismiss()
            }
                textExtractor.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TextExtractorFragment())
                        .commit()
                    dialog.dismiss()

                }
                signature.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, PdfFragment())
                        .commit()
                    dialog.dismiss()

                }

                textPdf.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TextToPdfFragment())
                        .commit()
                    dialog.dismiss()

                }
                barCodeScanning.setOnClickListener {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, BarCodeScanningFragment())
                        .commit()
                    dialog.dismiss()
                }
            }


           dialog.apply {
               setCancelable(false)
               setContentView(binding.root)
           }
            dialog.show()
        }
    }
    private fun setupViewPager() {
        val adapter = TabAdapter(requireActivity())
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "PDF"
                1 -> "Word"
                2 -> "Excel"
                3 -> "PPT"
                else -> "PDF"
            }
        }.attach()
    }

}