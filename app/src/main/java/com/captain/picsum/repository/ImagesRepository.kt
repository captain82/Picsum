package com.captain.picsum.repository

import com.captain.picsum.database.ServiceGenerator
import com.captain.picsum.models.ImagesResponseModel
import retrofit2.Call

class ImagesRepository {

    private val mApiClient = ServiceGenerator.api

    fun getAllImages():Call<List<ImagesResponseModel>>
    {


        return mApiClient.getAllImages()
    }

    companion object
    {
        @Volatile
        private var instance:ImagesRepository? = null

        fun getInstance() = instance?: synchronized(this){
            instance?:ImagesRepository().also { instance = it }
        }
    }
}