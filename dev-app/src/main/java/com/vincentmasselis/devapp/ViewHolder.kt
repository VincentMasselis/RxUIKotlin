package com.vincentmasselis.devapp

import android.view.View
import com.vincentmasselis.rxuikotlin.LifecycleViewHolder
import com.vincentmasselis.rxuikotlin.disposeOnState
import com.vincentmasselis.rxuikotlin.utils.ViewHolderState
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class ViewHolder(itemView: View) : LifecycleViewHolder(itemView) {

    var windowDisp: Disposable? = null

    override fun onWindowAttach() {
        super.onWindowAttach()
        windowDisp = Single.never<Unit>().subscribe().disposeOnState(ViewHolderState.WINDOW_DETACH, this)
    }

    var adapterDisp: Disposable? = null

    override fun onAdapterAttach() {
        super.onAdapterAttach()
        adapterDisp = Single.never<Unit>().subscribe().disposeOnState(ViewHolderState.ADAPTER_DETACH, this)
    }
}