package com.noemi.android.github.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.noemi.android.github.R
import com.noemi.android.github.model.Repository

class RepositoryAdapter(context: Context, listRepository: MutableList<Repository>?): ArrayAdapter<Repository>(context, 0, listRepository){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        var view = convertView
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_repo, parent, false)
        }

        val repo = getItem(position)

        val repoName = view!!.findViewById<TextView>(R.id.repository_name)
        repoName.text = String.format(context.getString(R.string.name), repo.name)

        val repoFullName = view.findViewById<TextView>(R.id.repository_full_name)
        repoFullName.text = String.format(context.getString(R.string.full_name), repo.fullName)

        return view
    }
}