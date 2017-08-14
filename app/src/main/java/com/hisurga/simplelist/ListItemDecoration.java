package com.hisurga.simplelist;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class ListItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]
            {
                    android.R.attr.listDivider
            };

    private Drawable listDivider;

    public ListItemDecoration(Context context) {
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        listDivider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        drawVertical(canvas, parent);
    }

    public void drawVertical(Canvas canvas, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + listDivider.getIntrinsicHeight();
            listDivider.setBounds(left, top, right, bottom);
            listDivider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, listDivider.getIntrinsicHeight());
    }
}
