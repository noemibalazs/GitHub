package com.noemi.android.github.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

internal abstract class PaginationScrollListener(var layoutManager: LinearLayoutManager): RecyclerView.OnScrollListener() {

    abstract val isLoading: Boolean
    abstract val isLastPage: Boolean

    companion object {
        private val PAGE_SIZE = 25
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (!isLoading && !isLastPage){
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE){
                loadMoreItems()

            }
        }
    }

    protected abstract fun loadMoreItems()

}