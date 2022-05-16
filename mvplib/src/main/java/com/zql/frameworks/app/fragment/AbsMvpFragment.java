package com.zql.frameworks.app.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.zql.frameworks.base.mvp.MvpPresenter;
import com.zql.frameworks.base.mvp.MvpView;


/**
 * Created by Liu Hanhong on 15/6/30.
 */
public abstract class AbsMvpFragment<P extends MvpPresenter> extends AbsBaseFragment implements MvpView {

    private P mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mPresenter == null) {
            mPresenter = createPresenter(getActivity());
        }
    }

    @Override
    protected final void internalInitMvp(Bundle savedInstanceState) {
        //noinspection unchecked
        mPresenter.attachView(this);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstance(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    protected P getPresenter() {
        return mPresenter;
    }

    @NonNull
    protected abstract P createPresenter(Context context);

}
