package com.vincentmasselis.rxui

import android.support.v7.widget.RecyclerView
import android.view.View
import com.vincentmasselis.rxui.utils.AttachListenerForViewWithLifecycle
import com.vincentmasselis.rxui.utils.ViewLifecycleProvider
import com.vincentmasselis.rxui.utils.ViewState
import com.vincentmasselis.rxui.utils.ViewWithLifecycle
import io.reactivex.subjects.BehaviorSubject

open class RxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ViewLifecycleProvider, ViewWithLifecycle {

    override val lifecycleObs: BehaviorSubject<ViewState> = BehaviorSubject.create<ViewState>()

    init {
        itemView.addOnAttachStateChangeListener(AttachListenerForViewWithLifecycle(this))
    }

    override fun onAttach() {
        lifecycleObs.onNext(ViewState.ATTACH)
    }

    override fun onDetach() {
        lifecycleObs.onNext(ViewState.DETACH)
    }
}