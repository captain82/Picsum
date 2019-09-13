package com.captain.picsum.database

import com.captain.picsum.models.ImagesResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {

    @GET(".")
    fun getAllImages(): Call<List<ImagesResponseModel>>
}