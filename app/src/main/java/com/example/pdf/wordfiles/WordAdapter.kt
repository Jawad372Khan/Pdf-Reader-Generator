package com.example.pdf.wordfiles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pdf.PdfFile
import com.example.pdf.R
import com.example.pdf.databinding.ItemWordBinding



class WordAdapter(val pdfFiles : List<PdfFile>) : RecyclerView.Adapter<WordAdapter.WordViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val binding = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(pdfFiles[position])

    }

    override fun getItemCount(): Int {
        return pdfFiles.size

    }

    class WordViewHolder(private val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pdfFile: PdfFile) {
            binding.apply {
                pdfName.text = pdfFile.name
                pdfDate.text = pdfFile.date
                pdfSize.text = "${pdfFile.size / 1024} KB"
                root.setOnClickListener {

                    val context = it.context
                    if (context is FragmentActivity) {
                        val fragment = WordFilesViewerFragment.newInstance(pdfFile.uri)
                        context.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, fragment)
                            .commit()
                    }
                }

            }

        }

    }
}