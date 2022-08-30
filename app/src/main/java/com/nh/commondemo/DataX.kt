package com.nh.commondemo


import com.google.gson.annotations.SerializedName
import com.nh.lib_uikit.view.filter.model.CategoryModel

data class DataX(
    @SerializedName("category")
    val category: List<CategoryModel>?,
    @SerializedName("moneyList")
    val moneyList: List<Money>?,
    @SerializedName("sort")
    val sort: List<Sort>?,
    @SerializedName("totalMoney")
    val totalMoney: Double?
)