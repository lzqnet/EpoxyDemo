package com.zql.frameworks.base.mvp;

import android.widget.BaseAdapter;

/**
 * 一般用于诸如ListView、GridView等列表数据加载页面的MvpView.
 * Created by Liu Hanhong on 15/6/30.
 */
public interface MvpLceView extends MvpView {
    /**
     * 显示一个加载中的视图
     *
     * @param pullToRefresh 如果是true,那么表示下拉刷新被触发了
     */
    void showLoading(boolean pullToRefresh);

    /**
     * 隐藏加载视图
     */
    void dismissLoading();

    /**
     * 显示 content view，一般是调用{@link BaseAdapter#notifyDataSetChanged()}.
     */
    void showContent();

    /**
     * 显示错误信息
     */
    void showError(String errMsg);

}
