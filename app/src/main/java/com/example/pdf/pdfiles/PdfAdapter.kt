package com.example.pdf.pdfiles

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf.PdfFile
import com.example.pdf.R
import com.example.pdf.databinding.ItemPdfBinding
import com.example.pdf.excelfiles.ExcelFilesViewerFragment
import com.example.pdf.pptfiles.PptFilesViewerFragment
import com.example.pdf.wordfiles.WordFilesViewerFragment

class PdfAdapter(private val pdfFiles : List<PdfFile>) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val binding = ItemPdfBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PdfViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        holder.bind(pdfFiles[position])

    }

    override fun getItemCount(): Int {
        return pdfFiles.size

    }
    class PdfViewHolder(private val binding : ItemPdfBinding) : RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("SetTextI18n")
        fun bind(pdfFile : PdfFile)
        {
            binding.apply {
                pdfName.text = pdfFile.name
                pdfDate.text = pdfFile.date
                pdfSize.text = "${pdfFile.size / 1024 } KB"
                root.setOnClickListener{
                    when{
                        pdfFile.name.endsWith(".pdf")->
                        {
                            val context = it.context
                            if(context is FragmentActivity)
                            {
                                val fragment = PdfViewerFragment.newInstance(pdfFile.uri)
                                context.supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer,fragment)
                                    .commit()
                            }
                        }
                        pdfFile.name.endsWith(".doc") || pdfFile.name.endsWith(".docx")->
                        {
                            val context = it.context
                            if(context is FragmentActivity)
                            {
                                val fragment = WordFilesViewerFragment.newInstance(pdfFile.uri)
                                context.supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer,fragment)
                                    .commit()
                            }
                        }
                        pdfFile.name.endsWith(".xlsx") || pdfFile.name.endsWith(".xls")->
                        {
                            val context = it.context
                            if(context is FragmentActivity)
                            {
                                val fragment = ExcelFilesViewerFragment.newInstance(pdfFile.uri)
                                context.supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer,fragment)
                                    .commit()
                            }
                        }
                        pdfFile.name.endsWith(".pptx")->
                        {
                            val context = it.context
                            if (context is FragmentActivity)
                            {
                                val fragment = PptFilesViewerFragment.newInstance(pdfFile.uri)
                                context.supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer,fragment)
                                    .commit()
                            }
                        }

                    }

                }


            }

        }

    }
}