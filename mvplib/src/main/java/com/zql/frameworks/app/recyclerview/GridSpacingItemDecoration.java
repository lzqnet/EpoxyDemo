package com.zql.frameworks.app.recyclerview;

import android.graphics.Rect;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * Created by liu.hanhong on 15/12/19.
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private boolean mStartSpacingEnable = false;
    private boolean mEndSpacingEnable = false;

    private boolean mEdgeSpacingEnable;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public GridSpacingItemDecoration(int spacing) {
        setSpacing(spacing);
    }

    public GridSpacingItemDecoration(int horizontalSpacing, int verticalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
        mVerticalSpacing = verticalSpacing;
    }

    public void setSpacing(int spacing) {
        mHorizontalSpacing = mVerticalSpacing = spacing;
    }

    public void setHorizontalSpacing(int spacing) {
        mHorizontalSpacing = spacing;
    }

    public void setVerticalSpacing(int spacing) {
        mVerticalSpacing = spacing;
    }

    /**
     * All spacing(left, right, top, bottom) enable.
     *
     * @param enable
     */
    public void setEdgeSpacingEnable(boolean enable) {
        mEdgeSpacingEnable = enable;
    }

    /**
     * When the orientation is horizontal, it means top spacing. Or the orientation is vertical, it means left spacing.
     *
     * @param enable
     */
    public void setStartSpacingEnable(boolean enable) {
        mStartSpacingEnable = enable;
    }

    /**
     * When the orientation is horizontal, it means bottom spacing. Or the orientation is vertical, it means right spacing.
     *
     * @param enable
     */
    public void setEndSpacingEnable(boolean enable) {
        mEndSpacingEnable = enable;
    }

//    @Override
//    public void onDraw(Canvas c, RecyclerView parent, State state) {
////        drawHorizontal(c, parent);
////        drawVertical(c, parent);
//
//    }
//
//    public void drawHorizontal(Canvas c, RecyclerView parent) {
//        int childCount = parent.getChildCount();
//        int spanCount = getSpanCount(parent);
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//            final int left = child.getLeft();
//            final int right = child.getRight() + mHorizontalSpacing;
//
//            if ((mTopSpacingEnable || mEdgeSpacingEnable) && i < spanCount) {//Draw top spacing
//                final int bottom = child.getTop();
//                final int top = bottom - mVerticalSpacing;
//                mDividerDrawable.setBounds(left, top, right, bottom);
//                mDividerDrawable.draw(c);
//            }
//
//            boolean isLastRow = isLastRow(parent, i, spanCount, parent.getAdapter().getItemCount());
//            if ((mEndSpacingEnable || mEdgeSpacingEnable) && isLastRow) {
//                final int top = child.getBottom();
//                final int bottom = top + mVerticalSpacing;
//                mDividerDrawable.setBounds(left, top, right, bottom);
//                mDividerDrawable.draw(c);
//            }
//
//            if (i >= spanCount) {
//                final int bottom = child.getTop();
//                final int top = bottom - mVerticalSpacing;
//                mDividerDrawable.setBounds(left, top, right, bottom);
//                mDividerDrawable.draw(c);
//            }
//        }
//    }
//
//    public void drawVertical(Canvas c, RecyclerView parent) {
//        final int childCount = parent.getChildCount();
//        int spanCount = getSpanCount(parent);
//        for (int i = 0; i < childCount; i++) {
//            final View child = parent.getChildAt(i);
//
//            final int top = child.getTop();
//            final int bottom = child.getBottom();
//
//            if (mEdgeSpacingEnable && i < spanCount) {//Left spacing
//                final int right = child.getLeft();
//                final int left = right - mVerticalSpacing;
//                mDividerDrawable.setBounds(left, top, right, bottom);
//                mDividerDrawable.draw(c);
//            }
//
//            if (mEdgeSpacingEnable && isLastColum(parent, i, spanCount, parent.getAdapter().getItemCount())) {//Right spacing
//                final int left = child.getRight();
//                final int right = left + mHorizontalSpacing;
//                mDividerDrawable.setBounds(left, top, right, bottom);
//                mDividerDrawable.draw(c);
//            }
//
//            if (i >= spanCount) {
//                final int right = child.getLeft();
//                final int left = right - mVerticalSpacing;
//                mDividerDrawable.setBounds(left, top, right, bottom);
//                mDividerDrawable.draw(c);
//            }
//        }
//    }
//

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int spanCount = getSpanCount(parent);
        int column = position % spanCount; // item column

        if (getOrientation(parent) == OrientationHelper.VERTICAL) {
            if (mEdgeSpacingEnable) {
                outRect.left = mHorizontalSpacing - column * mHorizontalSpacing / spanCount; // mSpacing - column * ((1f / spanCount) * mSpacing)
                outRect.right = (column + 1) * mHorizontalSpacing / spanCount; // (column + 1) * ((1f / spanCount) * mSpacing)

                if (position < spanCount) { // top edge
                    outRect.top = mVerticalSpacing;
                }
                outRect.bottom = mVerticalSpacing; // item bottom
            } else {
                outRect.left = column * mHorizontalSpacing / spanCount; // column * ((1f / spanCount) * mSpacing)
                outRect.right = mHorizontalSpacing - (column + 1) * mHorizontalSpacing / spanCount; // mSpacing - (column + 1) * ((1f / spanCount) * mSpacing)

                if (mStartSpacingEnable && position < spanCount) {
                    outRect.top = mVerticalSpacing; // item top
                }

                if (mEndSpacingEnable && isLastRow(parent, position, spanCount, parent.getAdapter().getItemCount())) {
                    outRect.bottom = mVerticalSpacing;
                }

                if (position >= spanCount) {
                    outRect.top = mVerticalSpacing; // item top
                }
            }
        } else {
            if (mEdgeSpacingEnable) {

                outRect.top = mVerticalSpacing - column * mVerticalSpacing / spanCount; // mSpacing - column * ((1f / spanCount) * mSpacing)
                outRect.bottom = (column + 1) * mVerticalSpacing / spanCount; // (column + 1) * ((1f / spanCount) * mSpacing)

                if (position < spanCount) { // top edge
                    outRect.left = mHorizontalSpacing;
                }
                outRect.right = mHorizontalSpacing; // item bottom
            } else {
                outRect.top = column * mVerticalSpacing / spanCount; // column * ((1f / spanCount) * mSpacing)
                outRect.bottom = mVerticalSpacing - (column + 1) * mVerticalSpacing / spanCount; // mSpacing - (column + 1) * ((1f /    spanCount) * mSpacing)

                if (mStartSpacingEnable && position < spanCount) {
                    outRect.left = mVerticalSpacing; // item top
                }

                if (mEndSpacingEnable && isLastRow(parent, position, spanCount, parent.getAdapter().getItemCount())) { //Last column
                    outRect.right = mVerticalSpacing;
                }

                if (position >= spanCount) {
                    outRect.left = mHorizontalSpacing; // item top
                }
            }
        }

    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    private int getOrientation(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }

        return OrientationHelper.VERTICAL;
    }

    private boolean isLastRow(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int remainder = childCount % spanCount;
            childCount = childCount - (remainder == 0 ? spanCount : remainder);
            // 如果是最后一行，则不需要绘制底部
            if (pos >= childCount) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount) {
                    return true;
                }
            } else { // StaggeredGridLayoutManager 且横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 如果是最后一列，则不需要绘制右边
            if ((pos + 1) % spanCount == 0) {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                if ((pos + 1) % spanCount == 0) {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一列，则不需要绘制右边
                if (pos >= childCount) {
                    return true;
                }
            }
        }
        return false;
    }
}
