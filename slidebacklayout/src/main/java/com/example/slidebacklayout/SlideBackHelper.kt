package com.example.slidebacklayout

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

object SlideBackHelper {

    fun attach(activity: Activity, @LayoutRes layoutId: Int): SlideBackLayout {
        return attach(activity, null, layoutId)
    }

    fun attach(
        activity: Activity?,
        listener: SlideBackLayout.OnSlideBackListener?, @LayoutRes layoutId: Int
    ): SlideBackLayout {
        val mSwipeBackLayout =
            LayoutInflater.from(activity).inflate(layoutId, null) as SlideBackLayout
        if (listener == null) {
            mSwipeBackLayout.setSwipeBackListener(object : SlideBackLayout.OnSlideBackListener {
                override fun completeSwipeBack() {
                    activity?.finish()
                }
            })
        } else {
            mSwipeBackLayout.setSwipeBackListener(listener)
        }

        if (activity == null || activity.window == null || activity.window.decorView !is ViewGroup) {
            return mSwipeBackLayout
        }

        val decorView = activity.window.decorView as ViewGroup
        if (decorView != null) {
            if (decorView.childCount > 0) {
                val child = decorView.getChildAt(0)
                decorView.removeView(child)
                mSwipeBackLayout.addView(child)
            }

            decorView.addView(mSwipeBackLayout)
        }
        return mSwipeBackLayout
    }
}