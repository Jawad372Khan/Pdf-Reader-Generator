package com.example.pdf.pdfiles

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pdf.PdfFile
import com.example.pdf.repositories.PdfRepository


class PdfViewModel(application: Application) : AndroidViewModel(application) {

    val pdfRepository = PdfRepository(application)
    val pdfFiles : LiveData<List<PdfFile>> = pdfRepository.getPdfFiles("pdf")


}