package com.example.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument

import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.property.UnitValue
import java.io.ByteArrayOutputStream

import java.io.OutputStream
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
    private fun scaleImage(bitmap : Bitmap,maxWidth : Float, maxHeight : Float) : Bitmap
    {
        val originalWidth = bitmap.width.toFloat()
        val originalHeight = bitmap.height.toFloat()

        val widthScale = maxWidth/originalWidth
        val heightScale = maxHeight/originalHeight

        val scale = Math.min(widthScale,heightScale)

        val scaledWidth = (originalWidth * scale).toInt()
        val scaledHeight = (originalHeight * scale).toInt()
        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false)
    }
}
