package com.nh.commondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nh.lib_uikit.view.filter.CustomFilterMenuView
import com.nh.lib_uikit.view.filter.CustomFilterTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filterTextView: CustomFilterTextView = findViewById(R.id.tvCustomFilterTextView)
        val mCustomFilterMenuView: CustomFilterMenuView = findViewById(R.id.customFilterMenuView)

        filterTextView.bindMenuView(mCustomFilterMenuView)
    }
}