package com.example.pdf.pptfiles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf.PdfFile
import com.example.pdf.R
import com.example.pdf.databinding.ItemPptxBinding


class PptAdapter(val pdfFiles : List<PdfFile>) : RecyclerView.Adapter<PptAdapter.PptViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PptViewHolder {
        val binding = ItemPptxBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PptViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PptViewHolder, position: Int) {
        holder.bind(pdfFiles[position])

    }

    override fun getItemCount(): Int {
        return pdfFiles.size

    }

    class PptViewHolder(private val binding: ItemPptxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pdfFile: PdfFile) {
            binding.apply {
                pdfName.text = pdfFile.name
                pdfDate.text = pdfFile.date
                pdfSize.text = "${pdfFile.size / 1024} KB"
                root.setOnClickListener {

                    val context = it.context
                    if (context is FragmentActivity) {
                        val fragment = PptFilesViewerFragment.newInstance(pdfFile.uri)
                        context.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit()
                    }
                }

            }

        }

    }
}