package com.zql.frameworks.app.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Liu Hanhong on 15/7/18.
 */
public abstract class RootActivity extends AppCompatActivity {

    private volatile Handler mHandler;

    protected Context getContext() {
        return this;
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

    /**
     * View已经就绪，显示之前，用于处理一些根据现有View的计算以及delay load。
     * @param runnable
     */
    protected void delayLoad(Runnable runnable) {
        getWindow().getDecorView().post(runnable);
    }

}
