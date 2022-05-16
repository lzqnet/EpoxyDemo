package com.example.epoxydemo

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.epoxydemo.viewholder.Fragment


class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_CODE_STORAGE_PERMS = 1

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_main)
        if (!hasPermissions() && getApiVersion() > 23) {
            requestNecessaryPermissions()
        }else{
            if (findViewById<View?>(R.id.container) != null
                && supportFragmentManager.findFragmentByTag("fragment")==null) {
                // Create a new Fragment to be placed in the activity layout
                val firstFragment = Fragment()

                // Add the fragment to the 'fragment_container' FrameLayout
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, firstFragment,"fragment").commit()
            }
        }
    }
    @TargetApi(23)
    private fun requestNecessaryPermissions() {
        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permissions, REQUEST_CODE_STORAGE_PERMS)
    }

    private fun hasPermissions(): Boolean {
        val res = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return res == PackageManager.PERMISSION_GRANTED
    }
    fun getApiVersion(): Int {
        return Build.VERSION.SDK_INT
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grandResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grandResults)
        var allowed = true
        when (requestCode) {
            REQUEST_CODE_STORAGE_PERMS -> for (res in grandResults) {
                allowed = allowed && res == PackageManager.PERMISSION_GRANTED
            }
            else                                           -> allowed = false
        }
        if (allowed) {
            Log.d("lzqtest", "FilePickerActivity2.onRequestPermissionsResult: ")
            if (findViewById<View?>(R.id.container) != null
                && supportFragmentManager.findFragmentByTag("fragment")==null) {
                // Create a new Fragment to be placed in the activity layout
                val firstFragment = Fragment()

                // Add the fragment to the 'fragment_container' FrameLayout
                supportFragmentManager.beginTransaction()
                    .add(R.id.container, firstFragment,"fragment").commit()
            }
        } else {
            finish()
        }
    }
}