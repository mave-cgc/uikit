package com.nh.commondemo


import com.google.gson.annotations.SerializedName

data class Date(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("data")
    val data: DataX?,
    @SerializedName("msg")
    val msg: String?
)