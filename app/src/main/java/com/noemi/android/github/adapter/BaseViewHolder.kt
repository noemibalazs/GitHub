package com.noemi.android.github.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    var currentPosition: Int = 0
        private set

    open fun onBind(position: Int){
        currentPosition = position
        clear()
    }

    protected abstract fun clear()

}