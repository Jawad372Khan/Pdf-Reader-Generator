package com.example.pdf.barcodescanning

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pdf.R
import com.example.pdf.databinding.FragmentBarCodeScanningBinding
import com.example.pdf.pdfiles.PdfViewerFragment
import com.example.pdf.utils.PdfUtils
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning


class BarCodeScanningFragment : Fragment() {
    private lateinit var binding : FragmentBarCodeScanningBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBarCodeScanningBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            scanCode()
         binding.generatePdfs.setOnClickListener {
             val text = binding.barCodeResult.text.toString()
             val uri = PdfUtils.generatePdf(requireContext(),text)
             if(uri != null)
             {
                 val fragment = PdfViewerFragment.newInstance(uri)
                 requireActivity().supportFragmentManager.beginTransaction()
                     .replace(R.id.fragmentContainer,fragment)
                     .commit()
             }
         }
    }

    @SuppressLint("SetTextI18n")
    private fun scanCode() {
        val options = GmsBarcodeScannerOptions.Builder().apply {
            setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS
            )
            enableAutoZoom()
        }
            .build()
        val scanner = GmsBarcodeScanning.getClient(requireContext(),options)
        scanner.startScan().apply {
            addOnSuccessListener {
                val valueType = it.valueType

                when (valueType) {
                    Barcode.TYPE_WIFI -> {
                        val ssid = it.wifi!!.ssid
                        val password = it.wifi!!.password
                        val type = it.wifi!!.encryptionType
                        binding.barCodeResult.text = "SSID : "+ ssid + "\n"+
                                                    "Password : "+ password + "\n"+
                                                    "Type : "
                    }
                    Barcode.TYPE_URL -> {
                        val title = it.url!!.title
                        val url = it.url!!.url
                        binding.barCodeResult.text =
                                                    "Url : "+url

                    }
                    Barcode.TYPE_CALENDAR_EVENT->
                    {
                        val description = it.calendarEvent!!.description
                        val end = it.calendarEvent!!.end
                        val start = it.calendarEvent!!.start
                        val location= it.calendarEvent!!.location

                        binding.barCodeResult.text = description + "\n" +
                                                    start + "\n" + end + "\n"+
                                                    location

                    }
                }

            }
            addOnCanceledListener {
                Toast.makeText(requireContext(),"Scanning Canceled",Toast.LENGTH_LONG).show()
            }
            addOnFailureListener {
                Toast.makeText(requireContext(),"Scanning Failed",Toast.LENGTH_LONG).show()
            }
        }
    }

}