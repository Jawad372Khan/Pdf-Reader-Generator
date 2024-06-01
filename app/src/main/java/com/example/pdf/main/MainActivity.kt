package com.example.pdf.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.pdf.R
import com.example.pdf.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val permissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
    )
    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        if(checkPermission())
        {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment())
                .commit()
        }
        else
        {
            requestPermission()
        }

    }

    private fun requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            try{
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package",this.packageName,null)
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
            ActivityCompat.requestPermissions(this,permissions,REQUEST_CODE)

        }
    }

    private val storageActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            if(Environment.isExternalStorageManager())
            {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, MainFragment())
                    .commit()
            }
            else
            {
                Toast.makeText(this,"Permission Required",Toast.LENGTH_SHORT).show()
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
            val write = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)
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
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, MainFragment())
                        .commit()
                }
                else
                {

                }
            }
        }
    }
}