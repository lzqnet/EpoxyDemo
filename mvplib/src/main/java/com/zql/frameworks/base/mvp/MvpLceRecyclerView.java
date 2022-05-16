package com.zql.frameworks.base.mvp;

/**
 * 适用于包含RecyclerView的数据加载页面的MvpView.
 * Created by Liu Hanhong on 15/7/8.
 */
public interface MvpLceRecyclerView extends MvpView {

    enum NotifyType {
        ItemChanged, ItemInsert, ItemRemoved, ItemMoved, ItemRangeChanged,
        ItemRangeInsert, ItemRangeRemoved, DataSetChanged
    }

    /**
     * 显示一个加载中的视图
     *
     * @param pullToRefresh 如果是true,那么表示下拉刷新被触发了
     */
    void showLoading(boolean pullToRefresh);

    /**
     * 隐藏加载视图
     */
    void cancelLoading();

    /**
     * 显示 content view.
     * 用于 RecyclerView.Adapter.
     * 根据 {@link NotifyType} 使用position或配合ext扩展参数代用RecyclerView.Adapter的notifyXXX系列方法.
     */
    void showContent(NotifyType type, int position, int ext);

    /**
     * 显示错误信息
     */
    void showError(String errMsg);

}
