package com.noemi.android.github.ui

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.noemi.android.github.R
import com.noemi.android.github.adapter.RepoListAdapter
import com.noemi.android.github.model.Repository
import com.noemi.android.github.retrofit.ApiClient
import com.noemi.android.github.retrofit.ApiService
import com.noemi.android.github.utils.PaginationScrollListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object {
        var PAGE_START = 1
    }

    private var loading: Boolean = false
    private var lastPage = false
    private var totalPage = 4
    private var currentPage = PAGE_START

    private var recyclerView: RecyclerView? = null
    private var customLayoutManager: LinearLayoutManager? = null
    private var repoAdapter: RepoListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.list_recycler)
        customLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = customLayoutManager
        repoAdapter = RepoListAdapter(this)
        recyclerView!!.adapter = repoAdapter


        recyclerView!!.addOnScrollListener(object : PaginationScrollListener(customLayoutManager!!){
            override val isLoading: Boolean
                get() = loading
            override val isLastPage: Boolean
                get() = lastPage

            override fun loadMoreItems() {
                loading = true
                currentPage++
                loadNextPage()
            }
        })

        loadFirstPage()
    }

    fun callRepoApi(): Call<MutableList<Repository>>{
        val retrofit = ApiClient.getInstance().create(ApiService::class.java)
        return retrofit.getRepositoryList()
    }

    fun loadFirstPage(){

        Log.d("MainActivity", "loadFirstPage")

        callRepoApi().enqueue(object : Callback<MutableList<Repository>>{

            override fun onResponse(call: Call<MutableList<Repository>>, response: Response<MutableList<Repository>>) {
                if (!response.isSuccessful) {
                    Log.d("TAG", "onResponse first Page: " + response.code());
                }

                if(response.body()!= null){
                    val repoList = response.body()!!

                    repoAdapter!!.addAllRepo(repoList)

                    if (currentPage <= totalPage)
                        repoAdapter!!.addLoading()
                    else
                        lastPage = true
                }
            }

            override fun onFailure(call: Call<MutableList<Repository>>, t: Throwable) {
                Toast.makeText(this@MainActivity, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Error loading first page!")

            }
        })
    }

    fun loadNextPage(){

        Log.d("MainActivity", "loadNextPage: " + currentPage);

        callRepoApi().enqueue(object : Callback<MutableList<Repository>>{

            override fun onResponse(call: Call<MutableList<Repository>>, response: Response<MutableList<Repository>>) {
                if (!response.isSuccessful){
                    Log.d("TAG", "onResponse next Page: " + response.code());
                }

                if (response.body() != null){

                    repoAdapter!!.removeLoading()
                    loading = false

                    val repoList = response.body()!!

                    repoAdapter!!.addAllRepo(repoList)

                    if (currentPage != totalPage)
                        repoAdapter!!.addLoading()
                    else
                        lastPage = true
                }

            }

            override fun onFailure(call: Call<MutableList<Repository>>, t: Throwable) {
                Toast.makeText(this@MainActivity, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Error loading next page!")
            }
        })
    }


}
