package com.vincentmasselis.rxuikotlin

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable

/**
 * Similar to [RecyclerView.ViewHolder] but this one adds an optional [onAttach(CompositeDisposable)]. Override this method to create a lifecycle-like behavior.
 */
abstract class LifecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var disps: CompositeDisposable? = null

    @Synchronized
    open fun onAttach() {
        disps = CompositeDisposable()
        onAttach(disps!!)
    }

    @Synchronized
    open fun onDetach() {
        disps!!.dispose()
        disps = null
    }

    /**
     * @return a [CompositeDisposable] if the current [LifecycleViewHolder] is between [onAttach] and [onDetach], returns null otherwise.
     */
    fun disposable(): CompositeDisposable? = disps

    /**
     * Called when the current [LifecycleViewHolder] view is attached to the window. Override this method and add every of your [io.reactivex.disposables.Disposable] to the filled
     * [detachDisposable]. [detachDisposable.dispose()] is automatically called when the view is detached from the window.
     */
    open fun onAttach(detachDisposable: CompositeDisposable) {}
}