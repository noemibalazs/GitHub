package com.noemi.android.github.retrofit

import com.noemi.android.github.model.Contributors
import com.noemi.android.github.model.RepoDetails
import com.noemi.android.github.model.Repository
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ApiService {

    @GET("repositories")
    fun getRepositoryList(): Call<MutableList<Repository>>

    @GET("repositories/{id}")
    fun getRepositoryDetails(@Path("id") id: Int): Call<RepoDetails>


    @GET("repos/{name}/{repo}/contributors")
    fun getContributors(@Path("name") name: String, @Path("repo") repo:String) : Call<MutableList<Contributors>>

}