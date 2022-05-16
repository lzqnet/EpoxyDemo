package com.zql.frameworks.app.recyclerview;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * This class is from the v7 samples of the Android SDK. It's not by me!
 * <p/>
 * See the license above for details.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
        android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private int mDividerHeight;
    private Drawable mDivider;
    private boolean mDrawTopDivider = false;
    private boolean mDrawBottomDivider = true;
    private int mTopDividerHeight;
    private int mBottomDividerHeight;

    private int mOrientation;

    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        if (mDivider != null) {
            mDividerHeight = mDivider.getIntrinsicHeight();
        }
        a.recycle();
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public void setDivider(Drawable divider) {
        mDivider = divider;
        if (mDivider.getIntrinsicHeight() > 0) {
            mDividerHeight = mDivider.getIntrinsicHeight();
        }
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerHeight = dividerHeight;
    }

    public void setDrawTopDivider(boolean drawTopDivider) {
        mDrawTopDivider = drawTopDivider;
    }

    public void setDrawBottomDivider(boolean drawBottomDivider) {
        mDrawBottomDivider = drawBottomDivider;
    }

    public void setTopDividerHeight(int dividerTop) {
        this.mTopDividerHeight = dividerTop;
    }

    public void setBottomDivideHeightr(int dividerBottom) {
        this.mBottomDividerHeight = dividerBottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            if (i == 0 && mDrawTopDivider) {
                final int bottom = child.getTop() + params.topMargin;
                final int top = bottom - mDividerHeight;
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }

            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            int position = parent.getChildAdapterPosition(view);
            if (position == 0) {
                outRect.set(0, mDrawTopDivider ? mTopDividerHeight == 0 ? mDividerHeight : mTopDividerHeight : 0, 0, mDividerHeight);
            } else if (position == state.getItemCount() - 1) {
                outRect.set(0, 0, 0, mDrawBottomDivider ? mBottomDividerHeight == 0 ? mDividerHeight : mBottomDividerHeight : 0);
            } else {
                outRect.set(0, 0, 0, mDividerHeight);
            }

        } else {
            outRect.set(0, 0, mDividerHeight, 0);
        }
    }
}
