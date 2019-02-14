package com.beblue.gfpf.test.bebluegfpftest.user.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CardItemDecoration extends RecyclerView.ItemDecoration {
    private int mLargePadding;
    private int mSmallPadding;

    public CardItemDecoration(int largePadding, int smallPadding) {
        mLargePadding = largePadding;
        mSmallPadding = smallPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = mSmallPadding;
        outRect.bottom = mSmallPadding;
        outRect.left = mSmallPadding;
        outRect.right = mSmallPadding;
    }
}
