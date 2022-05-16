package com.zql.frameworks.app.activity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.zql.appframework.R;

/**
 * Created by Liu Hanhong on 15/6/29.
 */
public abstract class AbsBaseActivity extends RootActivity {

    private boolean mBreakInit;

    /**
     * super.onCreate必须在initPresenter之后调用，防止Fragment在onCreate的时候使用Presenter，Presenter未初始化之前造成NullPointerException.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initPresenter();
        //super.onCreate必须在initPresenter之后调用，防止Fragment在onCreate的时候使用Presenter，Presenter未初始化之前造成NullPointerException.
        super.onCreate(savedInstanceState);
        View contentView = onCreateContentView(createContentView());
        //noinspection ConstantConditions
        if (contentView != null && contentView.getId() == View.NO_ID) {
            contentView.setId(R.id.content_view_wrapper);
        }
        super.setContentView(contentView);
        mBreakInit = false;
        bindViews();
        callPresenterOnCreate(savedInstanceState);

        if (mBreakInit) {
            return;
        }
        initData();
        if (mBreakInit) {
            return;
        }
        initViews();
        if (mBreakInit) {
            return;
        }
        initActions();
    }

    protected View createContentView() {
        return LayoutInflater.from(this).inflate(getContentViewLayoutId(), null);
    }

    /**
     * 提供给AbsMvpActivity初始化presenter.
     */
    protected abstract void initPresenter();

    /**
     * 中断初始化工作（initViews()、initActions()），用于在init过程中合法性数据验证不通过，终止执行后续逻辑
     */
    protected void breakInit() {
        mBreakInit = true;
    }

    /**
     * 供AsbMvpActivity初始化presenter，不要在子类中重写该方法
     */
    protected void callPresenterOnCreate(Bundle savedInstanceState) {
    }

    @Deprecated
    @Override
    public void setContentView(int layoutResID) {
        throw new RuntimeException("You should init content view by getContentViewLayoutId()");
    }

    @Deprecated
    @Override
    public void setContentView(View view) {
        throw new RuntimeException("You should init content view by getContentViewLayoutId()");
    }

    @Deprecated
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        throw new RuntimeException("You should init content view by getContentViewLayoutId()");
    }

    /**
     * Customize content view.
     *
     * @return the view will be set to content view;
     */
    @NonNull
    protected View onCreateContentView(View contentView) {
        return contentView;
    }

    @LayoutRes
    protected abstract int getContentViewLayoutId();

    /**
     * 非强制实现，可依靠Kotlin-extension初始化
     */
    protected void bindViews() {
    }

    protected abstract void initData();

    protected abstract void initViews();

    protected abstract void initActions();

}
