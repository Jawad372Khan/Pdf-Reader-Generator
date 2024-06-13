package com.example.pdf.dataclasses

import android.net.Uri

data class PdfFile(

    val uri:Uri,
    val name: String,
    val date : String,
    val size : Int


)
