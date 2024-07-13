package com.beblue.gfpf.test.bebluegfpftest.presentation.user.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class UserCardItemDecoration(private val mLargePadding: Int, private val mSmallPadding: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = mSmallPadding
        outRect.bottom = mSmallPadding
        outRect.left = mSmallPadding
        outRect.right = mSmallPadding
    }
}
