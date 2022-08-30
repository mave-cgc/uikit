package com.nh.commondemo


import com.google.gson.annotations.SerializedName

data class Sort(
    @SerializedName("order")
    val order: Int?,
    @SerializedName("sort")
    val sort: String?,
    @SerializedName("title")
    val title: String?
)