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
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.nh.lib_uikit.R
import com.nh.lib_uikit.view.SpaceItemDecoration
import com.nh.lib_uikit.view.filter.model.CategoryModel

class CustomMenuChildView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        private const val MAX_COUNT = 3
    }

    private val itemMenuChildRlv: RecyclerView by lazy {
        findViewById(R.id.itemMenuChildRlv)
    }
    private val fl: FrameLayout by lazy {
        findViewById(R.id.itemMenuChildChildFl)
    }
    private val itemMenuChildChildRlv: RecyclerView by lazy {
        findViewById(R.id.itemMenuChildChildRlv)
    }

    init {
        inflate(context, R.layout.item_menu_child_child_list, this)
    }

    private var tabClickListener: ((CustomMenuChildView) -> Unit)? = null
    private val mAdapter by lazy {
        CustomMenuChildItemAdapter()
    }
    private val mChildAdapter by lazy {
        CustomMenuChildChildItemAdapter()
    }

    private var selectedPosition = -1
    private var isSelectedState: Boolean = false

    init {
        itemMenuChildRlv.apply {
            layoutManager = GridLayoutManager(context, MAX_COUNT)
            addItemDecoration(
                SpaceItemDecoration(
                    15, 0, 0, 0, MAX_COUNT, false, isHasFooter = false
                )
            )
            isNestedScrollingEnabled = false
            adapter = mAdapter
        }
        itemMenuChildChildRlv.apply {
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(
                SpaceItemDecoration(
                    9, 9, 0, 0, MAX_COUNT, false, isHasFooter = false
                )
            )
            isNestedScrollingEnabled = false
            adapter = mChildAdapter
        }

        mAdapter.setOnItemClickListener { _, _, position ->
            if (selectedPosition != position) {
                tabClickListener?.invoke(this)
                onSelected(position)
            } else {
                onReset()
            }
        }

        mChildAdapter.setOnItemClickListener { adapter, view, position ->
            mChildAdapter.data.forEach {
                it.isSelected = false
            }
            mChildAdapter.data[position].isSelected = true
            mChildAdapter.notifyDataSetChanged()
        }
    }

    fun showMenuChild() {
        fl.visibility = View.VISIBLE
    }

    fun hintMenuChild() {
        fl.visibility = View.GONE
    }

    /**选择指定项*/
    private fun onSelected(position: Int) {
        mAdapter.data.forEach {
            it.isSelected = false
        }
        mChildAdapter.data.forEach {
            it.isSelected = false
        }
        if (fl.visibility == View.GONE) {
            fl.visibility = View.VISIBLE
        }
        mAdapter.data[position].isSelected = selectedPosition != position
        isSelectedState = selectedPosition != position
        selectedPosition = position
        mAdapter.notifyDataSetChanged()
        mChildAdapter.setNewInstance(mAdapter.data[position].subCategory as MutableList<CategoryModel>?)
    }

    /**重置*/
    fun onReset() {
        mAdapter.data.forEach {
            it.isSelected = false
        }
        fl.visibility = View.GONE
        isSelectedState = false
        selectedPosition = -1
        mAdapter.notifyDataSetChanged()
    }

    fun setTabClickListener(tabClickListener: (CustomMenuChildView) -> Unit) {
        this.tabClickListener = tabClickListener
    }

    fun setNewData(list: MutableList<CategoryModel>?) {
        mAdapter.setNewInstance(list)
        list?.forEachIndexed { index, menuItemChildModel ->
            if (menuItemChildModel.isSelected) {
                tabClickListener?.invoke(this)
                onSelected(index)
                return@forEachIndexed
            }
        }
    }

    fun isHasData(): Boolean {
        return mAdapter.data.size > 0
    }

    private class CustomMenuChildItemAdapter : BaseQuickAdapter<CategoryModel, BaseViewHolder>(R.layout.item_menu_child_chili_tab_list) {

        override fun convert(holder: BaseViewHolder, item: CategoryModel) {
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTabBg).isChecked = item.isSelected
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTab).isChecked = item.isSelected
            holder.setImageResource(R.id.itemChildTabImg, if (item.isSelected) R.mipmap.icon_tab_top else R.mipmap.icon_tab_down)
        }
    }

    private class CustomMenuChildChildItemAdapter : BaseQuickAdapter<CategoryModel, BaseViewHolder>(R.layout.item_menu_child_chili_tab_list) {

        override fun convert(holder: BaseViewHolder, item: CategoryModel) {
            holder.setBackgroundResource(R.id.itemChildTabBg, R.drawable.item_menu_child_child_tab_bg)
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTab).setTextColor(ContextCompat.getColor(context, R.color.item_menu_child_child_text_color))
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTabBg).isChecked = item.isSelected
            holder.getView<AppCompatCheckedTextView>(R.id.itemChildTab).isChecked = item.isSelected
            holder.setGone(R.id.itemChildTabImg, true)
        }
    }
}