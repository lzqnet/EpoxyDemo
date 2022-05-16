package com.zql.frameworks.base.mvp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu Hanhong on 16/3/19.
 */
public abstract class AbsMvpLoadingPresenter<V extends MvpLoadingView> extends AbsMvpPresenter<V> {

    @SuppressWarnings("SpellCheckingInspection")
    private List<LoadingInteractor> mLoadingInteractors = new ArrayList<>();

    public AbsMvpLoadingPresenter(Context context) {
        super(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (LoadingInteractor interactor : mLoadingInteractors) {
            interactor.cancelRequest();
        }
        cancelRequest();
    }

    @Override
    public void addInteractor(Interactor interactor) {
        super.addInteractor(interactor);
        if (interactor instanceof LoadingInteractor) {
            mLoadingInteractors.add((LoadingInteractor) interactor);
        }
    }

    public abstract void cancelRequest();
}
