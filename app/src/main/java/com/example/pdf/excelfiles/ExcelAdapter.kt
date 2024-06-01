package com.example.pdf.excelfiles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf.PdfFile
import com.example.pdf.R
import com.example.pdf.databinding.ItemExcelBinding


class ExcelAdapter(val pdfFiles : List<PdfFile>) : RecyclerView.Adapter<ExcelAdapter.ExcelViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExcelViewHolder {
        val binding = ItemExcelBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExcelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExcelViewHolder, position: Int) {
        holder.bind(pdfFiles[position])

    }

    override fun getItemCount(): Int {
        return pdfFiles.size

    }
    class ExcelViewHolder(private val binding : ItemExcelBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(pdfFile : PdfFile)
        {
            binding.apply {
                pdfName.text = pdfFile.name
                pdfDate.text = pdfFile.date
                pdfSize.text = "${pdfFile.size / 1024 } KB"
                root.setOnClickListener{

                            val context = it.context
                            if(context is FragmentActivity)
                            {
                                val fragment = ExcelFilesViewerFragment.newInstance(pdfFile.uri)
                                context.supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragmentContainer,fragment)
                                    .commit()
                            }
                }

            }

        }

    }
}