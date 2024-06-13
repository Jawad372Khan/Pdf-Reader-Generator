package com.example.pdf.repositories

import android.app.Application
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import com.example.pdf.dataclasses.PdfFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PdfRepository (private val application : Application) {

    fun getPdfFiles(mimeType: String) : MutableLiveData<List<PdfFile>>
    {
        val pdfFilesLiveData = MutableLiveData<List<PdfFile>>()
        val pdfFilesList = mutableListOf<PdfFile>()



        val collection =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
            {
                MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
            }
        else
            {
                MediaStore.Files.getContentUri("external")
            }

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.SIZE
        )




        val selection : String
        selection =  when(mimeType)
        {
            "pdf" ->
            {
                "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"

            }
            else->
            {
                "${MediaStore.Files.FileColumns.MIME_TYPE} IN (?,?)"

            }
        }
        val selectionArgs =
            when(mimeType)
            {
                "pdf" ->
                {
                    arrayOf("application/pdf")

                }
                "word" ->
                {
                    arrayOf(
                        "application/msword",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    )
                }
                "excel" ->
                {

                        arrayOf(
                            "application/vnd.ms-excel",
                            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                }
                else->
                {
                     arrayOf(
                        "application/vnd.ms-powerpoint",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation"
                    )
                }
            }



        val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"

        val query : Cursor? = application.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder

        )

        GlobalScope.launch(Dispatchers.IO){
            query?.use { cursor->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

                while (cursor.moveToNext())
                {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val date = cursor.getLong(dateColumn) * 1000L
                    val size = cursor.getInt(sizeColumn)

                    val contentUri : Uri = Uri.withAppendedPath(
                        MediaStore.Files.getContentUri("external"),
                        id.toString()
                    )

                    pdfFilesList += PdfFile(contentUri,name,SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(date)),size)
                }

            }

            pdfFilesLiveData.postValue(pdfFilesList)
        }

        return pdfFilesLiveData
    }

}