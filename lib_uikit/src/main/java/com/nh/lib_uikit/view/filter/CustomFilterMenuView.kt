package com.nh.lib_uikit.view.filter

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
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

    private var newList: MutableList<MenuItemChildModel>? = null
    private val menuRlv: RecyclerView by lazy {
        findViewById(R.id.menuRlv)
    }
    private val mAdapter by lazy {
        MenuListAdapter(newList)
    }

    init {
        inflate(context, R.layout.view_custom_filter_menu, this)
        setBackgroundColor(Color.parseColor("#55000000"))

        val layoutManager = LinearLayoutManager(context)
        menuRlv.layoutManager = layoutManager
        menuRlv.adapter = mAdapter
        menuRlv.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        setPadding(0, 0, 0, 0)

        mAdapter.setOnOPenListener(object : MenuListAdapter.OnOpenListener {
            override fun onOpen(position: Int) {
                moveToPosition(layoutManager, menuRlv, position)
            }
        })

        setOnClickListener {
            hintMenu()
            mAdapter.getDismissBlock()?.invoke()
        }
    }

    fun showMenu() {
        visibility = View.VISIBLE
        refreshData()
    }

    fun hintMenu() {
        visibility = View.GONE
    }

    fun setOnDismissListener(block: (() -> Unit)?) {
        mAdapter.setOnDismissListener(block)
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
            mAdapter.apply {
                if (currentSelectedItemPosition >= 0 && data.size > 0) {
                    currentSelectedPosition = currentSelectedItemPosition
                    data[currentSelectedItemPosition].apply {
                        isSelected = true
                        if (currentSelectedItemMenuPosition >= 0 && menuList?.size ?: 0 > 0) {
                            currentSelectedChildPosition = currentSelectedItemMenuPosition
                            menuList?.get(currentSelectedItemMenuPosition)?.apply {
                                isSelected = true
                                if (currentSelectedItemMenuChildPosition >= 0 && subCategory?.size ?: 0 > 0) {
                                    subCategory?.get(currentSelectedItemMenuChildPosition)?.apply {
                                        isSelected = true
                                    }
                                }
                            }
                        }
                    }
                    notifyItemChanged(currentSelectedItemPosition)
                }
            }
        }
        super.onVisibilityChanged(changedView, visibility)
        mAdapter.apply {
            if (visibility == View.VISIBLE && currentSelectedItemPosition >= 0) {
                getOpenListener()?.onOpen(currentSelectedItemPosition)
            }
        }
    }

    private fun refreshData() {
        mAdapter.notifyDataSetChanged()
    }

    fun setNewData(list: List<CategoryModel>?) {
        newList?.clear()
        newList = mutableListOf()
        ListUtil.averageAssignFixLength(list, 3).forEach {
            newList?.add(MenuItemChildModel().apply {
                menuList = it as MutableList<CategoryModel>?
            })
        }
        newList?.forEachIndexed { itemIndex, menuItemChildModel ->
            menuItemChildModel.menuList?.forEachIndexed { childIndex, categoryModel ->
                if (categoryModel.isSelected) {
                    menuItemChildModel.isSelected = true
                    mAdapter.currentSelectedPosition = itemIndex
                    mAdapter.currentSelectedItemPosition = itemIndex
                    mAdapter.currentSelectedItemMenuPosition = childIndex
                    categoryModel.subCategory?.forEachIndexed { childchildIndex, categoryModel ->
                        if (categoryModel.isSelected) {
                            mAdapter.currentSelectedItemMenuChildPosition = childchildIndex
                        }
                    }
                }
            }
        }
        mAdapter.setNewData(newList)
    }

    /**
     * RecyclerView ????????????????????????
     *
     * @param manager   ??????RecyclerView?????????manager
     * @param mRecyclerView  ?????????RecyclerView
     * @param n  ??????????????????
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