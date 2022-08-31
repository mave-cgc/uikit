package com.nh.lib_uikit.view.filter.model

import android.text.TextUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName

/**筛选菜单类*/
class CategoryModel(
    @SerializedName("searchSubType")
    val searchSubType: String?,
    @SerializedName("searchType")
    val searchType: String?,
    @SerializedName("subCategory")
    val subCategory: List<CategoryModel>?,
    @SerializedName("title")
    val title: String?
) : MultiItemEntity {
    /**是否被选中*/
    var isSelected: Boolean = false
    var isOnClick: Boolean = false

    override fun getItemType(): Int {
        return if (TextUtils.isEmpty(title)) 2 else 1
    }
}