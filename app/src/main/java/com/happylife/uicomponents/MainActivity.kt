package com.happylife.uicomponents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 圆角动图
        tv_corner_gif_view.setOnClickListener {
            Intent().apply {
                setClass(this@MainActivity, CornerGifActivity::class.java)
                startActivity(this)
            }
        }

        // 滑动返回
        slide_back.setOnClickListener {
            Intent().apply {
                setClass(this@MainActivity, SlideBackActivity::class.java)
                startActivity(this)
            }
        }

        // 点赞动效
        like_animator.setOnClickListener {
            Intent().apply {
                setClass(this@MainActivity, LoveViewActivity::class.java)
                startActivity(this)
            }
        }
    }
}