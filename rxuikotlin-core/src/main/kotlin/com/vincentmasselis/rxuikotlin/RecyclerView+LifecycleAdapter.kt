package com.vincentmasselis.rxuikotlin

import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.annotations.CheckReturnValue
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable

@CheckReturnValue
fun <T : LifecycleViewHolder> RecyclerView.subscribe(adapter: LifecycleAdapter<T>): Disposable = Completable
    .create { downStream ->
        setTag(R.integer.adapter_recycler_view_tag, Unit)
        downStream.setCancellable { this.adapter = null }
        this.adapter = adapter
    }
    .subscribe()