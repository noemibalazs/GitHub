package com.noemi.android.github.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noemi.android.github.R
import com.noemi.android.github.model.Repository
import com.noemi.android.github.ui.RepositoryDetails
import kotlinx.android.synthetic.main.item_loading.view.*
import kotlinx.android.synthetic.main.item_post.view.*
import java.util.ArrayList


class RepoListAdapter internal constructor(var context: Context) : RecyclerView.Adapter<BaseViewHolder>() {

    private var listRepository: MutableList<Repository>? = null
    private var isLoaderVisible: Boolean = false

    init {
        listRepository = ArrayList()
    }

    companion object {
        private val ITEM: Int = 0
        private val LOADING = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var viewHolder: BaseViewHolder? = null
        when (viewType) {
            ITEM -> {
                val viewItem = layoutInflater.inflate(R.layout.item_post, parent, false)
                viewHolder = RepoViewHolder(viewItem)
            }

            LOADING -> {
                val viewItem = layoutInflater.inflate(R.layout.item_loading, parent, false)
                viewHolder = LoadingView(viewItem)
            }
            else -> {
                viewHolder = null
            }
        }

        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return if (listRepository == null) 0 else listRepository!!.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == listRepository!!.size - 1) LOADING else ITEM
        } else {
            ITEM
        }
    }

    fun getItem(position: Int): Repository = listRepository!!.get(position)

    fun addRepo(repo: Repository) {
        listRepository!!.add(repo)
        notifyItemInserted(listRepository!!.size - 1)
    }

    fun addAllRepo(listRepo: MutableList<Repository>) {
        for (repo in listRepo) {
            addRepo(repo)
        }
    }

    fun removeRepo(repo: Repository) {
        val position = listRepository!!.indexOf(repo)
        if (position > -1) {
            listRepository!!.removeAt(position)
            notifyItemRemoved(position)
        }

    }

    fun addLoading() {
        isLoaderVisible = true
        addRepo(Repository())
    }

    fun removeLoading() {
        isLoaderVisible = false
        val position = listRepository!!.size - 1
        val repo = getItem(position)
        listRepository!!.removeAt(position)
        notifyItemRemoved(position)

    }

    fun clear() {
        isLoaderVisible = false
        while (itemCount > 0) {
            removeRepo(this.getItem(0)!!)
        }
    }

    inner class RepoViewHolder(view: View) : BaseViewHolder(view) {

        var repoId: Int = 0
        var repoName = view.repository_name
        var repoFullNmae = view.repository_full_name
        val container = view.container


        override fun onBind(position: Int) {

            val repository = listRepository?.let { it[position] }
            repoId = repository!!.id
            repoName.text = String.format(context.getString(R.string.name), repository.name)
            repoFullNmae.text = String.format(context.getString(R.string.full_name), repository.fullName)
            container.setOnClickListener {view ->
                val intent = Intent(context, RepositoryDetails::class.java)
                intent.putExtra(RepositoryDetails.ID, repoId)
                intent.putExtra(RepositoryDetails.NAME, repository.fullName)
                context.startActivity(intent)
            }
        }

        override fun clear() {
        }

    }

    inner class LoadingView(view: View) : BaseViewHolder(view) {
        var progressbar = view.main_progress
        override fun clear() {
        }

    }

}