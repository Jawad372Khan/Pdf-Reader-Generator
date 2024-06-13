package com.example.pdf.wordfiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pdf.dataclasses.PdfFile
import com.example.pdf.repositories.PdfRepository

class WordFilesViewModel (application : Application)  : AndroidViewModel(application){

    private val wordFilesRepository = PdfRepository(application)
    val wordFiles : LiveData<List<PdfFile>> = wordFilesRepository.getPdfFiles("word")
}