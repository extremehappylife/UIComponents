package com.happylife.uicomponents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_love_view.*

class LoveViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_love_view)
        like_view.setDrawableId(R.drawable.details_icon_like_pressed)
    }
}
