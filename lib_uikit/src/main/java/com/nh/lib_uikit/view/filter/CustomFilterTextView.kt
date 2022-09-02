package com.nh.lib_uikit.view.filter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.nh.lib_uikit.R

/**自定义重写TextView*/
class CustomFilterTextView : AppCompatTextView {

    private var defDrawable: Drawable? = null
    private var selDrawable: Drawable? = null
    private var defTextColor: Int = Color.parseColor("#333333")
    private var selTextColor: Int = Color.parseColor("#333333")
    private var showLocation: Int = 3

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val styleable = context.obtainStyledAttributes(attrs, R.styleable.CustomFilterTextView)

        defDrawable = styleable.getDrawable(R.styleable.CustomFilterTextView_defDrawable)
        selDrawable = styleable.getDrawable(R.styleable.CustomFilterTextView_selDrawable)
        showLocation = styleable.getInt(R.styleable.CustomFilterTextView_showLocation, 3)

        defTextColor = styleable.getColor(R.styleable.CustomFilterTextView_defTextColor, Color.parseColor("#333333"))
        selTextColor = styleable.getColor(R.styleable.CustomFilterTextView_selTextColor, Color.parseColor("#333333"))

        setDrawable()
    }

    private var currentSelectedState = false

    init {
        setOnClickListener {
            currentSelectedState = !currentSelectedState
            setDrawable()
            changeUpdateTextState(currentSelectedState)
            if (currentSelectedState) {
                mCustomFilterMenuView?.showMenu()
            } else {
                mCustomFilterMenuView?.hintMenu()
            }
            onClickStateChangeListener?.onClickStateChange(currentSelectedState)
        }
    }

    fun selectedState() {
        currentSelectedState = !currentSelectedState
        setDrawable()
        changeUpdateTextState(isSelected)
    }

    private fun setDrawable() {
        when (showLocation) {
            0 -> setCompoundDrawablesWithIntrinsicBounds(null, changeUpdateState(currentSelectedState), null, null)
            1 -> setCompoundDrawablesWithIntrinsicBounds(null, null, null, changeUpdateState(currentSelectedState))
            2 -> setCompoundDrawablesWithIntrinsicBounds(changeUpdateState(currentSelectedState), null, null, null)
            3 -> setCompoundDrawablesWithIntrinsicBounds(null, null, changeUpdateState(currentSelectedState), null)
        }
    }

    private fun changeUpdateState(isSelected: Boolean): Drawable? {
        return if (isSelected) selDrawable else defDrawable
    }

    private fun changeUpdateTextState(isSelected: Boolean) {
        setTextColor(if (isSelected) selTextColor else defTextColor)
    }

    private var mCustomFilterMenuView: CustomFilterMenuView? = null

    fun bindMenuView(menuView: CustomFilterMenuView) {
        mCustomFilterMenuView = menuView
    }

    private var onClickStateChangeListener: OnClickStateChangeListener? = null

    fun setOnClickStateChangeListener(listener: OnClickStateChangeListener) {
        this.onClickStateChangeListener = listener
    }

    interface OnClickStateChangeListener {
        fun onClickStateChange(isClick: Boolean)
    }

}