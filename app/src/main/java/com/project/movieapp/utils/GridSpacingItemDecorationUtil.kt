package com.project.movieapp.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecorationUtil : RecyclerView.ItemDecoration {
    private var mItemOffset: Int
    private var type: String? = null

    constructor(itemOffsetFloat: Float, type: String?) {
        mItemOffset = itemOffsetFloat.toInt()
        this.type = type
    }

    constructor(mItemOffset: Float) {
        this.mItemOffset = mItemOffset.toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (type == null) outRect[mItemOffset, mItemOffset, mItemOffset] = mItemOffset else {
            if (type.equals(GlobalVariable.KEY_ORIENTASI_VERTICAL, ignoreCase = true)) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect[0, mItemOffset, 0] = mItemOffset
                } else {
                    outRect[0, 0, 0] = mItemOffset
                }
            }
            if (type.equals(GlobalVariable.KEY_ORIENTASI_HORIZONTAL, ignoreCase = true)) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect[mItemOffset, 0, mItemOffset] = 0
                } else {
                    outRect[0, 0, mItemOffset] = 0
                }
            }
            if (type.equals(GlobalVariable.KEY_ORIENTASI_GRID, ignoreCase = true)) {
                outRect[mItemOffset, 0, mItemOffset] = 0
            }
        }
    }
}
