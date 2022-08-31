package com.nh.lib_uikit.view.filter.model

import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity

class MenuItemChildModel : MultiItemEntity {

    var title: String? = null
    var menuList: MutableList<CategoryModel>? = null
    var isSelected: Boolean = false
    var currentSelectedChildPosition: Int = -1

    override fun getItemType(): Int {
        return if (TextUtils.isEmpty(title)) 2 else 1
    }
}