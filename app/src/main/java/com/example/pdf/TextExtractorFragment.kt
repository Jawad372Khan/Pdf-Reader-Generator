package com.example.pdf

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.pdf.databinding.FragmentTextExtractorBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TextExtractorFragment : Fragment() {

    private lateinit var binding : FragmentTextExtractorBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextExtractorBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openDocumentScanner()
    }

    private fun openDocumentScanner() {
        val options = GmsDocumentScannerOptions.Builder().apply {
            setGalleryImportAllowed(true)
            setPageLimit(1)
            setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG)
            setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
        }
            .build()

        val scanner = GmsDocumentScanning.getClient(options)
        scanner.getStartScanIntent(requireActivity()).addOnSuccessListener {
            scannerLauncher.launch(IntentSenderRequest.Builder(it).build())
        }
            .addOnFailureListener {

            }
    }

    private val scannerLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){
        if(it.resultCode == Activity.RESULT_OK)
        {
            it.data.let {
                val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(it)
                scanResult?.getPages()?.let { pages ->


                    for (page in pages) {
                        val imageUri = page.imageUri
                        processImageForText(imageUri)
                    }

                }
            }
        }
    }

    private fun processImageForText(imageUri: Uri) {
        try {
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromFilePath(requireContext(),imageUri)
            recognizer.process(image).addOnSuccessListener {
                val resultText = StringBuilder()
                for(blocks in it.textBlocks)
                {
                    for(line in blocks.lines)
                    resultText.append(line.text).append("\n")
                }
                binding.textView.text = resultText.toString()
            }
                .addOnFailureListener {

                }

        }
        catch (e : Exception)
        {
            e.printStackTrace()
        }

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            TextExtractorFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}