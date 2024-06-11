package com.example.pdf.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pdf.R
import com.example.pdf.databinding.FragmentStoragePermissionRequestBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class StoragePermissionRequestFragment : Fragment() {

    private lateinit var binding : FragmentStoragePermissionRequestBinding
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )
    private val REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentStoragePermissionRequestBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            if(checkPermission())
            {

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, MainFragment())
                        .commit()

            }

        binding.allowBtn.setOnClickListener{
            requestPermission()
        }

    }

    private fun requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            try{
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package",requireContext().packageName,null)
                intent.data = uri
                storageActivityResultLauncher.launch(intent)
            }
            catch (e : Exception)
            {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                storageActivityResultLauncher.launch(intent)

            }

        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),permissions,REQUEST_CODE)

        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            if(Environment.isExternalStorageManager())
            {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, MainFragment())
                    .commit()
            }
            else
            {
                Toast.makeText(requireContext(),"Permission Required", Toast.LENGTH_SHORT).show()
            }

        }

    }
    private fun checkPermission() : Boolean
    {

        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            Environment.isExternalStorageManager()
        }
        else
        {
            val write = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE)
        {
            if(grantResults.isNotEmpty())
            {
                val write = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val read = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if(write && read)
                {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, MainFragment())
                        .commit()
                }

            }
        }
    }

}