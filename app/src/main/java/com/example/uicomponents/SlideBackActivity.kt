package com.example.uicomponents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.slidebacklayout.SlideBackHelper
import com.example.slidebacklayout.SlideBackLayout

class SlideBackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide_back)

        val mSlideBackLayout: SlideBackLayout =
            SlideBackHelper.attach(this, R.layout.slide_back)
        // mSlideBackLayout.setRightSlideEnable(true);
        // mSlideBackLayout.setLeftSlideEnable(true);
        mSlideBackLayout.setSwipeBackListener(object : SlideBackLayout.OnSlideBackListener {
            override fun completeSwipeBack() {
                finish()
            }
        })
    }
}