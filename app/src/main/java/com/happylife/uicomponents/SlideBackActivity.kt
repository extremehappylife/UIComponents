package com.happylife.uicomponents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.happylife.slidebacklayout.SlideBackHelper
import com.happylife.slidebacklayout.SlideBackLayout

class SlideBackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_back)

        val mSlideBackLayout: SlideBackLayout = SlideBackHelper.attach(this)
        // mSlideBackLayout.setRightSlideEnable(true);
        // mSlideBackLayout.setLeftSlideEnable(true);
        mSlideBackLayout.setSwipeBackListener(object : SlideBackLayout.OnSlideBackListener {
            override fun completeSwipeBack() {
                finish()
            }
        })
    }
}