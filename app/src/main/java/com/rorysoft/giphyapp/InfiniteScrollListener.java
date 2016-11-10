package com.rorysoft.giphyapp;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Created by Alexander on 11/9/16.
 */

// This class was an attempt to create infinite scrolling by querying more data (In main Activity) after 6 objects
// were below user view. It only works once and causes the recyclerView to reset its position.

public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    private int previousTotal = 0;
    private boolean loading = true;
    private int scrollThreshold = 6;
    private int firstItem, currentItemCount, totalItemCount;
    private int[] firstItemsArray;

    private int count;

    private StaggeredGridLayoutManager staggeredGridLayoutManager;

    public InfiniteScrollListener(StaggeredGridLayoutManager staggeredGridLayoutManager, int count) {
        this.staggeredGridLayoutManager = staggeredGridLayoutManager;
        this.count = count;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        currentItemCount = recyclerView.getChildCount();
        totalItemCount = staggeredGridLayoutManager.getItemCount();
        firstItemsArray = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);

        if(loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }

        if(!loading && (totalItemCount - currentItemCount) <= (firstItemsArray.length + scrollThreshold)) {
            int update = count;

            onLoadMore(update);

            loading = true;
        }
    }

    public abstract void onLoadMore(int current_page);
}
