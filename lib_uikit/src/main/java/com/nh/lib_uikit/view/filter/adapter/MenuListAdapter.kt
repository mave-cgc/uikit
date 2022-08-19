package com.nh.lib_uikit.view.filter.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.nh.lib_uikit.R
import com.nh.lib_uikit.view.filter.CustomMenuChildView
import com.nh.lib_uikit.view.filter.model.MenuItemChildModel

class MenuListAdapter : BaseMultiItemQuickAdapter<MenuItemChildModel, BaseViewHolder>() {
    init {
        addItemType(1, R.layout.item_menu_title_list)
        addItemType(2, R.layout.item_menu_child_list)
    }

    /**当前被选中的位置*/
    var currentSelectedPosition: Int = -1

    override fun convert(holder: BaseViewHolder, item: MenuItemChildModel) {
        if (item.itemType == 2) {
            val mCustomMenuChildChildView = holder.getView<CustomMenuChildView>(R.id.menuChiliRlv)
            mCustomMenuChildChildView.setNewData(holder.layoutPosition, this)
            if (item.isSelected) {
                mCustomMenuChildChildView.openChildMenu(item.currentSelectedChildPosition)
            } else {
                mCustomMenuChildChildView.closeChildMenu()
            }
        } else {
            holder.setText(R.id.itemMenuTvTitle, item.title)
        }
    }

    private var mOnOpenListener: OnOpenListener? = null
    private var selectedTabListener: ((String, String, String) -> Unit)? = null

    fun getOpenListener(): OnOpenListener? {
        return mOnOpenListener
    }

    fun selectedTabListener(): ((String, String, String) -> Unit)? {
        return selectedTabListener
    }

    fun setOnOPenListener(listener: OnOpenListener) {
        this.mOnOpenListener = listener
    }

    fun setSelectedTabListener(selectedTabListener: ((String, String, String) -> Unit)?) {
        this.selectedTabListener = selectedTabListener
    }

    interface OnOpenListener {
        fun onOpen(position: Int)
    }
}