package com.noemi.android.github.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.noemi.android.github.R
import com.noemi.android.github.model.Contributors
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.contributors_item.view.*

class ContributorsAdapter(val context: Context,  var contributorsList: MutableList<Contributors>?= null): RecyclerView.Adapter<ContributorsAdapter.ContributorsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContributorsViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.contributors_item, parent, false)
        return ContributorsViewHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return if (contributorsList == null) 0 else contributorsList!!.size

    }

    override fun onBindViewHolder(holder: ContributorsViewHolder, position: Int) {
        val contributor = contributorsList?.let{ it[position]}

        holder.login.text = contributor!!.name
        Picasso.get()
                .load(contributor.avatar)
                .error(R.drawable.anonymus)
                .transform(CircleAdapter())
                .placeholder(R.drawable.anonymus)
                .into(holder.avatar)
    }

    inner class ContributorsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val avatar = view.contributor_avatar
        val login = view.contributor_login
    }

}