package com.example.epoxydemo.databinding

import com.zql.filepickerlib.model.FileModel
import com.zql.filepickerlib.picker.FileList
import com.zql.frameworks.base.mvp.MvpView

interface IMvpView :MvpView{
    fun onLoadSuccess(data:FileList<FileModel>?)
    fun onLoadFail(cause:String)

}