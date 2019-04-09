package com.noemi.android.github.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.noemi.android.github.R
import com.noemi.android.github.adapter.RepositoryAdapter
import com.noemi.android.github.model.Repository
import com.noemi.android.github.retrofit.ApiClient
import com.noemi.android.github.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class MainActivity : AppCompatActivity() {


    var myList: MutableList<Repository> = mutableListOf()

    private var listView: ListView? = null
    private var prevButton: Button? = null
    private var nextButton: Button? = null

    private var myAdapter: RepositoryAdapter? = null

    private var currentPage = 0
    private var totalPage = getTotalPages()

    companion object {
        private var ITEMS_PER_PAGE = 25
        private var TOTAL_ITEMS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById<ListView>(R.id.listview)
        prevButton = findViewById(R.id.prevBtn)
        nextButton = findViewById(R.id.nextBtn)

        val retrofit = ApiClient.getInstance().create(ApiService::class.java)
        val result = retrofit.getRepositoryList()
        result.enqueue(object: Callback<MutableList<Repository>>{


            override fun onResponse(call: Call<MutableList<Repository>>, response: Response<MutableList<Repository>>) {
                if (!response.isSuccessful) {
                    Log.d("TAG", "onResponse repositories: " + response.code());
                }
                if (response.isSuccessful) {
                    myList = response.body()!!
                }
            }

            override fun onFailure(call: Call<MutableList<Repository>>, t: Throwable) {
                Toast.makeText(this@MainActivity, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Error loading repositories!")
            }
        })

        bindData(currentPage)
        prevButton!!.isEnabled = false

        nextButton!!.setOnClickListener {
            currentPage++
            bindData(currentPage)
            toggleButtons()
        }

        prevButton!!.setOnClickListener {
            currentPage--
            bindData(currentPage)
            toggleButtons()

        }

        val context = this
        listView!!.setOnItemClickListener { _, _, position, _ ->

            val repository = myList[position]
            val intent = Intent(this, RepositoryDetails::class.java)
            intent.apply {
                putExtra(RepositoryDetails.ID, repository.id)
                putExtra(RepositoryDetails.NAME, repository.fullName)
            }
            startActivity(intent)
        }

    }


    fun bindData(page: Int){
        myAdapter = RepositoryAdapter(this, getCurrentContributorsList(page)!!)
        listView!!.adapter = myAdapter
    }

    fun getTotalPages(): Int{
        val remainingItems =  TOTAL_ITEMS % ITEMS_PER_PAGE
        if (remainingItems >0){
            return TOTAL_ITEMS / ITEMS_PER_PAGE
        }
        return (TOTAL_ITEMS / ITEMS_PER_PAGE) -1
    }

    fun getCurrentContributorsList(currentPage: Int): MutableList<Repository>?{

        var visibleList:MutableList<Repository> = mutableListOf()

        val startItem = currentPage * ITEMS_PER_PAGE
        val lastItem = startItem + ITEMS_PER_PAGE
        try {
            for ((index, repos) in myList.withIndex()){
                if (index >= startItem && index < lastItem){
                    visibleList.add(repos)
                }
            }
            return visibleList
        }catch (e: Exception){
            e.printStackTrace()
            return null
        }
    }

    fun toggleButtons(){
        if (totalPage <=1){
            prevButton!!.isEnabled = false
            nextButton!!.isEnabled = false
        }

        if (currentPage == totalPage){
            nextButton!!.isEnabled = false
            prevButton!!.isEnabled = true
        }

        if (currentPage == 0){
            prevButton!!.isEnabled = false
            nextButton!!.isEnabled = true
        }

        if (currentPage >=1 && currentPage < totalPage){
            prevButton!!.isEnabled = true
            nextButton!!.isEnabled = true

        }
    }

}
