package com.vincentmasselis.rxuikotlin

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Similar to [RecyclerView.ViewHolder] but this one adds an optional [onAttach(CompositeDisposable)]. Override this method to create a lifecycle-like behavior.
 */
abstract class LifecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val onWindowAttachSubject = PublishSubject.create<Boolean>()
    fun windowAttaches(): Observable<Boolean> = onWindowAttachSubject.hide()

    open fun onWindowAttach() {
        onWindowAttachSubject.onNext(true)
    }

    open fun onWindowDetach() {
        onWindowAttachSubject.onNext(false)
    }

    internal val isAttachAsked = AtomicBoolean(false)
    private val onAdapterAttachSubject = PublishSubject.create<Boolean>()
    fun adapterAttaches(): Observable<Boolean> = onAdapterAttachSubject.hide()

    open fun onAdapterAttach() {
        onAdapterAttachSubject.onNext(true)
    }

    open fun onAdapterDetach() {
        onAdapterAttachSubject.onNext(false)
    }
}