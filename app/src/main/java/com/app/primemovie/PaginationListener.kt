package com.app.primemovie

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!isLoading() && !isLastPage()) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    companion object {
        const val PAGE_START = 1

        private const val PAGE_SIZE = 4
    }


}