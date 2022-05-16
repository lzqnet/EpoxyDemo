package com.example.epoxydemo

import android.app.Application
import com.zql.filepickerlib.mimeType.MimeTypeHelper

class DemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MimeTypeHelper.getMimeTypeHelperInstance().initMimeTypes(this)

    }
}