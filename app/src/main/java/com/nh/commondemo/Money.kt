package com.nh.commondemo


import com.google.gson.annotations.SerializedName

data class Money(
    @SerializedName("desc")
    val desc: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("money")
    val money: Double?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("totalMoney")
    val totalMoney: Double?
)