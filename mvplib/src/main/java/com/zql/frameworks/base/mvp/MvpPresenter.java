package com.zql.frameworks.base.mvp;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Liu Hanhong on 15/6/29.
 */
public interface MvpPresenter<V extends MvpView> {
    void addInteractor(Interactor interactor);

    void attachView(V view);

    void detachView();

    void onCreate(Bundle extras, Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onSaveInstance(Bundle outState);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
