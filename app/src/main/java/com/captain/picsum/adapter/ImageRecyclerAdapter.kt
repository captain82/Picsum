package com.captain.picsum.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.captain.picsum.R
import com.captain.picsum.models.ImagesResponseModel
import com.captain.picsum.view.Callback

class ImageRecyclerAdapter(val onBindviewHolderCallback: Callback.onBindviewHolderCallback):RecyclerView.Adapter<ImageRecyclerAdapter.viewHolder>(){

    private var imageList:List<ImagesResponseModel>? = arrayListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {

        return viewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_single_layout,parent,false))

    }

    override fun getItemCount(): Int {

        return imageList?.size?:0
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        onBindviewHolderCallback.onBindViewHolder(holder,position)
    }

    fun showAllImages(list: List<ImagesResponseModel>)
    {
        imageList = list
        notifyDataSetChanged()
    }


    inner class viewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

}