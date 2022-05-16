package com.zql.frameworks.app.context;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import androidx.fragment.app.Fragment;

/**
 * Context wrapper for fragment.
 * Created by Liu Hanhong on 16/9/14.
 */

public class FragmentContext extends ContextWrapper {

    private Fragment mFragment;

    public FragmentContext(Context base, Fragment fragment) {
        super(base);
        mFragment = fragment;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, requestCode);
        } else {
            startActivity(intent);
        }
    }
}
