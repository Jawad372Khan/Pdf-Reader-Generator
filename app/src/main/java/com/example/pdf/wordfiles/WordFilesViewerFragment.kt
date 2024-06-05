package com.example.pdf.wordfiles



import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import com.example.pdf.databinding.FragmentWordFilesViewerBinding
import org.apache.poi.xwpf.usermodel.XWPFDocument
import android.view.ViewGroup
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import java.io.*





@Suppress("DEPRECATION")
class WordFilesViewerFragment : Fragment() {


    private lateinit var binding : FragmentWordFilesViewerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordFilesViewerBinding.inflate(inflater,container,false)
        return binding.root
    }


    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val uri = arguments?.getParcelable<Uri>("word_uri")



        if (uri != null) {
            requireContext().contentResolver.openInputStream(uri).let {
                if (it != null) {

                    val pdfFile = File(Environment.getExternalStorageDirectory(), "document.pdf")
                    convertWordToPdf(it,pdfFile)
                    binding.wordView.fromFile(pdfFile).load()


                }

            }
        }
    }




    private fun convertWordToPdf(inputStream: InputStream, pdfFile: File) {
        val document = XWPFDocument(inputStream)
        val pdfWriter = PdfWriter(FileOutputStream(pdfFile))
        val pdfDoc = PdfDocument(pdfWriter)
        val doc = com.itextpdf.layout.Document(pdfDoc)


            for (para in document.paragraphs) {
                val pdfParagraph = Paragraph()

                // Iterate through the runs in the paragraph
                for (run in para.runs) {
                    if (run.embeddedPictures.isNotEmpty()) {
                        // If the run contains images, add them to the PDF
                        for (pic in run.embeddedPictures) {
                            val picData = pic.pictureData.data
                            val img =
                                com.itextpdf.layout.element.Image(ImageDataFactory.create(picData))
                            doc.add(img)
                        }
                    } else {
                        // Add the text run to the PDF paragraph
                        val text = Text(run.text())

                        // Handle text formatting
                        if (run.isBold) text.setBold()
                        if (run.isItalic) text.setItalic()
                        if (run.isStrike) text.setUnderline()
                        if (run.fontSize != -1) text.setFontSize(run.fontSize.toFloat())

                        pdfParagraph.add(text)
                    }
                }

                // Handle paragraph alignment
                when (para.alignment) {
                    ParagraphAlignment.CENTER -> pdfParagraph.setTextAlignment(TextAlignment.CENTER)
                    ParagraphAlignment.RIGHT -> pdfParagraph.setTextAlignment(TextAlignment.RIGHT)
                    ParagraphAlignment.BOTH -> pdfParagraph.setTextAlignment(TextAlignment.JUSTIFIED)
                    else -> pdfParagraph.setTextAlignment(TextAlignment.LEFT)
                }

                doc.add(pdfParagraph)
            }

            // Close the documents
            doc.close()
            pdfDoc.close()
        }






    companion object {

        @JvmStatic
        fun newInstance(uri : Uri) =
            WordFilesViewerFragment().apply {
                arguments = Bundle().apply {
                 putParcelable("word_uri",uri)
                }
            }
    }
}
