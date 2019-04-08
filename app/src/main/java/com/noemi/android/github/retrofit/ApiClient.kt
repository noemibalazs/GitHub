package com.noemi.android.github.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {

        val baseUrl = "https://api.github.com/"
        var rertofit: Retrofit? = null

        fun getInstance(): Retrofit{

            if ( rertofit == null){
                rertofit = Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }
            return rertofit!!
        }
    }
}