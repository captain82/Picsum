package com.captain.picsum.view

import androidx.recyclerview.widget.RecyclerView
import com.captain.picsum.adapter.ImageRecyclerAdapter

interface Callback {

    interface onBindviewHolderCallback {

        fun onBindViewHolder(p0:ImageRecyclerAdapter.viewHolder,position:Int)

    }
}