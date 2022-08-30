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
        val mCustomFilterMenuView: CustomFilterMenuView = findViewById(R.id.customFilterMenuView)

        filterTextView.bindMenuView(mCustomFilterMenuView)

        mCustomFilterMenuView.setNewData(Data.getData() as MutableList<CategoryModel>?)
        mCustomFilterMenuView.setSelectedTabListener { searchSubType: String, searchType: String, title: String ->
            filterTextView.text = title
            Toast.makeText(this, "searchSubType:$searchSubType,searchType:$searchType", Toast.LENGTH_SHORT).show()
            mCustomFilterMenuView.hintMenu()
            filterTextView.selectedState()
        }
    }
}