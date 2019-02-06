package com.vincentmasselis.rxuikotlin

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable

@CheckReturnValue
fun <T : LifecycleViewHolder> RecyclerView.subscribe(adapter: LifecycleAdapter<T>): Disposable = Completable
    .create { downStream ->
        setTag(R.integer.adapter_recycler_view_tag, Unit)
        downStream.setCancellable { this.adapter = null }
        this.adapter = adapter
    }
    .subscribe()