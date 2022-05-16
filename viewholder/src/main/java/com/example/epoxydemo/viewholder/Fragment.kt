package com.example.epoxydemo.viewholder

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.example.epoxydemo.R
import com.zql.fileoperationlib.model.FileModel
import com.zql.fileoperationlib.util.FileList

import com.zql.frameworks.app.fragment.AbsMvpFragment

class Fragment : AbsMvpFragment<Presenter>(), IMvpView {
    private var fileList: EpoxyRecyclerView? = null
    private lateinit var mFileListViewController: Controller

    override fun getContentViewLayoutId(): Int {
        return R.layout.fragment_modelview
    }

    override fun initData() {
        presenter.fetchFileList()
    }

    override fun initViews(contentView: View?, savedInstanceState: Bundle?) {
        mFileListViewController = Controller(context)
        fileList = contentView?.findViewById(R.id.list_layout)
        fileList?.setController(mFileListViewController)
        fileList?.layoutManager = LinearLayoutManager(context)
    }

    override fun initActions(contentView: View?) {
    }

    override fun createPresenter(context: Context?): Presenter {
        return Presenter(getContext())
    }


    override fun onLoadSuccess(data: FileList<FileModel>?) {
        Log.d("lzqtest", "Fragment.onLoadSuccess: $data ")
        mFileListViewController.setData(data)

    }

    override fun onLoadFail(cause: String) {
    }
}