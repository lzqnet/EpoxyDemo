package com.zql.frameworks.base.mvp;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * 共享Interactor，支持多个presenter共用
 * Created by Liu Hanhong on 16/8/10.
 */
public abstract class ShareInteractor<V extends MvpView> extends Interactor<V> {

    private V mCurMvpView;
    private List<V> mMvpViews = new LinkedList<>();

    public ShareInteractor(Context context) {
        super(context);
    }

    @Override
    public void attachView(V view) {
        mCurMvpView = view;
        mMvpViews.add(0, view);
    }

    @Override
    public void detachView() {
        mMvpViews.remove(mCurMvpView);
        if (!mMvpViews.isEmpty()) {
            mCurMvpView = mMvpViews.get(0);
        } else {
            mCurMvpView = null;
        }
    }

    @Override
    protected boolean hasMvpView() {
        return mCurMvpView != null;
    }

    @Override
    protected V getMvpView() {
        return mCurMvpView;
    }

    protected List<V> getAllMvpViews() {
        return mMvpViews;
    }
}
