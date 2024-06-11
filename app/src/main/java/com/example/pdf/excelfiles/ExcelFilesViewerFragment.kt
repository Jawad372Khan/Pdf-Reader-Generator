package com.example.pdf.excelfiles

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf.databinding.FragmentExcelFilesViewerBinding
import org.apache.poi.ss.usermodel.WorkbookFactory

import java.io.InputStream


@Suppress("DEPRECATION")
class ExcelFilesViewerFragment : Fragment() {

    private lateinit var binding : FragmentExcelFilesViewerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExcelFilesViewerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val uri = arguments?.getParcelable<Uri>("excel_uri")
        if(uri != null)
        {
            requireContext().contentResolver.openInputStream(uri).let {
                if(it != null)
                {
                    val htmlContent = converExcelToHtml(it)
                    displayHtmlContent(htmlContent)
                    it.close()
                }

            }
        }



    }

    private fun displayHtmlContent(htmlContent: String) {
        binding.webView.loadDataWithBaseURL(null,htmlContent,"text/html","UTF-8",null)

    }

    private fun converExcelToHtml(inputStream: InputStream): String {
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)
        val htmlBuilder = StringBuilder()

        htmlBuilder.append("<html><body><table border='1'>")
        for(row in sheet)
        {
            htmlBuilder.append("<tr>")
            for(cell in row)
            {
                htmlBuilder.append("<td>")
                htmlBuilder.append(cell.toString())
                htmlBuilder.append("</td>")
            }
            htmlBuilder.append("</tr>")

        }
        htmlBuilder.append("</table></body></html>")

        workbook.close()
        return htmlBuilder.toString()

    }




    companion object {

        @JvmStatic
        fun newInstance(uri : Uri) =
            ExcelFilesViewerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("excel_uri",uri)
                }
            }
    }
}