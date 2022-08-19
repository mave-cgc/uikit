package com.nh.lib_uikit.view.filter

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nh.lib_uikit.R
import com.nh.lib_uikit.view.ListUtil
import com.nh.lib_uikit.view.filter.adapter.MenuListAdapter
import com.nh.lib_uikit.view.filter.model.CategoryModel
import com.nh.lib_uikit.view.filter.model.MenuItemChildModel

class CustomFilterMenuView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        internal var currentSelectedItemPosition = -1
        internal var currentSelectedItemMenuPosition = -1
        internal var currentSelectedItemMenuChildPosition = -1
    }


    private val menuRlv: RecyclerView by lazy {
        findViewById(R.id.menuRlv)
    }
    private val mAdapter by lazy {
        MenuListAdapter()
    }

    init {
        inflate(context, R.layout.view_custom_filter_menu, this)
        setBackgroundColor(Color.parseColor("#55000000"))

        val layoutManager = LinearLayoutManager(context)
        menuRlv.layoutManager = layoutManager
        menuRlv.adapter = mAdapter

        mAdapter.setOnOPenListener(object : MenuListAdapter.OnOpenListener {
            override fun onOpen(position: Int) {
                moveToPosition(layoutManager, menuRlv, position)
            }
        })
    }

    fun showMenu() {
        visibility = View.VISIBLE
        refreshData()
    }

    fun hintMenu() {
        visibility = View.GONE
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mAdapter.currentSelectedPosition = -1
            mAdapter.data.forEach { item ->
                item.currentSelectedChildPosition = -1
                item.isSelected = false
                item.menuList?.forEach { menu ->
                    menu.isSelected = false
                    menu.subCategory?.forEach { menuChild ->
                        menuChild.isSelected = false
                    }
                }
            }
            mAdapter.notifyDataSetChanged()
        } else {
            if (currentSelectedItemPosition >= 0) {
                mAdapter.currentSelectedPosition = currentSelectedItemPosition
                mAdapter.data[currentSelectedItemPosition].apply {
                    isSelected = true
                    if (currentSelectedItemMenuPosition >= 0) {
                        currentSelectedChildPosition = currentSelectedItemMenuPosition
                        menuList?.get(currentSelectedItemMenuPosition)?.apply {
                            isSelected = true
                            if (currentSelectedItemMenuChildPosition >= 0) {
                                subCategory?.get(currentSelectedItemMenuChildPosition)?.apply {
                                    isSelected = true
                                }
                            }
                        }
                    }
                }
                mAdapter.notifyItemChanged(currentSelectedItemPosition)
            }
        }
        super.onVisibilityChanged(changedView, visibility)
        if (visibility == View.VISIBLE && currentSelectedItemPosition >= 0) {
            mAdapter.getOpenListener()?.onOpen(currentSelectedItemPosition)
        }
    }

    private fun refreshData() {
        mAdapter.notifyDataSetChanged()
    }

    fun setNewData(list: MutableList<CategoryModel>?) {
        val newList = mutableListOf<MenuItemChildModel>()
        ListUtil.averageAssignFixLength(list, 3).forEach {
            newList.add(MenuItemChildModel().apply {
                menuList = it as MutableList<CategoryModel>?
            })
        }
        mAdapter.setNewInstance(newList)
    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager   设置RecyclerView对应的manager
     * @param mRecyclerView  当前的RecyclerView
     * @param n  要跳转的位置
     */
    fun moveToPosition(manager: LinearLayoutManager, mRecyclerView: RecyclerView, n: Int) {
        val firstItem = manager.findFirstVisibleItemPosition()
        val lastItem = manager.findLastVisibleItemPosition()
        when {
            n <= firstItem -> {
                mRecyclerView.scrollToPosition(n)
            }
            n <= lastItem -> {
                val top = mRecyclerView.getChildAt(n - firstItem).top
                mRecyclerView.scrollBy(0, top)
            }
            else -> {
                mRecyclerView.scrollToPosition(n)
            }
        }
    }

    fun setSelectedTabListener(selectedTabListener: ((String, String, String) -> Unit)?) {
        mAdapter.setSelectedTabListener(selectedTabListener)
    }
}