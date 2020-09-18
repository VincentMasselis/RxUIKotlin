package com.vincentmasselis.rxuikotlin

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Similar to [RecyclerView.ViewHolder] but this one adds an optional [onAttach(CompositeDisposable)]. Override this method to create a lifecycle-like behavior.
 */
abstract class LifecycleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val onWindowAttachSubject = PublishSubject.create<Boolean>()
    fun windowAttaches(): Observable<Boolean> = onWindowAttachSubject.hide()

    open fun onWindowAttach() {
        onWindowAttachSubject.onNext(true)
        onAttach()
    }

    open fun onWindowDetach() {
        onDetach()
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

    // -------------- Deprecated part
    private var disps: CompositeDisposable? = null

    @Synchronized
    @Deprecated("onAttach is deprecated, now you have to use onWindowAttach() with disposeOnState() at the end", ReplaceWith("onWindowAttach()"))
    open fun onAttach() {
        disps = CompositeDisposable()
        onAttach(disps!!)
    }

    @Synchronized
    @Deprecated("onDetach is deprecated, now you have to use onWindowDetach() with disposeOnState() at the end", ReplaceWith("onWindowDetach()"))
    open fun onDetach() {
        disps!!.dispose()
        disps = null
    }

    /**
     * @return a [CompositeDisposable] if the current [LifecycleViewHolder] is between [onAttach] and [onDetach], returns null otherwise.
     */
    @Deprecated("disposable is deprecated, stop using it")
    fun disposable(): CompositeDisposable? = disps

    /**
     * Called when the current [LifecycleViewHolder] view is attached to the window. Override this method and add every of your [io.reactivex.disposables.Disposable] to the filled
     * [detachDisposable]. [detachDisposable.dispose()] is automatically called when the view is detached from the window.
     */
    @Deprecated("onAttach is deprecated", ReplaceWith("onWindowAttach()"))
    open fun onAttach(detachDisposable: CompositeDisposable) {
    }
}