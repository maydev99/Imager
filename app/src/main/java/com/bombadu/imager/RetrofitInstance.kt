package com.bombadu.imager

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {

    val api: PixabayApi by lazy {
        Retrofit.Builder()
                .baseUrl("https://pixabay.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PixabayApi::class.java)


    }


}