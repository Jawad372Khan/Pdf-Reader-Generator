package com.example.pdf

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.pdf.pdfiles.PdfViewerFragment

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument

import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.property.UnitValue
import java.io.ByteArrayOutputStream

import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

object PdfUtils {




    fun createPdfFromImages(context : Context, imageUris : List<Uri>, outputStream : OutputStream)
   {

        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        for(uri in imageUris)
        {
           val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val itextImage = Image(ImageDataFactory.create(byteArray))

            itextImage.setWidth(UnitValue.createPercentValue(100f))
            document.add(itextImage)

            }
        document.close()
        }

    fun createPdfFromBitmaps(context : Context,bitmaps: List<Bitmap>, outputStream: OutputStream) {
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        for (bitmap in bitmaps) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val itextImage = Image(ImageDataFactory.create(byteArray))

            itextImage.setWidth(UnitValue.createPercentValue(100f))
            document.add(itextImage)
        }

        document.close()
    }


}
