package com.nh.lib_uikit.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.nh.lib_uikit.view.DisplayUtil.dpToPx
import java.util.*

/**
 * TODO
 * shige chen on 2020/1/4
 * shigechen@globalsources.com
 */
internal class SpaceItemDecoration : RecyclerView.ItemDecoration {
    private var topMargin: Int
    private var bottomMargin: Int
    private var horizontalSpacing: Int
    private var verticalSpacing: Int
    private var spanCount: Int
    private var isHasHeader = false
    private var isHasFooter = false
    private var isShowEndSpacing = true
    private var currentRowCount = 0

    constructor(horizontalSpacing: Int, verticalSpacing: Int, bottomMargin: Int) : this(horizontalSpacing, verticalSpacing, 0, bottomMargin, 2, false, false)
    constructor(
        horizontalSpacing: Int, verticalSpacing: Int, bottomMargin: Int, isHasHeader: Boolean,
        isHasFooter: Boolean
    ) : this(horizontalSpacing, verticalSpacing, 0, bottomMargin, 2, isHasHeader, isHasFooter) {
    }

    constructor(horizontalSpacing: Int, verticalSpacing: Int, isHasHeader: Boolean, isHasFooter: Boolean) : this(
        horizontalSpacing,
        verticalSpacing,
        0,
        verticalSpacing,
        2,
        isHasHeader,
        isHasFooter
    ) {
    }

    constructor(
        horizontalSpacing: Int, verticalSpacing: Int, topMargin: Int, bottomMargin: Int,
        isHasHeader: Boolean, isHasFooter: Boolean
    ) : this(horizontalSpacing, verticalSpacing, topMargin, bottomMargin, 2, isHasHeader, isHasFooter) {
    }

    constructor(
        horizontalSpacing: Int, verticalSpacing: Int, topMargin: Int = 0, bottomMargin: Int = 0,
        spanCount: Int = 2, isHasHeader: Boolean = false, isHasFooter: Boolean = false
    ) {
        this.horizontalSpacing = horizontalSpacing
        this.verticalSpacing = verticalSpacing
        this.topMargin = topMargin
        this.spanCount = spanCount
        this.bottomMargin = bottomMargin
        this.isHasHeader = isHasHeader
        this.isHasFooter = isHasFooter
    }

    constructor(horizontalSpacing: Int, verticalSpacing: Int, spanCount: Int = 2, isShowEndSpacing: Boolean) {
        this.horizontalSpacing = horizontalSpacing
        this.verticalSpacing = verticalSpacing
        this.topMargin = 0
        this.spanCount = spanCount
        this.bottomMargin = 0
        this.isHasHeader = false
        this.isHasFooter = false
        this.isShowEndSpacing = isShowEndSpacing
    }

    constructor() {
        horizontalSpacing = 10
        verticalSpacing = 10
        bottomMargin = 0
        topMargin = 10
        spanCount = 2
    }

    fun setTopMargin(topMargin: Int): SpaceItemDecoration {
        this.topMargin = topMargin
        return this
    }

    fun setBottomMargin(bottomMargin: Int): SpaceItemDecoration {
        this.bottomMargin = bottomMargin
        return this
    }

    fun setHorizontalSpacing(horizontalSpacing: Int): SpaceItemDecoration {
        this.horizontalSpacing = horizontalSpacing
        return this
    }

    fun setVerticalSpacing(verticalSpacing: Int): SpaceItemDecoration {
        this.verticalSpacing = verticalSpacing
        return this
    }

    fun setSpanCount(spanCount: Int): SpaceItemDecoration {
        this.spanCount = spanCount
        return this
    }

    fun setHasHeader(hasHeader: Boolean): SpaceItemDecoration {
        isHasHeader = hasHeader
        return this
    }

    fun setHasFooter(hasFooter: Boolean): SpaceItemDecoration {
        isHasFooter = hasFooter
        return this
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        var position = parent.getChildLayoutPosition(view)
        if (isHasHeader && parent.getChildLayoutPosition(view) == 0) {
            return
        }
        if (isHasHeader) {
            position -= 1
        }
        var newSpanCount = spanCount
        if (Objects.requireNonNull(parent.adapter).itemCount % 2 != 0) {
            newSpanCount = spanCount - 1
        }
        if (parent.adapter!!.itemCount - position <= newSpanCount) {
            outRect.bottom = dpToPx(verticalSpacing.toFloat())
        } else {
            outRect.bottom = dpToPx(verticalSpacing.toFloat())
        }
        if (position < spanCount) {
            outRect.top = dpToPx(topMargin.toFloat())
        } else {
            outRect.top = 0
        }
        //由于每行都只有spanCount个，所以第一个都是spanCount的倍数，把左边距设为0
        val childCount = parent.adapter?.itemCount ?: 0
        val posi = position % spanCount
        val rowCount: Int = ((childCount / (spanCount * 1.0)) + 0.5).toInt()
        currentRowCount = if (posi == 0) currentRowCount + 1 else currentRowCount
        when {
            posi == 0 -> {
                outRect.left = 0
                outRect.right = dpToPx((horizontalSpacing shr 1).toFloat())
            }
            posi < spanCount - 1 -> {
                outRect.left = dpToPx((horizontalSpacing shr 1).toFloat())
                if (!isShowEndSpacing && currentRowCount == rowCount && posi == spanCount - 2) {
                    outRect.right = 0
                } else {
                    outRect.right = dpToPx((horizontalSpacing shr 1).toFloat())
                }
            }
            else -> {
                outRect.left = dpToPx((horizontalSpacing shr 1).toFloat())
                outRect.right = 0
            }
        }
    }
}