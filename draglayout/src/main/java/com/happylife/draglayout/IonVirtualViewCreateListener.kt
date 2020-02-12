package com.happylife.draglayout

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface IonVirtualViewCreateListener {
    fun onCreateVirtualView(): View
    fun onCreateVirtualRecyclerItemView(
        virtualItemView: View,
        position: Int,
        viewHolder: RecyclerView.ViewHolder
    ): View
}