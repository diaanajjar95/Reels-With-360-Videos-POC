package com.example.instagramreelspoc

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fi.finwe.orion360.sdk.pro.OrionContext

private const val TAG = "ReelsAdapter"

class ReelsAdapter(
    videoItems: List<ReelItem>,
    private val orionContext: OrionContext?
) :
    RecyclerView.Adapter<ReelViewHolder>() {
    private val mVideoItems: List<ReelItem>

    init {
        Log.d(TAG, "init: videoItems : $videoItems")
        mVideoItems = videoItems
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelViewHolder {
        Log.d(TAG, "onCreateViewHolder: ")
        return ReelViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_item_reel_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReelViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: ")
        holder.bind(mVideoItems[position], orionContext)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${mVideoItems.size}}")
        return mVideoItems.size
    }

    override fun onViewAttachedToWindow(holder: ReelViewHolder) {
        super.onViewAttachedToWindow(holder)
        Log.d(TAG, "onViewAttachedToWindow: ")
    }

    override fun onViewDetachedFromWindow(holder: ReelViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.pause()
        Log.d(TAG, "onViewDetachedFromWindow: ")
    }
}