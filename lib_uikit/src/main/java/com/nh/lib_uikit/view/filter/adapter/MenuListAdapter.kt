package com.nh.lib_uikit.view.filter.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nh.lib_uikit.R
import com.nh.lib_uikit.view.filter.CustomMenuChildView
import com.nh.lib_uikit.view.filter.model.MenuItemChildModel

class MenuListAdapter(data: MutableList<MenuItemChildModel>?) : BaseMultiItemQuickAdapter<MenuItemChildModel, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.item_menu_title_list)
        addItemType(2, R.layout.item_menu_child_list)
    }

    var currentSelectedItemPosition = -1
    var currentSelectedItemMenuPosition = -1
    var currentSelectedItemMenuChildPosition = -1

    /**当前被选中的位置*/
    var currentSelectedPosition: Int = -1

    override fun convert(holder: BaseViewHolder?, item: MenuItemChildModel?) {
        holder?.apply {
            item?.apply {
                if (itemType == 2) {
                    val mCustomMenuChildChildView = getView<CustomMenuChildView>(R.id.menuChiliRlv)
                    mCustomMenuChildChildView.setNewData(layoutPosition, this@MenuListAdapter)
                    if (isSelected) {
                        mCustomMenuChildChildView.openChildMenu(currentSelectedChildPosition)
                    } else {
                        mCustomMenuChildChildView.closeChildMenu()
                    }
                } else {
                    setText(R.id.itemMenuTvTitle, title)
                }
            }
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