package com.happylife.slidebacklayout

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

object SlideBackHelper {

    @JvmOverloads
    fun attach(
        activity: Activity,
        listener: SlideBackLayout.OnSlideBackListener? = null
    ): SlideBackLayout {
        return attach(activity, listener, R.layout.slide_back)
    }

    private fun attach(
        activity: Activity?,
        listener: SlideBackLayout.OnSlideBackListener?,
        @LayoutRes layoutId: Int
    ): SlideBackLayout {

        val mSlideBackLayout =
            LayoutInflater.from(activity).inflate(layoutId, null) as SlideBackLayout
        if (listener == null) {
            mSlideBackLayout.setSwipeBackListener(object : SlideBackLayout.OnSlideBackListener {
                override fun completeSwipeBack() {
                    activity?.finish()
                }
            })
        } else {
            mSlideBackLayout.setSwipeBackListener(listener)
        }

        if (activity == null || activity.window == null || activity.window.decorView !is ViewGroup) {
            return mSlideBackLayout
        }

        val decorView = activity.window.decorView as ViewGroup
        if (decorView.childCount > 0) {
            val child = decorView.getChildAt(0)
            decorView.removeView(child)
            mSlideBackLayout.addView(child)
        }

        decorView.addView(mSlideBackLayout)
        return mSlideBackLayout
    }
}