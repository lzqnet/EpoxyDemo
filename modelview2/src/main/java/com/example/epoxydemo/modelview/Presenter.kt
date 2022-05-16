package com.example.epoxydemo.modelview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.zql.fileoperationlib.command.ListCommand
import com.zql.fileoperationlib.config.DisplayRestrictions
import com.zql.fileoperationlib.model.FileModel
import com.zql.fileoperationlib.model.FileSystemObject
import com.zql.fileoperationlib.util.DefaultIconProvider
import com.zql.fileoperationlib.util.FileList
import com.zql.fileoperationlib.util.RestrictionHelper
import com.zql.fileoperationlib.util.StorageHelper

import com.zql.frameworks.base.mvp.AbsMvpPresenter
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Presenter(context: Context?) : AbsMvpPresenter<IMvpView>(context) {
    val mDefaultIconProvider = DefaultIconProvider()
    val mListCommand = ListCommand()


    fun fetchFileList() {

        Flowable.just("").observeOn(Schedulers.io()).map { _ ->
            StorageHelper.getStorageVolumes(context, true)
        }.map {
            Log.d("lzqtest", "Presenter.fetchFileList: $it ")
            if (it.size > 0) {
                val volume = it.get(0)
                fetchFileList(context, volume.path, null)

            } else {
                null
            }
        }.observeOn(AndroidSchedulers.mainThread()).subscribe({
            mvpView.onLoadSuccess(it)

        }, {
            Log.d("lzqtest", "Presenter.fetchFileList: 42 ", it)
        })

    }

    @SuppressLint("CheckResult")
    fun fetchFileList(
        context: Context,
        path: String,
        mRestrictions: Map<DisplayRestrictions?, Any?>?
    ): FileList<FileModel> {
        //        val test = atomicInteger.incrementAndGet()

        Log.d("lzqtest", "FileListViewModel.fetchFileList: enter path=$path")
        val fileSystemObjectList = mListCommand.execute(path)
        Log.d(
            "lzqtest",
            "FileListViewModel.fetchFileList:fileSystemObjectList size= ${fileSystemObjectList.size} get list thread=${Thread.currentThread().name} "
        )
        val sortedFiles = RestrictionHelper.applyUserPreferences(
            fileSystemObjectList,
            mRestrictions,
            false,
            context
        )
        Log.d("lzqtest", "FileListViewModel.fetchFileList:sortedFiles size= ${sortedFiles.size} ")
        Log.d("lzqtest", "FileListViewModel.fetchFileList: before convertFileModel ")
        val ret = convertFileModel(sortedFiles, context, null)
        Log.d("lzqtest", "FileListViewModel.fetchFileList: after convertFileModel ")
        return ret


    }

    private fun convertFileModel(
        fileSystemObjects: List<FileSystemObject>,
        context: Context,
        selectedItems: HashMap<String, FileModel>?
    ): FileList<FileModel> {
        val fileModelList = FileList<FileModel>()
        Log.d(
            "lzqtest",
            "FileListViewModel.convertFileModel:size= ${fileSystemObjects.size} thread=${Thread.currentThread()}  "
        )

        Log.d(
            "lzqtest",
            "FileListViewModel.convertFileModel: withstate thread=${Thread.currentThread().name}   "
        )
        for (fso in fileSystemObjects) {
            val model: FileModel = FileModel(fso)
            val resourceId = mDefaultIconProvider.getDefaultIconResId(context, fso)
            model.setResourceIconId(resourceId)
            val selectModel: FileModel? = selectedItems?.get(fso.getFullPath())
            if (selectModel != null) {
                model.setSelected(true)
            } else {
                model.setSelected(false)
            }
            fileModelList.add(model)
        }

        Log.d("lzqtest", "FileListViewModel.convertFileModel:size ${fileModelList.size}  ")
        return fileModelList
    }


    override fun detachView() {
        super.detachView()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}