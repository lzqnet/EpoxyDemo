package com.zql.frameworks.app.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.Fragment;

import com.zql.frameworks.app.context.FragmentContext;

/**
 * Created by Liu Hanhong on 15/7/18.
 */
public abstract class RootFragment extends Fragment {

    private FragmentContext mContext;
    private volatile Handler mHandler;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mContext == null || mContext.getBaseContext() != context) {
            mContext = new FragmentContext(context, this);
        }
    }

    /**
     * Return the {@link FragmentContext} this fragment is currently associated with.
     * Note: this context is NOT Activity.
     */
    public FragmentContext getFragmentContext() {
        return mContext;
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
        if (getActivity() != null) {
            getActivity().getWindow().getDecorView().post(runnable);
        }
    }

    /**
     * Check to see whether this activity is in the process of finishing,
     * either because you called finish on it or someone else
     * has requested that it finished.  This is often used in
     * {@link #onPause} to determine whether the activity is simply pausing or
     * completely finishing.
     *
     * @return If the activity is finishing, returns true; else returns false.
     *
     */
    protected boolean isFinishing() {
        return getActivity() == null || getActivity().isFinishing();
    }
}
