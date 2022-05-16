package com.zql.frameworks.base.mvp;

import android.content.Context;
import android.os.Bundle;

/**
 * Use Case，功能点，亦可当做测试点
 * V 为寄主Presenter的MvpView或其子集View
 * Created by Liu Hanhong on 16/3/19.
 */
public abstract class Interactor<V extends MvpView> {

    private V mMvpView;
    private Context mContext;

    public Interactor(Context context) {
        mContext = context;
    }

    public void onCreate(Bundle extras, Bundle savedInstanceState) {
    }

    public void attachView(V view) {
        mMvpView = view;
    }

    public void detachView() {
        mMvpView = null;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    protected boolean hasMvpView() {
        return mMvpView != null;
    }

    protected V getMvpView() {
        return mMvpView;
    }
}
