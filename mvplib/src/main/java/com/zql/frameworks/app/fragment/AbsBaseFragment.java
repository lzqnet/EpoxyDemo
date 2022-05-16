package com.zql.frameworks.app.fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

/**
 * Created by Liu Hanhong on 15/6/30.
 */
public abstract class AbsBaseFragment extends RootFragment {

    private boolean mBreakInit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBreakInit = false;
        View contentView = inflater.inflate(getContentViewLayoutId(), container, false);
        bindViews(contentView);
        internalInitMvp(savedInstanceState);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mBreakInit) {
            return;
        }
        initData();
        if (mBreakInit) {
            return;
        }
        initViews(view, savedInstanceState);
        if (mBreakInit) {
            return;
        }
        initActions(view);
    }

    /**
     * 中断初始化工作（initViews()、initActions()），用于在init过程中合法性数据验证不通过，终止执行后续逻辑
     */
    protected void breakInit() {
        mBreakInit = true;
    }

    /**
     * 供AsbMvpFragment初始化presenter，不要在子类中重写该方法
     */
    protected void internalInitMvp(Bundle savedInstanceState) {
    }

    @LayoutRes
    protected abstract int getContentViewLayoutId();

    /**
     * 非强制实现，可依靠Kotlin-extension初始化
     */
    protected void bindViews(View parent) {
    }

    protected abstract void initData();

    protected abstract void initViews(View contentView, Bundle savedInstanceState);

    protected abstract void initActions(View contentView);

}
