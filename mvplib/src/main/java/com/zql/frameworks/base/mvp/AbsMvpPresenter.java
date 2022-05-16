package com.zql.frameworks.base.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Liu Hanhong on 15/9/25.
 */
public abstract class AbsMvpPresenter<V extends MvpView> implements MvpPresenter<V> {

    private V mMvpView;
    private volatile Handler mHandler;
    private Context mContext;

    @SuppressWarnings("SpellCheckingInspection")
    private List<Interactor> mInteractors = new ArrayList<>();
    private List<LifeCycleReceiver> mLifeCycleReceivers = new ArrayList<>();

    public AbsMvpPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(Bundle extras, Bundle savedInstanceState) {
        if (!mInteractors.isEmpty()) {
            for (Interactor interactor : mInteractors) {
                //noinspection unchecked
                interactor.onCreate(extras, savedInstanceState);
            }
        }
    }

    @Override
    public void addInteractor(Interactor interactor) {
        mInteractors.add(interactor);
        if (interactor instanceof LifeCycleReceiver) {
            mLifeCycleReceivers.add((LifeCycleReceiver) interactor);
        }
        if (hasMvpView()) {
            //noinspection unchecked
            interactor.attachView(getMvpView());
        }
    }

    @Override
    public void attachView(V view) {
        mMvpView = view;
        if (!mInteractors.isEmpty()) {
            for (Interactor interactor : mInteractors) {
                //noinspection unchecked
                interactor.attachView(view);
            }
        }
    }

    @Override
    public void detachView() {
        mMvpView = null;
        if (!mInteractors.isEmpty()) {
            for (Interactor interactor : mInteractors) {
                interactor.detachView();
            }
        }
    }

    @Override
    public void onStart() {
        if (!mLifeCycleReceivers.isEmpty()) {
            for (LifeCycleReceiver receiver : mLifeCycleReceivers) {
                receiver.onStart();
            }
        }
    }

    @Override
    public void onResume() {
        if (!mLifeCycleReceivers.isEmpty()) {
            for (LifeCycleReceiver receiver : mLifeCycleReceivers) {
                receiver.onResume();
            }
        }
    }

    @Override
    public void onPause() {
        if (!mLifeCycleReceivers.isEmpty()) {
            for (LifeCycleReceiver receiver : mLifeCycleReceivers) {
                receiver.onPause();
            }
        }
    }

    @Override
    public void onStop() {
        if (!mLifeCycleReceivers.isEmpty()) {
            for (LifeCycleReceiver receiver : mLifeCycleReceivers) {
                receiver.onStop();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (!mLifeCycleReceivers.isEmpty()) {
            for (LifeCycleReceiver receiver : mLifeCycleReceivers) {
                receiver.onDestroy();
            }
        }
    }

    @Override
    public void onSaveInstance(Bundle outState) {
        if (!mLifeCycleReceivers.isEmpty()) {
            for (LifeCycleReceiver receiver : mLifeCycleReceivers) {
                receiver.onSaveInstance(outState);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mLifeCycleReceivers.isEmpty()) {
            for (LifeCycleReceiver receiver : mLifeCycleReceivers) {
                receiver.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    protected Context getContext() {
        return mContext;
    }

    protected boolean hasMvpView() {
        return mMvpView != null;
    }

    protected V getMvpView() {
        return mMvpView;
    }

    protected Handler getHandler() {
        if (mHandler == null) {
            synchronized (this) {
                if (mHandler == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
            }
        }

        return mHandler;
    }

    protected void runOnUiThread(Runnable runnable) {
        getHandler().post(runnable);
    }

}
