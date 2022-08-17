package com.nh.lib_uikit.view

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import kotlin.math.roundToInt

internal object DisplayUtil {
    /**
     * 获取屏幕宽度
     *
     * @return
     */
    @JvmStatic
    val screenWidth: Int
        get() = Resources.getSystem().displayMetrics.widthPixels

    /**
     * 获取屏幕高度
     *
     * @return
     */
    @JvmStatic
    val screenHeight: Int
        get() = Resources.getSystem().displayMetrics.heightPixels

    /**
     * 底部导航栏(navigation bar)
     */
    val navigationBarHeight: Int
        get() {
            val resources = Resources.getSystem()
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return resources.getDimensionPixelSize(resourceId)
        }

    /**
     * 获取屏幕物理像素密度
     *
     * @return
     */
    val screenDensity: Float
        get() = Resources.getSystem().displayMetrics.density

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    fun dpToPx(dp: Float): Int {
        return (Resources.getSystem().displayMetrics.density * dp + 0.5f).roundToInt()
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    fun dpToPxF(dp: Float): Float {
        return Resources.getSystem().displayMetrics.density * dp + 0.5f
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    fun dpToPxf(dp: Float): Float {
        return (Resources.getSystem().displayMetrics.density * dp + 0.5f).roundToInt().toFloat()
    }

    /**
     * sp转px
     *
     * @param sp
     * @return
     */
    fun spToPx(sp: Int): Int {
        return (Resources.getSystem().displayMetrics.scaledDensity * sp + 0.5f).roundToInt()
    }

    /**
     * px转dp
     *
     * @param px
     * @return
     */
    fun pxToDp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.density + 0.5f).roundToInt()
    }

    /**
     * px转sp
     *
     * @param px
     * @return
     */
    fun pxToSp(px: Int): Int {
        return (px / Resources.getSystem().displayMetrics.scaledDensity + 0.5f).roundToInt()
    }

    ///**
    // * 获取状态栏的高度
    // * @param context
    // * @return
    // */
    //public static int getStatusHeight(Context context) {
    //	Rect rect = new Rect();
    //	((Activity)context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
    //	return rect.top;
    //}
    fun getStatusBarHeight(context: Context): Int {
        // 获得状态栏高度
        val resourceId = context.resources.getIdentifier(
            "status_bar_height", "dimen",
            "android"
        )
        return context.resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取屏幕参数
     *
     * @return
     */
    val screenParams: String
        get() = "$screenWidth*$screenHeight"

    /**
     * 保持字体大小不随系统设置变化（用在界面加载之前）
     * 要重写Activity的attachBaseContext()
     */
    @JvmStatic
    fun attachBaseContext(context: Context, fontScale: Float): Context? {
        val config: Configuration = context.resources.configuration
        //错误写法
//        if(config.fontScale != fontScale) {
//            config.fontScale = fontScale;
//            return context.createConfigurationContext(config);
//        } else {
//            return context;
//        }
        //正确写法
        config.fontScale = fontScale
        return context.createConfigurationContext(config)
    }

    /**
     * 保持字体大小不随系统设置变化（用在界面加载之前）
     * 要重写Activity的getResources()
     */
    @JvmStatic
    fun getResources(context: Context, resources: Resources, fontScale: Float): Resources? {
        val config: Configuration = resources.configuration
        return if (config.fontScale !== fontScale) {
            config.fontScale = fontScale
            context.createConfigurationContext(config).resources
        } else {
            resources
        }
    }

    /**
     * 保存字体大小，后通知界面重建，它会触发attachBaseContext，来改变字号
     */
    @JvmStatic
    fun recreate(activity: Activity) {
//          activity.getWindow().getDecorView().requestLayout();
//          activity.getWindow().getDecorView().invalidate();
        //只有这句才有效，其它两句都无效
        activity.recreate()
    }

}