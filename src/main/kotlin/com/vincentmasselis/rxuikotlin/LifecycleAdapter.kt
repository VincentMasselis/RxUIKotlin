package com.vincentmasselis.rxuikotlin

import androidx.recyclerview.widget.RecyclerView

/**
 * Helper class to create a lifecycle-like behavior into a [RecyclerView.ViewHolder] by using [LifecycleViewHolder]. Subclass [LifecycleAdapter] like you did with a
 * [RecyclerView.Adapter]
 */
abstract class LifecycleAdapter<T : LifecycleViewHolder> : RecyclerView.Adapter<T>() {

    override fun onViewAttachedToWindow(holder: T) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: T) {
        holder.onDetach()
        super.onViewDetachedFromWindow(holder)
    }
}