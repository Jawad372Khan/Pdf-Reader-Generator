package com.example.pdf.wordfiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pdf.PdfFile
import com.example.pdf.repositories.PdfRepository

class WordFilesViewModel (application : Application)  : AndroidViewModel(application){

    val wordFilesRepository = PdfRepository(application)
    val wordFiles : LiveData<List<PdfFile>> = wordFilesRepository.getPdfFiles("word")
}