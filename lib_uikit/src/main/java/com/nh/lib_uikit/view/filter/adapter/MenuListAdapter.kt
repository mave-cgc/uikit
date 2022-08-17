package com.nh.lib_uikit.view.filter.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.nh.lib_uikit.R
import com.nh.lib_uikit.view.filter.CustomMenuChildView
import com.nh.lib_uikit.view.filter.model.CategoryModel

class MenuListAdapter : BaseMultiItemQuickAdapter<CategoryModel, BaseViewHolder>() {
    init {
        addItemType(1, R.layout.item_menu_title_list)
        addItemType(2, R.layout.item_menu_child_list)
    }

    private val selectedMenuViewList = mutableListOf<CustomMenuChildView>()

    override fun convert(holder: BaseViewHolder, item: CategoryModel) {
        if (item.itemType == 2) {
            val mCustomMenuChildChildView = holder.getView<CustomMenuChildView>(R.id.menuChiliRlv)
            mCustomMenuChildChildView.setTabClickListener {
                if (it !in selectedMenuViewList) {
                    onResetAll()
                    selectedMenuViewList.add(it)
                }
            }
            if (!mCustomMenuChildChildView.isHasData()) {
                mCustomMenuChildChildView.setNewData(item.subCategory as MutableList<CategoryModel>?)
            }
        } else {
            holder.setText(R.id.itemMenuTvTitle, item.title)
        }
    }


    private fun onResetAll() {
        selectedMenuViewList.forEach {
            it.onReset()
        }
        selectedMenuViewList.clear()
    }
}