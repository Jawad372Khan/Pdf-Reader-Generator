package com.example.pdf.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf.R
import com.example.pdf.databinding.BottomSheetBinding
import com.example.pdf.databinding.FragmentMainBinding
import com.example.pdf.imagestopdf.CameraFragment
import com.example.pdf.imagestopdf.ImagesFragment
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
            binding.imgToPdf.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ImagesFragment())
                    .commit()
                dialog.dismiss()
            }
            binding.scanDocument.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, CameraFragment())
                    .commit()
                dialog.dismiss()

            }
            dialog.setCancelable(false)
            dialog.setContentView(binding.root)
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

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}