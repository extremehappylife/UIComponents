package com.example.uicomponents

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uicomponents.cornergifview.CornerGifActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_corner_gif_view.setOnClickListener {
            Intent().apply {
                setClass(this@MainActivity, CornerGifActivity::class.java)
                startActivity(this)
            }
        }
    }
}