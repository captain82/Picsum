package com.captain.picsum.view

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.captain.picsum.R
import com.captain.picsum.adapter.ImageRecyclerAdapter
import com.captain.picsum.models.ImagesResponseModel
import com.captain.picsum.viewModels.ImagesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(),Callback.onBindviewHolderCallback {
    override fun onBindViewHolder(p0: ImageRecyclerAdapter.viewHolder, position: Int) {

        if (imageList.isNotEmpty())
        {
            p0.itemView.findViewById<TextView>(R.id.filename).text =imageList[position].filename
        }

        p0.itemView.findViewById<ImageView>(R.id.download_image).setOnClickListener {

            Log.i("Download" , imageList[position].post_url+"/download"+" "+imageList[position].filename.toString())

            GetImage(imageList[position].post_url.plus("/download"),imageList[position].filename.toString())
        }

    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(ImagesViewModel::class.java) }

    private val mAdapter by lazy { ImageRecyclerAdapter(this) }

    private var imageList:List<ImagesResponseModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        viewModel.getAllImages().observe(this, Observer {

            if (it!=null && !it.isEmpty()) {

                toast(it[0].filename.toString())
                imageList = it
                mAdapter.showAllImages(imageList.take(20))
            }
            else
            {
                imageList = arrayListOf()
                mAdapter.showAllImages(imageList)
            }
        })

        images_recyclerView.adapter = mAdapter
        viewModel.getImagesFromNetwork()


    }



    inner class GetImage:AsyncTask<Any,Any,Any>
    {

        private var imageUrl:String = ""
        private var fileName:String = ""

         constructor( imageUrl:String,fileName:String)
        {
            this.imageUrl = imageUrl
            this.fileName = fileName

        }

        override fun doInBackground(vararg p0: Any?) {

            Log.i("Download" , imageUrl.plus("").plus(fileName))


        }



    }
}
