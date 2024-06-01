package com.example.pdf.main


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.pdf.excelfiles.ExcelFilesFragment
import com.example.pdf.pdfiles.PdfFragment
import com.example.pdf.pptfiles.PptFilesFragment
import com.example.pdf.wordfiles.WordFilesFragment


class TabAdapter(fragmentActivity : FragmentActivity ) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return  when(position)
        {
            0 -> PdfFragment()
            1 -> WordFilesFragment()
            2 -> ExcelFilesFragment()
            3 -> PptFilesFragment()
            else ->
                PdfFragment()

        }
    }

}