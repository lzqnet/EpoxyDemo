package com.zql.frameworks.app.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.zql.frameworks.base.mvp.MvpPresenter;
import com.zql.frameworks.base.mvp.MvpView;


/**
 * Created by Liu Hanhong on 15/6/29.
 */
public abstract class AbsMvpActivity<P extends MvpPresenter> extends AbsBaseActivity implements MvpView {

    private P mPresenter;

    @Override
    protected final void initPresenter() {
        if (mPresenter == null) {
            mPresenter = createPresenter(this);
        }
    }

    @Override
    protected final void callPresenterOnCreate(Bundle savedInstanceState) {
        if (mPresenter == null) { //再次检查
            mPresenter = createPresenter(this);
        }
        //noinspection unchecked
        mPresenter.attachView(this);
        mPresenter.onCreate(getIntent().getExtras(), savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstance(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    protected P getPresenter() {
        return mPresenter;
    }

    @NonNull
    protected abstract P createPresenter(Context context);
}
