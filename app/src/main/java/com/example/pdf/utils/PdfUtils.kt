package com.example.pdf.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore

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


    // Create PDF from Text


     fun generatePdf(context : Context,text : String) : Uri?{
        val fileName = generateFileName()
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME,"$fileName.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE,"application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH,"Documents/")
        }

        val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"),contentValues)
        uri.let {
            if(it != null)
                context.contentResolver.openOutputStream(it).use { outputStream ->
                    if(outputStream != null)
                    {
                        writePdf(text,outputStream)
                    }
                }
        }
        return uri
    }

    private fun writePdf(text : String,outputStream: OutputStream) {
        val writer = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        document.add(Paragraph(text))
        document.close()
    }


    private fun generateFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        return "PDF_" + sdf.format(Date())
    }
}
