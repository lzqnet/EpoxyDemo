package com.zql.frameworks.base.mvp;

/**
 * 用于非列表类加载需求的页面
 * Created by Liu Hanhong on 16/3/16.
 */
public interface MvpLoadingView extends MvpView {

    /**
     * 显示加载视图，一般使用ProgressDialog.
     */
    void showLoading();

    /**
     * 隐藏加载视图
     */
    void cancelLoading();

    /**
     * 隐藏错误信息
     * @param msg
     */
    void showError(String msg);
}
