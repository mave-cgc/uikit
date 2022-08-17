package com.nh.lib_uikit.view.filter

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nh.lib_uikit.R
import com.nh.lib_uikit.view.filter.adapter.MenuListAdapter
import com.nh.lib_uikit.view.filter.model.CategoryModel

class CustomFilterMenuView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val menuRlv: RecyclerView by lazy {
        findViewById(R.id.menuRlv)
    }
    private val mAdapter by lazy {
        MenuListAdapter()
    }

    init {
        inflate(context, R.layout.view_custom_filter_menu, this)
        setBackgroundColor(Color.parseColor("#55000000"))

        menuRlv.layoutManager = LinearLayoutManager(context)
        menuRlv.adapter = mAdapter
    }

    fun showMenu() {
        visibility = View.VISIBLE
    }

    fun hintMenu() {
        visibility = View.GONE
    }

    fun setNewData(list: MutableList<CategoryModel>?) {
        mAdapter.setNewInstance(list)
    }
}