package com.example.uicomponents

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_corner_gif.*

class CornerGifActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_corner_gif)
        Glide.with(this)
            .load("http://qn.niaogebiji.com/7339550725ac1cba9881a65.90582627.gif")
            .into(iv_gif_corner)
    }
}