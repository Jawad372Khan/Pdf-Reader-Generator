package com.example.pdf.excelfiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pdf.PdfFile
import com.example.pdf.repositories.PdfRepository

class ExcelFilesViewModel(application : Application) : AndroidViewModel(application) {

    val excelFilesRepository = PdfRepository(application)
    val excelList : LiveData<List<PdfFile>> = excelFilesRepository.getPdfFiles("excel")
}