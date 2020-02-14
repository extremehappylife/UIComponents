package com.happylife.uicomponents

import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.happylife.compositepicture.CompositeUtil
import kotlinx.android.synthetic.main.activity_composite.*

class CompositeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_composite)
        var bm1: Bitmap? = null
        var bm2: Bitmap? = null
        Glide.with(this)
            .asBitmap()
            .load("http://a3.att.hudong.com/14/75/01300000164186121366756803686.jpg")
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    iv_composite_1.setImageBitmap(resource)
                    bm1 = resource
                }
            })
        Glide.with(this)
            .asBitmap()
            .load("http://a2.att.hudong.com/36/48/19300001357258133412489354717.jpg")
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    iv_composite_2.setImageBitmap(resource)
                    bm2 = resource
                }
            })
        tv_composite.setOnClickListener {
            if (bm1 != null && bm2 != null) {
                // 写死值
                // CompositeUtil.setXYLabel(550, 520)
                // 百分比
                val x = if(!TextUtils.isEmpty(et_X.text)) (et_X.text.toString()).toFloat() else 0.0f
                val y = if(!TextUtils.isEmpty(et_Y.text)) (et_Y.text.toString()).toFloat() else 0.0f
                CompositeUtil.setXYLabelPer(x, y)
                iv_composite_3.setImageBitmap(CompositeUtil.combineImages(bm1,bm2))
            }
        }
    }
}