package com.captain.picsum.view

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.captain.picsum.R
import com.captain.picsum.adapter.ImageRecyclerAdapter
import com.captain.picsum.models.ImagesResponseModel
import com.captain.picsum.viewModels.ImagesViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.net.URL

import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.recycler_single_layout.*


class MainActivity : AppCompatActivity(), Callback.onBindviewHolderCallback {


    override fun onBindViewHolder(p0: ImageRecyclerAdapter.viewHolder, position: Int) {

        if (imageList.isNotEmpty()) {
            p0.itemView.findViewById<TextView>(R.id.filename).text = imageList[position].filename
        }

        p0.itemView.findViewById<ImageView>(R.id.download_image).setOnClickListener {

            mNotifyManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            mBuilder = NotificationCompat.Builder(this)

            mBuilder?.setContentTitle("Downloading ${p0.itemView.findViewById<TextView>(R.id.filename).text}")
                ?.setContentText("Download in Progress")
                ?.setSmallIcon(R.drawable.ic_file_download_black_24dp)
            Log.i(
                "Download",
                imageList[position].post_url + "/download" + " " + imageList[position].filename.toString()
            )

            GetImage(
                imageList[position].post_url.plus("/download"),
                imageList[position].filename.toString(),
                p0.itemView.findViewById<ProgressBar>(R.id.progressBar)
            ).execute()
        }

        val status = checkImageExist(imageList[position].filename,p0.itemView.findViewById<ImageView>(R.id.download_image))
        if (status.equals(true))
        {
            //p0.itemView.findViewById<ImageView>(R.id.download_image) = View.INVISIBLE

        }


    }

    private fun checkImageExist(filename: String?, itemView: View):Boolean {

        val root = Environment.getExternalStorageDirectory()
        Log.i("Path" , root.absolutePath.toString())
        val picDir = File(root.absolutePath +"/Picsum/${filename}")
        Log.i("Status" , filename+" "+picDir.exists())
        return (picDir.exists()).also {
            if (it)
            {
                itemView.visibility = View.GONE
            }
            else
            {
                itemView.visibility = View.VISIBLE
            }
        }

    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(ImagesViewModel::class.java) }

    private val mAdapter by lazy { ImageRecyclerAdapter(this) }

    private var imageList: List<ImagesResponseModel> = arrayListOf()

    private var mNotifyManager: NotificationManager? = null

    private var mBuilder: NotificationCompat.Builder? = null

    private val id = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.captain.picsum.R.layout.activity_main)


        viewModel.getAllImages().observe(this, Observer {

            if (it != null && !it.isEmpty()) {

                toast(it[0].filename.toString())
                imageList = it
                mAdapter.showAllImages(imageList.take(20))
            } else {
                imageList = arrayListOf()
                mAdapter.showAllImages(imageList)
            }
        })

        images_recyclerView.adapter = mAdapter
        viewModel.getImagesFromNetwork()

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);

            }


        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
        }
        else
        {
            Toast.makeText(this,"Permission Required to save Image",Toast.LENGTH_LONG).show()
        }
    }


    inner class GetImage : AsyncTask<Int, Int, Any> {

        private var imageUrl: String = ""
        private var fileName: String = ""
        private var bitmap: Bitmap? = null
        private var progressBar:ProgressBar? = null


        constructor(
            imageUrl: String,
            fileName: String,
            progressBar: ProgressBar
        ) {
            this.imageUrl = imageUrl
            this.fileName = fileName
            this.progressBar = progressBar

        }

        override fun onPreExecute() {
            super.onPreExecute()

            mBuilder?.setProgress(25,0,false)
            mNotifyManager?.notify(id,mBuilder?.setContentTitle(fileName)?.build())

        }

        override fun onProgressUpdate(vararg values: Int?) {

            mBuilder?.setProgress(25,values[0]?:0,false)
            mNotifyManager?.notify(id,mBuilder?.setContentTitle(fileName)?.build())

            super.onProgressUpdate(*values)

        }
        override fun doInBackground(vararg p0: Int?): Any? {

            try {
                val imageUrl = URL(imageUrl)
                val conn = imageUrl.openConnection()
                bitmap = BitmapFactory.decodeStream(conn.getInputStream())
                //Log.i("Progress" , )
                for (i in 0..25)
                {
                    publishProgress(minOf(i,25))
                    runOnUiThread { progressBar?.progress = i
                    }
                }


            } catch (e: Exception) {
                e.stackTrace
            }
            return null

        }



        override fun onPostExecute(result: Any?) {
            super.onPostExecute(result)

            saveImage(bitmap)
            mBuilder?.setContentText("Download Complete")
            mBuilder?.setProgress(0,0,false)
            mNotifyManager?.notify(id,mBuilder?.setContentTitle(fileName)?.build())



        }

        private fun saveImage(bitmap: Bitmap?) {
            val root = Environment.getExternalStorageDirectory()
            Log.i("Path" , root.absolutePath.toString())
            val picDir = File(root.absolutePath +"/Picsum")
            picDir.mkdirs()
           val  file = File(picDir.absoluteFile,fileName)

            try {
                val out = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.JPEG,90,out)
                out.flush()
                out.close()
                mAdapter.notifyDataSetChanged()
            }catch (e:Exception)
            {
                e.printStackTrace()
            }



        }


    }
}
