package com.example.pdf.pptfiles
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pdf.databinding.FragmentPptFilesViewerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.poi.sl.usermodel.PictureData
import org.apache.poi.xslf.usermodel.XMLSlideShow
import org.apache.poi.xslf.usermodel.XSLFPictureShape
import org.apache.poi.xslf.usermodel.XSLFTextShape
import java.io.InputStream



@Suppress("DEPRECATION")
class PptFilesViewerFragment : Fragment() {

    private lateinit var binding : FragmentPptFilesViewerBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPptFilesViewerBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val uri = arguments?.getParcelable<Uri>("ppt_uri")
        if(uri != null)
        {
            requireContext().contentResolver.openInputStream(uri).let{
                if(it != null)
                {
                    val htmlContent = convertPPTXToHTML(it)
                    loadHTMLInWebView(htmlContent)
                }
            }

        }

    }

    private fun loadHTMLInWebView(htmlContent: String) {
        binding.pptWebView.loadDataWithBaseURL(null,htmlContent,"text/html","UTF-8",null)
    }

    private fun convertPPTXToHTML(inputStream: InputStream): String {
        val pptx = XMLSlideShow(inputStream)
        val slides = pptx.slides
        val htmlBuilder = StringBuilder()
        htmlBuilder.append("<html><body style='font-family: Arial, sans-serif;'>")

            for ((index, slide) in slides.withIndex()) {
                htmlBuilder.append("<div style='border:1px solid black; margin:10px; padding:10px;'>")
                htmlBuilder.append("<h2 style='text-align: center;'>Slide ${index + 1}</h2>")
                for (shape in slide.shapes) {
                    when (shape) {
                        is XSLFTextShape -> {
                            htmlBuilder.append("<div style='margin-bottom: 10px;'>")
                            for (paragraph in shape.textParagraphs) {
                                htmlBuilder.append("<p>")
                                for (run in paragraph.textRuns) {
                                    htmlBuilder.append(paragraph.text.replace("\n", "<br>"))
                                }
                                htmlBuilder.append("</p>")
                            }
                            htmlBuilder.append("</div>")
                        }
                        is XSLFPictureShape -> {
                            val pictureData = shape.pictureData
                            val imageData = pictureData.data
                            val base64Image = android.util.Base64.encodeToString(imageData, android.util.Base64.NO_WRAP)
                            val imageType = when (pictureData.type) {
                                PictureData.PictureType.JPEG -> "jpeg"
                                PictureData.PictureType.PNG -> "png"
                                PictureData.PictureType.GIF -> "gif"
                                PictureData.PictureType.BMP -> "bmp"
                                else -> "png"
                            }
                            htmlBuilder.append("<img src='data:image/$imageType;base64,$base64Image' style='width:100%;'/>")
                        }
                    }
                }

                htmlBuilder.append("</div>")
            }

            htmlBuilder.append("</body></html>")

            pptx.close()

        return htmlBuilder.toString()

    }

    companion object {

        @JvmStatic
        fun newInstance(uri : Uri) =
            PptFilesViewerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("ppt_uri",uri)
                }
            }
    }
}