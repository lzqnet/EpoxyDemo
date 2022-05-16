package com.example.epoxydemo.viewholder


import com.zql.fileoperationlib.model.FileModel
import com.zql.fileoperationlib.util.FileList
import com.zql.frameworks.base.mvp.MvpView

interface IMvpView :MvpView{
    fun onLoadSuccess(data: FileList<FileModel>?)
    fun onLoadFail(cause:String)

}