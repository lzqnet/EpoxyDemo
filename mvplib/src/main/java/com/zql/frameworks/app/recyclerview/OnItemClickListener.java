package com.zql.frameworks.app.recyclerview;

import android.view.View;

/**
 * Created by apple on 15/11/12.
 */
public interface OnItemClickListener<T> {
    void onItemClick(View view, T t, int position);
}
