package com.captain.picsum.database

import com.captain.picsum.utils.Constants
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceGenerator {

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    private val retrofit = retrofitBuilder.build()

     val api: Api = retrofit.create(Api::class.java)
}