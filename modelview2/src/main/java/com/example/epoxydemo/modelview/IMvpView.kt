package com.example.epoxydemo.modelview


import com.zql.fileoperationlib.model.FileModel
import com.zql.fileoperationlib.util.FileList
import com.zql.frameworks.base.mvp.MvpView

interface IMvpView :MvpView{
    fun onLoadSuccess(data: FileList<FileModel>?)
    fun onLoadFail(cause:String)

}