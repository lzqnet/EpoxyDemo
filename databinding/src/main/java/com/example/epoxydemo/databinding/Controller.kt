package com.example.epoxydemo.databinding

import android.content.Context
import android.util.Log
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.TypedEpoxyController
import com.example.epoxydemo.ItemViewDatabindingBindingModel_
import com.example.epoxydemo.itemViewDatabinding
import com.zql.filepickerlib.model.FileModel
import com.zql.filepickerlib.picker.IClickListener
import com.zql.filepickerlib.picker.IFileClickListener
import com.zql.filepickerlib.picker.fileItemView
import com.zql.filepickerlib.picker.folderItemView
import com.zql.filepickerlib.util.DateUtil
import com.zql.filepickerlib.util.FileHelper

class Controller(val context: Context?): TypedEpoxyController<List<FileModel>>() {
    override fun buildModels(data: List<FileModel>?) {
        if (!data.isNullOrEmpty()) {
            Log.d("lzqtest", "FileListViewController.buildModels: enter thread=${Thread.currentThread().name} ")
            for ((index, item) in data.withIndex()) {
                Log.d("lzqtest", "Controller.buildModels: 19 ")
                if (item.isDirectory) {
//                    folderItemView {
//                        id(index)
//                        name(item.fileSystemObject.name)
//                        iconRes(item.resourceIconId)
//                        clickListener(object : IClickListener<FileModel> {
//
//                            override fun OnClick() {
//                                Log.d("lzqtest", "PickerFragment.OnClick: folder click data=${item.fileSystemObject.fullPath} ")
//                                currentFolderViewModel.setCurrentPath(item.fileSystemObject.fullPath)
//                            }
//
//                        })
//
//                    }

                } else {
                    Log.d("lzqtest", "Controller.buildModels: 37 ")
                    itemViewDatabinding {
                        setIcon(item.resourceIconId)
                        setName(item.fileSystemObject.name)
                        setSize(FileHelper.getHumanReadableSize(item.fileSystemObject))
                        id(index)
                    }

                }
            }
        }
    }

}