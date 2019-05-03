package com.vincentmasselis.rxuikotlin

import androidx.recyclerview.widget.RecyclerView

/**
 * Helper class to create a lifecycle-like behavior into a [RecyclerView.ViewHolder] by using [LifecycleViewHolder]. Subclass [LifecycleAdapter] like you did with a
 * [RecyclerView.Adapter]
 */
abstract class LifecycleAdapter<T : LifecycleViewHolder> : RecyclerView.Adapter<T>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        if (recyclerView.getTag(R.integer.adapter_recycler_view_tag) == null) throw IllegalStateException("Current adapter $this must be set to the recyclerView $recyclerView by calling RecyclerView.subscribe() instead of RecyclerView.setAdapter()")

        super.onAttachedToRecyclerView(recyclerView)
    }

    private val adapterAttachedList = mutableListOf<T>()

    override fun onViewAttachedToWindow(holder: T) {
        super.onViewAttachedToWindow(holder)
        if (holder.isAttachAsked.compareAndSet(false, true)) {
            holder.onAdapterAttach()
            adapterAttachedList += holder
        }
        holder.onWindowAttach()
    }

    override fun onViewDetachedFromWindow(holder: T) {
        holder.onWindowDetach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        adapterAttachedList.forEach { it.onAdapterDetach() }
        adapterAttachedList.clear()
        super.onDetachedFromRecyclerView(recyclerView)
    }
}