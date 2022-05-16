package com.zql.frameworks.base.mvp;

import android.content.Context;

/**
 * Created by Liu Hanhong on 16/3/19.
 */
public abstract class LoadingInteractor<V extends MvpLoadingView> extends Interactor<V> {

    public LoadingInteractor(Context context) {
        super(context);
    }

    public abstract void cancelRequest();
}
