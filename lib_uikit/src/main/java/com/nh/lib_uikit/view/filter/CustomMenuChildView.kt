package com.nh.lib_uikit.view.filter

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatCheckedTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nh.lib_uikit.R
import com.nh.lib_uikit.view.SpaceItemDecoration
import com.nh.lib_uikit.view.filter.adapter.MenuListAdapter
import com.nh.lib_uikit.view.filter.model.CategoryModel

class CustomMenuChildView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var currentSelectedItemMenuPosition = -1
    var currentSelectedItemMenuChildPosition = -1

    companion object {
        private const val MAX_COUNT = 3
    }

    private var mMenuListAdapter: MenuListAdapter? = null
    private var mMenuItemPosition: Int = -1

    private val itemMenuChildRlv: RecyclerView by lazy {
        findViewById(R.id.itemMenuChildRlv)
    }
    private val fl: FrameLayout by lazy {
        findViewById(R.id.itemMenuChildChildFl)
    }
    private val itemMenuChildChildRlv: RecyclerView by lazy {
        findViewById(R.id.itemMenuChildChildRlv)
    }
    private val mAdapter by lazy {
        CustomMenuChildItemAdapter()
    }
    private val mChildAdapter by lazy {
        CustomMenuChildChildItemAdapter()
    }

    init {
        inflate(context, R.layout.item_menu_child_child_list, this)
        initAdapter()
    }

    private var tabStateClickListener: ((Int, Boolean) -> Unit)? = null
    private var selectedTabListener: ((String, String) -> Unit)? = null

    private fun showMenuChild() {
        if (fl.visibility == View.GONE) {
            fl.visibility = View.VISIBLE
        }
    }

    private fun hintMenuChild() {
        if (fl.visibility == View.VISIBLE) {
            fl.visibility = View.GONE
        }
    }


    private fun initAdapter() {
        itemMenuChildRlv.apply {
            layoutManager = GridLayoutManager(context, MAX_COUNT)
            if (itemDecorationCount <= 0) {
                addItemDecoration(
                    SpaceItemDecoration(
                        15, 0, 0, 0, MAX_COUNT, false, isHasFooter = false
                    )
                )
            }
            isNestedScrollingEnabled = false
            adapter = mAdapter
        }
        itemMenuChildChildRlv.apply {
            layoutManager = GridLayoutManager(context, 3)
            if (itemDecorationCount <= 0) {
                addItemDecoration(
                    SpaceItemDecoration(9, 9, 0, 0, MAX_COUNT, false, isHasFooter = false)
                )
            }
            isNestedScrollingEnabled = false
            adapter = mChildAdapter
        }
        mAdapter.setOnItemClickListener { _, _, position ->
            onSelected(position)
        }

        mChildAdapter.setOnItemClickListener { _, _, position ->
            mChildAdapter.data.forEach {
                it.isSelected = false
            }
            mChildAdapter.data[position].apply {
                isSelected = true
                //记录临时选择的菜单子项
                currentSelectedItemMenuChildPosition = position
                onClickChildMenuTab(this)
            }
            mChildAdapter.notifyDataSetChanged()
        }
    }

    /**选择菜单项*/
    private fun onSelected(position: Int) {
        if (position < 0) {
            return
        }
        val clickCategoryModel = mAdapter.data[position]
        if (clickCategoryModel.isSelected) {
            if (clickCategoryModel.subCategory?.isNotEmpty() == true) {
                mAdapter.data[position].isSelected = clickCategoryModel.subCategory.filter { it.isSelected }.isNotEmpty()
            }
        } else {
            mAdapter.data.forEach {
                it.isSelected = false
            }
            mAdapter.data[position].isSelected = true
        }
        val isSelected = mAdapter.data[position].isSelected
        mMenuListAdapter?.apply {
            data[mMenuItemPosition].isSelected = isSelected
            data[mMenuItemPosition].currentSelectedChildPosition = if (isSelected) position else -1
            if (currentSelectedPosition >= 0 && currentSelectedPosition != mMenuItemPosition) {
                if (data[currentSelectedPosition].isSelected) {
                    data[currentSelectedPosition].isSelected = false
                    notifyItemChanged(currentSelectedPosition)
                }
            }
            mAdapter.data[position].isOnClick = true
            currentSelectedPosition = mMenuItemPosition
            notifyItemChanged(mMenuItemPosition)
        }
        //如果无子项就直接选中菜单项
        if (mAdapter.data[position].subCategory?.isEmpty() == true) {
            //记录临时选择的菜单项
            currentSelectedItemMenuPosition = position
            postDelayed({ onClickChildMenuTab(clickCategoryModel) }, 300)
        }
    }

    private fun onClickChildMenuTab(mCategoryModel: CategoryModel) {
        mCategoryModel.let { model ->
            CustomFilterMenuView.currentSelectedItemPosition = mMenuItemPosition
            CustomFilterMenuView.currentSelectedItemMenuPosition = currentSelectedItemMenuPosition
            CustomFilterMenuView.currentSelectedItemMenuChildPosition = currentSelectedItemMenuChildPosition
            mMenuListAdapter?.selectedTabListener()?.invoke(model.searchSubType ?: "", model.searchType ?: "", model.title ?: "")
        }
    }

    fun openChildMenu(itemMenuChildPosition: Int) {
        if (itemMenuChildPosition < 0) {
            return
        }
        if (mAdapter.data[itemMenuChildPosition].subCategory?.isNotEmpty() == true) {
            showMenuChild()
            //记录临时选择的菜单项
            currentSelectedItemMenuPosition = itemMenuChildPosition
            mChildAdapter.setNewData(mAdapter.data[itemMenuChildPosition].subCategory as MutableList<CategoryModel>?)
            if (mAdapter.data[itemMenuChildPosition].isOnClick && mMenuItemPosition >= 0) {
                mMenuListAdapter?.getOpenListener()?.onOpen(mMenuItemPosition)
                mAdapter.data[itemMenuChildPosition].isOnClick = false
            }
        } else {
            hintMenuChild()
            mChildAdapter.setNewData(null)
        }
        mAdapter.notifyDataSetChanged()
    }

    fun closeChildMenu() {
        hintMenuChild()
        mAdapter.data.forEach {
            it.isSelected = false
        }
        currentSelectedItemMenuPosition = mMenuListAdapter?.data?.get(mMenuItemPosition)?.currentSelectedChildPosition ?: -1
        mAdapter.notifyDataSetChanged()
    }

    fun setSelectedStateListener(tabStateClickListener: (Int, Boolean) -> Unit) {
        this.tabStateClickListener = tabStateClickListener
    }

    fun setSelectedTabListener(selectedTabListener: ((String, String) -> Unit)?) {
        this.selectedTabListener = selectedTabListener
    }

    fun setNewData(itemMenuPosition: Int, mMenuListAdapter: MenuListAdapter) {
        this.mMenuListAdapter = mMenuListAdapter
        this.mMenuItemPosition = itemMenuPosition
        if (itemMenuPosition >= 0 && itemMenuPosition < mMenuListAdapter.data.size) {
            mAdapter.setNewData(mMenuListAdapter.data[itemMenuPosition].menuList)
        }
    }

    private class CustomMenuChildItemAdapter :
        BaseQuickAdapter<CategoryModel, BaseViewHolder>(R.layout.item_menu_child_chili_tab_list) {

        override fun convert(holder: BaseViewHolder, item: CategoryModel) {
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTabBg).isChecked = item.isSelected
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTab).isChecked = item.isSelected
            holder.setImageResource(R.id.itemChildTabImg, if (item.isSelected) R.mipmap.icon_tab_top else R.mipmap.icon_tab_down)

            holder.setVisible(R.id.itemChildTabImg, item.subCategory?.isNotEmpty() == true)
            holder.setText(R.id.itemChildTab, item.title)
        }
    }

    private class CustomMenuChildChildItemAdapter : BaseQuickAdapter<CategoryModel, BaseViewHolder>(R.layout.item_menu_child_chili_tab_list) {

        override fun convert(holder: BaseViewHolder, item: CategoryModel) {
            holder.setBackgroundRes(R.id.itemChildTabBg, R.drawable.item_menu_child_child_tab_bg)
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTab).setTextColor(ContextCompat.getColor(mContext, R.color.item_menu_child_child_text_color))
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTabBg).isChecked = item.isSelected
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTab).isChecked = item.isSelected
            holder.setGone(R.id.itemChildTabImg, true)

            holder.setText(R.id.itemChildTab, item.title)
        }
    }
}