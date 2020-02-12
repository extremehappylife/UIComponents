package com.happylife.uicomponents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.happylife.draglayout.IonVirtualViewCreateListener
import kotlinx.android.synthetic.main.activity_drag_layout.*
import kotlinx.android.synthetic.main.layout_drag_content.*

class DragLayoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_layout)
        renderView()
        iv_header.setOnClickListener {
            Log.i("asdasd", "setOnClickListener")
        }
        iv_header.setOnLongClickListener {
            Log.i("asdasd", "setOnLongClickListener")
            true
        }
        dl_parent.setOnVirtualViewCreatedListener(object : IonVirtualViewCreateListener {
            override fun onCreateVirtualView(): View {
                val view = LayoutInflater.from(this@DragLayoutActivity)
                    .inflate(R.layout.layout_drag_content, null)
                Glide.with(this@DragLayoutActivity).load("")
                    .into(view.findViewById(R.id.iv_header))
                val tv = view.findViewById(R.id.tv_content) as TextView
                tv.text = "哈市的厚爱苏丹红我还得"
                return view
            }

            override fun onCreateVirtualRecyclerItemView(
                virtualItemView: View,
                position: Int,
                viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
            ): View {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun renderView() {
        Glide.with(this).load("").into(iv_header)
        tv_content.text = "哈市的厚爱苏丹红我还得"
    }
}