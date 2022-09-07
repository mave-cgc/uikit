package com.nh.commondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nh.lib_uikit.view.filter.CustomFilterMenuView
import com.nh.lib_uikit.view.filter.CustomFilterTextView
import com.nh.lib_uikit.view.filter.model.CategoryModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filterTextView: CustomFilterTextView = findViewById(R.id.tvCustomFilterTextView)
        val tvCustomFilterTextView2: CustomFilterTextView = findViewById(R.id.tvCustomFilterTextView2)
        val mCustomFilterMenuView: CustomFilterMenuView = findViewById(R.id.customFilterMenuView)
        mCustomFilterMenuView.setNewData(Data.getData())
        mCustomFilterMenuView.setNewData(Data.getData2())

        filterTextView.setOnClickStateChangeListener {
            tvCustomFilterTextView2.setCheckState(false)
            if (it) {
                mCustomFilterMenuView.showMenu()
            } else {
                mCustomFilterMenuView.hintMenu()
            }
        }
        tvCustomFilterTextView2.setOnClickStateChangeListener {
            filterTextView.setCheckState(false)
            if (it) {
                mCustomFilterMenuView.showMenu()
            } else {
                mCustomFilterMenuView.hintMenu()
            }
        }
        mCustomFilterMenuView.setOnDismissListener {
            filterTextView.setCheckState(false)
            tvCustomFilterTextView2.setCheckState(false)
        }
        mCustomFilterMenuView.setSelectedTabListener { searchSubType: String, searchType: String, title: String ->
            filterTextView.text = title
            Toast.makeText(this, "searchSubType:$searchSubType,searchType:$searchType", Toast.LENGTH_SHORT).show()
            mCustomFilterMenuView.hintMenu()
            filterTextView.setCheckState(false)
            tvCustomFilterTextView2.setCheckState(false)
        }
    }
}