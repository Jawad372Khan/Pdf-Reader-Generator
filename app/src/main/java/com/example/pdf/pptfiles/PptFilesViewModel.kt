package com.example.pdf.pptfiles

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.pdf.repositories.PdfRepository

class PptFilesViewModel(application : Application) : AndroidViewModel(application) {

    val pptRepository = PdfRepository(application)
    val pptList = pptRepository.getPdfFiles("ppt")


}