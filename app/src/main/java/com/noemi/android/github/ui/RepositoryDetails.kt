package com.noemi.android.github.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.noemi.android.github.R
import com.noemi.android.github.adapter.ContributorsAdapter
import com.noemi.android.github.model.Contributors
import com.noemi.android.github.model.RepoDetails
import com.noemi.android.github.retrofit.ApiClient
import com.noemi.android.github.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RepositoryDetails : AppCompatActivity() {

    companion object {
        var ID: String = "repo_id"
        var NAME: String = "repo_full_name"
    }

    private var recyclerView: RecyclerView? = null
    private var list: MutableList<Contributors> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_details)

        recyclerView = findViewById(R.id.contributors_recycle)
        val myManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = myManager
        recyclerView!!.setHasFixedSize(true)

        val repoSize: TextView = findViewById(R.id.repository_size)
        val repoStargazers: TextView = findViewById(R.id.repository_stargazers)
        val repoForks: TextView = findViewById(R.id.repository_forks)

        val intent = getIntent()

        if (intent == null) {
            finish()
            Toast.makeText(this, getString(R.string.toast), Toast.LENGTH_SHORT).show();
        }

        val repoId = intent.extras.getInt(ID)
        val fullName = intent.extras.getString(NAME)
        val ownerRepo = letsSplit(fullName)

        val retrofit = ApiClient.getInstance().create(ApiService::class.java)

        val result = retrofit.getRepositoryDetails(repoId)
        result.enqueue(object : Callback<RepoDetails>{

            override fun onResponse(call: Call<RepoDetails>, response: Response<RepoDetails>) {
                if (!response.isSuccessful){
                    Log.d("TAG", "onResponse repo's details: " + response.code());
                }

                if (response.body() != null){

                    val details = response.body()

                    repoSize.text = String.format(applicationContext.getString(R.string.repo_size), details!!.repoSize)
                    repoStargazers.text = String.format(applicationContext.getString(R.string.repo_stargazers),details.repoGazers)
                    repoForks.text = String.format(applicationContext.getString(R.string.repo_fork), details.repoForks)
                }
            }

            override fun onFailure(call: Call<RepoDetails>, t: Throwable) {
                Toast.makeText(this@RepositoryDetails, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Error loading repository details page!")
            }
        })

        val contributors = retrofit.getContributors(ownerRepo[0], ownerRepo[1])
        contributors.enqueue(object : Callback<MutableList<Contributors>>{

            override fun onResponse(call: Call<MutableList<Contributors>>, response: Response<MutableList<Contributors>>) {
                if (!response.isSuccessful){
                    Log.d("TAG", "onResponse contributors: " + response.code());
                }

                if (response.body() != null) {

                    list = response.body()!!
                    val myAdapter = ContributorsAdapter(applicationContext, list)
                    recyclerView!!.adapter = myAdapter

                }

            }

            override fun onFailure(call: Call<MutableList<Contributors>>, t: Throwable) {

                Toast.makeText(this@RepositoryDetails, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "Error loading contributor list!")

            }

        })

    }

    fun letsSplit(fullName: String): List<String> = fullName.split("/")


}
